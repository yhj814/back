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
public class PayWorkDTO {
    @EqualsAndHashCode.Include
    private Long Id;                    // tbl_post의 id
    private String postType;            // post_type에 VIEDO가 붙으면 영상, TEXT가 붙으면 글으로 구분
    private String postTitle;           // post테이블의 post_title
    private String workPrice;           // 작품 가격
    private String profileName;         // tbl_buy_work 의 member_profile_id 구매자
    private String createdDate;         // tbl_buy_work 의 created_date
}
