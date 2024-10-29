document.addEventListener("DOMContentLoaded", () => {
    console.log("DOMContentLoaded 이벤트 호출됨");  // DOMContentLoaded 이벤트 확인

    const workId = document.querySelector('.layout')?.getAttribute('data-work-id');
    console.log("workId 확인:", workId);  // workId가 올바르게 설정되었는지 확인

    // workId가 설정되지 않았을 경우 오류 출력
    if (!workId) {
        console.error("workId가 설정되지 않았습니다.");
        return;
    }

    // 비동기로 댓글 불러오기
    async function loadReplies(page = 1) {
        console.log("loadReplies 함수 호출됨, page:", page);  // loadReplies 호출 확인
        if (!workId) {
            console.error("workId가 설정되지 않았습니다.");
            return;
        }

        try {
            const response = await fetch(`/api/replies?workId=${workId}&page=${page}`);
            console.log("fetch 요청 완료, 응답 상태:", response.status);  // fetch 요청 상태 확인
            if (!response.ok) throw new Error("댓글 로딩 실패");

            const data = await response.json();
            console.log("fetch 응답 데이터:", data);  // fetch 응답 데이터 확인

            const commentContainer = document.getElementById("comment-row");
            if (!commentContainer) {
                console.error("comment-row 요소가 존재하지 않습니다.");
                return;
            }
            commentContainer.innerHTML = "";

            data.replies.forEach((reply) => {
                console.log("각 댓글 데이터:", reply);  // 각 댓글 데이터 확인

                const replyBox = document.createElement("div");
                replyBox.className = "comment-box";
                replyBox.innerHTML = `
                    <div class="comment-wrapper">
                        <div class="write-user">
                            <img src="" class="comment-writer-img" />
                            <div class="comment-writer-name">
                                <div class="writer-name">${reply.profileNickName}</div>
                                <button class="btn-comment-delete cursor" onclick="deleteReply(${reply.id})">삭제</button>
                                <button class="btn-comment-report cursor">신고</button>
                            </div>
                        </div>
                        <div class="comment">
                            <a class="comment-content">${reply.replyContent}</a>
                        </div>
                        <div class="comment-date-created">${reply.createDate}</div>
                        <div class="review-score">${reply.star}</div>
                    </div>
                `;
                commentContainer.appendChild(replyBox);
            });

            document.querySelectorAll(".reply-count").forEach((el) => (el.textContent = data.totalCount));
            renderPagination(data.pagination);
        } catch (error) {
            console.error("Error loading replies:", error);
        }
    }

    // 댓글 삭제 기능
    async function deleteReply(replyId) {
        console.log("댓글 삭제 기능 호출됨, replyId:", replyId);  // 댓글 삭제 호출 확인
        try {
            const response = await fetch(`/api/replies/${replyId}`, {
                method: "DELETE",
            });
            if (response.ok) {
                console.log("댓글 삭제 성공");
                loadReplies();
            } else {
                console.error("Failed to delete reply");
            }
        } catch (error) {
            console.error("Error deleting reply:", error);
        }
    }

    // 댓글 작성 기능
    document.querySelector(".btn-create-comment").addEventListener("click", async (event) => {
        event.preventDefault();

        const replyContent = document.getElementById("comment").value;
        const starRating = document.querySelector(".star.selected")?.getAttribute("data-value");
        console.log("댓글 작성 데이터:", { replyContent, starRating });  // 댓글 작성 데이터 확인

        if (!replyContent) return alert("댓글 내용을 입력하세요.");

        const replyData = {
            workId,
            replyContent,
            star: starRating,
            profileNickname: document.querySelector(".comment-writer-name").textContent
        };
        try {
            const response = await fetch("/api/replies", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(replyData),
            });
            if (response.ok) {
                console.log("댓글 작성 성공");
                loadReplies();
                document.getElementById("comment").value = "";
            } else {
                console.error("Failed to submit reply");
            }
        } catch (error) {
            console.error("Error submitting reply:", error);
        }
    });

    // 댓글 페이징
    function renderPagination(pagination) {
        console.log("페이징 데이터:", pagination);  // 페이징 데이터 확인
        const paginationContainer = document.querySelector(".pagination-box");
        paginationContainer.innerHTML = "";

        if (pagination.hasPreviousPage) {
            const prevLink = document.createElement("a");
            prevLink.className = "page-link";
            prevLink.textContent = "이전";
            prevLink.onclick = () => loadReplies(pagination.previousPage);
            paginationContainer.appendChild(prevLink);
        }

        for (let i = pagination.startPage; i <= pagination.endPage; i++) {
            const pageLink = document.createElement("a");
            pageLink.className = `page-link ${pagination.currentPage === i ? "active" : ""}`;
            pageLink.textContent = i;
            pageLink.onclick = () => loadReplies(i);
            paginationContainer.appendChild(pageLink);
        }

        if (pagination.hasNextPage) {
            const nextLink = document.createElement("a");
            nextLink.className = "page-link";
            nextLink.textContent = "다음";
            nextLink.onclick = () => loadReplies(pagination.nextPage);
            paginationContainer.appendChild(nextLink);
        }
    }

    // 초기 로드
    loadReplies();
});


document.querySelectorAll(".star-rating .star").forEach(function (star) {
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

        console.log("Selected rating:", value);
    });

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

    star.addEventListener("mouseout", function () {
        document.querySelectorAll(".star-rating .star").forEach(function (s) {
            s.classList.remove("hover");
        });
    });
});

function getNextSiblings(elem) {
    let siblings = [];
    siblings.push(elem);
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

    if (replyReportBtn) {
        replyReportBtn.addEventListener("click", () => {
            replyModal.style.display = "block";
        });
    } else {
        console.error(".btn-comment-report 요소가 페이지에 없습니다.");
    }
spanReply.addEventListener("click", () => {
    replyModal.style.display = "none";
});
window.addEventListener("click", (event) => {
    if (event.target === replyModal) {
        replyModal.style.display = "none";
    }
});
