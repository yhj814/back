document.querySelectorAll(".star-rating .star").forEach(function (star) {
    // 클릭 이벤트 리스너 등록
    star.addEventListener("click", function () {
        let value = this.getAttribute("data-value");

        // 모든 별의 선택 상태 초기화
        document.querySelectorAll(".star-rating .star").forEach(function (s) {
            s.classList.remove("selected");
        });

        // 클릭한 별과 그 이전 별들 선택 상태로 변경
        this.classList.add("selected");
        let nextSiblings = getNextSiblings(this);
        nextSiblings.forEach(function (sibling) {
            sibling.classList.add("selected");
        });

        console.log("Selected rating:", value); // 선택된 별점 로그 출력
    });

    // 마우스 오버 시 미리보기 효과
    star.addEventListener("mouseover", function () {
        document.querySelectorAll(".star-rating .star").forEach(function (s) {
            s.classList.remove("hover");
        });
        this.classList.add("hover");
        let nextSiblings = getNextSiblings(this);
        nextSiblings.forEach(function (sibling) {
            sibling.classList.add("hover");
        });
    });

    // 마우스가 별에서 나갔을 때 미리보기 초기화
    star.addEventListener("mouseout", function () {
        document.querySelectorAll(".star-rating .star").forEach(function (s) {
            s.classList.remove("hover");
        });
    });
});

function getNextSiblings(elem) {
    let siblings = [];
    siblings.push(elem); // 자신 포함
    while ((elem = elem.previousElementSibling)) {
        siblings.push(elem);
    }
    return siblings;
}

// 댓글 아이콘 툴팁
const replyBtn = document.querySelector(".reply-btn");
const replyPtag = document.querySelector(".action-tooltip-reply");

replyBtn.addEventListener("mouseover", () => {
    replyPtag.style.display = "block";
});
replyBtn.addEventListener("mouseout", () => {
    replyPtag.style.display = "none";
});

// 작품 신고 모달
const modal = document.getElementById("report-modal");
const btn = document.getElementById("report-btn");
const span = document.getElementsByClassName("close")[0];

btn.addEventListener("click", () => {
    modal.style.display = "block";
});
span.addEventListener("click", () => {
    modal.style.display = "none";
});
window.addEventListener("click", (event) => {
    if (event.target === modal) {
        modal.style.display = "none";
    }
});

// 댓글 신고 모달
const replyModal = document.getElementById("report-modal-reply");
const replyReportBtn = document.querySelector(".btn-comment-report");
const spanReply = document.getElementsByClassName("modal-reply-close")[0];

replyReportBtn.addEventListener("click", () => {
    replyModal.style.display = "block";
});
spanReply.addEventListener("click", () => {
    replyModal.style.display = "none";
});
window.addEventListener("click", (event) => {
    if (event.target === replyModal) {
        replyModal.style.display = "none";
    }
});

// 비동기로 댓글 가져오기
async function loadComments(page = 1) {
    try {
        const response = await fetch(`/api/comments?workId=${workId}&page=${page}`);
        const data = await response.json();

        const commentContainer = document.getElementById("comment-row");
        commentContainer.innerHTML = ""; // 기존 댓글 내용 초기화

        data.comments.forEach((comment) => {
            const commentBox = document.createElement("div");
            commentBox.className = "comment-box";
            commentBox.innerHTML = `
                <div class="comment-wrapper">
                    <div class="write-user">
                        <img src="/path/to/profile/${comment.profileImage}" class="comment-writer-img" />
                        <div class="comment-writer-name">
                            <div class="writer-name">${comment.profileNickName}</div>
                            ${
                comment.isAuthor
                    ? `<button class="btn-comment-delete cursor" onclick="deleteComment(${comment.id})">삭제</button>`
                    : ""
            }
                            <button class="btn-comment-report cursor">신고</button>
                        </div>
                    </div>
                    <div class="comment">
                        <a class="comment-content">${comment.replyContent}</a>
                    </div>
                    <div class="comment-date-created">${comment.createdDate}</div>
                    <div class="review-score">${comment.star}</div>
                </div>
            `;
            commentContainer.appendChild(commentBox);
        });

        // 댓글 수 업데이트
        document.querySelectorAll(".reply-count").forEach((el) => (el.textContent = data.totalCount));
        renderPagination(data.pagination);
    } catch (error) {
        console.error("Error loading comments:", error);
    }
}

// 댓글 삭제 기능
async function deleteComment(commentId) {
    try {
        const response = await fetch(`/api/comments/${commentId}`, {
            method: "DELETE",
        });
        if (response.ok) {
            loadComments(); // 댓글 목록 새로고침
        } else {
            console.error("Failed to delete comment");
        }
    } catch (error) {
        console.error("Error deleting comment:", error);
    }
}

// 댓글 작성 기능
document.querySelector(".btn-create-comment").addEventListener("click", async (event) => {
    event.preventDefault();

    const content = document.getElementById("comment").value;
    const starRating = document.querySelector(".star.selected").getAttribute("data-value");
    const authorName = document.querySelector(".comment-writer-name").textContent;

    if (!content) return alert("댓글 내용을 입력하세요.");

    const commentData = { workId, content, star: starRating, authorName };
    try {
        const response = await fetch("/api/comments", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(commentData),
        });
        if (response.ok) {
            loadComments(); // 댓글 새로고침
            document.getElementById("comment").value = ""; // 댓글 입력창 초기화
        } else {
            console.error("Failed to submit comment");
        }
    } catch (error) {
        console.error("Error submitting comment:", error);
    }
});

// 댓글 페이징
function renderPagination(pagination) {
    const paginationContainer = document.querySelector(".pagination-box");
    paginationContainer.innerHTML = "";

    if (pagination.hasPreviousPage) {
        const prevLink = document.createElement("a");
        prevLink.className = "page-link";
        prevLink.textContent = "이전";
        prevLink.onclick = () => loadComments(pagination.previousPage);
        paginationContainer.appendChild(prevLink);
    }

    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        const pageLink = document.createElement("a");
        pageLink.className = `page-link ${pagination.currentPage === i ? "active" : ""}`;
        pageLink.textContent = i;
        pageLink.onclick = () => loadComments(i);
        paginationContainer.appendChild(pageLink);
    }

    if (pagination.hasNextPage) {
        const nextLink = document.createElement("a");
        nextLink.className = "page-link";
        nextLink.textContent = "다음";
        nextLink.onclick = () => loadComments(pagination.nextPage);
        paginationContainer.appendChild(nextLink);
    }
}

// 초기 로드
document.addEventListener("DOMContentLoaded", () => {
    loadComments(); // 댓글 불러오기
});
