document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.querySelector(".label-input-partner input");
    const labelInputPartner = document.querySelector(".label-input-partner");
    const textareaElement = document.querySelector(".textarea__border textarea");
    const textareaBorder = document.querySelector(".textarea__border");
    let uploadedFiles = []; // 업로드된 파일명을 저장하는 배열


    // 썸네일 관련 요소 설정
    const thumbnailUploadInput = document.getElementById("thumbnail-upload");
    const previewContainer = document.getElementById("preview-container");
    const thumbnailPreview = previewContainer.querySelector("img");

    let isThumbnailChanged = false; // 썸네일 변경 여부를 추적하는 변수
    let uploadedThumbnailFileName = null;
    // 썸네일 변경 버튼 클릭 시 파일 선택창 열기
    document.querySelector(".work-thumbnail-add-btn").addEventListener("click", function () {
        if (thumbnailUploadInput) {
            thumbnailUploadInput.click();
        }
    });

    // 썸네일 파일 선택 시 서버로 업로드 및 미리보기 업데이트
    thumbnailUploadInput.addEventListener("change", function () {
        const file = thumbnailUploadInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                thumbnailPreview.src = e.target.result;
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
                        uploadedThumbnailFileName = fileName; // 업로드된 파일명 저장

                        // 숨겨진 입력 필드에 파일명 설정
                        let hiddenInput = document.getElementById('thumbnailFileName');
                        if (!hiddenInput) {
                            hiddenInput = document.createElement('input');
                            hiddenInput.type = 'hidden';
                            hiddenInput.name = 'thumbnailFileName';
                            hiddenInput.id = 'thumbnailFileName';
                            writeForm.appendChild(hiddenInput);
                        }
                        hiddenInput.value = fileName;

                        // 썸네일 변경 여부를 표시하는 숨겨진 입력 필드 설정
                        let thumbnailChangedInput = document.getElementById('thumbnailChanged');
                        if (!thumbnailChangedInput) {
                            thumbnailChangedInput = document.createElement('input');
                            thumbnailChangedInput.type = 'hidden';
                            thumbnailChangedInput.name = 'thumbnailChanged';
                            thumbnailChangedInput.id = 'thumbnailChanged';
                            writeForm.appendChild(thumbnailChangedInput);
                        }
                        thumbnailChangedInput.value = 'true';

                        isThumbnailChanged = true; // 썸네일 변경 여부 설정
                    } else {
                        console.error('썸네일 파일 업로드 실패.');
                    }
                })
                .catch(error => {
                    console.error('썸네일 업로드 중 에러 발생:', error);
                });
        }
    });

    // 폼 제출 시 썸네일과 삭제할 파일 정보 포함
    const writeForm = document.getElementById("write-form");
    writeForm.addEventListener("submit", function (event) {
        // 폼 제출 버튼이 비활성화된 경우 제출 방지
        if (submitButton.disabled) {
            event.preventDefault();
            return;
        }

        console.log("폼 제출 시 삭제할 파일 IDs:", deletedFileIds); // 삭제할 파일 ID 로그 출력

        if (deletedFileIds.length > 0) {
            const hiddenInput = document.createElement("input");
            hiddenInput.setAttribute("type", "hidden");
            hiddenInput.setAttribute("name", "deletedFileIds");
            hiddenInput.setAttribute("value", deletedFileIds.join(","));
            writeForm.appendChild(hiddenInput);
        }

        if (isThumbnailChanged) {
            const hiddenThumbnailInput = document.createElement("input");
            hiddenThumbnailInput.type = "hidden";
            hiddenThumbnailInput.name = "thumbnailChanged";
            hiddenThumbnailInput.value = "true";
            writeForm.appendChild(hiddenThumbnailInput);
        }
        console.log("폼 제출 시 업로드된 파일들:", uploadedFiles);
    });


    // 인풋 포커스 효과 설정
    setupInputEffects(inputElement, labelInputPartner);
    setupTextareaEffects(textareaElement, textareaBorder);

    // 기존 이미지 박스에 이벤트 리스너 추가
    document.querySelectorAll(".img-box-list").forEach(setupEventListeners);

    // 파일 추가 버튼 클릭 시 새로운 이미지 박스 추가
    document.querySelector(".img-add").addEventListener("click", function () {
        addNewImageBox();
    });

    // 함수 정의
    // 버튼 활성화 확인 함수
    const submitButton = document.querySelector(".btn-submit");
    function checkFormCompletion() {
        const isInputValid = inputElement && inputElement.value.trim() !== "";
        const isTextareaValid = textareaElement && textareaElement.value.trim() !== "";

        if (isInputValid && isTextareaValid) {
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

    // 인풋 효과 설정 함수
    function setupInputEffects(input, label) {
        if (!input) return;

        input.style.outline = "none";
        input.style.border = "none";

        input.addEventListener("focus", function () {
            label.classList.add("label-effect");
            if (!input.classList.contains("error")) {
                input.style.borderColor = "#00a878";
                input.style.borderWidth = "1px";
                input.style.borderStyle = "solid";
            }
        });

        input.addEventListener("blur", function () {
            if (!input.value) {
                label.classList.remove("label-effect");
                input.classList.add("error");
                input.style.borderColor = "#e52929";
                input.style.borderWidth = "1px";
                input.style.borderStyle = "solid";
            } else {
                input.classList.remove("error");
                input.style.border = "1px solid #e0e0e0";
            }
        });

        input.addEventListener("input", function () {
            if (input.classList.contains("error")) {
                input.classList.remove("error");
                input.style.borderColor = "#00a878";
                input.style.borderWidth = "1px";
                input.style.borderStyle = "solid";
            }
            label.classList.add("label-effect");
        });

        input.addEventListener("mouseover", function () {
            if (!input.classList.contains("error")) {
                input.style.borderColor = "#00a878";
                input.style.borderWidth = "1px";
                input.style.borderStyle = "solid";
            }
        });

        input.addEventListener("mouseout", function () {
            if (!input.value && !input.classList.contains("error")) {
                input.style.border = "1px solid #e0e0e0";
            }
        });

        input.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
            }
        });
    }

    // 텍스트 에어리어 효과 설정 함수
    function setupTextareaEffects(textarea, border) {
        if (!textarea) return;

        textarea.style.outline = "none";
        textarea.style.border = "none";

        textarea.addEventListener("focus", function () {
            border.classList.add("active");
            if (border.classList.contains("error")) {
                border.style.borderColor = "#e52929";
            } else {
                border.style.borderColor = "#00a878";
            }
        });

        textarea.addEventListener("blur", function () {
            if (!textarea.value) {
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

        textarea.addEventListener("mouseover", function () {
            if (border.classList.contains("error")) {
                border.style.borderColor = "#e52929";
            }
        });
    }

    // 이미지 박스 이벤트 리스너 설정 함수
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
                preview.src = "https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png";
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
    }

    const workPriceInput = document.querySelector('input[name="workPrice"]');
    if (workPriceInput) {
        workPriceInput.addEventListener("keydown", function (event) {
            // 숫자 키와 제어 키만 허용
            if (
                (event.keyCode < 48 || event.keyCode > 57) && // 숫자 키
                (event.keyCode < 96 || event.keyCode > 105) && // 숫자 패드 키
                event.keyCode !== 8 && // Backspace
                event.keyCode !== 9 && // Tab
                event.keyCode !== 37 && // 왼쪽 화살표
                event.keyCode !== 39 && // 오른쪽 화살표
                event.keyCode !== 46 // Delete
            ) {
                event.preventDefault();
            }

            // 최대 9자리까지 입력 제한
            if (this.value.length >= 9 && event.keyCode !== 8 && event.keyCode !== 46) {
                event.preventDefault();
            }
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
                    } else {
                        console.error('파일 업로드 실패.');
                    }
                })
                .catch(error => {
                    console.error('에러 발생:', error);
                });
        }
    }

    // 파일 ID 삭제 기능
    const deletedFileIds = [];

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
