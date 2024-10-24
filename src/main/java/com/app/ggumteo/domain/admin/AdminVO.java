package com.app.ggumteo.domain.admin;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String adminVerifyCode;
}
