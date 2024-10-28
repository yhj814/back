
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
    prevLink.href = `/text/list?page=${pagination.startPage - 1}&genreType=${pagination.genreType}`;
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
    pageLink.href = `/text/list?page=${i}&genreType=${pagination.genreType}`;
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
    nextLink.href = `/text/list?page=${pagination.endPage + 1}&genreType=${pagination.genreType}`;
    nextLink.innerText = '다음';
    nextLi.appendChild(nextLink);
    paginationContainer.appendChild(nextLi);
}

    // 페이지네이션 데이터 예시 (서버에서 받아와야 함)
    const paginationData = {
};
    function filterByGenre(genre) {
        const url = `http://localhost:10000/text/list?page=0&genreType=${genre}`;
        window.location.href = url; // 해당 장르로 페이지 이동
    }

    function searchWorks() {
        const keyword = document.getElementById('searchKeyword').value.trim();
        const url = `/text/list?page=1&keyword=${keyword}`;
        window.location.href = url; // 검색 결과 페이지로 이동
    }


    // 페이지네이션 렌더링
    renderPagination(paginationData);
