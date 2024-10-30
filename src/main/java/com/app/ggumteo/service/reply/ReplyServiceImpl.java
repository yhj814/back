package com.app.ggumteo.service.reply;

import com.app.ggumteo.domain.reply.ReplyDTO;
import com.app.ggumteo.domain.reply.ReplyListDTO;
import com.app.ggumteo.pagination.Pagination;
import com.app.ggumteo.repository.reply.ReplyDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ReplyServiceImpl implements ReplyService {

    private final ReplyDAO replyDAO;

    // 댓글 삽입
    @Override
    public void insertReply(ReplyDTO replyDTO) {
        replyDAO.insertReply(replyDTO);
    }

    // 댓글 삭제
    @Override
    public void deleteReplyById(Long id) {
        replyDAO.deleteReplyById(id);
    }

    // 특정 작품에 대한 댓글 목록 조회
    @Override
    public ReplyListDTO selectRepliesByWorkId(int page, Pagination pagination, Long workId) {
        ReplyListDTO replyListDTO = new ReplyListDTO();
        pagination.setPage(page);
        pagination.setTotal(replyDAO.countRepliesByWorkId(workId));
        pagination.progress();
        replyListDTO.setPagination(pagination);
        replyListDTO.setReplies(replyDAO.selectRepliesByWorkId(workId, pagination));
        return replyListDTO;
    }

    // 작품의 평균 별점 조회
    @Override
    public Double selectAverageStarByWorkId(Long workId) {
        return replyDAO.selectAverageStarByWorkId(workId);
    }

    // 특정 작품에 대한 댓글 수 조회
    @Override
    public int countRepliesByWorkId(Long workId) {
        return replyDAO.countRepliesByWorkId(workId);
    }
}
