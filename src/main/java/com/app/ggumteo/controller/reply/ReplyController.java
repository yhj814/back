package com.app.ggumteo.controller.reply;

import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/replies/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> addReply(@RequestBody ReplyDTO replyDTO) {
        try {
            replyService.insertReply(replyDTO);
            return ResponseEntity.ok().body(Map.of("message", "댓글 작성 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "댓글 작성 중 오류가 발생했습니다."));
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId) {
        try {
            replyService.deleteReplyById(replyId);
            return ResponseEntity.ok().body(Map.of("message", "댓글 삭제 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "댓글 삭제 중 오류가 발생했습니다."));
        }
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<?> getRepliesByWorkId(@RequestParam Long workId, @RequestParam(defaultValue = "1") int page) {
        try {
            List<ReplyDTO> replies = replyService.selectRepliesByWorkId(workId);
            int totalReplies = replyService.countRepliesByWorkId(workId);
            double averageStar = replyService.selectAverageStarByWorkId(workId);

            Map<String, Object> response = new HashMap<>();
            //해쉬 맵 선언하고 예를들어 replies라는 이름으로 replies 사용, totalCount라는 이름으로 totalReplies 사용...
            response.put("replies", replies);
            response.put("totalCount", totalReplies);
            response.put("averageStar", averageStar);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "댓글 조회 중 오류가 발생했습니다."));
        }
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
