document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("write-form");
    const submitButton = document.querySelector(".btn-submit");

    // 인풋 및 텍스트에어리어 요소
    const inputElement = document.querySelector(".label-input-partner input");
    const labelInputPartner = document.querySelector(".label-input-partner");
    const textareaElement = document.querySelector(".textarea__border textarea");
    const textareaBorder = document.querySelector(".textarea__border");

    // 이미지 및 파일 관련 요소
    const existingFilesContainer = document.getElementById("existing-files-container");
    const imgBoxContainer = document.getElementById("img-box-container");
    const addImageButton = document.querySelector(".img-add");

    // 펀딩 상품 관련 요소
    const existingProductsContainer = document.getElementById("existing-products-container");
    const newProductsContainer = document.getElementById("new-products-container"); // 새로운 상품 컨테이너
    const productAddButton = document.querySelector(".product-div-add");

    // 인덱스 관리: 기존 상품 수를 기준으로 새로운 인덱스 시작
    let existingProductCount = existingProductsContainer.querySelectorAll(".existing-product").length;
    let newProductIndex = existingProductCount; // 기존 상품 수로 초기화

    // 상태 추적 변수
    let thumbnailFileId = form.querySelector('input[name="thumbnailFileId"]').value || null;
    let deletedFileIds = []; // 삭제할 파일 ID 배열
    let fundingProductIds = []; // 삭제할 상품 ID 배열
    let uploadedThumbnailFileName = null;
    let isThumbnailChanged = false; // 썸네일 변경 여부

    // 썸네일 변경 버튼 클릭 시 파일 선택창 열기
    const thumbnailAddButton = document.querySelector(".work-thumbnail-add-btn");
    if (thumbnailAddButton) {
        thumbnailAddButton.addEventListener("click", function () {
            const thumbnailUploadInput = document.getElementById("thumbnail-upload");
            if (thumbnailUploadInput) {
                thumbnailUploadInput.click();
            }
        });
    }

    // 썸네일 파일 선택 시 서버로 업로드 및 미리보기 업데이트
    const thumbnailUploadInput = document.getElementById("thumbnail-upload");
    if (thumbnailUploadInput) {
        thumbnailUploadInput.addEventListener("change", function () {
            const file = thumbnailUploadInput.files[0];
            if (file) {
                console.log("썸네일 파일 선택:", file.name);
                const reader = new FileReader();
                reader.onload = function (e) {
                    const thumbnailPreview = document.querySelector("#preview-container img");
                    if (thumbnailPreview) {
                        thumbnailPreview.src = e.target.result;
                        console.log("썸네일 미리보기 업데이트");
                    }
                };
                reader.readAsDataURL(file);

                // 기존 썸네일 ID를 삭제할 파일 ID 배열에 추가 (중복 방지)
                if (thumbnailFileId && !deletedFileIds.includes(thumbnailFileId)) {
                    deletedFileIds.push(thumbnailFileId);
                    document.getElementById("deletedFileIds").value = deletedFileIds.join(",");
                    console.log("삭제할 파일 ID 추가:", thumbnailFileId);
                }

                // 서버로 파일 업로드
                const formData = new FormData();
                formData.append('file', file);

                fetch('/video/funding/upload', {
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
                                form.appendChild(hiddenInput);
                            }
                            hiddenInput.value = fileName;


                            // 썸네일 변경 여부를 표시하는 숨겨진 입력 필드 설정
                            let thumbnailChangedInput = document.getElementById('thumbnailChanged');
                            if (!thumbnailChangedInput) {
                                thumbnailChangedInput = document.createElement('input');
                                thumbnailChangedInput.type = 'hidden';
                                thumbnailChangedInput.name = 'thumbnailChanged';
                                thumbnailChangedInput.id = 'thumbnailChanged';
                                form.appendChild(thumbnailChangedInput);
                            }
                            thumbnailChangedInput.value = 'true';


                            isThumbnailChanged = true; // 썸네일 변경 여부 설정

                            // 업데이트된 썸네일 ID를 null로 설정 (이미 삭제된 상태이므로)
                            thumbnailFileId = null;
                        } else {
                            console.error('썸네일 파일 업로드 실패.');
                        }
                    })
                    .catch(error => {
                        console.error('썸네일 업로드 중 에러 발생:', error);
                    });
            }
        });
    }

    // 폼 제출 시 데이터 추출 및 디버깅
    form.addEventListener("submit", function (event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        // 폼 데이터 추출
        const formData = new FormData(form);

        // FormData 객체를 일반 객체로 변환
        const data = {};
        formData.forEach((value, key) => {
            // fundingProducts는 배열 형태로 변환
            if (key.startsWith("fundingProducts")) {
                const match = key.match(/fundingProducts\[(\d+)\]\.(\w+)/);
                if (match) {
                    const index = match[1];
                    const field = match[2];
                    if (!data.fundingProducts) data.fundingProducts = [];
                    if (!data.fundingProducts[index]) data.fundingProducts[index] = {};
                    data.fundingProducts[index][field] = value;
                }
            } else {
                data[key] = value;
            }
        });

        // 삭제할 파일 IDs, 삭제할 상품 IDs는 별도로 추가
        data.deletedFileIds = deletedFileIds;
        data.fundingProductIds = fundingProductIds;
        data.thumbnailFileName = uploadedThumbnailFileName;

        // 추출된 데이터 로그 출력
        console.log("제출할 데이터:", data);


        form.submit();
    });

    // 폼 유효성 검사 함수
    function checkFormCompletion() {
        // 파일 업로드가 필수가 아닌 경우
        const isAnyFileUploaded = existingFilesContainer.querySelectorAll('.existing-file:not([style*="display: none"])').length > 0;

        const isInputValid = inputElement && inputElement.value.trim() !== "";
        const isTextareaValid = textareaElement && textareaElement.value.trim() !== "";
        const isGenreSelected = Array.from(form.querySelectorAll('input[name="genreType"]')).some(radio => radio.checked);
        const isFundingStatusSelected = Array.from(form.querySelectorAll('input[name="fundingStatus"]')).some(radio => radio.checked);

        // 기존 상품 중 삭제되지 않은 상품 수
        const existingProductCount = existingProductsContainer.querySelectorAll('.existing-product:not([style*="display: none"])').length;

        // 새로운 추가된 상품 수
        const newProductCount = newProductsContainer.querySelectorAll('.new-product').length;

        // 전체 상품 수 (기존에서 삭제되지 않은 상품 + 새로운 상품)
        const totalProductCount = existingProductCount + newProductCount;

        // 최소 한 개의 상품이 존재해야 함
        const isProductValid = totalProductCount > 0;

        if (
            // isAnyFileUploaded && // 파일 업로드가 필수가 아닌 경우 주석 처리
            isInputValid &&
            isTextareaValid &&
            isGenreSelected &&
            isFundingStatusSelected &&
            isProductValid
        ) {
            submitButton.disabled = false;
            submitButton.classList.add("active");

        } else {
            submitButton.disabled = true;
            submitButton.classList.remove("active");

        }
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
            checkFormCompletion();
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
            checkFormCompletion();
        });

        textarea.addEventListener("input", function () {
            if (border.classList.contains("error")) {
                border.classList.remove("error");
                border.style.borderColor = "#00a878";

            }
            checkFormCompletion();
        });

        textarea.addEventListener("mouseover", function () {
            if (border.classList.contains("error")) {
                border.style.borderColor = "#e52929";

            }
        });
    }

    // 이미지 박스 이벤트 리스너 설정 함수
    function setupEventListeners(imgBox) {
        const btnDeleteImage = imgBox.querySelector(".delete-existing-file-btn");
        const btnChangeImage = imgBox.querySelector(".change-image-btn");
        const fileUpload = imgBox.querySelector('input[type="file"]');
        const preview = imgBox.querySelector("img");
        const videoPreview = imgBox.querySelector("video");
        const imgBoxDiv = imgBox.querySelector(".img-box"); // For general clicks

        if (btnDeleteImage) {
            btnDeleteImage.addEventListener("click", function () {
                const fileId = this.getAttribute("data-file-id");
                const imgElement = imgBox.querySelector("img");
                const fileName = imgElement ? imgElement.getAttribute("alt") : null;

                //삭제 버튼 클릭 시 fileId와 fileName 확인
                console.log("삭제 버튼 클릭, fileId:", fileId, "fileName:", fileName);

                // 기존 파일일 경우 삭제할 ID에 추가 (중복 방지)
                if (fileId && !deletedFileIds.includes(fileId)) {
                    deletedFileIds.push(fileId);

                    // 삭제할 파일 ID를 숨겨진 필드에 추가
                    document.getElementById("deletedFileIds").value = deletedFileIds.join(",");
                    console.log("삭제할 파일 ID 추가:", fileId);
                } else {
                    console.log("이미 삭제된 파일 ID:", fileId);
                }

                imgBox.classList.add("hidden"); // 'hidden' 클래스 추가


                // 폼 유효성 검사
                checkFormCompletion();
            });
        }

        if (btnChangeImage && fileUpload) {
            btnChangeImage.addEventListener("click", function () {
                fileUpload.click();
            });
        }

        if (imgBoxDiv && fileUpload) {
            imgBoxDiv.addEventListener("click", function () {
                fileUpload.click();
            });
        }

        if (fileUpload) {
            fileUpload.addEventListener("change", function () {
                const file = fileUpload.files[0];
                if (file) {
                    console.log("파일 업로드 선택:", file.name);
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        if (file.type.startsWith("image/")) {
                            preview.src = e.target.result;
                            preview.style.display = "block";
                            videoPreview.style.display = "none";
                            console.log("이미지 미리보기 업데이트");
                        } else if (file.type.startsWith("video/")) {
                            videoPreview.src = e.target.result;
                            videoPreview.style.display = "block";
                            preview.style.display = "none";
                            console.log("비디오 미리보기 업데이트");
                        }
                    };
                    reader.readAsDataURL(file);

                    // 서버로 파일 업로드
                    const formData = new FormData();
                    formData.append('file', file);

                    fetch('/video/funding/upload', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.text())
                        .then(fileName => {
                            if (fileName !== "error") {
                                console.log('파일이 성공적으로 업로드되었습니다:', fileName);

                                // 숨겨진 입력 필드 생성
                                const hiddenInput = document.createElement('input');
                                hiddenInput.type = 'hidden';
                                hiddenInput.name = 'fileNames';
                                hiddenInput.value = fileName;
                                // 고유한 ID 설정 (필요 시)
                                hiddenInput.id = `file-${fileName}`;
                                fileUpload.parentNode.appendChild(hiddenInput);

                                // 파일명을 input 요소에 저장 (삭제 시 사용)
                                fileUpload.setAttribute('data-file-name', fileName);

                                // 파일 업로드 후 특정 div 요소 제거 및 img-edit-box 표시
                                ["img-box-title", "img-box-text", "img-box-help"].forEach(selector => {
                                    const element = imgBox.querySelector(`.${selector}`);
                                    if (element) {
                                        element.remove();
                                        console.log(`'${selector}' 요소 제거`);
                                    }
                                });

                                // img-edit-box 표시
                                const imgEditBox = imgBox.querySelector(".img-edit-box");
                                if (imgEditBox) {
                                    imgEditBox.style.display = "block";
                                }
                            } else {
                                console.error('파일 업로드 실패.');
                            }
                        })
                        .catch(error => {
                            console.error('파일 업로드 중 에러 발생:', error);
                        });
                }
            });
        }
    }

    // 상품 삭제 버튼 이벤트 리스너 설정 함수
    function setupProductDeleteListener(productElement) {
        const deleteProductButton = productElement.querySelector(".delete-existing-product-btn");
        if (deleteProductButton) {
            deleteProductButton.addEventListener("click", function () {
                const productId = this.getAttribute("data-product-id");
                console.log("상품 삭제 버튼 클릭, productId:", productId);

                if (productId) {
                    if (!fundingProductIds.includes(productId)) {
                        fundingProductIds.push(productId);

                        // fundingProductIds 배열을 콤마로 구분된 문자열로 변환하여 hidden 필드에 설정
                        document.getElementById("fundingProductIds").value = fundingProductIds.join(",");
                        console.log("fundingProductIds 업데이트:", fundingProductIds.join(","));
                    } else {
                        console.log("이미 삭제된 상품 ID:", productId);
                    }
                } else {
                    console.log("신규 상품 삭제, productId 없음");
                }

                // UI에서 해당 상품 요소 숨기기 (display: none 설정)
                productElement.style.display = "none";

                // 폼 유효성 검사
                checkFormCompletion();
            });
        }
    }

    // 기존 펀딩 상품에 이벤트 리스너 설정
    document.querySelectorAll(".existing-product").forEach(setupProductDeleteListener);

    // 새로운 이미지 박스 추가 함수
    function addNewImageBox() {
        const timestamp = Date.now();
        const newImgBox = document.createElement("div");
        newImgBox.classList.add("img-box-list", "new-file"); // 'new-file' 클래스 추가 (선택 사항)
        newImgBox.innerHTML = `
            <div class="img-content-box">
                <div class="img-edit-box" style="display: none">
                    <div class="btn-edit-item change-image-btn">이미지 변경</div>
                    <div class="btn-edit-item delete-existing-file-btn">이미지 삭제</div>
                </div>
                <div class="center-text img-box">
                    <div class="default-img">
                        <!-- 이미지 미리보기 -->
                        <img
                            src="https://www.wishket.com/static/renewal/img/partner/profile/icon_btn_add_portfolio_image.png"
                            class="img-tag"
                            alt="new-file-${timestamp}"
                        />
                        <video class="video-tag" style="display: none;" controls></video>
                        <div class="img-box-title">작품 영상, 이미지 등록</div>
                        <div class="img-box-text">작품 결과물 혹은 설명을 돕는 이미지를 선택해 주세요.</div>
                        <div class="img-box-help"><span>· 이미지 최적 사이즈: 가로 720px</span></div>
                        <!-- 숨겨진 파일 첨부 기능 -->
                        <input type="file" name="newFiles" accept="image/*,video/*" style="display: none;" />
                    </div>
                </div>
            </div>
        `;
        imgBoxContainer.appendChild(newImgBox);
        setupEventListeners(newImgBox); // 이벤트 리스너 설정
        checkFormCompletion();
    }

    // 새로운 상품 추가 함수
    function addNewProductBox() {
        const newProduct = document.createElement("div");
        newProduct.classList.add("new-product");
        newProduct.innerHTML = `
            <div class="add-box">
                <!-- 상품명 -->
                <div class="product-name">상품명 <span class="star">*</span></div>
                <input type="text" class="product-name-input"
                       name="fundingProducts[${newProductIndex}].productName"
                       placeholder="등록할 상품명을 입력해주세요." required />
                
                <!-- 상품가격 -->
                <div class="product-price">상품가격 <span class="star">*</span></div>
                <input type="number" class="product-price-input"
                       name="fundingProducts[${newProductIndex}].productPrice"
                       placeholder="등록할 상품가격을 입력해주세요." required />
                
                <!-- 상품수량 -->
                <div class="product-quantity">상품수량 <span class="star">*</span></div>
                <input type="number" class="product-quantity-input"
                       name="fundingProducts[${newProductIndex}].productAmount"
                       placeholder="등록할 상품수량을 입력해주세요." required />
                
                <!-- 삭제 버튼 -->
                <div class="product-cancel">
                    <button class="delete-existing-product-btn" type="button">삭제</button>
                </div>
            </div>
        `;
        newProductsContainer.appendChild(newProduct);
        setupProductDeleteListener(newProduct); // 삭제 버튼 이벤트 리스너 설정
        newProductIndex++; // 인덱스 증가
        checkFormCompletion();
    }

    // 새로운 상품 추가 버튼 클릭 시 상품 추가
    productAddButton.addEventListener("click", () => {
        addNewProductBox();
    });

    // 인풋 및 텍스트에어리어 이벤트 리스너 설정
    setupInputEffects(inputElement, labelInputPartner);
    setupTextareaEffects(textareaElement, textareaBorder);

    // 기존 파일 삭제 버튼에 이벤트 리스너 설정 (중복 제거)
    document.querySelectorAll(".img-box-list").forEach(setupEventListeners);

    // 새로운 이미지 박스 추가 버튼 클릭 시 이미지 박스 추가
    addImageButton.addEventListener("click", () => {
        addNewImageBox();
    });

    // 초기 폼 유효성 검사
    checkFormCompletion();
});
