package com.app.ggumteo.domain.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuditionApplicationFileDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private String fileSize;
    private String createdDate;
    private String updatedDate;
    private Long auditionApplicationId;

    public AuditionApplicationFileDTO(Long id, String fileName, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public AuditionApplicationFileVO toVO() { return  new AuditionApplicationFileVO(id, auditionApplicationId);};
}
