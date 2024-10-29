// 공지사항 저장 함수
    function saveAnnouncement() {
    const title = document.getElementById('edit-post-title').value;
    const content = document.getElementById('edit-post-content').value;

    // 입력 검증
    if (!title || !content) {
    alert("제목과 내용을 입력해 주세요.");
    return;
}

    const announcementData = {
    announcementTitle: title,
    announcementContent: content
};

    fetch('/admin/announcement', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(announcementData)
})
    .then(response => {
    if (response.ok) {
    alert("공지사항이 등록되었습니다.");
    // 모달 닫기 및 입력 필드 초기화
    document.querySelector('.close-button').click();
    document.getElementById('edit-post-title').value = '';
    document.getElementById('edit-post-content').value = '';
} else {
    alert("공지사항 등록에 실패했습니다.");
}
})
    .catch(error => {
    console.error("Error:", error);
    alert("오류가 발생했습니다.");
});
}

    // 모달 열기
    document.getElementById("openModal").onclick = function() {
    document.querySelector('.notice-full-modal').style.display = 'block';
};

document.addEventListener("DOMContentLoaded", function () {
    const selectAllCheckbox = document.querySelector(".select-all");
    const checkboxes = document.querySelectorAll(".apply-checkbox");

    // 전체 선택 체크박스 기능
    selectAllCheckbox.addEventListener("change", function () {
        checkboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });
});


document.addEventListener("DOMContentLoaded", function () {
    function loadAnnouncements(page) {
        fetch(`/admin/announcements/list?page=${page}`)
            .then(response => response.text())
            .then(html => {
                document.querySelector(".announcement-container").innerHTML = html;
            })
            .catch(error => console.error("Error loading announcements:", error));
    }

    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("pagination-page-link") ||
            e.target.classList.contains("pagination-prev-link") ||
            e.target.classList.contains("pagination-next-link")) {
            e.preventDefault(); // 링크 기본 동작 방지
            const page = e.target.getAttribute("data-page"); // data-page 속성에서 페이지 번호 가져옴
            if (page) {
                loadAnnouncements(page);
            }
        }
    });
});


