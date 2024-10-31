// 공지사항 작성 모달 열기
document.getElementById("openModal").onclick = function() {
    document.querySelector('.notice-full-modal').style.display = 'block';
};

// 공지사항 작성,수정 모달 닫기
function closeModal() {
    document.querySelector('.notice-full-modal').style.display = 'none';
    document.getElementById('edit-post-title').value = '';
    document.getElementById('edit-post-content').value = '';
}

// 공지사항 작성 ,수정 저장 버튼 리스너
function setSaveButtonListener() {
    const saveButton = document.querySelector('.save-button');
    saveButton.onclick = saveAnnouncement;
}


// 수정 버튼에 이벤트 리스너
function setEditButtonListeners() {
    const editButtons = document.querySelectorAll('#announcement-id .notice-edit');
    const noticeModal = document.querySelector('.notice-full-modal.edit');
    const titleInput = noticeModal.querySelector('.edit-post-title'); // 모달창 제목 필드
    const contentInput = noticeModal.querySelector('.edit-post-content'); // 모달창 내용 필드
    const saveButton = noticeModal.querySelector('.save-button'); // 저장 버튼

    editButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 버튼의 데이터 속성에서 제목과 내용을 가져옴
            const id = button.getAttribute('data-id');
            const title = button.getAttribute('data-title');
            const content = button.getAttribute('data-content');

            // 모달창 입력 필드에 제목과 내용 설정
            titleInput.value = title;
            contentInput.value = content;

            // 모달창 표시
            noticeModal.style.display = 'flex';

            // 저장 버튼 클릭 이벤트 등록
            saveButton.onclick = function () {
                // 수정된 제목과 내용을 업데이트 요청
                updateAnnouncement(id, titleInput.value, contentInput.value);
            };
        });
    });

    // 모달창 닫기 기능
    const closeButton = noticeModal.querySelector('.close-button');
    const backgroundOverlay = noticeModal.querySelector('.background-overlay');

    closeButton.addEventListener('click', () => {
        noticeModal.style.display = 'none';
    });
    backgroundOverlay.addEventListener('click', () => {
        noticeModal.style.display = 'none';
    });
}


setEditButtonListeners();