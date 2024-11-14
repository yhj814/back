document.getElementById("file_select_btn").addEventListener("click", function () {
    const fileInput = document.querySelector(".temp-upload-resume");
    fileInput.click();

    // fileInput에 대한 change 이벤트 리스너가 중복 추가되지 않도록 기존 리스너 제거 후 추가
    fileInput.removeEventListener("change", handleFileUpload);
    fileInput.addEventListener("change", handleFileUpload);
});

function handleFileUpload(event) {
    const fileInput = event.target;
    const selectedFile = fileInput.files[0];

    if (selectedFile) {
        const fileName = selectedFile.name;

        // 파일명을 표시
        const fileNameViewer = document.getElementById("selected_file_name_viewer");
        fileNameViewer.textContent = fileName.length > 15 ? fileName.substring(0, 12) + "..." : fileName;

        // tooltip-group 및 placeholder 조정
        document.querySelector(".tooltip-group").style.display = "block";
        document.querySelector(".placeholder-attachment").style.display = "none";

        // 파일을 서버로 전송
        const formData = new FormData();
        formData.append("file", selectedFile);

        fetch("/audition/video/upload-apply", {
            method: "POST",
            body: formData,
        })
            .then((response) => response.text())
            .then((fileName) => {
                if (fileName && fileName !== "error") {
                    console.log("파일이 성공적으로 업로드되었습니다.");

                    // 서버로부터 받은 파일명을 숨겨진 입력 필드에 저장
                    let hiddenInput = document.querySelector('input[name="fileNames"]');
                    if (!hiddenInput) {
                        hiddenInput = document.createElement('input');
                        hiddenInput.type = 'hidden';
                        hiddenInput.name = 'fileNames';
                        document.getElementById('base-edit-form').appendChild(hiddenInput);
                    }
                    hiddenInput.value = fileName;

                    // 파일 입력 필드에서 name 속성 제거
                    const fileInput = document.querySelector(".temp-upload-resume");
                    fileInput.removeAttribute('name');
                    // 파일명을 input 요소에 저장 (삭제 시 사용)
                    fileInput.setAttribute('data-file-name', fileName);
                } else {
                    console.error("파일 업로드 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => {
                console.error("Error:", error);
            });

    }
}

// 파일 삭제 버튼 클릭 시 초기화
document.getElementById("btn_delete_resume").addEventListener("click", function () {
    const fileInput = document.querySelector(".temp-upload-resume");
    fileInput.value = ""; // 파일 선택 초기화

    // tooltip-group 및 placeholder 조정
    document.querySelector(".tooltip-group").style.display = "none";
    document.querySelector(".placeholder-attachment").style.display = "block";

    // 파일명 및 다운로드 링크 초기화
    document.getElementById("selected_file_name_viewer").textContent = "";

    // 숨겨진 입력 필드 제거
    const hiddenInput = document.querySelector('input[name="uploadedFileName"]');
    if (hiddenInput) {
        hiddenInput.remove();
    }
});

// 폼 제출 시 파일 입력 필드 비활성화
document.getElementById('base-edit-form').addEventListener('submit', function (event) {
    const fileInputs = this.querySelectorAll('input[type="file"]');
    fileInputs.forEach(fileInput => {
        fileInput.disabled = true;
    });
});

