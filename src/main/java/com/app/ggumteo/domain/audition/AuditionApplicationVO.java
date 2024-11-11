package com.app.ggumteo.domain.audition;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionApplicationVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private Long memberProfileId;
    private String applyEtc;
    private Long auditionId;
    private String createdDate;
    private String confirmStatus;
}
