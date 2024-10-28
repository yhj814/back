package com.app.ggumteo.domain.file;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFileVO {
    @EqualsAndHashCode.Include
    private Long id;
    private Long memberProfileId;
}
