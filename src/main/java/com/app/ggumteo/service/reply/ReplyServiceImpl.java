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

    @Override
    public void insertReply(ReplyDTO replyDTO) {
        replyDAO.insertReply(replyDTO);
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
