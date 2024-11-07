document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.querySelector(".label-input-partner input");
    const labelInputPartner = document.querySelector(".label-input-partner");
    const textareaElement = document.querySelector(".textarea__border textarea");
    const textareaBorder = document.querySelector(".textarea__border");

    // Input field styling and validation
    if (inputElement) {
        inputElement.style.outline = "none";
        inputElement.style.border = "none";

        inputElement.addEventListener("focus", function () {
            labelInputPartner.classList.add("label-effect");
            if (!inputElement.classList.contains("error")) {
                inputElement.style.borderColor = "#00a878";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            }
        });

        inputElement.addEventListener("blur", function () {
            if (!inputElement.value) {
                labelInputPartner.classList.remove("label-effect");
                inputElement.classList.add("error");
                inputElement.style.borderColor = "#e52929";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            } else {
                inputElement.classList.remove("error");
                inputElement.style.border = "1px solid #e0e0e0";
            }
        });

        inputElement.addEventListener("input", function () {
            if (inputElement.classList.contains("error")) {
                inputElement.classList.remove("error");
                inputElement.style.borderColor = "#00a878";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            }
            labelInputPartner.classList.add("label-effect");
        });

        inputElement.addEventListener("mouseover", function () {
            if (!inputElement.classList.contains("error")) {
                inputElement.style.borderColor = "#00a878";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            }
        });

        inputElement.addEventListener("mouseout", function () {
            if (!inputElement.value && !inputElement.classList.contains("error")) {
                inputElement.style.border = "1px solid #e0e0e0";
            }
        });

        inputElement.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
            }
        });
    }

    // Textarea field styling and validation
    if (textareaElement) {
        textareaElement.style.outline = "none";
        textareaElement.style.border = "none";

        textareaElement.addEventListener("focus", function () {
            textareaBorder.classList.add("active");
            if (textareaBorder.classList.contains("error")) {
                textareaBorder.style.borderColor = "#e52929";
            } else {
                textareaBorder.style.borderColor = "#00a878";
            }
        });

        textareaElement.addEventListener("blur", function () {
            if (!textareaElement.value) {
                textareaBorder.classList.add("error");
                textareaBorder.style.borderColor = "#e52929";
            } else {
                textareaBorder.classList.remove("error");
                textareaBorder.style.border = "1px solid #e0e0e0";
            }
            textareaBorder.classList.remove("active");
        });

        textareaElement.addEventListener("input", function () {
            if (textareaBorder.classList.contains("error")) {
                textareaBorder.classList.remove("error");
                textareaBorder.style.borderColor = "#00a878";
            }
        });

        textareaElement.addEventListener("mouseover", function () {
            if (textareaBorder.classList.contains("error")) {
                textareaBorder.style.borderColor = "#e52929";
            }
        });
    }

    // Handle file upload and preview setup
    const fileUploadInputs = document.querySelectorAll('input[type="file"]');
    fileUploadInputs.forEach((fileInput) => {
        fileInput.addEventListener("change", function () {
            uploadFileToServer(fileInput); // Server upload when file changes
        });
    });

    const form = document.querySelector("#writeForm");
    const submitButton = document.querySelector(".btn-submit");

    submitButton.addEventListener("click", function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        for (let [key, value] of formData.entries()) {
            console.log(`${key}: ${value}`);
        }

        fetch("/text/write", {
            method: "POST",
            body: formData
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    window.location.href = "/text/list";
                } else {
                    alert("저장 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => console.error("Error:", error));
    });

    const radioButtons = document.querySelectorAll('input[type="radio"]');
    const fileInputs = document.querySelectorAll('input[type="file"]');

    function checkFormCompletion() {
        const isAnyFileUploaded = Array.from(fileInputs).some(
            (fileInput) => fileInput.files.length > 0
        );

        const isInputValid = inputElement && inputElement.value.trim() !== "";
        const isTextareaValid =
            textareaElement && textareaElement.value.trim() !== "";
        const isRadioSelected = Array.from(radioButtons).some(
            (radio) => radio.checked
        );

        if (
            isAnyFileUploaded &&
            isInputValid &&
            isTextareaValid &&
            isRadioSelected
        ) {
            submitButton.disabled = false;
            submitButton.classList.add("active");
        } else {
            submitButton.disabled = true;
            submitButton.classList.remove("active");
        }
    }

    if (inputElement) {
        inputElement.addEventListener("input", checkFormCompletion);
    }
    if (textareaElement) {
        textareaElement.addEventListener("input", checkFormCompletion);
    }
    radioButtons.forEach((radio) => {
        radio.addEventListener("change", checkFormCompletion);
    });
    fileInputs.forEach((fileInput) => {
        fileInput.addEventListener("change", checkFormCompletion);
    });

    checkFormCompletion();

    function setupEventListeners(imgBox) {
        const defaultImg = imgBox.querySelector(".default-img");
        const fileUpload = imgBox.querySelector('input[type="file"]');
        const preview = imgBox.querySelector("img");
        const videoPreview = imgBox.querySelector("video");

        defaultImg.addEventListener("click", function () {
            fileUpload.click();
        });

        fileUpload.addEventListener("change", function () {
            previewFile(fileUpload, `#${preview.id}`, `#${videoPreview.id}`);
            uploadFileToServer(fileUpload);
        });

        const btnChangeImage = imgBox.querySelector(".btn-edit-item:nth-child(1)");
        btnChangeImage.addEventListener("click", function () {
            fileUpload.click();
        });

        const btnDeleteImage = imgBox.querySelector(".btn-edit-item:nth-child(2)");
        btnDeleteImage.addEventListener("click", function () {
            imgBox.remove();
        });
    }

    function uploadFileToServer(fileInput) {
        const file = fileInput.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        fetch("/text/upload", {
            method: "POST",
            body: formData,
        })
            .then((response) => {
                if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
                return response.json();
            })
            .then((data) => {
                if (data.fileName && data.filePath) {
                    console.log("파일이 성공적으로 업로드되었습니다.");
                } else {
                    console.error("파일 응답 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => {
                console.error("파일 업로드 중 오류가 발생했습니다:", error.message);
                alert("파일 업로드 중 오류가 발생했습니다. 다시 시도해 주세요.");
            });
    }

    document.querySelectorAll(".img-box-list").forEach(setupEventListeners);

    document.querySelector(".img-add").addEventListener("click", function () {
        const imgBoxContainer = document.getElementById("img-box-container");
        const timestamp = Date.now();

        const newImgBox = document.createElement("div");
        newImgBox.classList.add("img-box-list");
        newImgBox.innerHTML = `
        <div class="img-content-box">
            <div class="img-edit-box" style="margin-left: 510px; margin-top: 28px;">
                <div class="btn-edit-item" id="btn-change-image-${timestamp}">
                    이미지 변경
                </div>
                <div class="btn-edit-item" id="btn-delete-image-${timestamp}">
                    이미지 삭제
                </div>
            </div>
            <div class="center-text img-box">
                <div class="default-img" id="default-img-${timestamp}">
                    <img id="preview-${timestamp}" src="https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png" class="img-tag" />
                    <video id="video-preview-${timestamp}" class="video-tag" style="display: none;" controls></video>
                    <div class="img-box-title">작품 영상, 이미지 등록</div>
                    <div class="img-box-text">작품 결과물 혹은 설명을 돕는 이미지를 선택해 주세요.</div>
                    <div class="img-box-help"><span>· 이미지 최적 사이즈: 가로 720px</span></div>
                    <input
                        type="file"
                        name="workFile"
                        accept="image/*,video/*"
                        style="display: none;"
                        onchange="previewFile(this, '#preview-${timestamp}', '#video-preview-${timestamp}')"
                    />
                </div>
            </div>
        </div>
    `;

        imgBoxContainer.append(newImgBox);
        setupEventListeners(newImgBox);
    });

    function previewFile(fileInput, previewSelector, videoPreviewSelector) {
        if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
            console.error("No file selected.");
            return;
        }

        const file = fileInput.files[0];
        const preview = document.querySelector(previewSelector);
        const videoPreview = document.querySelector(videoPreviewSelector);

        if (!preview || !videoPreview) {
            console.error("Preview elements are not found.");
            return;
        }

        const reader = new FileReader();

        reader.addEventListener("load", function () {
            if (file.type.startsWith("image/")) {
                preview.src = reader.result;
                preview.style.display = "block";
                videoPreview.style.display = "none";
            } else if (file.type.startsWith("video/")) {
                videoPreview.src = reader.result;
                videoPreview.style.display = "block";
                videoPreview.style.width = "100%";
                preview.style.display = "none";
            }
        });

        reader.readAsDataURL(file);
    }

    document.addEventListener("click", () => {
        const categoryBoxes = document.querySelectorAll(".project-category-box");

        categoryBoxes.forEach((box) => {
            box.addEventListener("click", () => {
                const radioInput = box.querySelector('input[type="radio"]');
                if (radioInput) {
                    radioInput.checked = true;
                }
            });

            box.addEventListener("mouseenter", () => {
                box.style.backgroundColor = "#f7fafc";
            });

            box.style.backgroundColor = "";
        });
    });
});
