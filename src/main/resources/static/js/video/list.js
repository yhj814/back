// // 검색 기능: postType을 유지하며 검색
// function searchWorks() {
//     const keyword = document.getElementById('searchKeyword').value.trim();
//     const postType = document.getElementById('postType').value;
//
//     const url = `/video/list?page=1&keyword=${encodeURIComponent(keyword)}&postType=${encodeURIComponent(postType)}`;
//     window.location.href = url;
// }
//
// // 장르 필터 적용 함수
// function filterByGenre(genre) {
//     const keyword = document.getElementById('searchKeyword').value.trim();
//     const postType = document.getElementById('postType').value;
//
//     const url = `/video/list?page=1&genreType=${encodeURIComponent(genre)}${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}&postType=${encodeURIComponent(postType)}`;
//     window.location.href = url;
// }
// document.addEventListener("DOMContentLoaded", function () {
//     // 검색 기능: postType을 유지하며 검색
//     window.searchWorks = function () {
//         const keyword = document.getElementById('searchKeyword').value.trim();
//         const postType = document.getElementById('postType').value;
//
//         const url = `/video/list?page=1&keyword=${encodeURIComponent(keyword)}&postType=${encodeURIComponent(postType)}`;
//         console.log("Generated URL for searchWorks:", url);  // 로그 추가
//         window.location.href = url;
//     }
//
//     // 장르 필터 적용 함수
//     window.filterByGenre = function (genre) {
//         const keyword = document.getElementById('searchKeyword').value.trim();
//         const postType = document.getElementById('postType').value;
//
//         const url = `/video/list?page=1&genreType=${encodeURIComponent(genre)}${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}&postType=${encodeURIComponent(postType)}`;
//         console.log("Generated URL for filterByGenre:", url);  // 로그 추가
//         window.location.href = url;
//     }
//
//     // 검색 버튼 클릭 이벤트 등록
//     document.querySelector(".search-btn").addEventListener("click", searchWorks);
//
//     // 필터 버튼 클릭 이벤트 등록
//     const filterButtons = document.querySelectorAll(".filter-box");
//     filterButtons.forEach(button => {
//         button.addEventListener("click", function () {
//             filterByGenre(button.getAttribute("data-genre"));
//         });
//     });
// });


