// 공지사항 작성과 동시에 목록에 뿌리기
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

    // 목록조회
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
                // 공지사항 작성 후 목록 갱신
                loadAnnouncements(1);

                // 모달 닫기 및 입력 필드 초기화
                closeModal();
            } else {
                alert("공지사항 등록에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("오류가 발생했습니다.");
        });
}

// 공지사항 수정
function updateAnnouncement(id, newTitle, newContent) {
    fetch('/admin/announcement/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: id,
            announcementTitle: newTitle,
            announcementContent: newContent,
        }),
    })
        .then(response => response.text())
        .then(data => {
            alert('공지사항이 성공적으로 수정되었습니다.');
            loadAnnouncements(1); // 수정 후 목록 갱신
        })
        .catch(error => console.error('Error updating announcement:', error))
        .finally(() => {
            // 작성 모달창 닫기
            const noticeModal = document.querySelector('.notice-full-modal.edit');
            noticeModal.style.display = 'none';
        });
}

// 공지사항 삭제
function deleteAnnouncements() {
    const selectedIds = Array.from(document.querySelectorAll('#announcement-id .apply-checkbox:checked'))
        .map(checkbox => checkbox.getAttribute('data-id'));

    if (selectedIds.length === 0) {
        alert('삭제할 공지사항을 선택하세요.');
        return;
    }

    fetch('/admin/announcement/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(selectedIds),
    })
        .then(response => response.text())
        .then(data => {
            alert('선택한 공지사항이 성공적으로 삭제되었습니다.');
            loadAnnouncements(1); // 삭제 후 목록 갱신
        })
        .catch(error => console.error('Error deleting announcements:', error));
}

// 검색 수행 함수
function search() {
    const searchKeyword = document.getElementById('announcement-list-search').value.trim();
    loadAnnouncements(1, searchKeyword, 'createdDate'); // 검색어와 함께 첫 페이지 로드 (기본 정렬: 등록일 순)
}

// 8글자 초과 시 8자리 뒤에 '...' 처리
function truncateText(selector, maxLength = 8) {
    const elements = document.querySelectorAll(selector);
    elements.forEach(element => {
        const text = element.innerText;
        if (text.length > maxLength) {
            element.innerText = text.slice(0, maxLength) + '...';
        }
    });
}

//------------------------------------------------------------------------------------------------------------------
//문의사항

const inquiryService = {
    async fetchInquiries(page = 1) {
        const response = await fetch(`/admin/inquiries?page=${page}`);
        if (!response.ok) throw new Error("Failed to fetch inquiries");
        return await response.json();
    }
};