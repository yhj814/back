package com.app.ggumteo.domain.audition;

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
    private Long memberId;
    private int auditionField;
    private String auditionCareer;
    private String expectedAmount;
    private String serviceStartDate;
    private String auditionDeadline;
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
                auditionDeadline, auditionPersonnel, auditionLocation, auditionBackground,
                auditionCategory, fileContent, auditionStatus);
    }
}
