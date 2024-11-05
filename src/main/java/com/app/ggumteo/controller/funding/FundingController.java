package com.app.ggumteo.controller.funding;


import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.funding.FundingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/text/funding/*")
@RequiredArgsConstructor
@Slf4j
public class FundingController {
    private final HttpSession session;
    private final PostFileService postFileService;
    private final FundingService fundingService;

    @ModelAttribute
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(3L, "", "", "", "", ""));
        }
        if (session.getAttribute("memberProfile") == null) {
            session.setAttribute("memberProfile", new MemberProfileVO(3L, "", "", "", 99, "", "", "", 3L, "", ""));
        }
    }

    @PostMapping("upload")
    @ResponseBody
    public List<PostFileDTO> upload(@RequestParam("file") List<MultipartFile> files) {
        try {
            return postFileService.uploadFile(files);  // 서비스의 uploadFile 메서드 호출
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return Collections.emptyList();  // 오류 발생 시 빈 리스트 반환
        }
    }

    @GetMapping("write")
    public String goToWriteForm() {
        return "text/funding/funding-write";  // 컨트롤러에서 경로 리턴 대신 뷰 이름만
    }

    @PostMapping("write")
    public ResponseEntity<?> write(FundingDTO fundingDTO, @RequestParam("fundingFile") MultipartFile[] fundingFiles,
                                   @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }
            fundingDTO.setPostType(PostType.TEXT.name());
            fundingDTO.setMemberProfileId(member.getId());

            // Work 저장, 파일은 서비스에서 처리
            fundingService.write(fundingDTO, fundingFiles, thumbnailFile);

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }

}
