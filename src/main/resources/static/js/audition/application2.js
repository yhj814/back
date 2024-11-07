document.addEventListener("DOMContentLoaded", function () {
    // 모든 textarea__border에서 active 클래스 제거
    document.addEventListener("click", function (event) {
        document.querySelectorAll(".textarea__border").forEach(function (border) {
            border.classList.remove("active");
        });

        // 클릭된 요소가 textarea일 경우, 해당 요소의 부모 .textarea__border에 active 클래스 추가
        if (event.target.matches("textarea")) {
            const border = event.target.closest(".textarea__border");
            if (border) {
                border.classList.add("active");
            }
        }
    });

    const textAreas = document.querySelectorAll("textarea");

    textAreas.forEach((textarea) => {
        let hasBeenFocused = false;

        textarea.addEventListener("focus", () => {
            textarea.classList.add("active");
            hasBeenFocused = true;
        });

        textarea.addEventListener("blur", () => {
            textarea.classList.remove("active");
            if (hasBeenFocused && textarea.value.trim() === "") {
                textarea.classList.add("error");
                const errorMessage = textarea.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
            } else {
                textarea.classList.remove("error");
                const errorMessage = textarea.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });
    });

    document.addEventListener("focusout", function (event) {
        if (event.target.matches("textarea")) {
            const uiTextareaWishket = event.target.closest(".ui-textarea-wishket");
            if (uiTextareaWishket) {
                if (!event.target.value.trim()) {
                    uiTextareaWishket.classList.add("error");
                    uiTextareaWishket.querySelector(".error-text").style.display = "block";
                } else {
                    uiTextareaWishket.classList.remove("error");
                    uiTextareaWishket.querySelector(".error-text").style.display = "none";
                }
            }
        }
    });

    // textarea 유효성 검사 및 폼 제출
    function validateAndSubmitForm() {
        let isValid = true;
        document.querySelectorAll("textarea").forEach(function (textarea) {
            if (!textarea.value.trim()) {
                textarea.classList.add("error");
                const errorMessage = textarea.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
                isValid = false;
            } else {
                textarea.classList.remove("error");
                const errorMessage = textarea.parentElement.querySelector(".error-message");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });

        if (isValid) {
            document.querySelector("form").submit();
        }
    }

    document.getElementById("consulting_apply_button").addEventListener("click", function (event) {
        event.preventDefault();
        validateAndSubmitForm();
    });
});
