package com.app.ggumteo.controller.work;



import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.buy.BuyWorkDTO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.search.Search;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/video/*")
@RequiredArgsConstructor
@Slf4j
public class VideoWorkController {
    private final WorkService workService;
    private final HttpSession session;
    private final PostFileService postFileService;
    private final BuyWorkService buyWorkService;

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
        return "video/write";  // 컨트롤러에서 경로 리턴 대신 뷰 이름만
    }

    @PostMapping("write")
    public ResponseEntity<?> write(
            @ModelAttribute WorkDTO workDTO,
            @RequestParam(value = "thumbnailFileName", required = false) String thumbnailFileName,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            HttpSession session) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }

            workDTO.setPostType(PostType.WORKVIDEO.name());
            workDTO.setMemberProfileId(member.getId());

            // 파일명 리스트를 DTO에 설정
            workDTO.setFileNames(fileNames);

            // 썸네일 파일명 설정
            workDTO.setThumbnailFileName(thumbnailFileName);

            // 서비스 계층으로 로직 이동
            workService.write(workDTO);

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
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
            return "video/modify";
        } else {
            // work가 null인 경우 처리 (예: 에러 페이지로 이동)
            model.addAttribute("error", "작품을 찾을 수 없습니다.");
            return "video/error";
        }
    }

    // 작품 업데이트 요청 처리
    @PostMapping("modify")
    public String updateWork(
            @ModelAttribute WorkDTO workDTO,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            @RequestParam(value = "thumbnailFileName", required = false) String thumbnailFileName,
            @ModelAttribute Search search,  // 검색 조건 추가
            @RequestParam(value = "page", defaultValue = "1") int page,  // 페이지 파라미터 추가
            Model model) {
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

            // 수정 후 리스트 페이지로 이동하기 위해 필요한 데이터 모델에 추가
            search.setPostType(PostType.WORKVIDEO.name());  // 필요한 경우 게시글 타입 설정
            log.info("Received Search Parameters: {}", search);
            log.info("Received page: {}", page);

            Pagination pagination = new Pagination();
            pagination.setPage(page);

            int totalWorks = workService.findTotalWithSearchAndType(search);
            pagination.setTotal(totalWorks);
            pagination.progress2();  // 페이지네이션 계산

            List<WorkDTO> works = workService.findAllWithThumbnailAndSearchAndType(search, pagination);

            model.addAttribute("works", works);
            model.addAttribute("pagination", pagination);
            model.addAttribute("search", search);

            return "text/list";
        } catch (Exception e) {
            log.error("Error updating work: ", e);
            model.addAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "text/list";
        }
    }




    @GetMapping("list")
    public String list(
            @ModelAttribute Search search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {


        search.setPostType(PostType.WORKVIDEO.name());
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

        return "video/list";
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
        List<WorkDTO> threeWorks = workService.getThreeWorksByGenre(work.getGenreType(), work.getId(), PostType.WORKVIDEO.name());
        List<WorkDTO> threeAuthorWorks = workService.getThreeWorksByAuthor(work.getMemberProfileId(), work.getId(), PostType.WORKVIDEO.name());

        model.addAttribute("work", work);
        model.addAttribute("postFiles", postFiles);
        model.addAttribute("threeWorks", threeWorks);
        model.addAttribute("threeAuthorWorks", threeAuthorWorks);

        return "video/detail";
    }
    @PostMapping("/order")
    public ResponseEntity<String> completePayment(@RequestBody Map<String, Object> paymentData) {
        try {
            Long workId = Long.parseLong(paymentData.get("workId").toString());
            Long memberProfileId = Long.parseLong(paymentData.get("memberProfileId").toString());

            // 세션에서 MemberProfileDTO를 가져옵니다.
            MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");
            if (memberProfile == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("세션에 멤버 프로필 정보가 없습니다.");
            }

            BuyWorkDTO buyWorkDTO = new BuyWorkDTO();
            buyWorkDTO.setWorkId(workId);
            buyWorkDTO.setMemberProfileId(memberProfileId);
            buyWorkDTO.setProfileName(memberProfile.getProfileName());
            buyWorkDTO.setProfilePhone(memberProfile.getProfilePhone());
            buyWorkDTO.setProfileEmail(memberProfile.getProfileEmail());
            buyWorkDTO.setWorkSendStatus("0");

            buyWorkService.savePurchase(buyWorkDTO.toVO());

            return ResponseEntity.ok("결제 정보가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            log.error("결제 저장 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 중 오류가 발생했습니다.");
        }
    }

}

