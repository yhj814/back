package com.app.ggumteo.mapper.notification;

import com.app.ggumteo.domain.notification.ApplyAuditionNotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplyAuditionNotificationMapper {
    void insert(ApplyAuditionNotificationVO applyAuditionNotificationVO);
}
