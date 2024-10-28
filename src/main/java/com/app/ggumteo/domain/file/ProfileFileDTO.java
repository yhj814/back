package com.app.ggumteo.domain.file;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfileFileDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private String fileSize;
    private String createdDate;
    private String updatedDate;
    private Long memberProfileId;


    public ProfileFileVO toVO(){
        return new ProfileFileVO(id, memberProfileId);
    }
}
