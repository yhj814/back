const replyService = (() => {
    // 댓글 작성 함수
    const write = async (replyData) => {
        try {
            const response = await fetch("/replies/write", {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=utf-8" },
                body: JSON.stringify(replyData),
            });
            if (!response.ok) throw new Error("댓글 작성 실패");
            console.log("댓글 작성 성공");
            return response.json();
        } catch (error) {
            console.error("Error writing reply:", error);
        }
    };

    // 댓글 리스트 가져오기 함수
    const getList = async (page, workId, callback) => {
        try {
            const response = await fetch(`/replies/${workId}/${page}`);
            if (!response.ok) throw new Error("댓글 로딩 실패");

            const data = await response.json();
            console.log("댓글 리스트 불러오기 성공:", data);

            if (callback) callback(data);
            return data;
        } catch (error) {
            console.error("Error loading replies:", error);
        }
    };



    // 댓글 삭제 함수
    const remove = async (replyId) => {
        try {
            const response = await fetch(`/replies/${replyId}`, {
                method: "DELETE",
            });
            if (!response.ok) throw new Error("댓글 삭제 실패");
            console.log("댓글 삭제 성공");
            return response.json();
        } catch (error) {
            console.error("Error deleting reply:", error);
        }
    };

    return { write, getList, remove };
})();

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOMContentLoaded 이벤트 호출됨");

    const workId = document.querySelector(".layout")?.getAttribute("data-work-id");
    console.log("workId 확인:", workId);

    if (!workId) {
        console.error("workId가 설정되지 않았습니다.");
        return;
    }

    // 댓글 목록 및 페이지네이션 표시 함수
    const showList = ({ replies, pagination }) => {
        let text = ``;
        let pagingText = ``;

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

        // 페이지네이션 표시
        const paginationContainer = document.getElementById("pagination");
        if (paginationContainer) {
            // 페이지네이션 구성
            if (pagination.prev) {
                pagingText += `<div><a href="#" onclick="loadReplies(${pagination.startPage - 1}); return false;">이전</a></div>`;
            }
            for (let i = pagination.startPage; i <= pagination.endPage; i++) {
                if (pagination.page === i) {
                    pagingText += `<div style="color: red">${i}</div>`;
                } else {
                    pagingText += `<div><a href="#" onclick="loadReplies(${i}); return false;">${i}</a></div>`;
                }
            }
            if (pagination.next) {
                pagingText += `<div><a href="#" onclick="loadReplies(${pagination.endPage + 1}); return false;">다음</a></div>`;
            }
            paginationContainer.innerHTML = pagingText;
        } else {
            console.error("pagination 요소를 찾을 수 없습니다. HTML에서 id 속성을 확인하세요.");
        }
    };


    // 댓글 불러오기 함수
    const loadReplies = (page = 1) => {
        replyService.getList(page, workId, (data) => {
            const commentContainer = document.getElementById("comment-row");
            if (!commentContainer) {
                console.error("comment-row 요소가 존재하지 않습니다.");
                return;
            }
            showList(data);
            document.querySelectorAll(".reply-count").forEach((el) => (el.textContent = data.totalCount));
            addReportEvent();
        });
    };

    // 댓글 작성 이벤트
    document.querySelector(".btn-create-comment").addEventListener("click", async (event) => {
        event.preventDefault();

        const replyContent = document.getElementById("comment").value;
        const starRating = document.querySelector(".star.selected")?.getAttribute("data-value");

        if (!replyContent) return alert("댓글 내용을 입력하세요.");

        const replyData = { workId, replyContent, star: starRating };
        await replyService.write(replyData);
        document.getElementById("comment").value = "";
        loadReplies();
    });

    // 댓글 삭제 이벤트
    window.deleteReply = async (replyId) => {
        await replyService.remove(replyId);
        loadReplies();
    };

    // 신고 버튼 이벤트 추가 함수
    const addReportEvent = () => {
        document.querySelectorAll(".btn-comment-report").forEach((btn) => {
            btn.addEventListener("click", () => {
                const replyModal = document.getElementById("report-modal-reply");
                if (replyModal) replyModal.style.display = "block";
            });
        });
    };

    loadReplies();
});



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