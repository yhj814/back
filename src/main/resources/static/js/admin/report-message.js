//문자 발송 모달창을 수행하는 함수
function showMesaageModal(sectionId) {

    // 영상 작품 문자발송 이벤트
    const btnsVideo = document.querySelectorAll(
        `${sectionId} .video-wrapper .selected-btn.message`
    );
    const modalVideo = document.querySelector(
        `${sectionId} .video.message-modal`
    );
    btnsVideo.forEach((btn) => {
        btn.addEventListener("click", () => {
            // 모달창 나옴.
            modalVideo.style.display = "flex";
        });
    });

    // 글 작품 문자발송 이벤트
    const btnsText = document.querySelectorAll(
        `${sectionId} .text-wrapper .selected-btn.message`
    );
    const modalText = document.querySelector(
        `${sectionId} .text.message-modal`
    );
    // 문자발송 버튼 클릭했을 때
    btnsText.forEach((btn) => {
        btn.addEventListener("click", () => {
            // 모달창 나옴.
            modalText.style.display = "flex";
        });
    });


    //영상 펀딩 문자 이벤트
    const btnsFundingVideo = document.querySelector("#video-funding-report .selected-btn.message");
    const modalFundingVideo = document.querySelector(
        `${sectionId} .video-funding.message-modal`
    );
    // 문자발송 버튼 클릭했을 때
    btnsFundingVideo.addEventListener("click", () => {
        // 모달창 나옴.
        modalFundingVideo.style.display = "flex";
    });
}

showMesaageModal("#work-section");
showMesaageModal("#funding-section");

//------------------------------------------------------------------------------------------------------------------

// 영상 신고 문자전송 모달창과 버튼 이벤트
function setupMessageModal() {
    const modal = document.querySelector(".message-modal.video");
    const overlay = modal.querySelector(".background-overlay");
    const saveButton = modal.querySelector(".save-button");
    const textarea = modal.querySelector("textarea[name='reason']");

    // 발송 버튼 클릭 시
    saveButton.addEventListener("click", async () => {
        const messageContent = textarea.value.trim();
        const recipientNumber = "01090837645"; // 수신자 번호

        if (!messageContent) {
            alert("문자 내용을 입력하세요.");
            return;
        }

        try {
            // 서버에 문자 발송 요청
            const response = await fetch("/report/messages/videoReport/send", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ to: recipientNumber, text: messageContent })
            });

            const result = await response.json();
            if (result.success) {
                alert("문자가 성공적으로 발송되었습니다.");
                modal.style.display = "none"; // 모달창 닫기
                textarea.value = "";          // 입력 내용 초기화
            } else {
                alert("문자 발송에 실패했습니다: " + result.error);
            }
        } catch (error) {
            console.error("문자 발송 중 오류 발생:", error);
            alert("문자 발송에 실패했습니다. 다시 시도해주세요.");
        }
    });

    // 배경 클릭 시 모달 닫기
    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 페이지가 로드되면 모달 초기화 함수 실행
document.addEventListener("DOMContentLoaded", () => {
    setupMessageModal();
    setupMessageTextModal();
});

//-----------------------------------------------------------------------------------------------------
// 글 신고 문자전송 모달창과 버튼 이벤트
function setupMessageTextModal() {
    const textModal = document.querySelector(".message-modal.text");
    const overlay = textModal.querySelector(".background-overlay");
    const saveButton = textModal.querySelector(".save-button.text");
    const textarea = textModal.querySelector("textarea[name='reasonText']");

    // 발송 버튼 클릭 시
    saveButton.addEventListener("click", async () => {
        const messageContent = textarea.value.trim();
        const recipientNumber = "01090837645"; // 수신자 번호

        if (!messageContent) {
            alert("문자 내용을 입력하세요.");
            return;
        }

        try {
            // 서버에 문자 발송 요청
            const response = await fetch("/report/messages/textReport/send", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ to: recipientNumber, text: messageContent })
            });

            const result = await response.json();
            if (result.success) {
                alert("문자가 성공적으로 발송되었습니다.");
                textModal.style.display = "none"; // 모달창 닫기
                textarea.value = "";          // 입력 내용 초기화
            } else {
                alert("문자 발송에 실패했습니다: " + result.error);
            }
        } catch (error) {
            console.error("문자 발송 중 오류 발생:", error);
            alert("문자 발송에 실패했습니다. 다시 시도해주세요.");
        }
    });

    // 배경 클릭 시 모달 닫기
    overlay.addEventListener("click", () => {
        textModal.style.display = "none";
    });
}
//----------------------------------------------------------------------------------------------------
// 영상 펀딩 신고 문자전송 모달창과 버튼 이벤트
function setupMessageVideoFundingModal() {
    const modal = document.querySelector(".message-modal.video-funding");
    const overlay = modal.querySelector(".background-overlay");
    const saveButton = modal.querySelector(".save-button");
    const textarea = modal.querySelector("textarea[name='reason']");

    // 발송 버튼 클릭 시
    saveButton.addEventListener("click", async () => {
        const messageContent = textarea.value.trim();
        const recipientNumber = "01090837645"; // 수신자 번호

        if (!messageContent) {
            alert("문자 내용을 입력하세요.");
            return;
        }

        try {
            // 서버에 문자 발송 요청
            const response = await fetch("/report/messages/videoFunding/send", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ to: recipientNumber, text: messageContent })
            });

            const result = await response.json();
            if (result.success) {
                alert("문자가 성공적으로 발송되었습니다.");
                modal.style.display = "none"; // 모달창 닫기
                textarea.value = "";          // 입력 내용 초기화
            } else {
                alert("문자 발송에 실패했습니다: " + result.error);
            }
        } catch (error) {
            console.error("문자 발송 중 오류 발생:", error);
            alert("문자 발송에 실패했습니다. 다시 시도해주세요.");
        }
    });

    // 배경 클릭 시 모달 닫기
    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 페이지가 로드되면 모달 초기화 함수 실행
document.addEventListener("DOMContentLoaded", () => {
    setupMessageModal();
    setupMessageVideoFundingModal();
});
