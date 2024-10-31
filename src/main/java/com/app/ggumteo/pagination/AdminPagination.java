package com.app.ggumteo.pagination;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class AdminPagination {
    private Integer page;             // 현재 페이지 번호
    private int startRow;             // 현재 페이지의 시작 행 인덱스
    private int endRow;               // 현재 페이지의 끝 행 인덱스
    private int rowCount;             // 한 페이지당 표시할 데이터 수
    private int pageCount;            // 표시할 페이지 번호의 개수
    private int startPage;            // 페이지 그룹의 시작 페이지 번호
    private int endPage;              // 페이지 그룹의 끝 페이지 번호
    private int realEnd;              // 실제 데이터에 따른 마지막 페이지 번호
    private boolean prev, next;       // 이전/다음 페이지 여부 표시
    private int total;                // 전체 데이터 개수
    private String order;             // 데이터 정렬 기준
    private String search;            // 검색어 필드

    // 더보기 기능을 위한 다음 페이지의 게시글 1개를 추가로 가져오는 변수
    private int moreRowcount;

    // 페이징 계산 메서드
    public void progress() {
        // 페이지가 null인 경우 기본값으로 1 설정
        this.page = page == null ? 1 : page;
        // 한 페이지에 표시할 데이터 개수를 5로 설정
        this.rowCount = 5;

        // 페이지 번호 표시 개수를 10으로 설정
        this.pageCount = 10;

        // endRow: 현재 페이지에서 표시할 마지막 행 번호
        this.endRow = page * rowCount;

        // startRow: 현재 페이지에서 표시할 시작 행 번호
        this.startRow = endRow - rowCount + 1;

        // 현재 페이지 그룹의 끝 페이지 번호 계산
        this.endPage = (int) (Math.ceil(page / (double) pageCount) * pageCount);

        // 현재 페이지 그룹의 시작 페이지 번호 계산
        this.startPage = endPage - pageCount + 1;

        // 실제 데이터의 개수를 기준으로 마지막 페이지 번호(realEnd) 계산
        this.realEnd = (int) Math.ceil(total / (double) rowCount);

        // 실제 마지막 페이지가 현재 endPage보다 작다면 endPage를 realEnd로 조정
        if (realEnd < endPage) {
            endPage = realEnd == 0 ? 1 : realEnd;
        }

        // 이전 페이지가 있는지 여부를 확인
        this.prev = startPage > 1;

        // 다음 페이지가 있는지 여부를 확인
        this.next = endPage < realEnd;

        // DB의 인덱스에 맞게 시작 행을 조정 (0부터 시작하도록 1 감소)
        this.startRow--;
    }
}

