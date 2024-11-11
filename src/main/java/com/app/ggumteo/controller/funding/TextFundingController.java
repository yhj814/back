package com.app.ggumteo.controller.funding;


import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.funding.FundingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/text/funding/*")
@RequiredArgsConstructor
@Slf4j
public class TextFundingController {
    private final HttpSession session;
    private final PostFileService postFileService;
    private final FundingService fundingService;

    @ModelAttribute
    public void setMemberInfo(HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        boolean isLoggedIn = member != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            model.addAttribute("member", member);
            model.addAttribute("memberProfile", memberProfile);
            log.info("로그인 상태 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfile != null ? memberProfile.getId() : "null");
        } else {
            log.info("비로그인 상태입니다.");
        }
    }


    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            FileVO savedFile = postFileService.saveFile(file);  // 파일 저장 후 FileVO 반환
            String savedFileName = savedFile.getFileName();  // 파일명 추출
            return savedFileName;  // uuid+파일명 반환
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return "error";  // 오류 발생 시 "error" 문자열 반환
        }
    }


    @GetMapping("write")
    public String goToWriteForm() {
        return "text/funding/funding-write";
    }

    @PostMapping("write")
    public ResponseEntity<?> write(
            @ModelAttribute FundingDTO fundingDTO,
            @RequestParam(value = "thumbnailFileName", required = false) String thumbnailFileName,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            HttpSession session) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }

            fundingDTO.setPostType(PostType.FUNDINGTEXT.name());
            fundingDTO.setMemberProfileId(member.getId());

            // 파일명 리스트를 DTO에 설정
            if (fileNames != null && !fileNames.isEmpty()) {
                fundingDTO.setFileNames(fileNames);
            }

            // 썸네일 파일명 설정
            if (thumbnailFileName != null && !thumbnailFileName.isEmpty()) {
                fundingDTO.setThumbnailFileName(thumbnailFileName);
            }

            // 서비스 계층으로 로직 이동
            fundingService.write(fundingDTO);

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("list")
    public String list(
            @ModelAttribute Search search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {


        search.setPostType(PostType.FUNDINGTEXT.name());
        log.info("Received Search Parameters: {}", search);
        log.info("Received page: {}", page);

        Pagination pagination = new Pagination();
        pagination.setPage(page);

        int totalFundings = fundingService.findTotalWithSearchAndType(search);
        pagination.setTotal(totalFundings);
        pagination.progress2();

        log.info("Pagination - Page: {}", pagination.getPage());
        log.info("Pagination - Total: {}", pagination.getTotal());
        log.info("Pagination - Start Row: {}", pagination.getStartRow());
        log.info("Pagination - Row Count: {}", pagination.getRowCount());

        List<FundingDTO> fundings = fundingService.findFundingList(search, pagination);
        log.info("Retrieved funding list: {}", fundings);

        model.addAttribute("fundings", fundings);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "text/funding/funding-list";
    }

    @GetMapping("display")
    @ResponseBody
    public byte[] display(@RequestParam("fileName") String fileName) throws IOException {
        File file = new File("C:/upload", fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }

        return FileCopyUtils.copyToByteArray(file);
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        try {
            // 펀딩 상세 정보 조회 (기본 정보 포함)
            List<FundingDTO> fundingDTOList = fundingService.findFundingById(id);
            List<PostFileDTO> postFiles = fundingService.findFilesByPostId(id);

            if (fundingDTOList.isEmpty()) {
                log.warn("No funding found for ID: {}", id);
                return "redirect:/text/funding/funding-list";
            }

            FundingDTO fundingDTO = fundingDTOList.get(0);
            log.info("Retrieved FundingDTO: {}", fundingDTO);

            // 펀딩 상품 정보 조회 추가
            List<FundingProductVO> fundingProducts = fundingService.findFundingProductsByFundingId(id);
            fundingDTO.setFundingProducts(fundingProducts); // 상품 목록을 FundingDTO에 설정
            String genreType = fundingDTO.getGenreType();
            List<FundingDTO> relatedFundings = fundingService.findRelatedFundingByGenre(genreType, id);

            model.addAttribute("funding", fundingDTO);
            model.addAttribute("postFiles", postFiles);
            model.addAttribute("relatedFundings", relatedFundings);

            return "text/funding/funding-detail";  // 상세 페이지 뷰로 이동
        } catch (Exception e) {
            log.error("펀딩 상세 조회 중 오류 발생", e);
            return "redirect:/text/funding/funding-list";
        }
    }

    @GetMapping("modify/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        FundingDTO funding = fundingService.findFundingId(id);  // funding 객체 조회
        List<PostFileDTO> existingFiles = postFileService.findFilesByPostId(id); // 기존 파일 조회

        if (funding != null) {  // funding이 null이 아닌지 확인
            // 펀딩 상품 정보 조회
            List<FundingProductVO> fundingProducts = fundingService.findFundingProductsByFundingId(id);
            funding.setFundingProducts(fundingProducts); // 상품 목록을 FundingDTO에 설정
            log.info("펀딩 정보: {}", funding);
            log.info("펀딩 상품 목록: {}", fundingProducts);
            model.addAttribute("funding", funding);
            model.addAttribute("existingFiles", existingFiles);
            return "text/funding/funding-modify";
        } else {
            // funding가 null인 경우 처리 (예: 에러 페이지로 이동)
            model.addAttribute("error", "펀딩을 찾을 수 없습니다.");
            return "text/funding/error";
        }
    }
    // 펀딩 수정 요청 처리
    @PostMapping("modify")
    public String updateFunding(
            @ModelAttribute FundingDTO fundingDTO,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            @RequestParam(value = "thumbnailFileName", required = false) String thumbnailFileName) {
        try {
            log.info("수정 요청 - 펀딩 정보: {}", fundingDTO);
            log.info("삭제할 파일 IDs: {}", deletedFileIds);
            log.info("삭제할 펀딩 상품 IDs: {}", fundingDTO.getFundingProductIds());
            log.info("새로운 썸네일 파일명: {}", thumbnailFileName);

            // 세션에서 로그인 사용자 정보 가져오기
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return "redirect:/main";  // 로그인 페이지로 리다이렉트
            }

            // 펀딩 타입 설정
            fundingDTO.setPostType(PostType.FUNDINGTEXT.name());

            // 파일명 리스트를 DTO에 설정
            if (fileNames != null && !fileNames.isEmpty()) {
                fundingDTO.setFileNames(fileNames);
            }

            // 썸네일 파일명 설정
            if (thumbnailFileName != null && !thumbnailFileName.isEmpty()) {
                fundingDTO.setThumbnailFileName(thumbnailFileName);
            }

            // 서비스에서 펀딩 수정 로직 실행
            fundingService.updateFunding(fundingDTO, deletedFileIds);

            log.info("펀딩 수정 완료: 펀딩 ID {}", fundingDTO.getId());
            return "redirect:/text/funding/detail/" + fundingDTO.getId();
        } catch (Exception e) {
            log.error("펀딩 수정 중 오류 발생", e);
            return "redirect:/text/funding/modify/" + fundingDTO.getId();
        }
    }


    @PostMapping("order")
    public ResponseEntity<?> completeOrder(@RequestBody Map<String, Object> orderData) {
        try {
            // 필수 파라미터 체크
            if (!orderData.containsKey("fundingId") || !orderData.containsKey("memberProfileId") ||
                    !orderData.containsKey("productId") || !orderData.containsKey("amount") || !orderData.containsKey("productPrice")) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "필수 데이터가 누락되었습니다."));
            }

            Long fundingId = Long.valueOf(orderData.get("fundingId").toString());
            Long memberProfileId = Long.valueOf(orderData.get("memberProfileId").toString());
            Long fundingProductId = Long.valueOf(orderData.get("productId").toString());
            int amount = Integer.parseInt(orderData.get("amount").toString());
            int productPrice = Integer.parseInt(orderData.get("productPrice").toString());

            log.info("Received Order Data - Funding ID: {}, Member Profile ID: {}, Product ID: {}, Amount: {}, Product Price: {}", fundingId, memberProfileId, fundingProductId, amount, productPrice);

            // 펀딩 주문 처리 로직 실행
            fundingService.buyFundingProduct(memberProfileId, fundingId, fundingProductId, productPrice);

            log.info("Order processed successfully for Funding ID: {}", fundingId);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (NumberFormatException e) {
            log.error("주문 데이터 형식 오류", e);
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "잘못된 데이터 형식이 포함되어 있습니다."));
        } catch (Exception e) {
            log.error("펀딩 주문 처리 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "주문 처리 중 오류가 발생했습니다."));
        }
    }




}