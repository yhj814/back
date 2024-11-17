package com.app.ggumteo.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlarmVO {
    private Long id; // 알림 ID
    private String message; // 알림 내용
    private Long memberProfileId;  // 회원 프로필 ID
    private String createDate; // 생성 일시
    private boolean isRead; // 읽었는지 여부
    private String alarmType;  // 알림 유형
    private String postTitle; // 게시글 제목
}
