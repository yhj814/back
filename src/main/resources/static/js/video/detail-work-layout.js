
// 댓글 목록 및 페이지네이션 표시 함수
function showList({ replies, pagination }) {
    let text = '';
    let pagingText = '';

    replies.forEach((reply) => {
        text += `
        <div class="comment-group">
            <div class="comment-box">
                <div class="comment-wrapper">
                    <div class="write-user">
                        <img src="https://yozm.wishket.com/static/img/default_avatar.png" class="comment-writer-img" />
                        <div class="comment-writer-name">
                            <div class="writer-name">${reply.profileNickname}</div>
                            <div class="btn-comment-action">
                                <button class="btn-comment-delete cursor" onclick="deleteReply(${reply.id})">삭제</button>
                                <img src="/images/video/report.jpg" />
                                <button class="btn-comment-report cursor">신고</button>
                            </div>
                        </div>
                    </div>
                    <div class="comment">
                        <a class="comment-content">${reply.replyContent}</a>
                    </div>
                    <div class="comment-date-created">
                        ${reply.createDate}
                        <div class="comment-review">
                            <div class="review-box">
                                <div class="star-box">
                                    <img src="https://www.wishket.com/static/renewal/img/project/wishket-project-portfolio/icon-review-star-full.png" />
                                </div>
                                <span class="review-score">${reply.star}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        `;
    });

    document.getElementById("comment-row").innerHTML = text;

    const paginationContainer = document.getElementById("pagination");
    if (paginationContainer && pagination) {
        if (pagination.startPage > 1) {
            pagingText += `<li class="page-item prev"><a class="page-link" href="javascript:loadReplies(${pagination.startPage - 1})"> </a></li>`;
        }

        for (let i = pagination.startPage; i <= pagination.endPage; i++) {
            if (pagination.page === i) {
                pagingText += `<li class="page-item page-link" style="color: #00a878">${i}</li>`;
            } else {
                pagingText += `<li class="page-item"><a class="page-link" href="javascript:loadReplies(${i})">${i}</a></li>`;
            }
        }

        if (replies.length === pagination.rowCount && pagination.endPage < pagination.realEnd) {
            pagingText += `<li class="page-item next"><a class="page-link" href="javascript:loadReplies(${pagination.endPage + 1})"> </a></li>`;
        }
        paginationContainer.innerHTML = pagingText;
    } else {
        paginationContainer.innerHTML = ""; // 페이지네이션 데이터가 없을 때 비우기
    }
}
// 별점 평균을 업데이트하는 함수
async function updateAverageStar(workId) {
    try {
        const averageStar = await replyService.getAverageStar(workId);
        const starElement = document.getElementById("total");
        if (starElement) {
            starElement.textContent = averageStar.toFixed(1); // 소수점 첫째 자리까지 표시
        }
    } catch (error) {
        console.error("별점 평균을 업데이트하는 중 오류 발생:", error);
    }
}


// 댓글 수 업데이트 함수
async function updateReplyCount(workId) {
    try {
        const totalCount = await replyService.getReplyCount(workId);
        const totalReplyCountElement = document.getElementById("total-reply-count");
        const commentCountElement = document.getElementById("comment-count");

        if (totalReplyCountElement) totalReplyCountElement.textContent = totalCount;
        if (commentCountElement) commentCountElement.textContent = totalCount;
    } catch (error) {
        console.error("댓글 수를 불러오는 중 오류 발생:", error);
    }
}

// 신고 버튼 이벤트 추가 함수
function addReportEvent() {
    document.querySelectorAll(".btn-comment-report").forEach((btn) => {
        btn.addEventListener("click", () => {
            const replyModal = document.getElementById("report-modal-reply");
            if (replyModal) replyModal.style.display = "block";
        });
    });
}
