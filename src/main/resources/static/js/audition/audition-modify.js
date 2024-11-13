document.addEventListener("DOMContentLoaded", function () {
    const writeForm = document.getElementById("write-form");
    let uploadedFiles = []; // 업로드된 파일명을 저장하는 배열
    let deletedFileIds = [];

    // 페이지 첫 로드 시 input 값에 따라 label-effect 클래스 추가
    document.querySelectorAll('.label-input-partner').forEach((div) => {
        const input = div.querySelector('input');
        if (input && input.value.trim() !== '') {
            div.classList.add('label-effect');
        }
    });

    // 기존 파일 삭제 버튼 클릭 시
    document.querySelectorAll(".btn-edit-item[data-file-id]").forEach(button => {
        button.addEventListener("click", function () {
            const fileId = this.getAttribute("data-file-id");
            deletedFileIds.push(fileId); // 삭제할 파일 ID 저장
            console.log("삭제할 파일 ID 추가:", fileId); // 삭제할 파일 ID 로그 출력
            this.closest(".img-box-list").style.display = "none"; // UI에서 삭제 표시
        });
    });

    // 파일 추가 버튼 클릭 시 새로운 이미지 박스 추가
    document.querySelector(".img-add").addEventListener("click", function () {
        addNewImageBox();
    });

    // 새로운 이미지 박스 추가 함수
    function addNewImageBox() {
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
                        <input id="file-upload-${timestamp}" name="newFiles" type="file" accept="image/*,video/*" style="display: none;" />
                    </div>
                </div>
                <div class="img-caption-box" style="display: none;">
                    <p style="margin-bottom: 10px">파일 설명</p>
                    <div class="default-input-partner">
                        <input
                            type="text"
                            class="img-caption-box-content"
                            name="fileContent"
                            placeholder="올린 파일에 대한 설명을 입력해주세요."
                        />
                    </div>
                </div>
            </div>
        `;

        imgBoxContainer.append(newImgBox);
        setupEventListeners(newImgBox); // 새로 추가된 img-box-list에도 이벤트 리스너 설정
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
                } else if (file.type.startsWith("video/")) {
                    videoPreview.src = reader.result;
                    videoPreview.style.display = "block";
                    videoPreview.style.width = "100%";
                    preview.style.display = "none";
                }

                title.style.display = "none";
                text.style.display = "none";
                help.style.display = "none";
                imgCaptionBox && (imgCaptionBox.style.display = "block");
                imgEditBox && (imgEditBox.style.display = "block");
            }
        });

        if (file) {
            reader.readAsDataURL(file);

            // 서버로 파일 업로드
            const formData = new FormData();
            formData.append('file', file);

            fetch('/audition/text/upload', {
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

    // 폼 제출 시 삭제할 파일 및 업로드된 파일 정보 포함
    writeForm.addEventListener("submit", function (event) {
        const idInput = writeForm.querySelector("input[name='id']");

        if (!idInput || idInput.value === "") {
            console.error("오류: auditionDTO의 id가 설정되지 않았습니다.");
            event.preventDefault(); // 제출 중지
            return;
        }

        if (deletedFileIds.length > 0) {
            const hiddenInput = document.createElement("input");
            hiddenInput.setAttribute("type", "hidden");
            hiddenInput.setAttribute("name", "deletedFileIds");
            hiddenInput.setAttribute("value", deletedFileIds.join(","));
            writeForm.appendChild(hiddenInput);
        }

        console.log("폼 제출 시 업로드된 파일들:", uploadedFiles);
    });

    // 인풋 및 텍스트 에어리어 효과 설정
    const inputElements = document.querySelectorAll("input");
    inputElements.forEach((input) => {
        input.addEventListener("focus", function () {
            const parentDiv = input.closest(".label-input-partner");
            parentDiv && parentDiv.classList.add("label-effect");
        });

        input.addEventListener("blur", function () {
            if (input.value.trim() === "") {
                const parentDiv = input.closest(".label-input-partner");
                parentDiv && parentDiv.classList.remove("label-effect");
                input.classList.add("error");
                input.style.border = "solid 1px #e52929";
            } else {
                input.classList.remove("error");
                input.style.border = ""; // 원래 상태로 복구
            }
        });

        input.addEventListener("input", function () {
            if (input.classList.contains("error") && input.value.trim() !== "") {
                input.classList.remove("error");
                input.style.border = ""; // 원래 상태로 복구
            }
        });
    });

    const textareas = document.querySelectorAll(".textarea__border textarea");
    textareas.forEach((textarea) => {
        const parentDiv = textarea.closest(".textarea__border");
        textarea.addEventListener("focus", function () {
            parentDiv.classList.add("active");
        });
        textarea.addEventListener("blur", function () {
            if (textarea.value.trim() === "") {
                parentDiv.classList.remove("active");
                parentDiv.classList.add("error");
                textarea.style.border = "solid 1px #e52929";
            } else {
                parentDiv.classList.remove("error");
                textarea.style.border = ""; // 원래 상태로 복구
            }
        });
        textarea.addEventListener("input", function () {
            if (parentDiv.classList.contains("error") && textarea.value.trim() !== "") {
                parentDiv.classList.remove("error");
                textarea.style.border = ""; // 원래 상태로 복구
            }
        });
    });

    // 라디오 버튼 체크 및 배경색 효과
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

    // 날짜 입력 필드에 대한 처리
    const dateInputs = document.querySelectorAll('input[name="serviceStartDate"],input[name="auditionDeadLine"]');
    dateInputs.forEach(formatDateInput);

    function formatDateInput(input) {
        input.addEventListener("input", function () {
            let value = input.value.replace(/[^0-9]/g, "");
            let formattedValue = "";
            if (value.length <= 4) {
                formattedValue = value;
            } else if (value.length > 4 && value.length <= 6) {
                let year = value.slice(0, 4);
                let month = value.slice(4, 6);
                formattedValue = year + "." + month;
            } else if (value.length > 6 && value.length <= 8) {
                let year = value.slice(0, 4);
                let month = value.slice(4, 6);
                let day = value.slice(6, 8);
                formattedValue = year + "." + month + "." + day;
            }
            input.value = formattedValue;

            if (formattedValue.length !== 10) {
                input.classList.add("error");
                input.style.border = "solid 1px #e52929";
            } else {
                input.classList.remove("error");
                input.style.border = ""; // 원래 상태로 복구
            }
        });

        input.addEventListener("blur", function () {
            if (input.value.length !== 10) {
                input.classList.add("error");
                input.style.border = "solid 1px #e52929";
            }
        });
    }

    // 모집인원 입력 처리 함수
    const participantInput = document.querySelector('input[name="auditionPersonnel"]');
    if (participantInput) {
        participantInput.addEventListener("input", function () {
            let value = participantInput.value.replace(/[^0-9]/g, "");
            if (value) {
                participantInput.value = value + "명";
            }
        });

        participantInput.addEventListener("keydown", function (e) {
            if (participantInput.value.endsWith("명") && e.key === "Backspace") {
                participantInput.value = participantInput.value.slice(0, -1);
            }
        });
    }
});
