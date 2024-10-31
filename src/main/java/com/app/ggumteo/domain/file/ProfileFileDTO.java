package com.app.ggumteo.domain.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    // 필드 값을 초기화하는 생성자 추가
    public ProfileFileDTO(Long id, String fileName, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public ProfileFileVO toVO() {
        return new ProfileFileVO(id, memberProfileId);
    }
}
