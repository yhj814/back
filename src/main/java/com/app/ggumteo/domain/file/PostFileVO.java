package com.app.ggumteo.domain.file;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostFileVO {
    @EqualsAndHashCode.Include
    private Long id;
    private Long fileId;
    private Long postId;


    public PostFileVO(Long postId, Long fileId) {
        this.postId = postId;
        this.fileId = fileId;
    }

}
