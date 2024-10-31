package com.app.ggumteo.domain.audition;

import com.app.ggumteo.domain.post.PostVO;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class AuditionDTO {
    private Long id;
    private String postTitle;
    private String postContent;
    private String postType;
    private String profileName;
    private String profileNickName;
    private String profileEmail;
    private Long memberProfileId;
    private int auditionField;
    private String auditionCareer;
    private String expectedAmount;
    private String serviceStartDate;
    private String auditionDeadLine;
    private String auditionPersonnel;
    private String auditionLocation;
    private String auditionBackground;
    private String auditionCategory;
    private String fileContent;
    private String auditionStatus;
    private String createdDate;
    private String updatedDate;

    public AuditionVO toVO() {
        return new AuditionVO(id, auditionField, auditionCareer, expectedAmount, serviceStartDate,
                auditionDeadLine, auditionPersonnel, auditionLocation, auditionBackground,
                auditionCategory, fileContent, auditionStatus);
    }

    public PostVO toPostVO() {
        return new PostVO(id, postTitle, postContent, postType, memberProfileId, createdDate,updatedDate);
    }
}
