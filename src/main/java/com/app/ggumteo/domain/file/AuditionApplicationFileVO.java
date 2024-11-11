package com.app.ggumteo.domain.file;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter @Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditionApplicationFileVO implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private Long auditionApplicationId;
}
