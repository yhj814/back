package com.app.ggumteo.controller.funding;


import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.funding.FundingProductVO;
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
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(3L, "testEmail@test.com", "모집중", "profileImageUrl", "", ""));
        }
        if (session.getAttribute("memberProfile") == null) {
            session.setAttribute("memberProfile", new MemberProfileVO(
                    3L,
                    "홍길동",               // profileName
                    "010-1234-5678",       // profilePhone
                    "testEmail@test.com",  // profileEmail
                    99,
                    "닉네임",
                    "소개",
                    "기타",
                    3L,
                    "",
                    ""
            ));
        }
    }


    @PostMapping("upload")
    @ResponseBody
    public List<PostFileDTO> upload(@RequestParam("file") List<MultipartFile> files) {
        try {
            return postFileService.uploadFile(files);  // 서비스의 uploadFile 메서드 호출
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return Collections.emptyList();  // 오류 발생 시 빈 리스트 반환
        }
    }


    @GetMapping("write")
    public String goToWriteForm() {
        return "text/funding/funding-write";
    }

    @PostMapping("write")
    public ResponseEntity<?> write(FundingDTO fundingDTO, @RequestParam("fundingFile") MultipartFile[] fundingFiles,
                                   @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }
            fundingDTO.setPostType(PostType.TEXT.name());
            fundingDTO.setMemberProfileId(member.getId());
            log.info("Received FundingDTO: {}", fundingDTO);
            log.info("Received Funding Products: {}", fundingDTO.getFundingProducts());
            // 썸네일 파일 정보 로그 출력
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                log.info("Received Thumbnail File: Name - {}, Size - {}, ContentType - {}",
                        thumbnailFile.getOriginalFilename(), thumbnailFile.getSize(), thumbnailFile.getContentType());
            } else {
                log.warn("Thumbnail file is either null or empty");
            }

            // Work 저장, 파일은 서비스에서 처리
            fundingService.write(fundingDTO, fundingFiles, thumbnailFile);

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


        search.setPostType(PostType.TEXT.name());
        log.info("Received Search Parameters: {}", search);
        log.info("Received page: {}", page);

        Pagination pagination = new Pagination();
        pagination.setPage(page);

        int totalWorks = fundingService.findTotalWithSearchAndType(search);
        pagination.setTotal(totalWorks);
        pagination.progress2();

        log.info("Pagination - Page: {}", pagination.getPage());
        log.info("Pagination - Total: {}", pagination.getTotal());
        log.info("Pagination - Start Row: {}", pagination.getStartRow());
        log.info("Pagination - Row Count: {}", pagination.getRowCount());

        List<FundingDTO> fundings = fundingService.findFundingList(search, pagination);
        log.info("Retrieved works list: {}", fundings);

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

    @PostMapping("order")
    public ResponseEntity<?> completeOrder(@RequestBody Map<String, Object> orderData) {
        try {
            // 필수 파라미터 체크
            if (!orderData.containsKey("fundingId") || !orderData.containsKey("memberProfileId") ||
                    !orderData.containsKey("productId") || !orderData.containsKey("amount")) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "필수 데이터가 누락되었습니다."));
            }

            Long fundingId = Long.valueOf(orderData.get("fundingId").toString());
            Long memberProfileId = Long.valueOf(orderData.get("memberProfileId").toString());
            Long productId = Long.valueOf(orderData.get("productId").toString());
            int amount = Integer.parseInt(orderData.get("amount").toString());

            log.info("Received Order Data - Funding ID: {}, Member Profile ID: {}, Product ID: {}, Amount: {}", fundingId, memberProfileId, productId, amount);

            // 펀딩 주문 처리 로직 실행
            fundingService.buyFundingProduct(memberProfileId, productId, amount);

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