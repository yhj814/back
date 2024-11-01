package com.app.ggumteo.controller.audition;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.service.audition.AuditionService;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.member.MemberService;
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

    @ModelAttribute
    public void setMemberInfo(HttpSession session, Model model) {
        // 세션에서 member 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileVO memberProfile = (MemberProfileVO) session.getAttribute("memberProfile");

        // 로그인 여부를 확인
        boolean isLoggedIn = member != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        // 로그인된 상태면 member와 memberProfile 정보도 뷰에 추가
        if (isLoggedIn) {
            model.addAttribute("member", member);
            model.addAttribute("memberProfile", memberProfile);
            log.info("로그인 상태 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfile != null ? memberProfile.getId() : "null");
        } else {
            log.info("비로그인 상태입니다.");
        }
    }

    // 이후 메서드들에 추가적인 세션 체크 코드가 필요하지 않습니다.
    @GetMapping("audition-write")
    public void goToWritePage() {
        log.info("작성 페이지로 이동");
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
            auditionService.write(auditionDTO, auditionFiles);

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }

    // 다른 메서드들에서도 세션 정보는 @ModelAttribute로 설정된 값들을 사용합니다.
    @GetMapping("/audition-list")
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
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

    @GetMapping("/audition-detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        AuditionDTO audition = auditionService.findAuditionById(id);
        List<PostFileDTO> postFiles = auditionService.findAllPostFiles(id);

        model.addAttribute("audition", audition);
        model.addAttribute("postFiles", postFiles);

        return "/audition/video/audition-detail";
    }
}
