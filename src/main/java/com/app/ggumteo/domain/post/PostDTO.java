package com.app.ggumteo.domain.post;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @EqualsAndHashCode.Include
    private Long id; // 포스트 ID
    private String postTitle; // 포스트 제목
    private String postContent; // 포스트 내용
    private String postType; // 포스트 타입 (영상, 글, 문의사항 등)
    private Long memberProfileId; // 회원 ID
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
    private List<Long> ids; // 삭제할 파일 id 목록

    public PostVO toVO() {
        return new PostVO(id, postTitle, postContent, postType, memberProfileId, createdDate, updatedDate);
    }
}
