package com.app.ggumteo.domain.alarm;

import com.app.ggumteo.domain.funding.FundingDTO;
import com.app.ggumteo.pagination.MyAlarmPagination;
import com.app.ggumteo.pagination.MyWorkAndFundingPagination;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyAlarmListDTO {
    private List<AlarmDTO> myAlarms;
    private MyAlarmPagination myAlarmPagination;
}
