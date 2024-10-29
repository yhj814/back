package com.app.ggumteo.controller.member;

import com.app.ggumteo.domain.file.ProfileFileDTO;
import com.app.ggumteo.domain.member.MemberProfileDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
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
    private final MemberProfileVO memberProfileVO;
    private final MemberProfileDTO memberProfileDTO;
    private final ProfileFileServiceImpl profileFileServiceImpl;
    private final ProfileFileDTO profileFileDTO;

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
        }
    }

    @GetMapping("sign-up")
    public void goSingUp(){;}

    @PostMapping("sign-up")
    public ResponseEntity<?> singUp(MemberProfileDTO MemberProfileDTO, @RequestParam("profileFile") MultipartFile profileFile){

        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }

            memberProfileDTO.setMemberId(member.getId());

            if (!profileFile.isEmpty()) {
                profileFileServiceImpl.saveProFile(profileFile, profileFileDTO.getId());
            }

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
