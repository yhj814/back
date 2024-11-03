// 검색 기능: genreType을 빈 문자열로 설정하여 전체 검색
function searchWorks() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    document.getElementById('genreType').value = "";  // genreType을 빈 문자열로 설정

    const url = `/video/list?page=1&keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
}

// 장르 필터 적용 함수
function filterByGenre(genre) {
    const keyword = document.getElementById('searchKeyword').value.trim();
    document.getElementById('genreType').value = genre;  // 선택된 장르로 설정
    const url = `/video/list?page=1&genreType=${encodeURIComponent(genre)}${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}`;
    window.location.href = url;
}
