package com.app.ggumteo.repository.reply;

import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.mapper.reply.ReplyMapper;
import com.app.ggumteo.pagination.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyDAO {

    private final ReplyMapper replyMapper;

    // 댓글 삽입
    public void insertReply(ReplyDTO replyDTO) {
        replyMapper.insertReply(replyDTO);
    }

    // 댓글 삭제
    public void deleteReplyById(Long id) {
        replyMapper.deleteReplyById(id);
    }

    // 특정 작품에 대한 댓글 목록 조회
    public List<ReplyDTO> selectRepliesByWorkId(Long workId, Pagination pagination) {
       return replyMapper.selectRepliesByWorkId(workId, pagination);
    }

    // 작품의 평균 별점 조회
    public Double selectAverageStarByWorkId(Long workId) {
        return replyMapper.selectAverageStarByWorkId(workId);
    }

    // 특정 작품에 대한 댓글 수 조회
    public int countRepliesByWorkId(Long workId) {
        return replyMapper.countRepliesByWorkId(workId);
    }
}
