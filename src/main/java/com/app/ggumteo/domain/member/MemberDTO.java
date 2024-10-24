package com.app.ggumteo.domain.member;


import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MemberDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String email;
    private String profileImgUrl;
    private String createdDate;
    private String updatedDate;

    public MemberVO toVO(){
        return new MemberVO(id, name, email, profileImgUrl, createdDate, updatedDate);
    }
}

