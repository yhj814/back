package com.app.ggumteo.domain.audition;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
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
}
