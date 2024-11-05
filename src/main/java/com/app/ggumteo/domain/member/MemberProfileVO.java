package com.app.ggumteo.domain.member;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String profileName;
    private String profileNickName;
    private String profileGender;
    private int profileAge;
    private String profileEmail;
    private String profilePhone;
    private String profileEtc;
    private Long memberId;
    private String createdDate;
    private String updatedDate;

    public MemberProfileDTO toDTO() {
        MemberProfileDTO dto = new MemberProfileDTO();
        dto.setId(this.id);
        dto.setProfileName(this.profileName);
        dto.setProfileNickName(this.profileNickName);
        dto.setProfileGender(this.profileGender);
        dto.setProfileAge(this.profileAge);
        dto.setProfileEmail(this.profileEmail);
        dto.setProfilePhone(this.profilePhone);
        dto.setProfileEtc(this.profileEtc);
        dto.setMemberId(this.memberId);
        dto.setCreatedDate(this.createdDate);
        dto.setUpdatedDate(this.updatedDate);
        return dto;
    }
}
