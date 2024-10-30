package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Pagination {
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

    // 더보기 구현 시, 1개 더 가져오는 변수

    private int moreRowcount;

    // 기본 progress 메서드
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
        this.startRow--;  // limit 문법에 맞춰 인덱스 조정
    }

    // progress2 메서드 - 페이지네이션 조건을 다르게 적용할 때 사용
    public void progress2() {
        this.page = (this.page == null || this.page < 1) ? 1 : this.page;
        this.rowCount = 5;  // 한 페이지에 보여줄 게시물 수
        this.pageCount = 5;   // 페이지 버튼 갯수
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
        this.startRow--;
    }



//    // Thymeleaf에서 사용할 수 있는 getter 메서드 추가
//    public boolean isPrev() {
//        return prev;
//    }
//
//    public boolean isNext() {
//        return next;
//    }
//
//    // total을 설정하면 realEnd 값을 자동 계산
//    public void setTotal(int total) {
//        this.total = total;
//        this.realEnd = (int) Math.ceil(total / (double) rowCount); // realEnd는 total을 기반으로 계산됨
//    }
}
