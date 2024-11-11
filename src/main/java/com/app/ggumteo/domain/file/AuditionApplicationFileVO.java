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
    private Long fileId;
    private Long auditionApplicationId;

    public AuditionApplicationFileVO(Long fileId, Long auditionApplicationId) {
        this.fileId = fileId;
        this.auditionApplicationId = auditionApplicationId;
    }
}
