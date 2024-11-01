package com.app.ggumteo.controller.reply;

import com.app.ggumteo.domain.member.MemberProfileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.reply.ReplyListDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.service.reply.ReplyService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/replies/*")
@Slf4j
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final HttpSession session;

    @ModelAttribute
    public void setTestMemberProfile(HttpSession session) {
        if (session.getAttribute("memberProfile") == null) {
            session.setAttribute("memberProfile", new MemberProfileVO(2L, "", "", "", 30, "", "", "", 2L, "", ""));
        }
    }

    @PostMapping("write")
    public void write(@RequestBody ReplyDTO replyDTO) {
        MemberProfileVO memberProfile = (MemberProfileVO) session.getAttribute("memberProfile");

        if (memberProfile == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        replyDTO.setProfileNickname(memberProfile.getProfileNickName());
        replyDTO.setMemberProfileId(memberProfile.getId());
        replyService.insertReply(replyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteReply(@PathVariable Long id) {
        replyService.deleteReplyById(id);
    }

    @GetMapping("{workId}/{page}")
    public ReplyListDTO getRepliesByWorkId(@PathVariable("workId") Long workId,
                                           @PathVariable("page") int page,
                                           Pagination pagination) {
        return replyService.selectRepliesByWorkId(page, pagination, workId);
    }

    @GetMapping("/count/{workId}")
    public int getReplyCount(@PathVariable Long workId) {
        return replyService.countRepliesByWorkId(workId);
    }

    @GetMapping("/average-star/{workId}")
    public double getAverageStar(@PathVariable Long workId) {
        return replyService.selectAverageStarByWorkId(workId);
    }
}
