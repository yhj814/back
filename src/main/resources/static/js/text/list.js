// 검색 기능: genreType을 빈 문자열로 설정하여 전체 검색
function searchWorks() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    document.getElementById('genreType').value = "";  // genreType을 빈 문자열로 설정

    const url = `/text/list?page=1&keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
}

// 장르 필터 적용 함수
function filterByGenre(genre) {
    const keyword = document.getElementById('searchKeyword').value.trim();
    document.getElementById('genreType').value = genre;  // 선택된 장르로 설정
    const url = `/text/list?page=1&genreType=${encodeURIComponent(genre)}${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}`;
    window.location.href = url;
}




// // 작품 목록 로드 및 렌더링
// async function loadWorks(page = 1, genreType = '', keyword = '') {
//     try {
//         const response = await fetch(`/text/list?page=${page}&genreType=${encodeURIComponent(genreType)}&keyword=${encodeURIComponent(keyword)}`);
//
//         if (!response.ok) throw new Error(`Failed to load works: ${response.status}`);
//
//         const data = await response.json();
//         const works = data.works || [];
//         const pagination = data.pagination || {};
//
//         document.getElementById('works-list').innerHTML = '';
//         document.getElementById('pagination').innerHTML = '';
//
//         works.forEach(work => {
//             const workItem = document.createElement('li');
//             workItem.style.width = '213px';
//             workItem.innerHTML = `
//                 <a href="/text/detail/${work.id}" class="box">
//                     <img src="/text/display?fileName=${work.thumbnailFilePath}" class="thumbnail" alt="썸네일 이미지" />
//                     <div class="title" style="width: 180px">${work.postTitle}</div>
//                     <div class="detail">
//                         <span>${work.genreType}</span>
//                         <img src="https://yozm.wishket.com/static/renewal/img/news/icon-clock.png" class="content-meta-elem meta-icon" />
//                         <span style="margin-right: 40px">${work.createdDate.substring(0, 10)}</span>
//                     </div>
//                     <div class="content">${work.postContent}</div>
//                     <div class="detail review">
//                         <div class="review-box">
//                             <div class="star-box">
//                                 <img src="https://www.wishket.com/static/renewal/img/project/wishket-project-portfolio/icon-review-star-full.png" />
//                             </div>
//                             <span class="review-score">5.0</span>
//                         </div>
//                         <div class="portfolio-detail-seperator"></div>
//                         <div class="partner-profile-box">
//                             <img src="https://cdn.wishket.com/news_profiles/248055_20230718_02e200cef51139ed.jpg" class="partner-profile" />
//                         </div>
//                         <div class="name-box">${work.profileNickName}</div>
//                     </div>
//                 </a>
//             `;
//             document.getElementById('works-list').appendChild(workItem);
//         });
//
//         renderPagination(pagination, genreType, keyword);
//     } catch (error) {
//         console.error('Error loading works:', error);
//     }
// }
//
// // 검색 기능: genreType을 빈 문자열로 설정하여 전체 검색
// function searchWorks() {
//     const keyword = document.getElementById('searchKeyword').value.trim();
//     loadWorks(1, '', keyword);
// }
//
// // 장르 필터 적용 함수
// function filterByGenre(genre) {
//     const keyword = document.getElementById('searchKeyword').value.trim();
//     loadWorks(1, genre, keyword);
// }
//
// // 페이지네이션 렌더링
// function renderPagination(pagination, genreType, keyword) {
//     const paginationContainer = document.getElementById('pagination');
//
//     if (pagination.prev) {
//         const prevLink = document.createElement('a');
//         prevLink.classList.add('page-link');
//         prevLink.textContent = '이전';
//         prevLink.onclick = () => loadWorks(pagination.startPage - 5, genreType, keyword);
//         paginationContainer.appendChild(prevLink);
//     }
//
//     for (let i = pagination.startPage; i <= pagination.endPage; i++) {
//         const pageLink = document.createElement('a');
//         pageLink.classList.add('page-link');
//         if (pagination.page === i) pageLink.classList.add('active');
//         pageLink.textContent = i;
//         pageLink.onclick = () => loadWorks(i, genreType, keyword);
//         paginationContainer.appendChild(pageLink);
//     }
//
//     if (pagination.next) {
//         const nextLink = document.createElement('a');
//         nextLink.classList.add('page-link');
//         nextLink.textContent = '다음';
//         nextLink.onclick = () => loadWorks(pagination.endPage + 5, genreType, keyword);
//         paginationContainer.appendChild(nextLink);
//     }
// }
//
// document.addEventListener('DOMContentLoaded', () => {
//     loadWorks(1);
// });
