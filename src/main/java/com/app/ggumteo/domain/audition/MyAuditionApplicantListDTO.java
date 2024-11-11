package com.app.ggumteo.domain.audition;

import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @ToString
public class MyAuditionApplicantListDTO {
    private List<AuditionApplicationDTO> myAuditionApplicants;
    private SettingTablePagination settingTablePagination;
}
