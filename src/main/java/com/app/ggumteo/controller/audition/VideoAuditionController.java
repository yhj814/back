package com.app.ggumteo.controller.audition;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.audition.AuditionApplicationDTO;
import com.app.ggumteo.domain.audition.AuditionApplicationVO;
import com.app.ggumteo.domain.audition.AuditionDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileDTO;
import com.app.ggumteo.domain.file.AuditionApplicationFileVO;
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
            log.info("로그인 상태 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfile != null ? memberProfile.getId() : "null");
        } else {
            log.info("비로그인 상태입니다.");
        }
    }

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

    @PostMapping("upload-apply")
    @ResponseBody
    public List<AuditionApplicationFileDTO> uploadApply(@RequestParam("file") List<MultipartFile> files) {
        try {
            return auditionApplicationFileService.uploadFile(files);
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("write")
    public String goToWritePage() {
        log.info("작성 페이지로 이동");
        return "/audition/video/write";
    }

    @PostMapping("write")
    public String write(AuditionDTO auditionDTO, @RequestParam("auditionFile") MultipartFile[] auditionFiles, Model model) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

            if (member == null || memberProfile == null) {
                log.error("세션에 사용자 정보가 없습니다.");
                model.addAttribute("error", "세션에 사용자 정보가 없습니다.");
                return "/error";
            }

            auditionDTO.setPostType(PostType.AUDITIONVIDEO.name());
            auditionDTO.setAuditionStatus("YES");
            auditionDTO.setMemberProfileId(memberProfile.getId());
            auditionDTO.setMemberId(member.getId());

            log.info("write 메서드 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), auditionDTO.getMemberProfileId());

            auditionService.write(auditionDTO, auditionFiles);

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
            log.info("Fetched audition - ID: {}", audition.getId());
            model.addAttribute("audition", audition);
            model.addAttribute("existingFiles", existingFiles);
            return "/audition/video/modify";
        } else {
            log.error("해당 게시글을 찾을 수 없습니다. ID: {}", id);
            model.addAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "/audition/video/error";
        }
    }


    @PostMapping("/modify")
    public String updateAudition(
            @ModelAttribute AuditionDTO auditionDTO,
            @RequestParam(value = "newFiles", required = false) List<MultipartFile> newFiles,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds,
            Model model) {
        try {
            log.info("수정 요청 - AuditionDTO 정보: {}", auditionDTO);
            log.info("수정 요청 - 새 파일 목록: {}", newFiles);
            log.info("수정 요청 - 삭제할 파일 ID 목록: {}", deletedFileIds);

            AuditionDTO currentAudition = auditionService.findAuditionById(auditionDTO.getId());
            auditionDTO.setPostType(PostType.AUDITIONVIDEO.name());
            auditionDTO.setAuditionStatus("모집중");
            if (currentAudition != null) {
                log.info("게시글 id:{}", currentAudition.getId());
            }

            auditionService.updateAudition(auditionDTO, newFiles, deletedFileIds);

            log.info("업데이트 성공 - AuditionDTO ID: {}", auditionDTO.getId());

            model.addAttribute("audition", auditionDTO);

            return "redirect:/audition/video/detail/" + auditionDTO.getId();
        } catch (Exception e) {
            log.error("오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "/audition/video/error";
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

        log.info("detail 메서드 - 사용자 ID: {}, 프로필 ID: {}", member != null ? member.getId() : "null", memberProfile != null ? memberProfile.getId() : "null");

        AuditionDTO audition = auditionService.findAuditionById(id);
        List<PostFileDTO> postFiles = auditionService.findAllPostFiles(id);
        log.info("Audition ID: {}", audition.getId());

        int applicantCount = auditionApplicationService.countApplicantsByAuditionId(id);
        log.info("지원자 수 - 모집글 ID: {}, 지원자 수: {}", id, applicantCount);

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

        return "audition/video/application"; // 신청서 작성 페이지로 이동
    }


    @PostMapping("/application/{id}")
    public String submitApplication(
            @PathVariable("id") Long id,
            AuditionApplicationDTO auditionApplicationDTO,
            Model model) {
        // 세션에서 멤버 정보 가져오기
        MemberVO member = (MemberVO) session.getAttribute("member");
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        if (member == null || memberProfile == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        log.info("submitApplication 메서드 - 사용자 ID: {}, 프로필 ID: {}", member.getId(), memberProfile.getId());
        log.info("받아온 Audition ID: {}", id);

        // 신청 데이터 설정
        auditionApplicationDTO.setAuditionId(id);
        auditionApplicationDTO.setMemberProfileId(memberProfile.getId());

        log.info("모집 ID: {}", auditionApplicationDTO.getAuditionId());

        // 신청 데이터 저장
        auditionApplicationService.write(auditionApplicationDTO);

        return "redirect:/audition/video/detail/{id}"; // 신청 성공 후 리스트 페이지로 이동
    }




}
