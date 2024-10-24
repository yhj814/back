package com.app.ggumteo.domain.audition;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionApplicationVO implements Serializable {
    private Long id;
    private Long memberId;
    private String applyEtc;
    private Long auditionId;
    private String createdDate;
    private String confirmStatus;
}
