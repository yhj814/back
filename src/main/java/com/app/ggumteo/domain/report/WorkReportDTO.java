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

    // 작품신고 목록
    @EqualsAndHashCode.Include
    private Long postId;              // 게시글 ID
    private String profileName;       // 게시글 작성자 이름
    private String createdDate;       // 작성일
    private String genreType;         // 장르 유형 (예: 드라마, 코미디 등)
    private String postTitle;         // 게시글 제목
    private int readCount;            // 조회수
    private double star;              // 평점
    private String workPrice;         // 작품 가격
    private String reportStatus;      // 신고 상태 (ReportStatus Enum 사용)

    // 신고 내역 모달창
    private String reportProfileName;  // 신고자 이름
    private String reportProfileEmail; // 신고자 이메일
    private String reportCreatedDate;  // 신고 생성일
    private String reportContents;     // 신고 내용
}