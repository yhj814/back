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
    private final ReplyDTO replyDTO;
    private final HttpSession session;

    // 모든 요청 전 세션에 테스트용 사용자 정보 설정
    @ModelAttribute
    public void setTestMemberProfile(HttpSession session) {
        if (session.getAttribute("memberProfile") == null) {
            session.setAttribute("memberProfile", new MemberProfileVO(
                    2L,
                    "",
                    "",
                    "",
                    30,
                    "",
                    "",
                    "",
                    2L,
                    "",    // createdDate (example)
                    ""     // updatedDate (example)
            ));
        }
    }


    // 댓글 작성
    @PostMapping("write")
    public void write(@RequestBody ReplyDTO replyDTO, HttpSession session) {
        MemberProfileVO memberProfile = (MemberProfileVO) session.getAttribute("memberProfile");

        if (memberProfile == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        replyDTO.setProfileNickname(memberProfile.getProfileNickName());
        replyDTO.setMemberProfileId(memberProfile.getId());  // member_profile_id 설정
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
    // 댓글 수 조회
    @GetMapping("/count")
    public ResponseEntity<?> countRepliesByWorkId(@RequestParam Long workId) {
        try {
            int replyCount = replyService.countRepliesByWorkId(workId);
            return ResponseEntity.ok(Map.of("totalCount", replyCount));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "댓글 수 조회 중 오류가 발생했습니다."));
        }
    }

}
