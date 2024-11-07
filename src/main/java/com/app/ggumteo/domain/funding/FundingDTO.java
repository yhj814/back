package com.app.ggumteo.domain.funding;

import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostVO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private String fundingContent;
    private Long memberProfileId; // 회원 프로필 ID
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
    private String profileNickname; // 프로필 닉네임
    private Long memberId; // 회원 ID
    private String profileImgUrl; // 카카오톡 프로필 이미지 url
    private String thumbnailFileName; // 썸네일 파일 경로
    private String thumbnailFilePath; // 썸네일 파일 경로
    private Long thumbnailFileId; // 썸네일 파일 ID (추가된 필드)
    private List<Long> ids; // 삭제할 파일 id 목록
    private List<FundingProductVO> fundingProducts; // 펀딩 상품 목록
    private double fundingPercentage;
    private String endDate; // created_date에서 한 달 더한 값
    private String profileEmail;
    private String profileEtc;



    public FundingVO toVO() {
        return new FundingVO(id, genreType, investorNumber, targetPrice, convergePrice, fileContent, fundingStatus, fundingContent, createdDate, updatedDate);
    }
}
