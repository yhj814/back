package com.app.ggumteo.domain.funding;

import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostVO;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FundingDTO {
    private Long id; // 펀딩 ID
    private String genreType; // 장르 타입
    private int investorNumber; // 투자자 수
    private int targetPrice; // 목표 금액
    private int convergePrice; // 모인 금액
    private String fileContent; // 파일 설명
    private String fundingStatus; // 펀딩 상태
    private String postTitle; // 포스트 제목
    private String postContent; // 포스트 내용
    private String postType; // 포스트 타입 (영상, 글, 문의사항 등)
    private Long memberProfileId; // 회원 프로필 ID
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
    private String profileNickname; // 프로필 닉네임
    private Long memberId; // 회원 ID
    private String profileImgUrl; // 카카오톡 프로필 이미지 url
    private String fileName; // 파일명
    private String filePath; // 파일 경로


    public FundingVO toVO() {
        return new FundingVO(id, genreType, investorNumber, targetPrice, convergePrice, fileContent, fundingStatus, createdDate, updatedDate);
    }
}
