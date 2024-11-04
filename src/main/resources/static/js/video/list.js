// 검색 기능: genreType을 빈 문자열로 설정하여 전체 검색
function searchWorks() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    document.getElementById('genreType').value = "";  // genreType을 빈 문자열로 설정

    const url = `/video/list?page=1&keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
}
