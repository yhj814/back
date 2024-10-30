package com.app.ggumteo.domain.reply;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReplyVO  {
    @EqualsAndHashCode.Include
    private Long id;
    private String replyContent;
    private Long memberProfileId;
    private Long workId;
    private String createDate;
    private int star;
}