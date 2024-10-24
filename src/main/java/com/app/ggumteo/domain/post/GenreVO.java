package com.app.ggumteo.domain.post;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class GenreVO {
    @EqualsAndHashCode.Include
    private Long id;
    private String genreType;

}
