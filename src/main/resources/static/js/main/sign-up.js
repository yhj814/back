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

    document.getElementById("requestVerificationCode").addEventListener("click", function () {
        const phoneRegex = /^(010|011|016|017|018|019)-\d{3,4}-\d{4}$/;

        if (validateField(phoneField, phoneRegex, "올바른 전화번호를 입력해주세요.")) {
            const phoneCertificationInput = document.getElementById("verificationCode").closest(".certification-input");
            if (phoneCertificationInput) {
                phoneCertificationInput.classList.remove("certification-input");
            }
        }
    });

    function validateAndSubmitForm() {
        let isValid = true;

        const emailField = document.querySelector("input[name='profileEmail']");
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!validateField(emailField, emailRegex, "올바른 이메일 주소를 입력해주세요.")) {
            isValid = false;
        }

        const phoneRegex = /^(010|011|016|017|018|019)-\d{3,4}-\d{4}$/;
        if (!validateField(phoneField, phoneRegex, "올바른 전화번호를 입력해주세요.")) {
            isValid = false;
        }

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
