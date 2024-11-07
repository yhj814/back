document.getElementById("file_select_btn").addEventListener("click", function () {
    const fileInput = document.querySelector(".temp-upload-resume");
    fileInput.click();

    fileInput.addEventListener("change", function () {
        const selectedFile = fileInput.files[0];
        if (selectedFile) {
            const fileName = selectedFile.name;

            // 파일명을 p 태그에 넣기
            const fileNameViewer = document.getElementById("selected_file_name_viewer");
            fileNameViewer.textContent = fileName;

            // 파일명이 너무 길 경우 잘라서 표시 (예: 15자 이상일 때)
            if (fileName.length > 15) {
                fileNameViewer.textContent = fileName.substring(0, 12) + "...";
            }

            // tooltip-group을 block으로 변경
            const tooltipGroup = document.querySelector(".tooltip-group");
            tooltipGroup.style.display = "block";

            // placeholder-attachment를 none으로 변경
            const placeholderAttachment = document.querySelector(".placeholder-attachment");
            placeholderAttachment.style.display = "none";

            // 서버에 파일 업로드
            const formData = new FormData();
            formData.append("file", selectedFile);

            fetch("/audition/video/upload", {
                method: "POST",
                body: formData,
            })
                .then((response) => response.json())
                .then((data) => {
                    if (data.fileName && data.filePath) {
                        console.log("파일이 성공적으로 업로드되었습니다.");

                        // 업로드된 파일명을 a 태그의 download 속성에 넣기
                        const resumeDownloadLink = document.getElementById("resume_download");
                        resumeDownloadLink.setAttribute("download", data.fileName);
                        resumeDownloadLink.href = data.filePath; // 다운로드 링크 경로 설정
                    } else {
                        console.error("파일 업로드 중 오류가 발생했습니다.");
                        alert("파일 업로드에 실패했습니다. 다시 시도해주세요.");
                    }
                })
                .catch((error) => {
                    console.error("Error:", error);
                    alert("파일 업로드 중 문제가 발생했습니다.");
                });
        }
    });
});

// 삭제 버튼을 클릭했을 때 동작하는 함수
document.getElementById("btn_delete_resume").addEventListener("click", function () {
    // 첨부 파일 삭제 (input 값 초기화)
    const fileInput = document.querySelector(".temp-upload-resume");
    fileInput.value = ""; // 파일 선택 초기화

    // tooltip-group을 none으로 변경
    const tooltipGroup = document.querySelector(".tooltip-group");
    tooltipGroup.style.display = "none";

    // placeholder-attachment를 block으로 변경
    const placeholderAttachment = document.querySelector(".placeholder-attachment");
    placeholderAttachment.style.display = "block";

    // 파일명 초기화
    const fileNameViewer = document.getElementById("selected_file_name_viewer");
    fileNameViewer.textContent = "";

    // 다운로드 링크 초기화
    const resumeDownloadLink = document.getElementById("resume_download");
    resumeDownloadLink.removeAttribute("download");
    resumeDownloadLink.removeAttribute("href"); // 다운로드 링크 경로 초기화
});
