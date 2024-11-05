package com.app.ggumteo.controller.audition;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.audition.AuditionService;
import com.app.ggumteo.service.file.PostFileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.app.ggumteo.search.Search;
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

//    @ModelAttribute
//    public void setMemberInfo(HttpSession session, Model model) {
//        MemberVO member = (MemberVO) session.getAttribute("member");
//        MemberProfileVO memberProfileVO = (MemberProfileVO) session.getAttribute("memberProfile");
//
//        boolean isLoggedIn = member != null;
//        model.addAttribute("isLoggedIn", isLoggedIn);
//
//        if (isLoggedIn) {
//            model.addAttribute("member", member);
//
//            if (memberProfileVO != null) {
//                MemberProfileDTO memberProfileDTO = memberProfileVO.toDTO(); // VO를 DTO로 변환
//                model.addAttribute("memberProfile", memberProfileDTO);
//                log.info("로그인 상태 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfileDTO.getId());
//            } else {
//                log.info("로그인 상태 - 프로필 정보가 없습니다.");
//            }
//        } else {
//            log.info("비로그인 상태입니다.");
//        }
//    }


    @PostMapping("upload")
    @ResponseBody
    public List<PostFileDTO> upload(@RequestParam("file") List<MultipartFile> files) {
        try {
            return postFileService.uploadFile(files);
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("write")
    public void goToWritePage() {
        log.info("작성 페이지로 이동");
    }

    @PostMapping("write")
    public String write(AuditionDTO auditionDTO, @RequestParam("auditionFile") MultipartFile[] auditionFiles, Model model) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");
            if (member == null) {
                log.error("멤버 정보가 세션에 없습니다.");
                model.addAttribute("error", "세션에 정보가 없습니다.");
                return "/error";
            }

            auditionDTO.setPostType(PostType.VIDEO.name());
            auditionDTO.setAuditionStatus("모집중");

            auditionDTO.setMemberProfileId(memberProfile.getId());
            auditionDTO.setMemberId(member.getId());

            log.info("write 메서드 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), auditionDTO.getMemberProfileId());

            auditionService.write(auditionDTO, auditionFiles);

            // 리디렉션으로 이동
            return "redirect:/audition/video/detail/" + auditionDTO.getId();
        } catch (Exception e) {
            log.error("오류 발생", e);
            model.addAttribute("error", "저장 중 오류가 발생했습니다.");
            return "/error";
        }
    }

    @GetMapping("/modify/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        AuditionDTO audition = auditionService.findAuditionById(id);
        List<PostFileDTO> existingFiles = postFileService.findFilesByPostId(id);

        if (audition != null) {
            model.addAttribute("audition", audition);
            model.addAttribute("existingFiles", existingFiles);
            return "/audition/video/modify";
        } else {
            model.addAttribute("error","해당 게시글을 찾을 수 없습니다.");
            return "/audition/video/error";
        }
    }

    @PostMapping("/modify")
    public String updateAudition(
            @ModelAttribute AuditionDTO auditionDTO,
            @RequestParam(value = "newFiles", required = false) List<MultipartFile> newFiles,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            @ModelAttribute Search search,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            Model model) {
        try {
            // 전달된 AuditionDTO의 필드들을 로그로 확인
            log.info("수정 요청 - AuditionDTO 정보: {}", auditionDTO);
            log.info("수정 요청 - 새 파일 목록: {}", newFiles);
            log.info("수정 요청 - 삭제할 파일 ID 목록: {}", deletedFileIds);

            // 기존 데이터를 가져와서 필요한 필드를 설정
            auditionService.updateAudition(auditionDTO, newFiles, deletedFileIds);

            log.info("업데이트 성공 - AuditionDTO ID: {}", auditionDTO.getId());

            return "redirect:/audition/video/detail/" + auditionDTO.getId();
        } catch (Exception e) {
            log.error("오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "/audition/video/detail/{id}";
        }
    }


    @GetMapping("/list")
    public String list(@ModelAttribute Search search,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       Model model) {

        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        log.info("list 메서드 - 사용자 ID: {}, 프로필 ID: {}", member != null ? member.getId() : "null", memberProfile != null ? memberProfile.getId() : "null");

        AuditionPagination pagination = new AuditionPagination();
        pagination.setPage(page);

        int totalSearchAudition = auditionService.findTotalAuditionsSearch(PostType.VIDEO, search);
        log.info("모집건수: {}",totalSearchAudition);

        pagination.setTotal(totalSearchAudition);
        pagination.progress();

        List<AuditionDTO> auditions = auditionService.findAllAuditions(PostType.VIDEO, search, pagination);

        model.addAttribute("auditions", auditions);
        model.addAttribute("search", search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("totalSearchAudition", totalSearchAudition);


        return "/audition/video/list";
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
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        log.info("detail 메서드 - 사용자 ID: {}, 프로필 ID: {}", member != null ? member.getId() : "null", memberProfile != null ? memberProfile.getId() : "null");

        AuditionDTO audition = auditionService.findAuditionById(id);
        List<PostFileDTO> postFiles = auditionService.findAllPostFiles(id);

        model.addAttribute("audition", audition);
        model.addAttribute("postFiles", postFiles);

        return "/audition/video/detail";
    }
}
