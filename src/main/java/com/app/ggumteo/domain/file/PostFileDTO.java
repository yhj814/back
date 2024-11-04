package com.app.ggumteo.domain.file;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostFileDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private String fileSize;
    private String createdDate;
    private String updatedDate;
    private Long postId;



    public PostFileVO toVO(){
        return new PostFileVO(id, postId);
    }
}
