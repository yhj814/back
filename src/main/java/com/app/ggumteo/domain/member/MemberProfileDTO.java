package com.app.ggumteo.domain.member;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String profileName;
    private String memberEmail;
    private String profileNickName;
    private String profileGender;
    private int profileAge;
    private String profileEmail;
    private String profilePhone;
    private String profileEtc;
    private String memberStatus;
    private Long memberId;
    private String createdDate;
    private String updatedDate;

    public MemberProfileVO toVO() {
        return new MemberProfileVO(id, profileName, profileNickName, profileGender, profileAge, profileEmail, profilePhone, profileEtc, memberId, createdDate, updatedDate);
    }
}
