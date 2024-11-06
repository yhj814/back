// 검색 기능: genreType을 빈 문자열로 설정하여 전체 검색
function searchWorks() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    document.getElementById('genreType').value = "";  // genreType을 빈 문자열로 설정

    const url = `/text/list?page=1&keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
}
document.addEventListener("DOMContentLoaded", function () {
    const thumbnailImage = document.getElementById("thumbnail-image");

    // 이미지가 로드되지 않거나 src가 특정 패턴과 일치하는 경우 기본 이미지로 대체
    if (thumbnailImage.src.endsWith("/text/display?fileName=")) {
        thumbnailImage.src = 'https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png';
    }

    // 이미지 로드 오류 시에도 기본 이미지로 대체
    thumbnailImage.onerror = function () {
        this.onerror = null; // 무한 루프 방지
        this.src = 'https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png';
    };
});
//
// // 장르 필터 적용 함수
// function filterByGenre(genre) {
//     const keyword = document.getElementById('searchKeyword').value.trim();
//     document.getElementById('genreType').value = genre;  // 선택된 장르로 설정
//     const url = `/text/list?page=1&genreType=${encodeURIComponent(genre)}${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}`;
//     window.location.href = url;
// }
//
//
//
