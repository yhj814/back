let uploadedFiles = []; // 업로드된 파일명을 저장하는 배열
let thumbnailFileName = null; // 썸네일 파일명을 저장하는 변수

document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("#writeForm");
    const submitButton = document.querySelector(".btn-submit");
    const thumbnailFileInput = document.querySelector("#thumbnail-upload");
    const thumbnailPreviewImage = document.getElementById("preview-image");
    const inputElement = document.querySelector(".label-input-partner input");
    const labelInputPartner = document.querySelector(".label-input-partner");
    const textareaElement = document.querySelector(".textarea__border textarea");
    const textareaBorder = document.querySelector(".textarea__border");
    const radioButtons = document.querySelectorAll('input[type="radio"]');
    const categoryBoxes = document.querySelectorAll(".project-category-box");

    // 입력 필드 이벤트 리스너 추가
    function setupInputFieldEvents() {
        const inputElements = document.querySelectorAll(".label-input-partner input");
        const textareaElements = document.querySelectorAll(".textarea__border textarea");

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
                checkFormCompletion(); // 입력 값이 변경될 때마다 폼 상태 확인
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
                checkFormCompletion(); // 입력 값이 변경될 때마다 폼 상태 확인
            });

            textareaElement.addEventListener("mouseover", function () {
                if (textareaBorder.classList.contains("error")) {
                    textareaBorder.style.borderColor = "#e52929";
                }
            });
        }
    }

    // 함수 호출하여 이벤트 리스너 설정
    setupInputFieldEvents();

    // 썸네일 미리보기 및 업로드 함수
    function handleThumbnailUpload(event) {
        const file = event.target.files[0];
        if (!file) {
            console.error("파일이 선택되지 않았습니다.");
            return;
        }

        // 썸네일 미리보기 설정
        const reader = new FileReader();
        reader.onload = function (e) {
            thumbnailPreviewImage.src = e.target.result;
        };
        reader.readAsDataURL(file);

        // 서버로 파일 업로드
        const formData = new FormData();
        formData.append('file', file);

        fetch('/video/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(fileName => {
                if (fileName !== "error") {
                    console.log('썸네일 파일이 성공적으로 업로드되었습니다:', fileName);
                    thumbnailFileName = fileName; // 썸네일 파일명 저장

                    // 썸네일 파일명을 숨겨진 입력 필드에 저장
                    let thumbnailInput = document.querySelector('input[name="thumbnailFileName"]');
                    if (!thumbnailInput) {
                        thumbnailInput = document.createElement('input');
                        thumbnailInput.type = 'hidden';
                        thumbnailInput.name = 'thumbnailFileName';
                        form.appendChild(thumbnailInput);
                    }
                    thumbnailInput.value = fileName;

                    checkFormCompletion(); // 상태 업데이트
                } else {
                    console.error('썸네일 파일 업로드 실패.');
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
            });
    }

    // 썸네일 파일 변경 시 이벤트 설정
    thumbnailFileInput.addEventListener("change", handleThumbnailUpload);

    // 일반 파일 업로드 및 미리보기 함수
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

        fetch('/video/upload', {
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

                    // 파일명을 input 요소에 저장 (삭제 시 사용)
                    fileInput.setAttribute('data-file-name', fileName);

                    checkFormCompletion(); // 상태 업데이트
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
        const videoPreview = imgBox.querySelector("video"); // 비디오 미리보기 요소

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

            checkFormCompletion(); // 상태 업데이트
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
            </div>
        `;

        imgBoxContainer.append(newImgBox);
        setupEventListeners(newImgBox);
    });

    // 폼 제출 시 처리
    submitButton.addEventListener("click", function (event) {
        // 폼 제출 버튼이 비활성화된 경우 제출 방지
        if (submitButton.disabled) {
            event.preventDefault();
            return;
        }

        // 폼 제출
        form.submit();
    });

    // 버튼 활성화 확인 함수
    function checkFormCompletion() {
        const isAnyFileUploaded = uploadedFiles.length > 0 || thumbnailFileName !== null;
        const isInputValid = inputElement && inputElement.value.trim() !== "";
        const isTextareaValid = textareaElement && textareaElement.value.trim() !== "";
        const isRadioSelected = Array.from(radioButtons).some(radio => radio.checked);

        if (isAnyFileUploaded && isInputValid && isTextareaValid && isRadioSelected) {
            submitButton.disabled = false;
            submitButton.classList.add("active");
        } else {
            submitButton.disabled = true;
            submitButton.classList.remove("active");
        }
    }

    // 각 입력 필드에 이벤트 리스너 추가
    if (inputElement) {
        inputElement.addEventListener("input", checkFormCompletion);
    }
    if (textareaElement) {
        textareaElement.addEventListener("input", checkFormCompletion);
    }
    radioButtons.forEach((radio) => {
        radio.addEventListener("change", checkFormCompletion);
    });

    // 페이지 로드 시 버튼 상태 확인
    checkFormCompletion();

    // 카테고리 박스 클릭 시 라디오 버튼 선택 및 스타일 변경
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
