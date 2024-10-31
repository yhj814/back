package com.app.ggumteo.service.reply;

import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.reply.ReplyListDTO;
import com.app.ggumteo.pagination.Pagination;

import java.util.List;

public interface ReplyService {

    // 댓글 삽입
    void insertReply(ReplyDTO replyDTO);

    // 댓글 삭제
    void deleteReplyById(Long id);

    // 특정 작품에 대한 댓글 목록 조회
    public ReplyListDTO selectRepliesByWorkId(int page, Pagination pagination, Long workId);

    // 작품의 평균 별점 조회
    Double selectAverageStarByWorkId(Long workId);

    // 특정 작품에 대한 댓글 수 조회
    int countRepliesByWorkId(Long workId);
}
