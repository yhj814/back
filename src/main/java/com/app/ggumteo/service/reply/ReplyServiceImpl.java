package com.app.ggumteo.service.reply;

import com.app.ggumteo.constant.AlarmSubType;
import com.app.ggumteo.domain.post.PostDTO;
import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.reply.ReplyListDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.post.PostDAO;
import com.app.ggumteo.repository.reply.ReplyDAO;
import com.app.ggumteo.repository.work.WorkDAO;
import com.app.ggumteo.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ReplyServiceImpl implements ReplyService {

    private final ReplyDAO replyDAO;
    private final AlarmService alarmService;
    private final WorkDAO workDAO;
    private final PostDAO postDAO;

    @Override
    public void insertReply(ReplyDTO replyDTO) {
        // 댓글 삽입
        replyDAO.insertReply(replyDTO);
        log.info("Inserted ReplyDTO with ID: {}", replyDTO.getId());

        // 댓글 ID 확인
        if (replyDTO.getId() == null) {
            log.error("Reply ID is null after insertion. Cannot create alarm.");
            return;
        }

        // Work ID 확인
        Long workId = replyDTO.getWorkId();
        if (workId == null) {
            log.error("Work ID is null in ReplyDTO. Cannot create alarm.");
            return;
        }

        // Post 정보 조회 (workId를 postId로 사용)
        Long postId = workId;
        PostDTO post = postDAO.findPostById(postId);
        if (post == null) {
            log.warn("Cannot create alarm: Post not found with ID: {}", postId);
            return;
        }
        log.info("Fetched PostDTO: {}", post);

        // 알람 생성
        Long recipientMemberProfileId = post.getMemberProfileId();
        String postType = post.getPostType();

        AlarmSubType subType;
        if ("WORKTEXT".equalsIgnoreCase(postType)) {
            subType = AlarmSubType.TEXT;
        } else if ("WORKVIDEO".equalsIgnoreCase(postType)) {
            subType = AlarmSubType.VIDEO;
        } else {
            log.warn("Unknown postType: {} for postId: {}", postType, postId);
            return;
        }

        String message = "에 댓글이 달렸습니다.";
        log.info("Creating reply alarm with subType: {}", subType);
        try {
            alarmService.createReplyAlarm(recipientMemberProfileId, replyDTO.getId(), message, subType);
            log.info("Created reply alarm for ReplyID: {}", replyDTO.getId());
        } catch (Exception e) {
            log.error("Failed to create reply alarm for ReplyID: {}. Error: {}", replyDTO.getId(), e.getMessage());
            // 필요에 따라 예외를 재던지거나, 알림 생성 실패를 무시할 수 있음
        }
    }


    @Override
    public void deleteReplyById(Long id) {
        replyDAO.deleteReplyById(id);
    }

    @Override
    public ReplyListDTO selectRepliesByWorkId(int page, Pagination pagination, Long workId) {
        ReplyListDTO replyListDTO = new ReplyListDTO();
        pagination.setPage(page);
        pagination.setTotal(replyDAO.countRepliesByWorkId(workId));
        pagination.progress2();

        replyListDTO.setPagination(pagination);
        replyListDTO.setReplies(replyDAO.selectRepliesByWorkId(workId, pagination));
        return replyListDTO;
    }

    @Override
    public Double selectAverageStarByWorkId(Long workId) {
        return replyDAO.selectAverageStarByWorkId(workId);
    }

    @Override
    public int countRepliesByWorkId(Long workId) {
        return replyDAO.countRepliesByWorkId(workId);
    }
}
