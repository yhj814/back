package com.app.ggumteo.controller.work;



import com.app.ggumteo.enums.PostType;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.reply.ReplyService;
import com.app.ggumteo.service.work.WorkService;
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
import java.util.*;

@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class TextWorkController {
    private final WorkService workService;
    private final HttpSession session;
    private final PostFileService postFileService;
    private final ReplyService replyService;

    // 모든 요청 전 세션에 테스트용 사용자 정보 설정
    @ModelAttribute
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(
                    2L,

                    "",         // memberEmail
                    "",
                    "",              // profileUrl
                    "",          // createdDate
                    ""          // updatedDate
            ));
        }
    }

    @GetMapping("write")
    public void goToWriteForm() {;}

    @PostMapping("write")
    public ResponseEntity<?> write(WorkDTO workDTO, @RequestParam("workFile") MultipartFile[] workFiles,
                                   @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }

            // PostType을 TEXT로 고정
            workDTO.setPostType(PostType.TEXT.name());
            workDTO.setMemberProfileId(member.getId());

            // Work와 Post 저장
            workService.write(workDTO);

            // 파일 저장 로직 호출 (workFile 및 thumbnailFile 저장)
            if (workFiles != null && workFiles.length > 0) {
                for (MultipartFile file : workFiles) {
                    if (!file.isEmpty()) {
                        postFileService.saveFile(file, workDTO.getId()); // 각각의 파일 저장
                    }
                }
            }
            if (!thumbnailFile.isEmpty()) {
                postFileService.saveFile(thumbnailFile, workDTO.getId());
            }

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("/list")
    public String list(
            @RequestParam(value = "genreType", required = false) String genreType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        if (genreType == null) genreType = "";
        if (keyword == null) keyword = "";


        // 페이지네이션 설정
        Pagination pagination = new Pagination();
        pagination.setPage(page);

        // 검색어와 장르에 맞는 총 결과 수 계산
        int totalWorks = workService.findTotalWithSearch(genreType, keyword); // 검색 조건을 고려한 총 작품 수
        pagination.setTotal(totalWorks); // 검색 결과에 따른 총 페이지 수 반영
        pagination.progress2();



        // 검색어와 페이지네이션 데이터 기반으로 작품 목록 조회
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
        String rootPath = "C:/ggumteofile/"; // 실제 파일이 저장된 루트 경로
        File file = new File(rootPath + fileName); // rootPath와 요청된 상대 경로 결합

        // 파일 경로를 로그로 출력하여 확인
        log.info("Attempting to load file from path: {}", file.getAbsolutePath());

        // 파일이 존재하지 않으면 404 반환
        if (!file.exists()) {
            log.error("File not found at path: {}", file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = FileCopyUtils.copyToByteArray(file);
        HttpHeaders headers = new HttpHeaders();

        // MIME 타입 설정
        String contentType = Files.probeContentType(file.toPath());
        if (contentType != null) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        }

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }





    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        WorkDTO work = workService.findWorkById(id);
        workService.incrementReadCount(id);
        log.info("Detail view의 WorkDTO: {}", work);

        List<PostFileDTO> postFiles = workService.findFilesByPostId(id);

        model.addAttribute("work", work);
        model.addAttribute("postFiles", postFiles);

        // workId 값을 JavaScript로 전달하기 위해 추가
        model.addAttribute("workId", work.getId());

        return "text/detail";
    }



}
