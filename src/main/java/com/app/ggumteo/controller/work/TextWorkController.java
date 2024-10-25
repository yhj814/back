package com.app.ggumteo.controller.work;


import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.work.WorkService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class TextWorkController {
    private final WorkService workService;
    private final HttpSession session;
    private final PostFileService postFileService;

    // 모든 요청 전 세션에 테스트용 사용자 정보 설정
    @ModelAttribute
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(
                    2L,
                    "",      // memberName
                    "",         // memberEmail
                    "",
                    "",              // profileUrl
                    "",          // createdDate
                    ""          // updatedDate
            ));
        }
    }

    // write 화면 이동
    @GetMapping("write")
    public void goToWriteForm(WorkDTO workDTO) {




        log.info("세션에 설정된 memberId: " + ((MemberVO) session.getAttribute("member")).getId());}
    // 글 쓰기 후 처리
    @PostMapping("write")
    public ResponseEntity<?> write(WorkDTO workDTO, @RequestParam("workFile") MultipartFile workFile,
                                   @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        try {
            // 세션에서 멤버 정보 가져오기
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("멤버 정보가 세션에 없습니다.");

            }

            // WorkDTO에 세션에서 가져온 멤버 ID 설정
            workDTO.setMemberId(member.getId());
            workDTO.setPostType("TEXT");  // postType은 TEXT로 고정

            // 글과 워크 데이터 저장 (tbl_post, tbl_work)
            workService.write(workDTO);

            // 파일 저장 처리 (작품 파일)
            if (!workFile.isEmpty()) {
                FileVO savedFile = postFileService.saveFile(workFile);
                PostFileVO postFileVO = new PostFileVO();
                postFileVO.setId(savedFile.getId());
                postFileVO.setPostId(workDTO.getId());
                postFileService.savePostFile(postFileVO);
            }

            // 썸네일 파일 저장 처리
            if (!thumbnailFile.isEmpty()) {
                // 썸네일 파일 저장 로직 추가
                FileVO savedThumbnail = postFileService.saveFile(thumbnailFile);
                // 썸네일과 관련된 추가 처리 로직
            }


            // 성공 응답 (JSON 반환)
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }


}