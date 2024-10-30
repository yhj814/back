package com.app.ggumteo.controller.reply;

import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.reply.ReplyListDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.service.reply.ReplyService;
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
    private final ReplyDTO replyDTO;
    private final HttpSession session;

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

    // 댓글 작성
    @PostMapping("write")
    public void write(@RequestBody ReplyDTO replyDTO) {

        log.info("들어옴!!");
        log.info(replyDTO.toString());
        replyService.insertReply(replyDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public void deleteReply(@PathVariable Long id) {
       replyService.deleteReplyById(id);
    }

    // 댓글 조회
    @GetMapping("{workId}/{page}")
    public ReplyListDTO getRepliesByWorkId(@PathVariable("workId") Long workId,
                                           @PathVariable("page") int page,
                                           Pagination pagination,
                                           Model model) {
        return replyService.selectRepliesByWorkId(page, pagination, workId);
    }

    // 작품의 평균 별점 조회
    @GetMapping("/average-star")
    public ResponseEntity<?> getAverageStarByWorkId(@RequestParam Long workId) {
        try {
            double averageStar = replyService.selectAverageStarByWorkId(workId);
            return ResponseEntity.ok(Map.of("averageStar", averageStar));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "별점 평균 조회 중 오류가 발생했습니다."));
        }
    }
}
