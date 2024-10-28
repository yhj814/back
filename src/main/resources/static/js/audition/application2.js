document.addEventListener("DOMContentLoaded", function () {
    // 모든 textarea__border에서 active 클래스 제거
    document.addEventListener("click", function (event) {
        document
            .querySelectorAll(".textarea__border")
            .forEach(function (border) {
                border.classList.remove("active");
            });

        // 클릭된 요소가 input 또는 textarea일 경우, 해당 요소의 부모 .textarea__border에 active 클래스 추가
        if (event.target.matches("input, textarea")) {
            const border = event.target.closest(".textarea__border");
            if (border) {
                border.classList.add("active");
            }
        }
    });

    const formControls = document.querySelectorAll(".form-control");

    formControls.forEach((control) => {
        let hasBeenFocused = false;

        control.addEventListener("focus", () => {
            control.classList.add("active");
            hasBeenFocused = true;
        });

        control.addEventListener("blur", () => {
            control.classList.remove("active");
            if (
                hasBeenFocused &&
                control.value.trim() === "" &&
                !control.matches("textarea")
            ) {
                control.classList.add("error");
                const errorMessage =
                    control.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
            } else {
                control.classList.remove("error");
                const errorMessage =
                    control.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });
    });

    document.addEventListener("focusout", function (event) {
        if (event.target.matches("input, textarea")) {
            const uiTextareaWishket = event.target.closest(
                ".ui-textarea-wishket"
            );
            if (uiTextareaWishket) {
                if (
                    !event.target.value.trim() &&
                    !event.target.matches("textarea")
                ) {
                    uiTextareaWishket.classList.add("error");
                    uiTextareaWishket.querySelector(
                        ".error-text"
                    ).style.display = "block";
                } else {
                    uiTextareaWishket.classList.remove("error");
                    uiTextareaWishket.querySelector(
                        ".error-text"
                    ).style.display = "none";
                }
            }
        }
    });

    function validateAndSubmitForm() {
        let isValid = true;
        document.querySelectorAll(".form-control").forEach(function (input) {
            // 첨부파일(.temp-upload-resume)과 textarea는 유효성 검사 제외
            if (
                !input.value.trim() &&
                !input.matches(".temp-upload-resume, textarea")
            ) {
                input.classList.add("error");
                const errorMessage =
                    input.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
                isValid = false;
            } else {
                input.classList.remove("error");
                const errorMessage =
                    input.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });

        if (isValid) {
            document.querySelector("form").submit();
        }
    }

    document
        .getElementById("consulting_apply_button")
        .addEventListener("click", function (event) {
            event.preventDefault();
            validateAndSubmitForm();
        });
});
