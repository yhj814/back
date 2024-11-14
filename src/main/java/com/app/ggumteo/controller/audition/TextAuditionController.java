package com.app.ggumteo.controller.audition;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.constant.RedirectPaths;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.pagination.AuditionPagination;
import com.app.ggumteo.search.Search;
import com.app.ggumteo.service.alarm.AlarmService;
import com.app.ggumteo.service.audition.AuditionApplicationService;
import com.app.ggumteo.service.audition.AuditionService;
import com.app.ggumteo.service.file.AuditionApplicationFileService;
import com.app.ggumteo.service.file.PostFileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/audition/text/*")
@RequiredArgsConstructor
public class TextAuditionController {

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
            log.info("로그인 상태 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfile != null ? memberProfile.getId() : "null");
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
            log.error("파일 업로드 중 오류 발생: ", e);
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
            log.error("파일 업로드 중 오류 발생: ", e);
            return "error";
        }
    }


    @GetMapping("write")
    public String goToWritePage() {
        log.info("작성 페이지로 이동");
        return "/audition/text/write";
    }

    @PostMapping("write")
    public RedirectView write(AuditionDTO auditionDTO,
                              @RequestParam(value = "fileNames", required = false) List<String> fileNames,
                              Model model) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

            if (member == null || memberProfile == null) {
                log.error("세션에 사용자 정보가 없습니다.");
                model.addAttribute("error", "세션에 사용자 정보가 없습니다.");
                return new RedirectView("/error");
            }

            auditionDTO.setPostType(PostType.AUDITIONTEXT.name());
            auditionDTO.setAuditionStatus("YES");
            auditionDTO.setMemberProfileId(memberProfile.getId());
            auditionDTO.setMemberId(member.getId());

            log.info("write 메서드 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), auditionDTO.getMemberProfileId());

            auditionDTO.setFileNames(fileNames);

            auditionService.write(auditionDTO);

            return new RedirectView("/audition/text/detail/" + auditionDTO.getId());
        } catch (Exception e) {
            log.error("오류 발생", e);
            model.addAttribute("error", "저장 중 오류가 발생했습니다.");
            return new RedirectView("/error");
        }
    }

    @GetMapping("/modify/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        AuditionDTO audition = auditionService.findAuditionById(id);
        List<PostFileDTO> existingFiles = postFileService.findFilesByPostId(id);

        if (audition != null) {
            log.info("Fetched audition - ID: {}", audition.getId());
            model.addAttribute("audition", audition);
            model.addAttribute("existingFiles", existingFiles);
            return "/audition/text/modify";
        } else {
            log.error("해당 게시글을 찾을 수 없습니다. ID: {}", id);
            model.addAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "/audition/text/error";
        }
    }


    @PostMapping("/modify")
    public RedirectView updateAudition(
            @ModelAttribute AuditionDTO auditionDTO,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            Model model) {
        try {
            log.info("수정 요청 - AuditionDTO 정보: {}", auditionDTO);
            log.info("수정 요청 - 새 파일 목록: {}", fileNames);
            log.info("수정 요청 - 삭제할 파일 ID 목록: {}", deletedFileIds);

            AuditionDTO currentAudition = auditionService.findAuditionById(auditionDTO.getId());
            auditionDTO.setPostType(PostType.AUDITIONTEXT.name());
            auditionDTO.setAuditionStatus("모집중");
            if (currentAudition != null) {
                log.info("게시글 id:{}", currentAudition.getId());
            }

            // 업로드된 파일명 처리 로직 추가
            if (fileNames != null && !fileNames.isEmpty()) {
                auditionDTO.setFileNames(fileNames);
            }

            auditionService.updateAudition(auditionDTO, deletedFileIds);

            log.info("업데이트 성공 - AuditionDTO ID: {}", auditionDTO.getId());

            model.addAttribute("audition", auditionDTO);

            return new RedirectView("/audition/text/detail/" + auditionDTO.getId());
        } catch (Exception e) {
            log.error("오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return new RedirectView("/audition/text/error");
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

        int totalSearchAudition = auditionService.findTotalAuditionsSearch(PostType.AUDITIONTEXT, search);
        pagination.setTotal(totalSearchAudition);
        pagination.progress();

        List<AuditionDTO> auditions = auditionService.findAllAuditions(PostType.AUDITIONTEXT, search, pagination);

        model.addAttribute("auditions", auditions);
        model.addAttribute("search", search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("totalSearchAudition", totalSearchAudition);

        return "/audition/text/list";
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

        return "/audition/text/detail";
    }

    @GetMapping("/application/{id}")
    public String application(@PathVariable("id") Long id, Model model) {
        // 세션에서 멤버 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        log.info("application 메서드 - 사용자 ID: {}, 프로필 ID: {}, 프로필 이름: {}",
                member != null ? member.getId() : "null",
                memberProfile != null ? memberProfile.getId() : "null",
                memberProfile != null ? memberProfile.getProfileName() : "null");

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

        log.info("오디션 ID: {}", id);

        return "audition/text/application"; // 신청서 작성 페이지로 이동
    }


    @PostMapping("/application/{id}")
    public RedirectView submitApplication(
            @PathVariable("id") Long id,
            @RequestParam(value = "fileNames", required = false) List<String> fileNames,
            AuditionApplicationDTO auditionApplicationDTO,
            Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        if (member == null || memberProfile == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return new RedirectView("/login");
        }

        log.info("submitApplication 메서드 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfile.getId());
        log.info("받아온 Audition ID: {}", id);
        log.info("받아온 fileNames: {}", fileNames);

        // 신청 데이터 설정
        auditionApplicationDTO.setAuditionId(id);
        auditionApplicationDTO.setMemberProfileId(memberProfile.getId());
        auditionApplicationDTO.setFileNames(fileNames);

        // 신청 데이터 저장 및 알람 생성
        auditionApplicationService.write(auditionApplicationDTO, AlarmSubType.TEXT);

        log.info("Audition application processed and alarm created.");

        return new RedirectView("/audition/text/detail/" + id);
    }
}
