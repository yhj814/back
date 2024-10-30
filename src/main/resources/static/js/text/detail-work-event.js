
document.addEventListener("DOMContentLoaded", () => {
    console.log("DOMContentLoaded 이벤트 호출됨");

    const reportBtn = document.getElementById("report-btn");
    const reportModal = document.getElementById("report-modal");
    const closeBtn = document.querySelector("#report-modal .close");

    // 작품 신고하기 버튼 클릭 시 모달 표시
    reportBtn.addEventListener("click", () => {
        reportModal.style.display = "block";
    });

    // 작품 신고 모달 닫기 버튼 (X) 클릭 시 모달 숨기기
    closeBtn.addEventListener("click", () => {
        reportModal.style.display = "none";
    });

    // 모달 외부를 클릭했을 때 모달 숨기기
    window.addEventListener("click", (event) => {
        if (event.target === reportModal) {
            reportModal.style.display = "none";
        }
    });

    // 댓글 신고 모달 관련 설정
    const replyModal = document.getElementById("report-modal-reply");
    const closeReplyModalBtn = document.querySelector("#report-modal-reply .modal-reply-close");

    // 댓글 신고 버튼 클릭 시 모달 표시 (이벤트 위임)
    document.addEventListener("click", (event) => {
        if (event.target.classList.contains("btn-comment-report")) {
            replyModal.style.display = "block";
        }
    });

    // 댓글 신고 모달 닫기 버튼 (X) 클릭 시 모달 숨기기
    closeReplyModalBtn.addEventListener("click", () => {
        replyModal.style.display = "none";
    });

    // 모달 외부를 클릭했을 때 댓글 신고 모달 숨기기
    window.addEventListener("click", (event) => {
        if (event.target === replyModal) {
            replyModal.style.display = "none";
        }
    });
});
