package com.app.ggumteo.domain.post;

import com.app.ggumteo.constant.PostType;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostVO {
    @EqualsAndHashCode.Include
    private Long id; // 포스트 ID
    private String postTitle; // 포스트 제목
    private String postContent; // 포스트 내용
    private String postType; // 포스트 타입 (영상, 글, 문의사항 등)
    private Long memberProfileId;
    private String createdDate; // 생성 날짜
    private String updatedDate; // 수정 날짜
}
