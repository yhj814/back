document.addEventListener("DOMContentLoaded", () => {
    const inputElement = document.querySelector(".label-input-partner input");
    const labelInputPartner = document.querySelector(".label-input-partner");
    const textareaElement = document.querySelector(
        ".textarea__border textarea"
    );
    const textareaBorder = document.querySelector(".textarea__border");

    if (inputElement) {
        inputElement.style.outline = "none";
        inputElement.style.border = "none";

        inputElement.addEventListener("focus", () => {
            labelInputPartner.classList.add("label-effect");
            if (!inputElement.classList.contains("error")) {
                inputElement.style.borderColor = "#00a878";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            }
        });

        inputElement.addEventListener("blur", () => {
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

        inputElement.addEventListener("input", () => {
            if (inputElement.classList.contains("error")) {
                inputElement.classList.remove("error");
                inputElement.style.borderColor = "#00a878";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            }
            labelInputPartner.classList.add("label-effect");
        });

        inputElement.addEventListener("mouseover", () => {
            if (!inputElement.classList.contains("error")) {
                inputElement.style.borderColor = "#00a878";
                inputElement.style.borderWidth = "1px";
                inputElement.style.borderStyle = "solid";
            }
        });

        inputElement.addEventListener("mouseout", () => {
            if (
                !inputElement.value &&
                !inputElement.classList.contains("error")
            ) {
                inputElement.style.border = "1px solid #e0e0e0";
            }
        });

        inputElement.addEventListener("keydown", (event) => {
            if (event.key === "Enter") {
                event.preventDefault();
            }
        });
    }

    if (textareaElement) {
        textareaElement.style.outline = "none";
        textareaElement.style.border = "none";

        textareaElement.addEventListener("focus", () => {
            textareaBorder.classList.add("active");
            if (textareaBorder.classList.contains("error")) {
                textareaBorder.style.borderColor = "#e52929";
            } else {
                textareaBorder.style.borderColor = "#00a878";
            }
        });

        textareaElement.addEventListener("blur", () => {
            if (!textareaElement.value) {
                textareaBorder.classList.add("error");
                textareaBorder.style.borderColor = "#e52929";
            } else {
                textareaBorder.classList.remove("error");
                textareaBorder.style.border = "1px solid #e0e0e0";
            }
            textareaBorder.classList.remove("active");
        });

        textareaElement.addEventListener("input", () => {
            if (textareaBorder.classList.contains("error")) {
                textareaBorder.classList.remove("error");
                textareaBorder.style.borderColor = "#00a878";
            }
        });

        textareaElement.addEventListener("mouseover", () => {
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
    const videoPreview = imgBox.querySelector("video"); // 비디오 미리보기 요소 추가

    defaultImg.addEventListener("click", () => {
        fileUpload.click();
    });

    fileUpload.addEventListener("change", () => {
        previewFile(fileUpload, `#${preview.id}`, `#${videoPreview.id}`); // 비디오 미리보기 추가
    });

    const btnChangeImage = imgBox.querySelector(".btn-edit-item:nth-child(1)");
    btnChangeImage.addEventListener("click", () => {
        fileUpload.click();
    });

    const btnDeleteImage = imgBox.querySelector(".btn-edit-item:nth-child(2)");
    btnDeleteImage.addEventListener("click", () => {
        // 미리보기 이미지 및 관련 요소 초기화
        preview.src =
            "https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png"; // 기본 이미지
        videoPreview.src = ""; // 비디오 미리보기 초기화
        videoPreview.style.display = "none"; // 비디오 미리보기 숨기기
        const imgCaptionBox = imgBox.querySelector(".img-caption-box");
        const title = imgBox.querySelector(".img-box-title");
        const text = imgBox.querySelector(".img-box-text");
        const help = imgBox.querySelector(".img-box-help");
        const imgEditBox = imgBox.querySelector(".img-edit-box"); // img-edit-box에 대한 참조 추가

        imgCaptionBox.style.display = "none"; // 캡션 입력 박스 숨기기
        title.style.display = "block"; // 제목 다시 보이기
        text.style.display = "block"; // 설명 다시 보이기
        help.style.display = "block"; // 도움말 다시 보이기

        // 삭제버튼 누를 시 div 전체 삭제
        imgBox.style.display = "none";

        // 파일 입력 필드 초기화
        fileUpload.value = ""; // 파일 입력 필드 초기화
    });
}

// 기존 img-box-list에 대한 이벤트 리스너 설정
document.querySelectorAll(".img-box-list").forEach(setupEventListeners);

// 파일 추가 버튼 클릭 시 새로운 img-box-list 추가
document.querySelector(".img-add").addEventListener("click", () => {
    const imgBoxContainer = document.getElementById("img-box-container");
    const timestamp = Date.now(); // 고유 ID를 위한 타임스탬프 생성

    // 새로운 img-box-list 생성
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
                    <video id="video-preview-${timestamp}" class="video-tag" style="display: none;" controls></video> <!-- 비디오 미리보기 추가 -->
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

    // 추가된 img-box-list를 img-box-container 안의 마지막에 추가
    imgBoxContainer.append(newImgBox);

    // 새로 추가된 img-box-list에도 이벤트 리스너 설정
    setupEventListeners(newImgBox);
});

// 파일 미리보기 함수 (파일 선택 시 이미지 및 비디오 미리보기)
function previewFile(fileInput, previewSelector, videoPreviewSelector) {
    const file = fileInput.files[0];
    const preview = document.querySelector(previewSelector);
    const videoPreview = document.querySelector(videoPreviewSelector); // 비디오 미리보기 요소
    const imgBox = preview.closest(".img-box-list");
    const title = imgBox.querySelector(".img-box-title");
    const text = imgBox.querySelector(".img-box-text");
    const help = imgBox.querySelector(".img-box-help");
    const imgCaptionBox = imgBox.querySelector(".img-caption-box");
    const imgEditBox = imgBox.querySelector(".img-edit-box");

    const reader = new FileReader();

    reader.addEventListener("load", () => {
        if (file) {
            if (file.type.startsWith("image/")) {
                // 이미지 파일일 때
                preview.src = reader.result;
                preview.style.display = "block"; // 이미지 미리보기 표시
                videoPreview.style.display = "none"; // 비디오 미리보기 숨기기
                title.style.display = "none";
                text.style.display = "none";
                help.style.display = "none";
                imgCaptionBox.style.display = "block";
                imgEditBox.style.display = "block"; // img-edit-box를 block으로 설정
            } else if (file.type.startsWith("video/")) {
                // 비디오 파일일 때
                videoPreview.src = reader.result;
                videoPreview.style.display = "block"; // 비디오 미리보기 표시
                videoPreview.style.width = "100%";
                preview.style.display = "none"; // 이미지 미리보기 숨기기
                title.style.display = "none";
                text.style.display = "none";
                help.style.display = "none";
                imgCaptionBox.style.display = "block";
                imgEditBox.style.display = "block"; // img-edit-box를 block으로 설정
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

    // 이전 이미지를 지움
    previewContainer.innerHTML = "";

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            // 새 이미지 요소를 생성
            const img = document.createElement("img");
            img.src = e.target.result;
            img.alt = "Uploaded Preview";
            img.style.width = "100%";
            img.style.borderRadius = "10px";

            // 새 이미지를 미리보기 컨테이너에 추가
            previewContainer.appendChild(img);
        };
        // 업로드된 파일을 데이터 URL로 읽음
        reader.readAsDataURL(file);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const addButton = document.querySelector(".product-div-add");

    // 추가 버튼 클릭 시 상품 추가
    addButton.addEventListener("click", () => {
        const productContainer = document.querySelector(".product-container");

        // 복제할 product-add 요소 가져오기
        const productAdd = document
            .querySelector(".product-add")
            .cloneNode(true);
        productAdd.style.display = "block"; // 숨겨져 있을 경우 보이도록 처리

        // 복제된 요소의 취소 div display block 처리
        const productCancelDiv = productAdd.querySelector(".product-cancel");
        productCancelDiv.style.display = "block"; // 취소 div 표시

        // 취소 버튼 클릭 시 해당 product-add 삭제
        const cancelButton = productAdd.querySelector(".product-cancel-btn");
        cancelButton.addEventListener("click", () => {
            productAdd.remove();
        });

        // 새로운 요소를 product-div-add 위에 삽입
        const productDivAdd = document.querySelector(".product-div-add");
        productContainer.insertBefore(productAdd, productDivAdd);
    });
});
// radio-div 어디를 눌러도 클릭되게 함
document.addEventListener("click", () => {
    const categoryBoxes = document.querySelectorAll(".project-category-box");

    categoryBoxes.forEach((box) => {
        // div 클릭 시 내부 라디오 버튼 체크
        box.addEventListener("click", () => {
            const radioInput = box.querySelector('input[type="radio"]');
            if (radioInput) {
                radioInput.checked = true;
            }
        });

        // 마우스 올렸을 때 배경색 변경
        box.addEventListener("mouseenter", () => {
            box.style.backgroundColor = "#f7fafc";
        });

        // 마우스 뗐을 때 배경색 원래대로 변경
        box.addEventListener("mouseleave", () => {
            box.style.backgroundColor = ""; // 원래 배경색으로 복원
        });
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.querySelector(".label-input-partner input");
    const textareaElement = document.querySelector(
        ".textarea__border textarea"
    );
    const radioButtons = document.querySelectorAll('input[type="radio"]');
    const fileInputs = document.querySelectorAll('input[type="file"]');
    const submitButton = document.querySelector(".btn-submit");

    // 버튼 활성화 확인 함수
    function checkFormCompletion() {
        // 파일 업로드가 완료되었는지 확인
        const allFilesUploaded = Array.from(fileInputs).every(
            (fileInput) => fileInput.files.length > 0
        );
        // 텍스트 필드가 비어있지 않은지 확인
        const isInputValid = inputElement && inputElement.value.trim() !== "";
        const isTextareaValid =
            textareaElement && textareaElement.value.trim() !== "";
        // 라디오 버튼이 선택되었는지 확인
        const isRadioSelected = Array.from(radioButtons).some(
            (radio) => radio.checked
        );

        // 모든 조건을 만족하면 버튼 활성화, 그렇지 않으면 비활성화
        if (
            allFilesUploaded &&
            isInputValid &&
            isTextareaValid &&
            isRadioSelected
        ) {
            submitButton.disabled = false;
            submitButton.classList.add("active"); // 버튼 활성화 스타일 추가 (필요에 따라 클래스명 수정 가능)
        } else {
            submitButton.disabled = true;
            submitButton.classList.remove("active"); // 버튼 비활성화 스타일 제거
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
    fileInputs.forEach((fileInput) => {
        fileInput.addEventListener("change", checkFormCompletion);
    });

    // 페이지 로드 시에도 버튼 상태 확인
    checkFormCompletion();
});