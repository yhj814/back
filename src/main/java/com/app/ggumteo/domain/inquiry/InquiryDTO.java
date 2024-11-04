package com.app.ggumteo.domain.inquiry;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class InquiryDTO {
    private Long postId;  // 게시물 ID
    private String postTitle;  // 게시물 제목
    private String postContent;  // 게시물 내용
    private String postCreatedDate;  // 게시물 생성 날짜
    private String profileName;  // 프로필 이름
    private String profileEmail;  // 프로필 이메일
    private String inquiryStatus;  // 문의 상태 기본은 NO
    private String answerDate;  // 답변 작성 날짜
    private String answerContent;

    private Long memberId;
    private String profileImgUrl; // 프로필 이미지
    private String profileNickname; // 프로필 닉네임
}


