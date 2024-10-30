package com.app.ggumteo.domain.admin;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnnouncementDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private String createdDate;
    private String updatedDate;

    public AnnouncementVO toVO() {
        return new AnnouncementVO(id, announcementTitle, announcementContent, createdDate, updatedDate);
    }
}
