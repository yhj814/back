// 페이지네이션 데이터를 받아서 동적으로 렌더링
function renderPagination(pagination) {
    const paginationContainer = document.getElementById('pagination');
    paginationContainer.innerHTML = '';

    // 이전 버튼
    const prevLi = document.createElement('li');
    prevLi.classList.add('page-item');
    if (!pagination.hasPrevious) {
        prevLi.classList.add('disabled');
    }
    const prevLink = document.createElement('a');
    prevLink.classList.add('page-link');
    prevLink.href = `/text/list?page=${pagination.startPage - 1}&genreType=${pagination.genreType}&keyword=${pagination.keyword || ''}`;
    prevLink.innerText = '이전';
    prevLi.appendChild(prevLink);
    paginationContainer.appendChild(prevLi);

    // 페이지 번호 버튼들
    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        const pageLi = document.createElement('li');
        pageLi.classList.add('page-item');
        if (pagination.page === i) {
            pageLi.classList.add('active');
        }
        const pageLink = document.createElement('a');
        pageLink.classList.add('page-link');
        pageLink.href = `/text/list?page=${i}&genreType=${pagination.genreType}&keyword=${pagination.keyword || ''}`;
        pageLink.innerText = i;
        pageLi.appendChild(pageLink);
        paginationContainer.appendChild(pageLi);
    }

    // 다음 버튼
    const nextLi = document.createElement('li');
    nextLi.classList.add('page-item');
    if (!pagination.hasNext) {
        nextLi.classList.add('disabled');
    }
    const nextLink = document.createElement('a');
    nextLink.classList.add('page-link');
    nextLink.href = `/text/list?page=${pagination.endPage + 1}&genreType=${pagination.genreType}&keyword=${pagination.keyword || ''}`;
    nextLink.innerText = '다음';
    nextLi.appendChild(nextLink);
    paginationContainer.appendChild(nextLi);
}

// 페이지네이션 데이터 예시 (서버에서 받아와야 함)
const paginationData = {
    page: 1,
    startPage: 1,
    endPage: 5,
    hasPrevious: true,
    hasNext: true,
    genreType: '${genreType}',  // 서버에서 받아오는 값
    keyword: '${keyword}'  // 서버에서 받아오는 값
};

// 페이지네이션 렌더링
renderPagination(paginationData);

// 장르 필터 적용 함수
function filterByGenre(genre) {
    const keyword = document.getElementById('searchKeyword').value.trim(); // 기존 검색어 유지
    const url = `/text/list?page=1&genreType=${encodeURIComponent(genre)}&keyword=${encodeURIComponent(keyword || '')}`;
    window.location.href = url; // 해당 장르로 페이지 이동
}

// 검색 기능
function searchWorks() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    if (keyword !== "") {
        window.location.href = `/text/list?page=1&keyword=${encodeURIComponent(keyword)}`;
    } else {
        window.location.href = `/text/list?page=1`;
    }
}