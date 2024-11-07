package com.app.ggumteo.repository.notification;

import com.app.ggumteo.domain.notification.ApplyAuditionNotificationVO;
import com.app.ggumteo.mapper.notification.ApplyAuditionNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplyAuditionNotificationDAO {
    private final ApplyAuditionNotificationMapper applyAuditionNotificationMapper;

    public void save(Long auditionApplicationId) {applyAuditionNotificationMapper.insert(auditionApplicationId);}
}
