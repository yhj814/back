package com.app.ggumteo.domain.audition;

import com.app.ggumteo.domain.file.PostFileDTO;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Getter @Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionApplicationDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String applyEtc;
    private Long auditionId;
    private String confirmStatus;
    private Long memberProfileId;
    private String profileName;
    private String profileGender;
    private String profileAge;
    private String profileEmail;
    private String profilePhone;
    private Long fileId;
    private List<PostFileDTO> uploadedFiles;
    private List<MultipartFile> files;
    private MultipartFile auditionFile;
    private List<String> fileNames;
    private String filePath;
    private String createdDate;
    private String updatedDate;
}
