package com.app.ggumteo.controller.reply;

import com.app.ggumteo.domain.member.MemberProfileDTO;
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
import org.springframework.http.HttpStatus;
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

    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody ReplyDTO replyDTO) {
        MemberProfileDTO memberProfile = (MemberProfileDTO) session.getAttribute("memberProfile");

        if (memberProfile == null) {
            log.warn("로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        replyDTO.setProfileNickname(memberProfile.getProfileNickName());
        replyDTO.setMemberProfileId(memberProfile.getId());

        if (replyDTO.getWorkId() == null) {
            log.error("workId is null");
            return ResponseEntity.badRequest().body("workId는 필수 입력값입니다.");
        }

        try {
            replyService.insertReply(replyDTO);
            log.info("댓글이 성공적으로 작성되었습니다. ReplyDTO: {}", replyDTO);
            return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            log.error("댓글 작성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReply(@PathVariable Long id) {
        try {
            replyService.deleteReplyById(id);
            log.info("댓글이 성공적으로 삭제되었습니다. ID: {}", id);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("{workId}/{page}")
    public ResponseEntity<ReplyListDTO> getRepliesByWorkId(@PathVariable("workId") Long workId,
                                                           @PathVariable("page") int page,
                                                           Pagination pagination) {
        ReplyListDTO replies = replyService.selectRepliesByWorkId(page, pagination, workId);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/count/{workId}")
    public ResponseEntity<Integer> getReplyCount(@PathVariable Long workId) {
        int count = replyService.countRepliesByWorkId(workId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-star/{workId}")
    public ResponseEntity<Double> getAverageStar(@PathVariable Long workId) {
        double averageStar = replyService.selectAverageStarByWorkId(workId);
        return ResponseEntity.ok(averageStar);
    }
}
