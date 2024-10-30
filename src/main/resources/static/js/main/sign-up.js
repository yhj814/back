document.addEventListener("DOMContentLoaded", function () {
    const formControls = document.querySelectorAll(".form-control");

    formControls.forEach((control) => {
        let hasBeenFocused = false;

        control.addEventListener("focus", () => {
            control.classList.add("active");
            hasBeenFocused = true;
        });

        control.addEventListener("blur", () => {
            const errorMessage = control.closest(".input-gap")
                ? control.closest(".input-gap").parentElement.querySelector(".error-message")
                : control.parentElement.querySelector(".error-message");

            if (control.value.trim() === "" && !control.matches("textarea")) {
                control.classList.add("error");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
            } else {
                control.classList.remove("error");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });
    });

    // 유효성 검사 함수 (이메일, 전화번호, 인증번호 전용)
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

    // 전화번호 포맷 자동 추가
    const phoneField = document.querySelector("input[name='profilePhone']");
    phoneField.addEventListener("input", (event) => {
        let value = event.target.value.replace(/[^0-9]/g, "");
        if (value.length > 3 && value.length <= 7) {
            value = value.slice(0, 3) + "-" + value.slice(3);
        } else if (value.length > 7) {
            value = value.slice(0, 3) + "-" + value.slice(3, 7) + "-" + value.slice(7);
        }
        event.target.value = value;
    });

    // 이메일 인증번호 요청 버튼 클릭 시 유효성 검사 및 `.certification-input` 클래스 제거
    document.getElementById("requestEmailCode").addEventListener("click", function () {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const emailField = document.querySelector("input[name='profileEmail']");

        if (validateField(emailField, emailRegex, "올바른 이메일 주소를 입력해주세요.")) {
            const emailCertificationInput = document.getElementById("emailVerificationCode").closest(".certification-input");
            if (emailCertificationInput) {
                emailCertificationInput.classList.remove("certification-input");
            }
        }
    });

    // 전화번호 인증번호 요청 버튼 클릭 시 유효성 검사 및 `.certification-input` 클래스 제거
    document.getElementById("requestVerificationCode").addEventListener("click", function () {
        const phoneRegex = /^(010|011|016|017|018|019)-\d{3,4}-\d{4}$/;

        if (validateField(phoneField, phoneRegex, "올바른 전화번호를 입력해주세요.")) {
            const phoneCertificationInput = document.getElementById("verificationCode").closest(".certification-input");
            if (phoneCertificationInput) {
                phoneCertificationInput.classList.remove("certification-input");
            }
        }
    });

    // 전체 폼 유효성 검사 및 제출 함수
    function validateAndSubmitForm() {
        let isValid = true;

        // 이메일 필드 유효성 검사
        const emailField = document.querySelector("input[name='profileEmail']");
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!validateField(emailField, emailRegex, "올바른 이메일 주소를 입력해주세요.")) {
            isValid = false;
        }

        // 전화번호 필드 유효성 검사
        const phoneRegex = /^(010|011|016|017|018|019)-\d{3,4}-\d{4}$/;
        if (!validateField(phoneField, phoneRegex, "올바른 전화번호를 입력해주세요.")) {
            isValid = false;
        }

        // 인증번호 필드 유효성 검사
        const codeRegex = /^\d{4}$/;
        const emailCodeField = document.getElementById("emailVerificationCode");
        const phoneCodeField = document.getElementById("verificationCode");
        if (!validateField(emailCodeField, codeRegex, "4자리 인증번호를 입력해주세요.")) {
            isValid = false;
        }
        if (!validateField(phoneCodeField, codeRegex, "4자리 인증번호를 입력해주세요.")) {
            isValid = false;
        }

        // 기타 필드 유효성 검사
        document.querySelectorAll(".form-control").forEach(function (input) {
            const errorMessage = input.closest(".input-gap")
                ? input.closest(".input-gap").parentElement.querySelector(".error-message")
                : input.parentElement.querySelector(".error-message");

            if (!input.value.trim() && !input.matches(".temp-upload-resume") && !input.matches("textarea")) {
                input.classList.add("error");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
                isValid = false;
            } else {
                input.classList.remove("error");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });

        // 폼 제출 처리
        if (isValid) {
            document.getElementById("base-edit-form").submit();
        } else {
            console.log("Form validation failed. Please fill in all required fields.");
        }
    }

    document.getElementById("consulting_apply_button").addEventListener("click", function (event) {
        event.preventDefault();
        validateAndSubmitForm();
    });
});
