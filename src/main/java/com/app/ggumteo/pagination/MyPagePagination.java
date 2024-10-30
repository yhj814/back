package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MyPagePagination extends Pagination {
    private Integer page; // 페이지
    private int startRow; // 시작 행
    private int endRow; // 끝 행
    private int rowCount; // 행 갯수
    private int pageCount; // 페이지 갯수
    private int startPage; // 시작 페이지
    private int endPage; // 끝 페이지
    private int realEnd; // 진짜 끝
    private boolean prev, next; // 이전, 다음
    private int total; // 전체
    private String order;

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