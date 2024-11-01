package com.app.ggumteo.controller.audition;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.service.audition.AuditionService;
import com.app.ggumteo.service.file.PostFileService;
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
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/audition/video/*")
@RequiredArgsConstructor
public class VideoAuditionController {
    private final AuditionService auditionService;
    private final HttpSession session;
    private final PostFileService postFileService;

    // write 화면으로 이동
    @GetMapping("audition-write")
    public void goToWritePage() {
        if (session.getAttribute("member") == null) {
            // 테스트용 MemberVO 설정
            session.setAttribute("member", new MemberVO(
                    19L,
                    "",         // memberEmail
                    "",
                    "",              // profileUrl
                    "",          // createdDate
                    ""           // updatedDate
            ));
        }

        log.info("세션에 설정된 memberId: " + ((MemberVO) session.getAttribute("member")).getId());
    }

    @PostMapping("audition-write")
    public ResponseEntity<?> write(AuditionDTO auditionDTO, @RequestParam("auditionFile") MultipartFile[] auditionFiles) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("멤버 정보가 세션에 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 정보가 없습니다."));
            }

            // PostType을 VIDEO로 고정
            auditionDTO.setPostType(PostType.VIDEO.name());
            auditionDTO.setAuditionStatus("모집중");
            auditionDTO.setMemberProfileId(member.getId());

            // audition과 Post 저장
            auditionService.write(auditionDTO);

            // 파일 저장 로직 호출 (auditionFile 저장)
            if (auditionFiles != null && auditionFiles.length > 0) {
                for (MultipartFile file : auditionFiles) {
                    if (!file.isEmpty()) {
                        postFileService.saveFile(file, auditionDTO.getId());
                    }
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("/audition-list")
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        if (keyword == null) keyword = "";

        AuditionPagination pagination = new AuditionPagination();
        pagination.setPage(page);

        int totalAudition = auditionService.findTotal();
        int totalSearchAudition = auditionService.findTotalAuditionsSearch(keyword);

        int totalCount = keyword.isEmpty() ? totalAudition : totalSearchAudition;
        pagination.setTotal(totalCount);
        pagination.progress();

        List<AuditionDTO> auditions = auditionService.findAllAuditions(keyword, pagination);

        model.addAttribute("auditions", auditions);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);
        model.addAttribute("totalCount", totalCount);

        return "/audition/video/audition-list";
    }

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> display(@RequestParam("fileName") String fileName) throws IOException {
        String rootPath = "C:/ggumteofile/uploads/";
        File file = new File(rootPath + fileName);

        log.info("Attempting to load file from path: {}", file.getAbsolutePath());

        if (!file.exists()) {
            log.error("File not found at path: {}", file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = FileCopyUtils.copyToByteArray(file);
        HttpHeaders headers = new HttpHeaders();

        String contentType = Files.probeContentType(file.toPath());
        if (contentType != null) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        }

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/audition-detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        AuditionDTO audition = auditionService.findAuditionById(id);

        List<PostFileDTO> postFiles = auditionService.findAllPostFiles(id);

        model.addAttribute("audition", audition);
        model.addAttribute("postFiles", postFiles);

        return "/audition/video/audition-detail";
    }

}
