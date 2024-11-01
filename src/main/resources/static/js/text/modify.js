document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.querySelector(".label-input-partner input");
    const labelInputPartner = document.querySelector(".label-input-partner");
    const textareaElement = document.querySelector(".textarea__border textarea");
    const textareaBorder = document.querySelector(".textarea__border");

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
});

// 모든 img-box-list에 대해 이벤트 리스너를 설정하는 함수
function setupEventListeners(imgBox) {
    const defaultImg = imgBox.querySelector(".default-img");
    const fileUpload = imgBox.querySelector('input[type="file"]');
    const preview = imgBox.querySelector("img");
    const videoPreview = imgBox.querySelector("video");

    if (defaultImg) {
        defaultImg.addEventListener("click", function () {
            fileUpload && fileUpload.click();
        });
    }

    if (fileUpload) {
        fileUpload.addEventListener("change", function () {
            previewFile(fileUpload, `#${preview.id}`, `#${videoPreview.id}`);
        });
    }

    const btnChangeImage = imgBox.querySelector(".btn-edit-item:nth-child(1)");
    if (btnChangeImage) {
        btnChangeImage.addEventListener("click", function () {
            fileUpload && fileUpload.click();
        });
    }

    const btnDeleteImage = imgBox.querySelector(".btn-edit-item:nth-child(2)");
    if (btnDeleteImage) {
        btnDeleteImage.addEventListener("click", function () {
            preview.src =
                "https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png";
            videoPreview.src = "";
            videoPreview.style.display = "none";

            const imgCaptionBox = imgBox.querySelector(".img-caption-box");
            const title = imgBox.querySelector(".img-box-title");
            const text = imgBox.querySelector(".img-box-text");
            const help = imgBox.querySelector(".img-box-help");

            imgCaptionBox && (imgCaptionBox.style.display = "none");
            title && (title.style.display = "block");
            text && (text.style.display = "block");
            help && (help.style.display = "block");

            imgBox.style.display = "none";
            fileUpload.value = "";
        });
    }
}

// 기존 img-box-list에 대한 이벤트 리스너 설정
document.querySelectorAll(".img-box-list").forEach(setupEventListeners);

// 파일 추가 버튼 클릭 시 새로운 img-box-list 추가
document.querySelector(".img-add").addEventListener("click", function () {
    const imgBoxContainer = document.getElementById("img-box-container");
    const timestamp = Date.now();

    const newImgBox = document.createElement("div");
    newImgBox.classList.add("img-box-list");
    newImgBox.innerHTML = `
        <div class="img-content-box">
            <div class="img-edit-box">
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
                    <input id="file-upload-${timestamp}" type="file" accept="image/*,video/*" style="display: none;" />
                </div>
            </div>
            <div class="img-caption-box" style="display: none;">
                <div class="default-input-partner">
                    <input type="text" class="img-caption-box-content" placeholder="올린 파일에 대한 설명을 입력해주세요." />
                </div>
            </div>
        </div>
    `;

    imgBoxContainer.append(newImgBox);
    setupEventListeners(newImgBox);
});

// 파일 미리보기 함수 (파일 선택 시 이미지 및 비디오 미리보기)
function previewFile(fileInput, previewSelector, videoPreviewSelector) {
    const file = fileInput.files[0];
    const preview = document.querySelector(previewSelector);
    const videoPreview = document.querySelector(videoPreviewSelector);
    const imgBox = preview.closest(".img-box-list");
    const title = imgBox.querySelector(".img-box-title");
    const text = imgBox.querySelector(".img-box-text");
    const help = imgBox.querySelector(".img-box-help");
    const imgCaptionBox = imgBox.querySelector(".img-caption-box");
    const imgEditBox = imgBox.querySelector(".img-edit-box");

    const reader = new FileReader();

    reader.addEventListener("load", function () {
        if (file) {
            if (file.type.startsWith("image/")) {
                preview.src = reader.result;
                preview.style.display = "block";
                videoPreview.style.display = "none";
                title.style.display = "none";
                text.style.display = "none";
                help.style.display = "none";
                imgCaptionBox.style.display = "block";
                imgEditBox.style.display = "block";
            } else if (file.type.startsWith("video/")) {
                videoPreview.src = reader.result;
                videoPreview.style.display = "block";
                videoPreview.style.width = "100%";
                preview.style.display = "none";
                title.style.display = "none";
                text.style.display = "none";
                help.style.display = "none";
                imgCaptionBox.style.display = "block";
                imgEditBox.style.display = "block";
            }
        }
    });

    if (file) {
        reader.readAsDataURL(file);
    }
}

function previewImage(event) {
    const previewContainer = document.getElementById("preview-container");
    const file = event.target.files[0];

    previewContainer.innerHTML = "";

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.createElement("img");
            img.src = e.target.result;
            img.alt = "Uploaded Preview";
            img.style.width = "100%";
            img.style.borderRadius = "10px";

            previewContainer.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
}

// radio-div 클릭 이벤트 설정
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

        box.addEventListener("mouseleave", () => {
            box.style.backgroundColor = "";
        });
    });
});

// 제출 버튼 활성화 확인
document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.querySelector(".label-input-partner input");
    const textareaElement = document.querySelector(".textarea__border textarea");
    const radioButtons = document.querySelectorAll('input[type="radio"]');
    const fileInputs = document.querySelectorAll('input[type="file"]');
    const submitButton = document.querySelector(".btn-submit");

    function checkFormCompletion() {
        const allFilesUploaded = Array.from(fileInputs).every(
            (fileInput) => fileInput.files.length > 0
        );
        const isInputValid = inputElement && inputElement.value.trim() !== "";
        const isTextareaValid =
            textareaElement && textareaElement.value.trim() !== "";
        const isRadioSelected = Array.from(radioButtons).some(
            (radio) => radio.checked
        );

        if (
            allFilesUploaded &&
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
});
