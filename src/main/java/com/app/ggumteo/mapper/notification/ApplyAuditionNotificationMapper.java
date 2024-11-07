package com.app.ggumteo.mapper.notification;

import com.app.ggumteo.domain.notification.ApplyAuditionNotificationVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplyAuditionNotificationMapper {
    void insert(Long auditionApplicationId);
}
