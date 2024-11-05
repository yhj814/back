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
public class WorkReportDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Long postId;                    // 게시글의 고유 ID (tbl_post 테이블의 id)
    private String postTitle;               // 게시글 제목 (tbl_post 테이블의 post_title)
    private String postType;                // 게시글 유형 (영상, 글, 문의사항 등) (tbl_post 테이블의 post_type)

    private String profileName;             // 작성자 프로필 이름 (tbl_member_profile 테이블의 profile_name)
    private String profileEmail;            // 신고한 사람의 이메일 (tbl_member_profile 테이블의 profile_email)

    private String postCreatedDate;         // 게시글 작성일 (tbl_post 테이블의 created_date)
    private String genreType;               // 장르 유형 (tbl_work 테이블의 genre_type)
    private Integer readCount;              // 조회수 (tbl_work 테이블의 read_count)
    private double star;                    // 별점 (tbl_reply 테이블의 star 값), 소수점으로 표시 가능
    private String workPrice;               // 작품 가격 (tbl_work 테이블의 work_price)

    private String reportProfileName;       // 신고한사람
    private String reportProfileEmail;      // 신고한사람 연락받을 이메일
    private String reportStatus;            // 신고 상태 (tbl_work_report 테이블의 report_status), Enum 값으로 관리
    private String reportCreatedDate;       // 신고 작성일 (tbl_work_report 테이블의 created_date)
    private String reportContents;          // 신고 내용 (tbl_work_report 테이블의 report_contents)
}