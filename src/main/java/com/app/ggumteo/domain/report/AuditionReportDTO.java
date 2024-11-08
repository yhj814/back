package com.app.ggumteo.domain.report;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionReportDTO {
    @EqualsAndHashCode.Include
    private Long postId;
    private String profileName;
    private String createdDate;
    private String postType;
    private int auditionField;  // 영상 모집분야는 1 : 배우 , 2 : 스텝, 3 : 감독, 4 : 기타
                                // 글 모집분야는   1 : 썸네일 , 2 : 일러스트, 3 : 웹툰, 4 : 기타
    private String postTitle;
    private String applicationCount; // 지원자수 tbl_audition_application 테이블에 각 audition_id에 신청한 id 개수
    private String auditionDeadline; // 모집 종료일

    // 모달창에 보여야 할거
    private String reportProfileName;       // 신고한사람
    private String reportProfileEmail;      // 신고한사람 연락받을 이메일
    private String reportStatus;            // 신고 상태 (tbl_audition_report 테이블의 report_status), Enum 값으로 관리
    private String reportCreatedDate;       // 신고 작성일 (tbl_audition_report 테이블의 created_date)
    private String reportContents;          // 신고 내용 (tbl_audition_report 테이블의 report_contents)
}
