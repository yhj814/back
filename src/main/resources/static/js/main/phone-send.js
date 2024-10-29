// 필드 유효성 검사 함수
function validateField(input, regex, errorMessageText) {
    const errorMessage = input.closest(".input-gap")
        ? input.closest(".input-gap").parentElement.querySelector(".error-message")
        : input.parentElement.querySelector(".error-message");

    if (!regex.test(input.value.trim())) {
        input.classList.add("error");
        if (errorMessage) {
            errorMessage.textContent = errorMessageText;
            errorMessage.style.display = "block";
        }
        return false;
    } else {
        input.classList.remove("error");
        if (errorMessage) {
            errorMessage.style.display = "none";
        }
        return true;
    }
}

// 타이머 관련 변수
let timerInterval;
let timeRemaining = 180; // 3분(180초) 타이머
let isPhoneVerified = false; // 인증 완료 여부 확인용

// 전화번호 입력 필드
const phoneField = document.querySelector("input[name='profilePhone']");
const requestButton = document.getElementById("requestVerificationCode");
const verifyButton = document.getElementById("verifyCode");

// 인증번호 요청 버튼 클릭 시 타이머와 인증 로직 시작
requestButton.addEventListener("click", async function () {
    const phoneRegex = /^(010|011|016|017|018|019)-\d{3,4}-\d{4}$/;

    if (validateField(phoneField, phoneRegex, "올바른 전화번호를 입력해주세요.")) {
        const plainPhoneNumber = phoneField.value.replace(/-/g, "");
        requestButton.disabled = true; // 버튼 비활성화

        try {
            const response = await fetch("/sms/send", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    phoneNumber: plainPhoneNumber,
                }),
            });

            if (response.ok) {
                const message = await response.text();
                alert(message); // 성공 메시지 출력
                startTimer(); // 타이머 시작
                document.querySelector(".timer-container").style.display = "block"; // 타이머 표시
            } else {
                alert("인증번호 요청에 실패했습니다. 잠시 후 다시 시도해주세요.");
            }
        } catch (error) {
            console.error("Error:", error);
        } finally {
            requestButton.disabled = false; // 응답 후 버튼 활성화
        }
    }
});

// 타이머 시작 함수
function startTimer() {
    const timerDisplay = document.getElementById("timerDisplay");
    timeRemaining = 180; // 3분 초기화

    // 기존 타이머가 실행 중일 경우 초기화
    if (timerInterval) {
        clearInterval(timerInterval);
    }

    // 타이머 업데이트
    timerInterval = setInterval(() => {
        timeRemaining--;
        const minutes = Math.floor(timeRemaining / 60);
        const seconds = timeRemaining % 60;
        timerDisplay.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

        if (timeRemaining <= 0) {
            clearInterval(timerInterval);
            timerDisplay.textContent = "인증 시간 만료. 다시 요청해 주세요.";
            timerDisplay.style.color = "red"
            isPhoneVerified = false; // 인증 완료 여부 초기화
            console.log("타이머가 종료되었습니다."); // 타이머 종료 로그
        }
    }, 1000);
}

// 인증번호 확인 버튼 클릭 시
verifyButton.addEventListener("click", async function () {
    const verificationCode = document.getElementById("verificationCode").value;
    const verificationError = document.getElementById("verificationError"); // 에러/성공 메시지 표시 영역
    verifyButton.disabled = true; // 인증번호 확인 버튼 비활성화

    try {
        const response = await fetch("/sms/verify", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                phoneNumber: phoneField.value.replace(/-/g, ""),
                code: verificationCode,
            }),
        });

        const data = await response.text();
        console.log("서버 응답:", data); // 서버 응답 확인용 로그
        if (data === "인증에 성공하였습니다.") {
            isPhoneVerified = true; // 인증 완료
            clearInterval(timerInterval); // 타이머 중지
            document.querySelector(".timer-container").style.display = "none"; // 타이머 숨김
            verificationError.style.display = "block";
            verificationError.textContent = "인증이 완료되었습니다."; // 성공 메시지 출력
            verificationError.style.color = "#2099bb"; // 성공 메시지 색상
        } else {
            isPhoneVerified = false;
            verificationError.style.display = "block";
            verificationError.textContent = data; // 에러 메시지 출력
            verificationError.style.color = "red"; // 에러 메시지 색상
            console.log("인증 실패: ", data); // 인증 실패 로그
        }
    } catch (error) {
        console.error("Error:", error);
    } finally {
        verifyButton.disabled = false; // 응답 후 버튼 활성화
    }
});
