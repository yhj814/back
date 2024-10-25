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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class TextWorkController {
    private final WorkMapper workMapper;
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

                    "",              // profileUrl
                    "",          // createdDate
                    ""          // updatedDate
            ));
        }
    }

    // write 화면 이동
    @GetMapping("write")
    public void goToWriteForm(WorkDTO workDTO) {log.info("세션에 설정된 memberId: " + ((MemberVO) session.getAttribute("member")).getId());}
    // 글 쓰기 후 처리
    @PostMapping("write")
    public String write(WorkDTO workDTO, @RequestParam("file") MultipartFile file) {
        try {
            // 세션에서 멤버 정보 가져오기
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("멤버 정보가 세션에 없습니다.");
                return "redirect:/login";
            }

            // WorkDTO에 멤버 ID 설정
            workDTO.setMemberId(member.getId());

            // 글과 워크 데이터 저장 (tbl_post, tbl_work)
            workService.write(workDTO);

            // 파일 저장 처리 및 tbl_post_file 관계 저장
            if (!file.isEmpty()) {
                FileVO savedFile = postFileService.saveFile(file);  // 파일 저장 후 FileVO 반환

                // 파일과 포스트 간의 관계 저장 (tbl_post_file)
                PostFileVO postFileVO = new PostFileVO();
                postFileVO.setId(savedFile.getId());  // tbl_file의 ID
                postFileVO.setPostId(workDTO.getId());  // tbl_post의 ID
                postFileService.savePostFile(postFileVO);  // 관계 저장
            }

            return "redirect:/text/list";  // 성공 후 리다이렉트

        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return "error";  // 에러 발생시 에러 페이지로 이동
        }
    }


}