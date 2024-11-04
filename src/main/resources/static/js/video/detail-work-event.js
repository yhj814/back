document.addEventListener("DOMContentLoaded", () => {
    const workId = document.querySelector(".layout")?.getAttribute("data-work-id");
    if (!workId) return;

    // 댓글 불러오기 함수
    function loadReplies(page = 1) {
        replyService.getList(page, workId, (data) => {
            if (data && data.pagination) {
                showList(data);
                updateReplyCount(workId);
                updateAverageStar(workId);
                addReportEvent();
            }
        });
    }
    // 전역으로 loadReplies 함수 설정(페이지 네이션이 위에있어서0
    window.loadReplies = loadReplies;

    // 초기 댓글 로드
    loadReplies();

    // 댓글 작성 이벤트
    document.querySelector(".btn-create-comment").addEventListener("click", async (event) => {
        event.preventDefault();
        const replyContent = document.getElementById("comment").value;

        if (!replyContent) return alert("댓글 내용을 입력하세요.");
        if (!selectedRating) return alert("별점을 선택하세요.");

        const replyData = { workId, replyContent, star: selectedRating };
        await replyService.write(replyData);
        document.getElementById("comment").value = "";
        selectedRating = null;

        document.querySelectorAll(".star-rating .star").forEach((star) => {
            star.classList.remove("selected");
        });

        loadReplies();
        updateReplyCount(workId);
        updateAverageStar(workId);
    });

    // 댓글 삭제 이벤트
    window.deleteReply = async (replyId) => {
        const success = await replyService.remove(replyId);
        if (success) {
            loadReplies();
            updateReplyCount(workId);
            updateAverageStar(workId);
        } else {
            alert("댓글 삭제에 실패했습니다.");
        }
    };

    // 신고 모달 관련 설정
    const reportBtn = document.getElementById("report-btn");
    const reportModal = document.getElementById("report-modal");
    const closeBtn = document.querySelector("#report-modal .close");

    reportBtn.addEventListener("click", () => {
        reportModal.style.display = "block";
    });

    closeBtn.addEventListener("click", () => {
        reportModal.style.display = "none";
    });

    window.addEventListener("click", (event) => {
        if (event.target === reportModal) {
            reportModal.style.display = "none";
        }
    });

    // 댓글 신고 모달 설정
    const replyModal = document.getElementById("report-modal-reply");
    const closeReplyModalBtn = document.querySelector("#report-modal-reply .modal-reply-close");

    document.addEventListener("click", (event) => {
        if (event.target.classList.contains("btn-comment-report")) {
            replyModal.style.display = "block";
        }
    });

    closeReplyModalBtn.addEventListener("click", () => {
        replyModal.style.display = "none";
    });

    window.addEventListener("click", (event) => {
        if (event.target === replyModal) {
            replyModal.style.display = "none";
        }
    });

    // 별점 클릭 이벤트
    let selectedRating = null;
    document.querySelectorAll(".star-rating .star").forEach(function (star) {
        star.addEventListener("click", function () {
            selectedRating = parseInt(this.getAttribute("data-value"));

            document.querySelectorAll(".star-rating .star").forEach((s) => s.classList.remove("selected"));
            for (let i = 1; i <= selectedRating; i++) {
                document.querySelector(`.star-rating .star[data-value="${i}"]`).classList.add("selected");
            }
        });

        star.addEventListener("mouseover", function () {
            document.querySelectorAll(".star-rating .star").forEach((s) => s.classList.remove("hover"));
            this.classList.add("hover");
            getNextSiblings(this).forEach((sibling) => sibling.classList.add("hover"));
        });

        star.addEventListener("mouseout", function () {
            document.querySelectorAll(".star-rating .star").forEach((s) => s.classList.remove("hover"));
        });
    });

    // 클릭한 별의 이전 모든 별에 대한 `hover` 상태 관리
    function getNextSiblings(elem) {
        let siblings = [elem];
        while ((elem = elem.previousElementSibling)) {
            siblings.push(elem);
        }
        return siblings;
    }

    loadReplies(); // 초기 댓글 로드
});

