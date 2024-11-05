document.addEventListener("DOMContentLoaded", function () {
    const writeForm = document.getElementById("write-form");
    const deletedFileIds = [];

    // 폼 제출 시 삭제할 파일 정보 포함
    writeForm.addEventListener("submit", function () {
        if (deletedFileIds.length > 0) {
            const hiddenInput = document.createElement("input");
            hiddenInput.setAttribute("type", "hidden");
            hiddenInput.setAttribute("name", "deletedFileIds");
            hiddenInput.setAttribute("value", deletedFileIds.join(","));
            writeForm.appendChild(hiddenInput);
        }
    });

    // 인풋 및 텍스트 에어리어 효과 적용
    setupInputEffects(document.querySelector(".label-input-partner input"), document.querySelector(".label-input-partner"));
    setupInputEffects(document.querySelector("[name='auditionCareer']"), document.querySelector("[name='auditionCareer']").parentElement);
    setupInputEffects(document.querySelector("[name='expectedAmount']"), document.querySelector("[name='expectedAmount']").parentElement);
    setupTextareaEffects(document.querySelector(".textarea__border textarea"), document.querySelector(".textarea__border"));

    // 프로젝트 카테고리 박스 클릭 시 해당 라디오 버튼 체크
    document.querySelectorAll(".project-category-box").forEach(categoryBox => {
        categoryBox.addEventListener("click", function () {
            const radioInput = categoryBox.querySelector("input[type='radio']");
            if (radioInput) {
                radioInput.checked = true;
            }
        });
    });

    // 기존 이미지 박스에 이벤트 리스너 추가
    document.querySelectorAll(".img-box-list").forEach(setupEventListeners);

    // 파일 추가 버튼 클릭 시 새로운 이미지 박스 추가
    document.querySelector(".img-add").addEventListener("click", function () {
        addNewImageBox();
    });

    // 함수 정의

    // 인풋 효과 설정 함수
    function setupInputEffects(input, label) {
        if (!input) return;

        // 페이지 로드 시 label-effect 클래스 추가 (초기값이 있을 경우)
        if (input.value.trim()) {
            label.classList.add("label-effect");
        } else {
            label.classList.remove("label-effect");
        }

        input.addEventListener("focus", function () {
            label.classList.add("label-effect");
            input.style.border = "1px solid #00a878";
        });

        input.addEventListener("blur", function () {
            if (!input.value.trim()) {
                label.classList.remove("label-effect");
                input.classList.add("error");
                input.style.border = "1px solid #e52929";
            } else {
                input.classList.remove("error");
                input.style.border = "1px solid #e0e0e0";
            }
        });

        input.addEventListener("input", function () {
            if (input.classList.contains("error")) {
                input.classList.remove("error");
                input.style.border = "1px solid #00a878";
            }
            label.classList.add("label-effect");
        });
    }

    // 텍스트 에어리어 효과 설정 함수
    function setupTextareaEffects(textarea, border) {
        if (!textarea) return;

        if (textarea.value.trim()) {
            border.classList.add("active");
        } else {
            border.classList.remove("active");
        }

        textarea.addEventListener("focus", function () {
            border.classList.add("active");
            border.style.borderColor = "#00a878";
        });

        textarea.addEventListener("blur", function () {
            if (!textarea.value.trim()) {
                border.classList.add("error");
                border.style.borderColor = "#e52929";
            } else {
                border.classList.remove("error");
                border.style.border = "1px solid #e0e0e0";
            }
            border.classList.remove("active");
        });

        textarea.addEventListener("input", function () {
            if (border.classList.contains("error")) {
                border.classList.remove("error");
                border.style.borderColor = "#00a878";
            }
        });
    }

    // 이미지 박스 이벤트 리스너 설정 함수
    function setupEventListeners(imgBox) {
        const defaultImg = imgBox.querySelector(".default-img");
        const fileUpload = imgBox.querySelector('input[type="file"]');
        const preview = imgBox.querySelector("img");
        const videoPreview = imgBox.querySelector("video");

        defaultImg && defaultImg.addEventListener("click", () => fileUpload && fileUpload.click());

        if (fileUpload) {
            fileUpload.addEventListener("change", function () {
                previewFile(fileUpload, preview, videoPreview);
            });
        }

        const btnDeleteImage = imgBox.querySelector(".btn-edit-item[data-file-id]");
        btnDeleteImage && btnDeleteImage.addEventListener("click", function () {
            const fileId = btnDeleteImage.getAttribute("data-file-id");
            deletedFileIds.push(fileId);
            imgBox.style.display = "none";
        });
    }

    // 새로운 이미지 박스 추가 함수
    function addNewImageBox() {
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
                        <input id="file-upload-${timestamp}" type="file" name="newFiles" accept="image/*,video/*" style="display: none;" />
                    </div>
                </div>
            </div>
        `;

        imgBoxContainer.append(newImgBox);
        setupEventListeners(newImgBox);
    }

    // 파일 미리보기 함수 (파일 선택 시 이미지 및 비디오 미리보기)
    function previewFile(fileInput, preview, videoPreview) {
        const file = fileInput.files[0];
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
                    imgCaptionBox && (imgCaptionBox.style.display = "block");
                    imgEditBox && (imgEditBox.style.display = "block");
                } else if (file.type.startsWith("video/")) {
                    videoPreview.src = reader.result;
                    videoPreview.style.display = "block";
                    videoPreview.style.width = "100%";
                    preview.style.display = "none";
                    title.style.display = "none";
                    text.style.display = "none";
                    help.style.display = "none";
                    imgCaptionBox && (imgCaptionBox.style.display = "block");
                    imgEditBox && (imgEditBox.style.display = "block");
                }
            }
        });

        if (file) {
            reader.readAsDataURL(file);
        }
    }

    // 기존 파일 삭제 버튼 클릭 시
    document.querySelectorAll(".btn-edit-item[data-file-id]").forEach(button => {
        button.addEventListener("click", function () {
            const fileId = this.getAttribute("data-file-id");
            deletedFileIds.push(fileId); // 삭제할 파일 ID 저장
            console.log("삭제할 파일 ID 추가:", fileId); // 삭제할 파일 ID 로그 출력
            this.closest(".img-box-list").style.display = "none"; // UI에서 삭제 표시
        });
    });
});
