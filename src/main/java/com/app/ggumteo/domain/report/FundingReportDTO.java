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
public class FundingReportDTO implements Serializable {
    @EqualsAndHashCode.Include

    private Long postId;                    // 게시글의 고유 ID (tbl_post 테이블의 id)
    private String profileName;             // 작성자 프로필 이름
    private String createdDate;             // 글 작성일 (tbl_post 테이블의 created_date)
    private String endDate;                 // 종료일 (tbl_post 테이블의 created_date 의 30일 뒤)
    private String genreType;               // 글 장르
    private String postTitle;               // 게시글 제목 (tbl_post 테이블의 post_title)
    private String postType;                // 게시글 유형
    private int convergePrice;              // 현재까지 모인 금액
    private int targetPrice;                // 목표 금액
    private int investorNumber;             // 현재까지의 투자자수

    //신고모달창에 필요한 정보
    private String reportProfileName;       // 신고한사람
    private String reportProfileEmail;      // 신고한사람 연락받을 이메일
    private String reportStatus;            // 신고 상태 (tbl_funding_report 테이블의 report_status), Enum 값으로 관리
    private String reportCreatedDate;       // 신고 작성일 (tbl_funding_report 테이블의 created_date)
    private String reportContents;          // 신고 내용 (tbl_funding_report 테이블의 report_contents)
}
