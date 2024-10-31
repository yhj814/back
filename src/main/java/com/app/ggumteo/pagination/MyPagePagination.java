package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MyPagePagination {
    protected Integer page; // 페이지
    protected int startRow; // 시작 행
    protected int endRow; // 끝 행
    protected int rowCount; // 행 갯수
    protected int pageCount; // 페이지 갯수
    protected int startPage; // 시작 페이지
    protected int endPage; // 끝 페이지
    protected int realEnd; // 진짜 끝
    protected boolean prev, next; // 이전, 다음
    protected int total; // 전체
    protected String order;

    public void progress() {
        this.page = page == null ? 1 : page;
        this.rowCount = 2; // 행 갯수 2개
        this.pageCount = 2; // 페이지 갯수 2개
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