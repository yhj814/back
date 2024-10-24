package com.app.ggumteo.domain.work;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class WorkDTO {
    private Long id; // 작품 ID
    private String workPrice; // 작품 가격
    private Long genreId; // 장르 ID
    private int readCount; // 조회수
    private Long memberId;
    private String memberName;
    private String postTitle;
    private String postContent;
    private String postType;
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜

    public WorkVO toVO() {
        return new WorkVO(id, workPrice, genreId, readCount, createdDate, updatedDate);
    }
}
