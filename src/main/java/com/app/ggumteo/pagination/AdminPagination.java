package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class AdminPagination {
    private Integer page;
    private int startRow;
    private int endRow;
    private int rowCount;
    private int pageCount;
    private int startPage;
    private int endPage;
    private int realEnd;
    private boolean prev, next;
    private int total;
    private String order;
    //   더보기 구현 시, 1개 더 가져오는 변수이다.
    private int moreRowcount;

    public void progress() {
        this.page = page == null ? 1 : page;
        this.rowCount = 5;
        //    더보기 구현 시, 다음 페이지의 게시글 1개를 더 가져온다.
        this.moreRowcount = rowCount + 1;
        this.pageCount = 10;
        this.endRow = page * rowCount;
        this.startRow = endRow - rowCount + 1;
        this.endPage = (int) (Math.ceil(page / (double) pageCount) * pageCount);
        this.startPage = endPage - pageCount + 1;
        this.realEnd = (int) Math.ceil(total / (double) rowCount);
        if (realEnd < endPage) {
            endPage = realEnd == 0 ? 1 : realEnd;
        }
        this.prev = startPage > 1;
        this.next = endPage < realEnd;
//        limit 문법에서 시작 인덱스는 0부터 시작하기 때문에 1 감소해준다.
        this.startRow--;
    }
}