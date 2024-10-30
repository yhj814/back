package com.app.ggumteo.domain.admin;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnnouncementVO {
    @EqualsAndHashCode.Include
    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private String createdDate;
    private String updatedDate;
}
