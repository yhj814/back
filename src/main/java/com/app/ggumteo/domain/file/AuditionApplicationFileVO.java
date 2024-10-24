package com.app.ggumteo.domain.file;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionApplicationFileVO implements Serializable {
    private Long fileId;
    private Long auditionApplicationId;
}
