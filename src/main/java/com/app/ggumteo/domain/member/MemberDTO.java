package com.app.ggumteo.domain.member;


import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MemberDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String memberEmail;
    private String memberStatus;
    private String profileImgUrl;
    private String createdDate;
    private String updatedDate;
    private String profileName;
    private String profileNickName;
    private String profileGender;
    private int profileAge;
    private String profileEmail;
    private String profilePhone;
    private String profileEtc;


    public MemberVO toVO(){
        return new MemberVO(id, memberEmail,memberStatus, profileImgUrl, createdDate, updatedDate);
    }
}

