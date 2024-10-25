package com.app.ggumteo.domain.file;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String fileName;
    private String fileSize;
    private String fileType;
    private String filePath;
    private String createdDate;
    private String updatedDate;
}
