package com.app.ggumteo.domain.member;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileVO implements Serializable {
    private Long Id;
    private String profileGender;
    private String profileAge;
    private String profileEmail;
    private String profilePhone;
    private String profileEtc;
    private String memberId;
    private String updatedDate;
}
