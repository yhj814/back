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
    // 추가(마이페이지)
    private String auditionField; // 모집 분야
    private String auditionCareer; // 모집 경력
    private String expectedAmount; // 예상 금액
    private String auditionDeadLine; // 모집 마감날짜
    private String auditionPersonnel; // 모집 인원
    private String auditionLocation; // 모집 장소
    private String auditionStatus; // 모집 상태
    private String postTitle;
    private String postContent;

    public AuditionApplicationVO toVO() {
        return new AuditionApplicationVO(id, memberProfileId, applyEtc, auditionId, createdDate, confirmStatus);
    }
}
