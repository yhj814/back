package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MyAlarmPagination extends MyPagePagination {
    private int moreRowCount;

    @Override
    public void progress() {
        this.page = page == null ? 1 : page;
        this.rowCount = 5;
//        더보기 구현 시, 다음 페이지의 게시글 1개를 더 가져온다.
        this.moreRowCount = rowCount + 1;

        this.endRow = page * rowCount;
        this.startRow = endRow - rowCount + 1;

        this.realEnd = (int)Math.ceil(total / (double)rowCount);

        if(realEnd < endPage) {
            endPage = realEnd == 0 ? 1 : realEnd;
        }

        this.prev = startPage > 1;
        this.next = endPage < realEnd;

//        limit 문법에서 시작 인덱스는 0부터 시작하기 때문에 1 감소해준다.
        this.startRow--;
    }
}