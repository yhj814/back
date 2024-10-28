package com.app.ggumteo.domain.member;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;

    private String memberEmail;
    private String memberStatus;
    private String profileImgUrl;
    private String createdDate;
    private String updatedDate;
}
