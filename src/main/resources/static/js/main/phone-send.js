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

// 전화번호 인증 관련 변수
let phoneTimerInterval;
const initialPhoneTimeRemaining = 180; // 3분(180초) 타이머
let phoneTimeRemaining = initialPhoneTimeRemaining;
let isPhoneVerified = false;

const phoneField = document.querySelector("input[name='profilePhone']");
const requestPhoneButton = document.getElementById("requestVerificationCode");
const verifyPhoneButton = document.getElementById("verifyCode");
const phoneTimerDisplay = document.getElementById("timerDisplay");
const phoneVerificationError = document.getElementById("verificationError");
const phoneCertificationInput = document.querySelector(".certification-input-phone"); // 전화번호 인증 필드

// 전화번호 인증번호 요청 버튼 클릭 시
requestPhoneButton.addEventListener("click", async function () {
    const phoneRegex = /^(010|011|016|017|018|019)-\d{3,4}-\d{4}$/;

    if (validateField(phoneField, phoneRegex, "올바른 전화번호를 입력해주세요.")) {
        const plainPhoneNumber = phoneField.value.replace(/-/g, "");
        requestPhoneButton.disabled = true;

        // 타이머 및 만료 메시지 초기화
        phoneVerificationError.style.display = "none"; // 만료 메시지 숨김
        document.querySelector(".timer-container").style.display = "block"; // 타이머 컨테이너 표시
        phoneTimerDisplay.textContent = "3:00"; // 타이머 초기화
        phoneTimerDisplay.style.color = "#2099bb";

        clearInterval(phoneTimerInterval); // 기존 타이머 중지
        phoneTimeRemaining = initialPhoneTimeRemaining; // 타이머 시간 초기화

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
                alert("인증번호가 전화번호로 발송되었습니다."); // 성공 메시지 출력
                phoneCertificationInput.style.display = "block"; // 전화번호 인증 필드 표시

                startPhoneTimer(); // 타이머 시작
            } else {
                alert("전화번호 인증번호 요청에 실패했습니다. 잠시 후 다시 시도해주세요.");
            }
        } catch (error) {
            console.error("Error:", error);
        } finally {
            requestPhoneButton.disabled = false;
        }
    }
});

// 전화번호 타이머 시작 함수
function startPhoneTimer() {
    clearInterval(phoneTimerInterval); // 기존 타이머 초기화

    phoneTimerInterval = setInterval(() => {
        if (phoneTimeRemaining <= 0) {
            clearInterval(phoneTimerInterval); // 타이머 종료
            phoneTimerDisplay.textContent = "인증 시간 만료. 다시 요청해 주세요.";
            phoneTimerDisplay.style.color = "red"; // 만료 메시지 색상 변경
            phoneVerificationError.style.display = "none"; // 이전 만료 메시지 숨김
            return;
        }

        const minutes = Math.floor(phoneTimeRemaining / 60);
        const seconds = phoneTimeRemaining % 60;
        phoneTimerDisplay.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
        phoneTimeRemaining--; // 타이머 시간 감소
    }, 1000);
}

// 전화번호 인증번호 확인 버튼 클릭 시
verifyPhoneButton.addEventListener("click", async function () {
    const verificationCode = document.getElementById("verificationCode").value;

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
        if (data === "인증에 성공하였습니다.") {
            isPhoneVerified = true;
            clearInterval(phoneTimerInterval); // 타이머 종료
            document.querySelector(".timer-container").style.display = "none"; // 타이머 숨김
            phoneVerificationError.textContent = "전화번호 인증이 완료되었습니다.";
            phoneVerificationError.style.display = "block";
            phoneVerificationError.style.color = "#2099bb";
        } else {
            isPhoneVerified = false;
            phoneVerificationError.textContent = data;
            phoneVerificationError.style.display = "block";
            phoneVerificationError.style.color = "red";
        }
    } catch (error) {
        console.error("Error:", error);
    }
});
