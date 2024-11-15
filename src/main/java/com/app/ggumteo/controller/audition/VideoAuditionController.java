package com.app.ggumteo.controller.audition;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.constant.RedirectPaths;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.repository.file.AuditionApplicationFileDAO;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.audition.AuditionApplicationService;
import com.app.ggumteo.service.audition.AuditionApplicationServiceImpl;
import com.app.ggumteo.service.audition.AuditionService;
import com.app.ggumteo.service.file.AuditionApplicationFileService;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/audition/video/*")
@RequiredArgsConstructor
public class VideoAuditionController {

    private final AuditionService auditionService;
    private final AuditionApplicationService auditionApplicationService;
    private final HttpSession session;
    private final PostFileService postFileService;
    private final AuditionApplicationFileService auditionApplicationFileService;


    @ModelAttribute
    public void setMemberInfo(HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        boolean isLoggedIn = member != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (isLoggedIn) {
            model.addAttribute("member", member);
            model.addAttribute("memberProfile", memberProfile);
        } else {
            log.info("비로그인 상태입니다.");
        }
    }

    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            FileVO savedFile = postFileService.saveFile(file);
            String savedFileName = savedFile.getFileName();
            return savedFileName;
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("upload-apply")
    @ResponseBody
    public String uploadApply(@RequestParam("file") MultipartFile file) {
        try {
            FileVO savedFile = auditionApplicationFileService.saveFile(file);
            String savedFileName = savedFile.getFileName();
            return savedFileName;
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("write")
    public String goToWritePage() {
        return "/audition/video/write";
    }

    @PostMapping("write")
    public RedirectView write(AuditionDTO auditionDTO,
                        @RequestParam(value = "fileNames", required = false) List<String> fileNames,
                        Model model) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

            if (member == null || memberProfile == null) {
                model.addAttribute("error", "세션에 사용자 정보가 없습니다.");
                return new RedirectView("/main");
            }

            auditionDTO.setPostType(PostType.AUDITIONVIDEO.name());
            auditionDTO.setAuditionStatus("YES");
            auditionDTO.setMemberProfileId(memberProfile.getId());
            auditionDTO.setMemberId(member.getId());

            auditionDTO.setFileNames(fileNames);

            auditionService.write(auditionDTO);

            return new RedirectView("/audition/video/detail/" + auditionDTO.getId());
        } catch (Exception e) {
            model.addAttribute("error", "저장 중 오류가 발생했습니다.");
            return new RedirectView("/error");
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
            model.addAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "/audition/video/error";
        }
    }


    @PostMapping("/modify")
    public RedirectView updateAudition(
            @ModelAttribute AuditionDTO auditionDTO,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            Model model) {
        try {
            AuditionDTO currentAudition = auditionService.findAuditionById(auditionDTO.getId());
            auditionDTO.setPostType(PostType.AUDITIONVIDEO.name());
            auditionDTO.setAuditionStatus("모집중");
            if (currentAudition != null) {
                log.info("게시글 id:{}", currentAudition.getId());
            }

            // 업로드된 파일명 처리 로직 추가
            if (fileNames != null && !fileNames.isEmpty()) {
                auditionDTO.setFileNames(fileNames);
            }

            auditionService.updateAudition(auditionDTO, deletedFileIds);

            model.addAttribute("audition", auditionDTO);

            return new RedirectView("/audition/video/detail/" + auditionDTO.getId());
        } catch (Exception e) {
            model.addAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return new RedirectView("/audition/video/error");
        }
    }

    @GetMapping("/list")
    public String list(@ModelAttribute Search search,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       Model model) {

        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        AuditionPagination pagination = new AuditionPagination();
        pagination.setPage(page);

        int totalSearchAudition = auditionService.findTotalAuditionsSearch(PostType.AUDITIONVIDEO, search);
        pagination.setTotal(totalSearchAudition);
        pagination.progress();

        List<AuditionDTO> auditions = auditionService.findAllAuditions(PostType.AUDITIONVIDEO, search, pagination);

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

        AuditionDTO audition = auditionService.findAuditionById(id);
        List<PostFileDTO> postFiles = auditionService.findAllPostFiles(id);

        int applicantCount = auditionApplicationService.countApplicantsByAuditionId(id);

        model.addAttribute("audition", audition);
        model.addAttribute("postFiles", postFiles);
        model.addAttribute("applicantCount", applicantCount);

        return "/audition/video/detail";
    }

    @GetMapping("/application/{id}")
    public String application(@PathVariable("id") Long id, Model model) {
        // 세션에서 멤버 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        if (member == null || memberProfile == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // URL에서 전달받은 id를 사용하여 auditionDTO 조회
        AuditionDTO audition = auditionService.findAuditionById(id);
        if (audition == null) {
            model.addAttribute("error", "해당 오디션 정보를 찾을 수 없습니다.");
            return "/error"; // 오류 페이지로 이동
        }

        // 모델에 필요한 데이터 추가
        model.addAttribute("memberProfile", memberProfile);
        model.addAttribute("audition", audition);
        model.addAttribute("id", id);

        return "audition/video/application"; // 신청서 작성 페이지로 이동
    }


    @PostMapping("/application/{id}")
    public RedirectView submitApplication(
            @PathVariable("id") Long id,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            AuditionApplicationDTO auditionApplicationDTO,
            Model model) {
        // 세션에서 멤버 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        if (member == null || memberProfile == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return new RedirectView("/main"); // 로그인 페이지로 리다이렉트
        }

        // 신청 데이터 설정
        auditionApplicationDTO.setAuditionId(id);
        auditionApplicationDTO.setMemberProfileId(memberProfile.getId());
        auditionApplicationDTO.setFileNames(fileNames);

        // 신청 데이터 저장 및 알람 생성
        // subType을 VIDEO로 설정하여 서비스 메서드 호출
        auditionApplicationService.write(auditionApplicationDTO, AlarmSubType.VIDEO);

        return new RedirectView("/audition/video/detail" + id); // 상세 페이지로
    }

}
