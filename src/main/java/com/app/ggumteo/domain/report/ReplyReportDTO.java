package com.app.ggumteo.domain.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReplyReportDTO implements Serializable {
    @EqualsAndHashCode.Include
    private long replyId;                   // 댓글 id  (tbl_reply 테이블)
    private String postTitle;               // 게시글 제목 (tbl_post 테이블)
    private String postType;                // 게시글 종류 (tbl_post 테이블)
    private String replyContent;            // 댓글 내용 (tbl_reply 테이블)
    private String profileName;             // 댓글 단 사람 (tbl_reply 테이블에 member_profile_id 로 tbl_member_profile 테이블에 profileName 가져오기)
    private String replyCreatedDate;        // 댓글 단 시간 (tbl_reply 테이블)
    private String reportStatus;            // 신고 상태 (Enum 값으로 관리)

    // 신고내역 모달창에 필요한 정보
    private String reportProfileName;       // 신고한사람 (tbl_reply_report 테이블에 member_profile_id 로 tbl_member_profile 테이블에 profileName 가져오기)
    private String reportProfileEmail;      // 신고한사람 연락받을 이메일 (tbl_reply_report 테이블에 member_profile_id 로 tbl_member_profile 테이블에 profileEmail 가져오기)
    private String reportCreatedDate;       // 신고 작성일 (tbl_reply_report 테이블의 created_date)
    private String reportContents;          // 신고내용 (tbl_reply_report 테이블)
}
