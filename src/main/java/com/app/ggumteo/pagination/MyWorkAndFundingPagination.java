package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MyWorkAndFundingPagination extends MyPagePagination {
    @Override
    public void progress() {
        super.progress();
    }
}