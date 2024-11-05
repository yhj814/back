package com.app.ggumteo.domain.reply;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ReplyDTO {
    private Long id;
    private String replyContent;
    private Long memberProfileId;
    private Long workId;
    private String createDate;
    private int star;
    private String profileNickname;
    private String profileImgUrl; // 카카오톡 프로필 이미지 url

    public ReplyVO toVO() {
        return new ReplyVO(id, replyContent, memberProfileId, workId, createDate, star);
    }

}
