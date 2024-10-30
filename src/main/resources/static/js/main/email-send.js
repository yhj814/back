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

// 이메일 인증 관련 변수
let emailTimerInterval;
const initialEmailTimeRemaining = 180; // 180초 타이머
let emailTimeRemaining = initialEmailTimeRemaining;
let isEmailVerified = false;

const emailField = document.querySelector("input[name='profileEmail']");
const emailRequestButton = document.getElementById("requestEmailCode");
const emailVerifyButton = document.getElementById("verifyEmailCode");
const loadingGif = document.getElementById("loadingGif");
const emailTimerDisplay = document.getElementById("emailTimerDisplay");
const emailVerificationError = document.getElementById("emailVerificationError");
const emailCertificationInput = document.querySelector(".certification-input-email"); // 이메일 인증 필드

// 이메일 인증번호 요청 버튼 클릭 시
emailRequestButton.addEventListener("click", async function () {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (validateField(emailField, emailRegex, "올바른 이메일 주소를 입력해주세요.")) {
        emailRequestButton.disabled = true;
        loadingGif.style.display = "block"; // 로딩 GIF 표시

        // 타이머 및 만료 메시지 초기화
        emailVerificationError.style.display = "none"; // 만료 메시지 숨김
        document.querySelector(".email-timer-container").style.display = "block"; // 이메일 타이머 컨테이너 표시
        emailTimerDisplay.textContent = "3:00"; // 타이머 초기화
        emailTimerDisplay.style.color = "#2099bb";

        clearInterval(emailTimerInterval); // 기존 타이머 중지
        emailTimeRemaining = initialEmailTimeRemaining; // 타이머 시간 초기화

        try {
            const response = await fetch("/email/send", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    email: emailField.value,
                }),
            });

            if (response.ok) {
                alert("인증번호가 이메일로 발송되었습니다."); // 성공 메시지 출력
                emailCertificationInput.style.display = "block"; // 이메일 인증 필드 표시

                startEmailTimer(); // 타이머 시작
            } else {
                alert("이메일 인증번호 요청에 실패했습니다. 잠시 후 다시 시도해주세요.");
            }
        } catch (error) {
            console.error("Error:", error);
        } finally {
            emailRequestButton.disabled = false;
            loadingGif.style.display = "none"; // 로딩 GIF 숨김
        }
    }
});

// 이메일 타이머 시작 함수
function startEmailTimer() {
    clearInterval(emailTimerInterval); // 기존 타이머 초기화

    emailTimerInterval = setInterval(() => {
        if (emailTimeRemaining <= 0) {
            clearInterval(emailTimerInterval); // 타이머 종료
            emailTimerDisplay.textContent = "인증 시간 만료. 다시 요청해 주세요.";
            emailTimerDisplay.style.color = "red";
            emailVerificationError.style.display = "none"; // 이전 만료 메세지 숨김
            return;
        }

        const minutes = Math.floor(emailTimeRemaining / 60);
        const seconds = emailTimeRemaining % 60;
        emailTimerDisplay.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
        emailTimeRemaining--; // 타이머 시간 감소
    }, 1000);
}

// 이메일 인증번호 확인 버튼 클릭 시
emailVerifyButton.addEventListener("click", async function () {
    const verificationCode = document.getElementById("emailVerificationCode").value;

    try {
        const response = await fetch("/email/verify", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                email: emailField.value,
                code: verificationCode,
            }),
        });

        const data = await response.text();
        if (data === "인증에 성공하였습니다.") {
            isEmailVerified = true;
            clearInterval(emailTimerInterval); // 타이머 종료
            document.querySelector(".email-timer-container").style.display = "none"; // 타이머 숨김
            emailVerificationError.textContent = "이메일 인증이 완료되었습니다.";
            emailVerificationError.style.display = "block";
            emailVerificationError.style.color = "#2099bb";
        } else {
            isEmailVerified = false;
            emailVerificationError.textContent = data;
            emailVerificationError.style.display = "block";
            emailVerificationError.style.color = "red";
        }
    } catch (error) {
        console.error("Error:", error);
    }
});
