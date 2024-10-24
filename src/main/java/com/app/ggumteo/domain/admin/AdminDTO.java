package com.app.ggumteo.domain.admin;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO implements Serializable {
    private Long id;
    private String adminVerifyCode;

    public AdminVO toVO() {
        return new AdminVO(id, adminVerifyCode);
    }
}

