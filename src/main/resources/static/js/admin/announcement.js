// 페이지가 로드될 때 초기 데이터를 불러와서 표시
document.addEventListener('DOMContentLoaded', function () {
    loadAnnouncements(1,'createdDate'); // 첫 페이지를 로드하여 초기 화면 구성
    setDeleteButtonListener(); // 삭제 버튼 이벤트 리스너 등록
    setSelectAllListener(); // 전체 선택 체크박스 리스너 등록
    setSaveButtonListener(); // 공지사항 저장 버튼 리스너 등록
});


// 8글자 초과 시 '...' 처리 함수
function truncateText(selector, maxLength = 8) {
    const elements = document.querySelectorAll(selector);
    elements.forEach(element => {
        const text = element.innerText;
        if (text.length > maxLength) {
            element.innerText = text.slice(0, maxLength) + '...';
        }
    });
}

// 공지사항 목록 로드 함수
function loadAnnouncements(page,order= 'createdDate') {
    fetch(`/admin/announcements?page=${page}&order=${order}`)
        .then(response => response.json())
        .then(data => {
            const announcements = data.announcements;
            const pagination = data.pagination;

            const container = document.querySelector('#announcement-id #announcement-list');
            container.innerHTML = ''; // 기존 목록 초기화

            // 공지사항 목록을 HTML로 변환하여 추가
            announcements.forEach(announcement => {
                const row = `
                    <div class="apply-table-row">
                        <div class="apply-table-cell">
                            <input type="checkbox" class="apply-checkbox" data-id="${announcement.id}"/>
                        </div>
                        <div class="apply-table-cell">${announcement.id}</div>
                        <div class="apply-table-cell">${announcement.createdDate}</div>
                        <div class="apply-table-cell announcement-title">${announcement.announcementTitle}</div>
                        <div class="apply-table-cell announcement-content">${announcement.announcementContent}</div>
                        <div class="apply-table-cell">
                            <button class="edit-btn status notice-edit" data-id="${announcement.id}" 
                                    data-title="${announcement.announcementTitle}" 
                                    data-content="${announcement.announcementContent}">
                                수정
                            </button>
                        </div>
                        <div class="apply-table-cell">${announcement.updatedDate}</div>
                    </div>
                `;
                container.insertAdjacentHTML('beforeend', row); // 새 목록 추가
            });

            // 8글자 초과 시 제목과 내용을 '...' 처리
            truncateText('#announcement-id .announcement-title', 8);
            truncateText('#announcement-id .announcement-content', 8);

            // 페이지 버튼 업데이트
            updatePagination(pagination, page, order);

            // 수정 버튼 클릭 시 모달창 열기
            setEditButtonListeners();

            // 전체 선택 상태 초기화
            document.querySelector('#announcement-list-select-all').checked = false;

            // 개별 체크박스 리스너 등록
            setIndividualCheckboxListeners();
        })
        .catch(error => console.error('Error fetching announcements:', error));
}

// 정렬 필터 옵션 클릭 이벤트 등록
document.querySelector('#announcement-list-created-date').addEventListener('click', () => {
    loadAnnouncements(1, 'createdDate');
});
document.querySelector('#announcement-list-updated-date').addEventListener('click', () => {
    loadAnnouncements(1, 'updatedDate');
});

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
                loadAnnouncements(1); // 공지사항 작성 후 목록 갱신

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

// 모달 열기
document.getElementById("openModal").onclick = function() {
    document.querySelector('.notice-full-modal').style.display = 'block';
};

// 모달 닫기 함수
function closeModal() {
    document.querySelector('.notice-full-modal').style.display = 'none';
    document.getElementById('edit-post-title').value = '';
    document.getElementById('edit-post-content').value = '';
}

// 공지사항 저장 버튼 리스너 추가 함수
function setSaveButtonListener() {
    const saveButton = document.querySelector('.save-button');
    saveButton.onclick = saveAnnouncement;
}

// 전체 선택 체크박스 리스너 추가 함수
function setSelectAllListener() {
    const selectAllCheckbox = document.querySelector('#announcement-list-select-all');

    selectAllCheckbox.addEventListener('change', function () {
        const checkboxes = document.querySelectorAll('#announcement-id .apply-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });
}

// 개별 체크박스 리스너 등록 함수
function setIndividualCheckboxListeners() {
    const selectAllCheckbox = document.querySelector('#announcement-list-select-all');
    const checkboxes = document.querySelectorAll('#announcement-id .apply-checkbox');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            // 개별 체크박스가 하나라도 해제되면 전체 선택 해제
            if (!checkbox.checked) {
                selectAllCheckbox.checked = false;
            } else {
                // 모든 체크박스가 선택된 경우에만 전체 선택 체크박스 체크
                const allChecked = Array.from(checkboxes).every(cb => cb.checked);
                selectAllCheckbox.checked = allChecked;
            }
        });
    });
}

// 삭제 버튼에 이벤트 리스너 추가 함수
function setDeleteButtonListener() {
    const deleteButton = document.querySelector('#announcement-delete-btn');
    deleteButton.addEventListener('click', deleteAnnouncements);
}

// 공지사항 삭제 함수
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
// 수정 버튼에 이벤트 리스너 추가 함수
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

// 공지사항 수정 함수
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
            // 모달창 닫기
            const noticeModal = document.querySelector('.notice-full-modal.edit');
            noticeModal.style.display = 'none';
        });
}
// 페이지 버튼 업데이트 함수
function updatePagination(pagination, currentPage, order) {
    const paginationContainer = document.querySelector('#pagination-announcement .pagination-list');
    paginationContainer.innerHTML = ''; // 기존 페이지 버튼 초기화

    // 이전 페이지 버튼 (비활성화 조건 포함)
    paginationContainer.innerHTML += `
        <li class="pagination-prev ${currentPage === 1 ? 'disabled' : ''}">
            <a href="#" onclick="loadAnnouncements(${Math.max(currentPage - 1, 1)}, '${order}')">&lt;</a>
        </li>`;

    // 페이지 번호 버튼 생성
    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        paginationContainer.innerHTML += `
            <li class="pagination-page ${i === currentPage ? 'active' : ''}">
                <a href="#" onclick="loadAnnouncements(${i}, '${order}')">${i}</a>
            </li>`;
    }

    // 다음 페이지 버튼 (비활성화 조건 포함)
    paginationContainer.innerHTML += `
        <li class="pagination-next ${currentPage === pagination.realEnd ? 'disabled' : ''}">
            <a href="#" onclick="loadAnnouncements(${Math.min(currentPage + 1, pagination.realEnd)}, '${order}')">&gt;</a>
        </li>`;
}







