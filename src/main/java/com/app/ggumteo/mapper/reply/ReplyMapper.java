package com.app.ggumteo.mapper.reply;

import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {

    // 댓글 삽입
    void insertReply(ReplyDTO replyDTO);

    // 댓글 삭제
    void deleteReplyNotificationsByReplyId(@Param("replyId") Long replyId);
    void deleteReplyById(@Param("id") Long id);

    // 특정 작품에 대한 댓글 목록 조회
    List<ReplyDTO> selectRepliesByWorkId(@Param("workId") Long workId, @Param("pagination") Pagination pagination);

    // 작품의 평균 별점 조회
    Double selectAverageStarByWorkId(@Param("workId") Long workId);
    // 특정 작품에 대한 댓글 수 조회
    int countRepliesByWorkId(@Param("workId") Long workId);

    // 마이페이지 - 내 단 리뷰 총 갯수
    public int selectCountByMemberId(Long memberId, String postType);
}
