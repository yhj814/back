document.addEventListener("DOMContentLoaded", () => {
    console.log("DOMContentLoaded 이벤트 호출됨");

    // Work ID 가져오기
    const workId = document.querySelector('.layout')?.getAttribute('data-work-id');
    console.log("workId 확인:", workId);

    if (!workId) {
        console.error("workId가 설정되지 않았습니다.");
        return;
    }

    // 댓글 불러오기 함수
    async function loadReplies(page = 1) {
        console.log("loadReplies 함수 호출됨, page:", page);

        try {
            const response = await fetch(`/replies/${workId}/${page}`);
            console.log("fetch 요청 완료, 응답 상태:", response.status);
            if (!response.ok) throw new Error("댓글 로딩 실패");

            const data = await response.json();
            console.log("fetch 응답 데이터:", data);

            const commentContainer = document.getElementById("comment-row");
            if (!commentContainer) {
                console.error("comment-row 요소가 존재하지 않습니다.");
                return;
            }
            commentContainer.innerHTML = "";

            // 댓글 표시
            data.replies.forEach((reply) => {
                const replyBox = document.createElement("div");
                replyBox.className = "comment-box";
                replyBox.innerHTML = `
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
                commentContainer.appendChild(replyBox);
            });

            // 댓글 개수 업데이트
            document.querySelectorAll(".reply-count").forEach((el) => (el.textContent = data.totalCount));
            renderPagination(data.pagination);

            // 동적으로 생성된 신고 버튼에 이벤트 추가
            addReportEvent();
        } catch (error) {
            console.error("Error loading replies:", error);
        }
    }

    // 댓글 삭제 함수
    async function deleteReply(replyId) {
        console.log("댓글 삭제 기능 호출됨, replyId:", replyId);
        try {
            const response = await fetch(`/replies/${replyId}`, { method: "DELETE" });
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
        console.log("댓글 작성 데이터:", { replyContent, starRating });

        if (!replyContent) return alert("댓글 내용을 입력하세요.");

        const replyData = {
            workId,
            replyContent,
            star: starRating,
            profileNickname: document.querySelector(".comment-writer-name").textContent
        };
        try {
            const response = await fetch("/replies/write", {
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

    // 페이지네이션 렌더링
    function renderPagination(pagination) {
        console.log("페이징 데이터:", pagination);
        const paginationContainer = document.getElementById("pagination");

        if (!paginationContainer) {
            console.error("pagination 요소가 존재하지 않습니다.");
            return;
        }

        paginationContainer.innerHTML = ""; // 기존 내용을 초기화

        // <ul> 요소 생성 및 클래스 추가
        const paginationList = document.createElement("ul");
        paginationList.classList.add("pagination", "theme-ggumteo");

        // 이전 페이지 버튼
        if (pagination.hasPreviousPage) {
            const prevItem = document.createElement("li");
            prevItem.classList.add("page-item");
            const prevLink = document.createElement("a");
            prevLink.className = "page-link";
            prevLink.textContent = "이전";
            prevLink.onclick = () => loadReplies(pagination.previousPage);
            prevItem.appendChild(prevLink);
            paginationList.appendChild(prevItem);
        }

        // 페이지 번호 버튼
        for (let i = pagination.startPage; i <= pagination.endPage; i++) {
            const pageItem = document.createElement("li");
            pageItem.classList.add("page-item");
            const pageLink = document.createElement("a");
            pageLink.className = `page-link ${pagination.currentPage === i ? "active" : ""}`;
            pageLink.textContent = i;
            pageLink.onclick = () => loadReplies(i);
            pageItem.appendChild(pageLink);
            paginationList.appendChild(pageItem);
        }

        // 다음 페이지 버튼
        if (pagination.hasNextPage) {
            const nextItem = document.createElement("li");
            nextItem.classList.add("page-item");
            const nextLink = document.createElement("a");
            nextLink.className = "page-link";
            nextLink.textContent = "다음";
            nextLink.onclick = () => loadReplies(pagination.nextPage);
            nextItem.appendChild(nextLink);
            paginationList.appendChild(nextItem);
        }

        paginationContainer.appendChild(paginationList);
    }

    // 신고 버튼에 이벤트 추가하는 함수
    function addReportEvent() {
        document.querySelectorAll(".btn-comment-report").forEach((btn) => {
            btn.addEventListener("click", () => {
                const replyModal = document.getElementById("report-modal-reply");
                if (replyModal) replyModal.style.display = "block";
            });
        });
    }

    // 초기 댓글 로드
    loadReplies();

    // 별점 클릭 이벤트
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
    const spanReply = document.getElementsByClassName("modal-reply-close")[0];

    spanReply.addEventListener("click", () => {
        replyModal.style.display = "none";
    });
    window.addEventListener("click", (event) => {
        if (event.target === replyModal) {
            replyModal.style.display = "none";
        }
    });
});
