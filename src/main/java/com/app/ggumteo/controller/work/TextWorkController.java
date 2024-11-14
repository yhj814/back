package com.app.ggumteo.controller.work;



import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.buy.BuyWorkDTO;
import com.app.ggumteo.domain.buy.BuyWorkVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.exception.SessionNotFoundException;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.alarm.AlarmService;
import com.app.ggumteo.service.buy.BuyWorkService;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.reply.ReplyService;
import com.app.ggumteo.service.work.WorkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class TextWorkController {
    private final WorkService workService;
    private final HttpSession session;
    private final PostFileService postFileService;
    private final BuyWorkService buyWorkService;
    private final AlarmService alarmService;

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
        return "text/write";
    }

    @PostMapping("write")
    public RedirectView write(
            @ModelAttribute WorkDTO workDTO,
            @RequestParam(value = "thumbnailFileName", required = false) String thumbnailFileName,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            log.error("세션에 멤버 정보가 없습니다.");
            throw new SessionNotFoundException("세션에 멤버 정보가 없습니다.");
        }

        workDTO.setPostType(PostType.WORKTEXT.name());
        workDTO.setMemberProfileId(member.getId());

        // 파일명 리스트를 DTO에 설정
        if (fileNames != null && !fileNames.isEmpty()) {
            workDTO.setFileNames(fileNames);
            // 파일 타입 설정은 서비스 계층에서 처리
        }

        // 썸네일 파일명 설정
        if (thumbnailFileName != null && !thumbnailFileName.isEmpty()) {
            workDTO.setThumbnailFileName(thumbnailFileName);
            // 썸네일 파일 타입 설정은 서비스 계층에서 처리
        }

        // 서비스 계층으로 로직 이동
        workService.write(workDTO);

        log.info("작품 작성 완료: {}", workDTO);
        return new RedirectView("/text/list");
    }


    // 작품 수정 폼으로 이동
    @GetMapping("modify/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        WorkDTO work = workService.findWorkById(id);  // work 객체 조회
        List<PostFileDTO> existingFiles = postFileService.findFilesByPostId(id); // 기존 파일 조회
        log.info("Fetched work: {}", work);  // work 객체를 로그로 출력해 확인

        if (work != null) {  // work가 null이 아닌지 확인
            model.addAttribute("work", work);
            model.addAttribute("existingFiles", existingFiles);
            return "text/modify";
        } else {
            // work가 null인 경우 처리 (예: 에러 페이지로 이동)
            model.addAttribute("error", "작품을 찾을 수 없습니다.");
            return "text/error";
        }
    }

    // 작품 업데이트 요청 처리
    @PostMapping("modify")
    public RedirectView updateWork(
            @ModelAttribute WorkDTO workDTO,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            @RequestParam(value = "thumbnailFileName", required = false) String thumbnailFileName) {
        try {
            log.info("수정 요청 - 작품 정보: {}", workDTO);

            // 기존 데이터를 가져와서 필요한 필드를 설정
            WorkDTO currentWork = workService.findWorkById(workDTO.getId());
            if (currentWork != null) {
                workDTO.setThumbnailFileId(currentWork.getThumbnailFileId());
            }

            // 업로드된 파일명 처리 로직 추가
            if (fileNames != null && !fileNames.isEmpty()) {
                workDTO.setFileNames(fileNames);
            }

            // 썸네일 파일명 처리
            if (thumbnailFileName != null && !thumbnailFileName.isEmpty()) {
                workDTO.setThumbnailFileName(thumbnailFileName);
            }

            // 서비스에서 작품 업데이트 로직 실행
            workService.updateWork(workDTO, deletedFileIds);
            return new RedirectView("/text/detail/" + workDTO.getId());
        } catch (Exception e) {
            log.error("Error updating work: ", e);
            return new RedirectView("/text/modify/" + workDTO.getId());
        }
    }




    @GetMapping("list")
    public String list(
            @ModelAttribute Search search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {


        search.setPostType(PostType.WORKTEXT.name());
        log.info("Received Search Parameters: {}", search);
        log.info("Received page: {}", page);

        Pagination pagination = new Pagination();
        pagination.setPage(page);

        int totalWorks = workService.findTotalWithSearchAndType(search);
        pagination.setTotal(totalWorks);
        pagination.progress2();

        log.info("Pagination - Page: {}", pagination.getPage());
        log.info("Pagination - Total: {}", pagination.getTotal());
        log.info("Pagination - Start Row: {}", pagination.getStartRow());
        log.info("Pagination - Row Count: {}", pagination.getRowCount());

        List<WorkDTO> works = workService.findAllWithThumbnailAndSearchAndType(search, pagination);
        log.info("Retrieved works list: {}", works);

        model.addAttribute("works", works);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "text/list";
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

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        WorkDTO work = workService.findWorkById(id);
        workService.incrementReadCount(id);

        List<PostFileDTO> postFiles = workService.findFilesByPostId(id);
        List<WorkDTO> threeWorks = workService.getThreeWorksByGenre(work.getGenreType(), work.getId(), PostType.WORKTEXT.name());
        List<WorkDTO> threeAuthorWorks = workService.getThreeWorksByAuthor(work.getMemberProfileId(), work.getId(), PostType.WORKTEXT.name());

        model.addAttribute("work", work);
        model.addAttribute("postFiles", postFiles);
        model.addAttribute("threeWorks", threeWorks);
        model.addAttribute("threeAuthorWorks", threeAuthorWorks);

        return "text/detail";
    }

    @PostMapping("/order")
    public ResponseEntity<String> completePayment(@RequestBody Map<String, Object> paymentData) {
        try {
            // 클라이언트로부터 전달받은 결제 데이터에서 작품 ID를 추출
            Long workId = Long.parseLong(paymentData.get("workId").toString());

            // 세션에서 현재 로그인한 회원의 프로필 정보를 가져옴 (구매자 정보)
            MemberProfileDTO buyerProfile = (MemberProfileDTO) session.getAttribute("memberProfile");
            if (buyerProfile == null) {
                log.error("세션에 멤버 프로필 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("세션에 멤버 프로필 정보가 없습니다.");
            }
            Long buyerMemberProfileId = buyerProfile.getId();

            // 작품 정보 조회
            WorkDTO work = workService.findWorkById(workId);
            if (work == null) {
                log.error("해당 작품을 찾을 수 없습니다. 작품 ID: {}", workId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 작품을 찾을 수 없습니다.");
            }

            // 작품 작성자의 memberProfileId 가져오기
            Long authorMemberProfileId = work.getMemberProfileId();
            if (authorMemberProfileId == null) {
                log.error("작품의 작성자 정보를 찾을 수 없습니다. 작품 ID: {}", workId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작품의 작성자 정보를 찾을 수 없습니다.");
            }

            // BuyWorkDTO 객체 생성 및 데이터 설정
            BuyWorkDTO buyWorkDTO = new BuyWorkDTO();
            buyWorkDTO.setWorkId(workId);
            buyWorkDTO.setMemberProfileId(buyerMemberProfileId);
            buyWorkDTO.setProfileName(buyerProfile.getProfileName());
            buyWorkDTO.setProfilePhone(buyerProfile.getProfilePhone());
            buyWorkDTO.setProfileEmail(buyerProfile.getProfileEmail());
            buyWorkDTO.setWorkSendStatus("0"); // 초기 상태 설정 (예: '미발송')

            // BuyWorkVO 객체로 변환
            BuyWorkVO buyWorkVO = buyWorkDTO.toVO();

            // 구매 정보를 데이터베이스에 저장하고 저장된 BuyWorkVO 객체 반환
            BuyWorkVO savedBuyWork = buyWorkService.savePurchase(buyWorkVO);
            Long buyWorkId = savedBuyWork.getId(); // 저장된 구매 ID 추출

            // 알림 메시지 설정
            String message = "등록한 작품이 구매되었습니다.";

            // AlarmService를 통해 알림 생성 (작품 작성자의 memberProfileId 사용)
            alarmService.createWorkAlarm(authorMemberProfileId, buyWorkId, message, AlarmSubType.TEXT);

            log.info("작품 구매가 처리되었으며 알림이 생성되었습니다.");
            return ResponseEntity.ok("결제 정보가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            log.error("결제 저장 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 중 오류가 발생했습니다.");
        }
    }


}

