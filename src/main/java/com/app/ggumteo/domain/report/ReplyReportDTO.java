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
    private long replyId;                   // 댓글 id (tbl_reply 테이블)
    private String postTitle;               // 게시글 제목 (tbl_post 테이블)
    private String postType;                // 게시글 종류 (tbl_post 테이블)
    private String replyContent;            // 댓글 내용 (tbl_reply 테이블)
    private String profileName;             // 댓글 단 사람 (tbl_reply 테이블에 member_profile_id로 tbl_member_profile 테이블에서 profileName 가져오기)
    private String replyCreatedDate;        // 댓글 작성일 (tbl_reply 테이블)
    private String reportStatus;            // 신고 상태 (Enum 값으로 관리)

    // 신고내역 모달창에 필요한 정보
    private String reportProfileName;       // 신고한 사람 (tbl_reply_report 테이블에 member_profile_id로 tbl_member_profile 테이블에서 profileName 가져오기)
    private String reportProfileEmail;      // 신고한 사람 이메일 (tbl_reply_report 테이블에 member_profile_id로 tbl_member_profile 테이블에서 profileEmail 가져오기)
    private String reportCreatedDate;       // 신고 작성일 (tbl_reply_report 테이블의 created_date)
    private String reportContents;          // 신고 내용 (tbl_reply_report 테이블)

    // 작품 정보
    private long workId;                    // 작품 ID (tbl_work 테이블)
    private String workPrice;               // 작품 가격 (tbl_work 테이블)
    private String genreType;               // 작품 장르 (tbl_work 테이블)
}
