package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class AdminPagination {
    private Integer page;      // 현재 페이지 번호
    private int startRow;       // SQL LIMIT 구문의 시작 위치
    private int endRow;         // SQL LIMIT 구문의 종료 위치
    private int rowCount;       // 한 페이지에 보여줄 게시물 수
    private int pageCount;      // 페이지네이션 하단에 보여줄 페이지 수
    private int startPage;      // 페이지네이션 시작 페이지 번호
    private int endPage;        // 페이지네이션 종료 페이지 번호
    private int realEnd;        // 실제 마지막 페이지 번호
    private boolean prev;       // 이전 페이지 존재 여부
    private boolean next;       // 다음 페이지 존재 여부
    private int total;          // 전체 게시물 수

    public void progress() {
        // 기본 페이지 설정: 페이지가 null일 경우 1로 설정
        this.page = (page == null || page < 1) ? 1 : page;

        // 페이지 당 보여줄 게시물 수와 페이지네이션에 보여줄 페이지 수 설정
        this.rowCount = 5;      // 한 페이지에 5개의 게시물
        this.pageCount = 10;    // 페이지네이션에 10개의 페이지 번호 표시

        // SQL LIMIT의 시작 행 인덱스 설정
        this.startRow = (this.page - 1) * rowCount;

        // 실제 마지막 페이지 계산
        this.realEnd = (int) Math.ceil((double) total / rowCount);

        // 현재 페이지에서 보이는 페이지네이션의 끝 페이지 계산
        this.endPage = (int) (Math.ceil((double) page / pageCount) * pageCount);
        this.startPage = endPage - pageCount + 1;

        // 실제 끝 페이지가 현재 계산된 페이지네이션 끝 페이지보다 작으면 조정
        if (realEnd < endPage) {
            endPage = realEnd;
        }

        // 이전 페이지와 다음 페이지 존재 여부 설정
        this.prev = startPage > 1;
        this.next = endPage < realEnd;
    }
}
