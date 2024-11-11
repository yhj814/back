// JavaScript 파일 업로드 관련 코드 수정
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

        fetch("/audition/video/upload-apply", { // 경로 수정
            method: "POST",
            body: formData,
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.fileName && data.filePath) {
                    console.log("파일이 성공적으로 업로드되었습니다.");
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
    const resumeDownloadLink = document.getElementById("resume_download");
    resumeDownloadLink.removeAttribute("download");
    resumeDownloadLink.removeAttribute("href");
});
