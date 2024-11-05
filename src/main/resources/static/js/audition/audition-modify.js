document.addEventListener("DOMContentLoaded", function () {
    const writeForm = document.getElementById("write-form");
    const deletedFileIds = [];

    // 첫 번째 work-img-box (기존 파일들에 대한 작업)
    const existingWorkImgBox = document.querySelector(".work-img-box.existing-work-img-box");
    if (existingWorkImgBox) {
        const existingFileButtons = existingWorkImgBox.querySelectorAll(".btn-edit-item[data-file-id]");
        existingFileButtons.forEach(button => {
            button.addEventListener("click", function () {
                const fileId = this.getAttribute("data-file-id");
                deletedFileIds.push(fileId);
                console.log("삭제할 파일 ID 추가:", fileId);
                this.closest(".img-box-list").style.display = "none"; // UI에서 삭제 표시
            });
        });
    }

    // 두 번째 work-img-box (새 파일 추가 작업)
    const newWorkImgBox = document.querySelector(".work-img-box.new-work-img-box");
    if (newWorkImgBox) {
        const addImageButton = newWorkImgBox.querySelector(".img-add");
        addImageButton.addEventListener("click", function () {
            const newFileContainer = document.getElementById("img-box-container");
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
                    </div>
                </div>
            `;

            newFileContainer.append(newImgBox);
            setupEventListeners(newImgBox); // 새로 추가된 img-box-list에도 이벤트 리스너 설정
        });
    }

    // 폼 제출 시 삭제할 파일 정보 포함
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
    });

    // 모든 input 요소에 대한 blur 이벤트 추가
    const inputElements = document.querySelectorAll("input");
    inputElements.forEach((input) => {
        input.addEventListener("blur", function () {
            if (input.value.trim() === "") {
                input.classList.add("error");
                input.style.border = "solid 1px #e52929";
                input.style.position = "relative";
                input.style.zIndex = "2";
            }
        });

        input.addEventListener("input", function () {
            if (
                input.classList.contains("error") &&
                input.value.trim() !== ""
            ) {
                input.classList.remove("error");
                input.style.border = ""; // 원래 상태로 복구
                input.style.position = "";
                input.style.zIndex = "";
            }
        });
    });

    // 특정 input 필드에 페이지 로드 시 label-effect 추가/제거
    const targetInputNames = ["postTitle", "auditionCareer", "expectedAmount"];
    targetInputNames.forEach((name) => {
        const inputElement = document.querySelector(`input[name="${name}"]`);
        if (inputElement) {
            const parentDiv = inputElement.closest("div");
            if (parentDiv) {
                // 페이지 로드 시 값이 있을 경우 label-effect 클래스 추가
                if (inputElement.value.trim() !== "") {
                    parentDiv.classList.add("label-effect");
                }

                // focus 및 blur 이벤트 추가
                inputElement.addEventListener("focus", function () {
                    parentDiv.classList.add("label-effect");
                });

                inputElement.addEventListener("blur", function () {
                    if (inputElement.value.trim() === "") {
                        parentDiv.classList.remove("label-effect");
                    }
                });
            }
        }
    });

    // 날짜 입력 필드와 모집인원 입력 필드에 대한 처리
    const dateInputs = document.querySelectorAll('input[name="serviceStartDate"],input[name="auditionDeadLine"]');
    dateInputs.forEach(formatDateInput);
    const participantInput = document.querySelector('input[name="auditionPersonnel"]');
    if (participantInput) {
        formatParticipantInput(participantInput);
    }

    // textarea__border에 속한 textarea가 포커스될 때 active 클래스 추가 및 다른 곳 클릭 시 active 클래스 삭제
    const textareas = document.querySelectorAll(".textarea__border textarea");
    textareas.forEach((textarea) => {
        const parentDiv = textarea.closest(".textarea__border");
        if (parentDiv) {
            textarea.addEventListener("focus", function () {
                parentDiv.classList.add("active");
            });
        }
    });

    document.addEventListener("click", function (event) {
        textareas.forEach((textarea) => {
            const parentDiv = textarea.closest(".textarea__border");
            if (parentDiv && !parentDiv.contains(event.target) && textarea !== document.activeElement) {
                parentDiv.classList.remove("active");
            }
        });
    });

    // 날짜 입력 필드에 정확히 입력되지 않은 경우 error 클래스 추가 및 붉은 테두리 설정
    function formatDateInput(input) {
        input.addEventListener("input", function () {
            let value = input.value.replace(/[^0-9]/g, "");
            let formattedValue = "";
            if (value.length <= 4) {
                formattedValue = value;
            } else if (value.length > 4 && value.length <= 6) {
                let year = value.slice(0, 4);
                let month = value.slice(4, 6);
                if (parseInt(month) >= 0 && parseInt(month) <= 12) {
                    formattedValue = year + "." + month;
                } else {
                    formattedValue = year;
                }
            } else if (value.length > 6 && value.length <= 8) {
                let year = value.slice(0, 4);
                let month = value.slice(4, 6);
                let day = value.slice(6, 8);
                if (parseInt(day) >= 1 && parseInt(day) <= 31) {
                    formattedValue = year + "." + month + "." + day;
                } else {
                    formattedValue = year + "." + month;
                }
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
    function formatParticipantInput(input) {
        input.addEventListener("input", function () {
            let value = input.value.replace(/[^0-9]/g, "");
            if (value) {
                input.value = value + "명";
            }
        });

        input.addEventListener("keydown", function (e) {
            if (input.value.endsWith("명") && e.key === "Backspace") {
                input.value = input.value.slice(0, -1);
            }
        });
    }

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
});
