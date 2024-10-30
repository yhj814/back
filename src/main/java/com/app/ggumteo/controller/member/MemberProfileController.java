package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.file.ProfileFileDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.service.file.ProfileFileServiceImpl;
import com.app.ggumteo.service.member.MemberProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @ModelAttribute
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(
                    1L,
                    "",         // memberEmail
                    "",
                    "",              // profileUrl
                    "",          // createdDate
                    ""          // updatedDate
            ));
            log.info("세션에 테스트 멤버 설정 완료.");
        }
    }

    @GetMapping("/sign-up")
    public void goSignUp() {
        log.info("회원가입 페이지로 이동.");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(MemberProfileDTO memberProfileDTO, @RequestParam(value = "profileFile", required = false) MultipartFile profileFile) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }

            // 멤버 ID 설정 및 프로필 정보 DB 저장
            memberProfileDTO.setMemberId(member.getId());
            memberProfileService.write(memberProfileDTO);
            log.info("프로필 정보 DB 저장 완료.");

            // 파일이 있는 경우 파일 저장 로직 호출
            if (profileFile != null && !profileFile.isEmpty()) {
                ProfileFileDTO profileFileDTO = new ProfileFileDTO();
                profileFileDTO.setMemberProfileId(member.getId()); // 파일에 memberId 설정
                profileFileServiceImpl.saveProFile(profileFile, profileFileDTO.getId());
                log.info("첨부 파일 저장 완료.");
            } else {
                log.info("첨부 파일 없음. 파일 저장 과정 생략.");
            }

            log.info("작성 완료: 회원 프로필이 성공적으로 제출되었습니다.");
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("작성 실패: 글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
