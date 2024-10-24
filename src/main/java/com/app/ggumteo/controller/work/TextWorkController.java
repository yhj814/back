package com.app.ggumteo.controller.work;


import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.service.work.WorkService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class TextWorkController {
    private final WorkMapper workMapper;
    private final WorkService workService;
    private final HttpSession session;

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
    public String write(WorkDTO workDTO) {


        return "redirect:/text/list";
    }
}
