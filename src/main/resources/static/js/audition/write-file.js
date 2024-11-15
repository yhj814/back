let uploadedFiles = []; // 업로드된 파일명을 저장하는 배열
let thumbnailFileName = null; // 썸네일 파일명을 저장하는 변수

document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("#write-form");
    const submitButton = document.querySelector(".btn-submit");
    const inputElements = document.querySelectorAll(".label-input-partner input");
    const textareaElements = document.querySelectorAll(".textarea__border textarea");
    const radioButtons = document.querySelectorAll('input[type="radio"]');

    // 입력 필드 이벤트 리스너 설정
    function setupInputFieldEvents() {
        inputElements.forEach((inputElement) => {
            const labelInputPartner = inputElement.closest(".label-input-partner");
            if (inputElement) {

                inputElement.addEventListener("focus", function () {
                    labelInputPartner.classList.add("label-effect");
                    if (!inputElement.classList.contains("error")) {
                    }
                });

                inputElement.addEventListener("blur", function () {
                    if (!inputElement.value) {
                        labelInputPartner.classList.remove("label-effect");
                        inputElement.classList.add("error");
                    } else {
                        inputElement.classList.remove("error");
                    }
                });

                inputElement.addEventListener("input", function () {
                    if (inputElement.classList.contains("error")) {
                        inputElement.classList.remove("error");
                    }
                    labelInputPartner.classList.add("label-effect");
                });

                inputElement.addEventListener("mouseover", function () {
                    if (!inputElement.classList.contains("error")) {
                    }
                });

                inputElement.addEventListener("mouseout", function () {
                    if (!inputElement.value && !inputElement.classList.contains("error")) {

                    }
                });

                inputElement.addEventListener("keydown", function (event) {
                    if (event.key === "Enter") {
                        event.preventDefault();
                    }
                });
            }
        });

        textareaElements.forEach((textareaElement) => {
            const textareaBorder = textareaElement.closest(".textarea__border");
            if (textareaElement) {


                textareaElement.addEventListener("focus", function () {
                    textareaBorder.classList.add("active");
                    if (textareaBorder.classList.contains("error")) {
                    } else {
                    }
                });

                textareaElement.addEventListener("blur", function () {
                    if (!textareaElement.value) {
                        textareaBorder.classList.add("error");
                    } else {
                        textareaBorder.classList.remove("error");
                    }
                    textareaBorder.classList.remove("active");
                });

                textareaElement.addEventListener("input", function () {
                    if (textareaBorder.classList.contains("error")) {
                        textareaBorder.classList.remove("error");
                    }
                });

                textareaElement.addEventListener("mouseover", function () {
                    if (textareaBorder.classList.contains("error")) {
                    }
                });
            }
        });
    }

    // 함수 호출하여 이벤트 리스너 설정
    setupInputFieldEvents();

    // 이미지 및 비디오 파일 업로드 처리 함수
    function handleFileUpload(fileInput, previewSelector, videoPreviewSelector) {
        const file = fileInput.files[0];
        if (!file) {
            console.error("파일이 선택되지 않았습니다.");
            return;
        }

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

            // 이미지 업로드 시 텍스트 요소 숨기기
            if (title) title.style.display = "none";
            if (text) text.style.display = "none";
            if (help) help.style.display = "none";
            if (imgCaptionBox) imgCaptionBox.style.display = "block";
            if (imgEditBox) imgEditBox.style.display = "block";
        });
        reader.readAsDataURL(file);

        // 서버로 파일 업로드
        const formData = new FormData();
        formData.append('file', file);

        fetch('/audition/video/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(fileName => {
                if (fileName !== "error") {
                    console.log('파일이 성공적으로 업로드되었습니다:', fileName);
                    uploadedFiles.push(fileName); // 파일명 저장

                    // 숨겨진 입력 필드 생성
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'fileNames';
                    hiddenInput.value = fileName;
                    hiddenInput.id = `file-${fileName}`; // ID 설정
                    fileInput.parentNode.appendChild(hiddenInput);

                    // 파일 입력 필드에서 name 속성 제거
                    fileInput.removeAttribute('name');
                    // 파일명을 input 요소에 저장 (삭제 시 사용)
                    fileInput.setAttribute('data-file-name', fileName);
                } else {
                    console.error('파일 업로드 실패.');
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
            });
    }

    // 이미지 박스에 이벤트 리스너 설정하는 함수
    function setupEventListeners(imgBox) {
        const defaultImg = imgBox.querySelector(".default-img");
        const fileUpload = imgBox.querySelector('input[type="file"]');
        const preview = imgBox.querySelector("img");
        const videoPreview = imgBox.querySelector("video");

        defaultImg.addEventListener("click", function () {
            fileUpload.click();
        });

        fileUpload.addEventListener("change", function () {
            handleFileUpload(fileUpload, `#${preview.id}`, `#${videoPreview.id}`);
        });

        const btnChangeImage = imgBox.querySelector(".btn-edit-item:nth-child(1)");
        btnChangeImage.addEventListener("click", function () {
            fileUpload.click();
        });

        const btnDeleteImage = imgBox.querySelector(".btn-edit-item:nth-child(2)");
        btnDeleteImage.addEventListener("click", function () {
            const fileName = fileUpload.getAttribute('data-file-name');

            // 업로드된 파일 배열에서 해당 파일명 제거
            uploadedFiles = uploadedFiles.filter(name => name !== fileName);

            // 숨겨진 입력 필드 제거
            const hiddenInput = document.getElementById(`file-${fileName}`);
            if (hiddenInput) {
                hiddenInput.remove();
            }

            // DOM에서 imgBox 제거
            imgBox.remove();
        });
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
                <div class="img-edit-box" style="margin-left: 510px; margin-top: 28px; display: none;">
                    <div class="btn-edit-item">
                        이미지 변경
                    </div>
                    <div class="btn-edit-item">
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
                    <!-- 캡션 입력 박스 추가 가능 -->
                </div>
            </div>
        `;

        imgBoxContainer.append(newImgBox);
        setupEventListeners(newImgBox);
    });

    // 폼 제출 시 처리
    form.addEventListener("submit", function (event) {
        // 필요한 경우, 폼 유효성 검사 수행
        const isInputValid = Array.from(inputElements).every(input => input.value.trim() !== "");
        const isTextareaValid = Array.from(textareaElements).every(textarea => textarea.value.trim() !== "");
        const isRadioSelected = Array.from(radioButtons).some(radio => radio.checked);

        if (!isInputValid || !isTextareaValid || !isRadioSelected) {
            event.preventDefault();
            alert("모든 필수 입력 필드를 채워주세요.");
            return;
        }

        // 폼 제출 시 파일 입력 필드 제거
        const fileInputs = form.querySelectorAll('input[type="file"]');
        fileInputs.forEach(fileInput => {
            fileInput.remove();
        });

        // 업로드된 파일명들은 이미 숨겨진 입력 필드로 추가되어 있음
        // 폼이 정상적으로 제출되도록 기본 동작 유지
    });

    // 버튼 활성화 확인 함수
    function checkFormCompletion() {
        const isInputValid = Array.from(inputElements).every(input => input.value.trim() !== "");
        const isTextareaValid = Array.from(textareaElements).every(textarea => textarea.value.trim() !== "");
        const isRadioSelected = Array.from(radioButtons).some(radio => radio.checked);

        if (isInputValid && isTextareaValid && isRadioSelected) {
            submitButton.disabled = false;
            submitButton.classList.add("active");
        } else {
            submitButton.disabled = true;
            submitButton.classList.remove("active");
        }
    }

    // 각 입력 필드에 이벤트 리스너 추가
    inputElements.forEach((inputElement) => {
        inputElement.addEventListener("input", checkFormCompletion);
    });

    textareaElements.forEach((textareaElement) => {
        textareaElement.addEventListener("input", checkFormCompletion);
    });

    radioButtons.forEach((radio) => {
        radio.addEventListener("change", checkFormCompletion);
    });

    // 페이지 로드 시 버튼 상태 확인
    checkFormCompletion();

    // 카테고리 박스 클릭 시 라디오 버튼 선택 및 스타일 변경
    const categoryBoxes = document.querySelectorAll(".project-category-box");

    categoryBoxes.forEach((box) => {
        box.addEventListener("click", () => {
            const radioInput = box.querySelector('input[type="radio"]');
            if (radioInput) {
                radioInput.checked = true;
                checkFormCompletion();
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
