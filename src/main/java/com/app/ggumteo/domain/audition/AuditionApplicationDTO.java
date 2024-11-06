package com.app.ggumteo.domain.audition;

import lombok.*;
import org.springframework.stereotype.Component;

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
    private String fileName;
    private String fileType;
    private String fileSize;
    private String filePath;
    private String createdDate;
    private String updatedDate;
}
