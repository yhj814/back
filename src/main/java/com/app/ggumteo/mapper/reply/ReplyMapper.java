package com.app.ggumteo.mapper.reply;

import com.app.ggumteo.domain.reply.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {

    // 댓글 삽입
    void insertReply(ReplyDTO replyDTO);

    // 댓글 삭제
    void deleteReplyById(@Param("id") Long id);

    // 특정 작품에 대한 댓글 목록 조회
    List<ReplyDTO> selectRepliesByWorkId(@Param("workId") Long workId);

    // 작품의 평균 별점 조회
    Double selectAverageStarByWorkId(@Param("workId") Long workId);
    // 특정 작품에 대한 댓글 수 조회
    int countRepliesByWorkId(@Param("workId") Long workId);
}
