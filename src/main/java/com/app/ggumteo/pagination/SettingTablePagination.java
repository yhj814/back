package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SettingTablePagination extends MyPagePagination {
    @Override
    public void progress() {
        this.page = page == null ? 1 : page;
        this.rowCount = 4; // 행 갯수 4개
        this.pageCount = 0; // 페이지 갯수 0개
        this.endRow = page * rowCount; // 끝 행 = 페이지 * 행 갯수
        this.startRow = endRow - rowCount + 1; // 시작 행 = 끝 행 - 행 갯수 + 1
        this.endPage = (int)(Math.ceil(page / (double)pageCount) * pageCount);
        this.startPage = endPage - pageCount + 1;
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