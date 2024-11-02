package com.app.ggumteo.controller.work;



import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.buy.BuyWorkDTO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
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

   @ModelAttribute
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(2L, "", "", "", "", ""));
        }
        if (session.getAttribute("memberProfile") == null) {
            session.setAttribute("memberProfile", new MemberProfileVO(2L, "", "", "", 99, "", "", "", 2L, "", ""));
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
        return "text/write";  // 컨트롤러에서 경로 리턴 대신 뷰 이름만
    }

    @PostMapping("write")
    public ResponseEntity<?> write(WorkDTO workDTO, @RequestParam("workFile") MultipartFile[] workFiles,
                                   @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }
            workDTO.setPostType(PostType.TEXT.name());
            workDTO.setMemberProfileId(member.getId());

            // Work 저장, 파일은 서비스에서 처리
            workService.write(workDTO, workFiles, thumbnailFile);

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
            return "text/modify";
        } else {
            // work가 null인 경우 처리 (예: 에러 페이지로 이동)
            model.addAttribute("error", "작품을 찾을 수 없습니다.");
            return "text/error";
        }
    }

    // 작품 업데이트 요청 처리
    @PostMapping("modify")
    public String updateWork(
            @ModelAttribute WorkDTO workDTO,
            @RequestParam(value = "newFiles", required = false) List<MultipartFile> newFiles,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            @RequestParam(value = "newThumbnailFile", required = false) MultipartFile newThumbnailFile,
            Model model) {
        try {
            workService.updateWork(workDTO, newFiles, deletedFileIds, newThumbnailFile); // 서비스로 전달
            return "redirect:/text/list";
        } catch (Exception e) {
            log.error("Error updating work: ", e);
            model.addAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "text/modify";
        }
    }







    @GetMapping("/list")
    public String list(
            @RequestParam(value = "genreType", required = false) String genreType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        Pagination pagination = new Pagination();
        pagination.setPage(page);

        int totalWorks = workService.findTotalWithSearch(genreType, keyword);
        pagination.setTotal(totalWorks);
        pagination.progress2();

        List<WorkDTO> works = workService.findAllWithThumbnailAndSearch(genreType, keyword, pagination);

        model.addAttribute("works", works);
        model.addAttribute("pagination", pagination);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genreType", genreType);

        return "text/list";
    }

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> display(@RequestParam("fileName") String fileName) throws IOException {
        byte[] fileData = postFileService.getFileData(fileName);
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        WorkDTO work = workService.findWorkById(id);
        workService.incrementReadCount(id);

        List<PostFileDTO> postFiles = workService.findFilesByPostId(id);
        List<WorkDTO> threeWorks = workService.getThreeWorksByGenre(work.getGenreType(), work.getId());
        List<WorkDTO> threeAuthorWorks = workService.getThreeWorksByAuthor(work.getMemberProfileId(), work.getId());

        model.addAttribute("work", work);
        model.addAttribute("postFiles", postFiles);
        model.addAttribute("threeWorks", threeWorks);
        model.addAttribute("threeAuthorWorks", threeAuthorWorks);

        return "text/detail";
    }
  @PostMapping("/order")
    public ResponseEntity<String> completePayment(@RequestBody Map<String, Object> paymentData) {
        try {
            Long workId = Long.parseLong(paymentData.get("workId").toString());
            Long memberProfileId = Long.parseLong(paymentData.get("memberProfileId").toString());

            MemberProfileVO memberProfile = (MemberProfileVO) session.getAttribute("memberProfile");
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

