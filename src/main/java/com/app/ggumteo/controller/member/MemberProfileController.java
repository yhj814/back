package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.file.ProfileFileDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.service.file.ProfileFileServiceImpl;
import com.app.ggumteo.service.member.MemberProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberProfileController {
    private final MemberProfileService memberProfileService;
    private final HttpSession session;
    private final ProfileFileServiceImpl profileFileServiceImpl;

    @GetMapping("/sign-up")
    public String goSignUp(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        boolean isLoggedIn = member != null;

        model.addAttribute("isLoggedIn", isLoggedIn);
        log.info("회원가입 페이지로 이동. 로그인 상태: {}", isLoggedIn);
        log.info("세션 ID (회원가입 페이지): {}", session.getId()); // 세션 ID 로그 추가

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(HttpServletRequest request, MemberProfileDTO memberProfileDTO,
                         @RequestParam(value = "profileFile", required = false) MultipartFile profileFile) {
        try {
            // 세션에서 'member' 객체 가져오기
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버가 없습니다. 요청 URI: {}", request.getRequestURI());
                log.info("세션 ID (회원가입 처리): {}", session.getId());
                return "redirect:/sign-up";
            }

            // 'memberId' 설정
            Long memberId = member.getId();
            memberProfileDTO.setMemberId(memberId); // member 객체의 id를 memberProfileDTO의 memberId로 설정
            log.info("설정된 memberId: {}", memberId);

            // 프로필 정보 DB 저장
            memberProfileService.write(memberProfileDTO);
            log.info("프로필 정보 DB 저장 완료.");
            log.info("세션 ID (회원가입 처리): {}", session.getId());

            // 파일이 있는 경우 파일 저장 로직 호출
            if (profileFile != null && !profileFile.isEmpty()) {
                ProfileFileDTO profileFileDTO = new ProfileFileDTO();
                profileFileDTO.setMemberProfileId(memberId); // 파일에 memberId 설정
                profileFileServiceImpl.saveProFile(profileFile, profileFileDTO.getId());
                log.info("첨부 파일 저장 완료.");
            } else {
                log.info("첨부 파일 없음. 파일 저장 과정 생략.");
            }

            log.info("작성 완료: 회원 프로필이 성공적으로 제출되었습니다.");
            return "redirect:/main";
        } catch (Exception e) {
            log.error("작성 실패: 글 저장 중 오류 발생. 요청 URI: {}", request.getRequestURI(), e);
            return "redirect:/sign-up";
        }
    }

}
