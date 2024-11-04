// 페이지가 로드될 때 초기 데이터를 불러와서 표시
document.addEventListener('DOMContentLoaded', function () {
    loadAnnouncements(1,'','createdDate'); // 첫 페이지를 로드하여 초기 화면 구성
    setDeleteButtonListener(); // 삭제 버튼 이벤트 리스너
    setSelectAllListener(); // 전체 선택 체크박스 리스너
    setSaveButtonListener(); // 공지사항 저장 버튼 리스너

    // 검색 필드에서 Enter 키 이벤트 리스너
    const searchInput = document.getElementById('announcement-list-search');
    searchInput.addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // Enter 기본 동작 방지
            search(); // 검색 수행 함수 호출
        }
    });
});

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

// 삭제 버튼에 이벤트 리스너
function setDeleteButtonListener() {
    const deleteButton = document.querySelector('#announcement-delete-btn');
    deleteButton.addEventListener('click', deleteAnnouncements);
}

// 전체 선택 체크박스 리스너
function setSelectAllListener() {
    const selectAllCheckbox = document.querySelector('#announcement-list-select-all');

    selectAllCheckbox.addEventListener('change', function () {
        const checkboxes = document.querySelectorAll('#announcement-id .apply-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });
}

// 개별 체크박스 리스너
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

// 정렬 필터 옵션 클릭 이벤트
// 작성일 순
document.querySelector('#announcement-list-created-date').addEventListener('click', () => {
    loadAnnouncements(1, '','createdDate');
});

// 수정 날짜
document.querySelector('#announcement-list-updated-date').addEventListener('click', () => {
    loadAnnouncements(1,'', 'updatedDate');
});

//------------------------------------------------------------------------------------------------------------------
//문의사항
const inquiryEvent = {
    // 정렬 기준을 상수로 정의하여 HTML의 필터 ID와 매칭
    ORDER_INQUIRY_CREATED_DATE: "inquiry-created-date",
    ORDER_ANSWER_CREATED_DATE: "inquiry-answer-created-date",
    ORDER_NO_ANSWER: "no-answer-filter",

    async loadInquiries(page = 1, order = this.ORDER_INQUIRY_CREATED_DATE, searchQuery = "") {
        try {
            const data = await inquiryService.fetchInquiries(page, order, searchQuery);
            inquiryLayout.renderInquiries(data.inquiries, order);
            inquiryLayout.renderPagination(data.pagination, page, order, searchQuery);
        } catch (error) {
            console.error("Error loading inquiries:", error);
            alert("문의사항을 로드하는 중 오류가 발생했습니다.");
        }
    },

    setupEventListeners() {
        // 페이지네이션 클릭 이벤트 설정
        document.getElementById("pagination-inquiry").addEventListener("click", (event) => {
            event.preventDefault();
            const pageLink = event.target.closest("a[data-page]");
            if (pageLink) {
                const page = parseInt(pageLink.getAttribute("data-page"));
                const order = document.querySelector(".sort-filter-option.selected")?.id || this.ORDER_INQUIRY_CREATED_DATE;
                const searchQuery = document.getElementById("inquiry-search")?.value.trim() || "";
                this.loadInquiries(page, order, searchQuery);
            }
        });

        // 필터 옵션 클릭 이벤트 설정
        document.querySelectorAll("#inquiry-filter .sort-filter-option").forEach(option => {
            option.addEventListener("click", (event) => {
                document.querySelectorAll("#inquiry-filter .sort-filter-option").forEach(el => el.classList.remove("selected"));
                event.target.classList.add("selected");
                const order = event.target.id;
                const searchQuery = document.getElementById("inquiry-search")?.value.trim() || "";
                inquiryEvent.loadInquiries(1, order, searchQuery); // 첫 페이지부터 필터 적용
            });
        });


        // 검색창에서 엔터 키 입력 시 검색 이벤트
        document.getElementById("inquiry-search").addEventListener("keyup", (event) => {
            if (event.key === "Enter") {
                const searchQuery = event.target.value.trim();
                const order = document.querySelector(".sort-filter-option.selected")?.id || inquiryEvent.ORDER_INQUIRY_CREATED_DATE;
                inquiryEvent.loadInquiries(1, order, searchQuery); // 첫 페이지부터 검색 결과 로드
            }
        });

        // 삭제 버튼 클릭 이벤트 설정
        document.getElementById("inquiry-delete-btn").addEventListener("click", async () => {
            // 선택된 체크박스의 ID 수집
            const selectedIds = Array.from(document.querySelectorAll('#inquiry-list .apply-checkbox:checked'))
                .map(checkbox => parseInt(checkbox.getAttribute('data-id')));

            if (selectedIds.length === 0) {
                alert("삭제할 문의사항을 선택하세요.");
                return;
            }

            // 삭제 요청
            try {
                const resultMessage = await inquiryService.deleteSelectedInquiries(selectedIds);
                alert(resultMessage);  // 서버의 응답 메시지 출력

                // 목록 갱신
                inquiryEvent.loadInquiries(); // this.loadInquiries() 대신 inquiryEvent 사용
            } catch (error) {
                console.error("문의사항 삭제 오류:", error);
                alert("문의사항 삭제 중 오류가 발생했습니다. 다시 시도해 주세요.");
            }
        });
    }
};


// 초기 로드 및 이벤트 설정
document.addEventListener("DOMContentLoaded", () => {
    inquiryEvent.loadInquiries(); // 첫 페이지 로드
    inquiryEvent.setupEventListeners();
    inquirySelectAllListener();
    noAnswerModalEvents();
    inquiryCheckboxListeners();
});

// 전체 선택 체크박스 리스너
function inquirySelectAllListener() {
    const selectAllCheckbox = document.querySelector('#inquiry-list-select-all');
    selectAllCheckbox.addEventListener('change', function () {
        const checkboxes = document.querySelectorAll('#inquiry-id .apply-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });
}

// 개별 체크박스 리스너 설정
function inquiryCheckboxListeners() {
    const selectAllCheckbox = document.querySelector('#inquiry-list-select-all');
    const checkboxes = document.querySelectorAll('#inquiry-id .apply-checkbox');

    checkboxes.forEach(checkbox => {
        checkbox.removeEventListener('change', updateSelectAllCheckbox); // 기존 리스너 제거
        checkbox.addEventListener('change', updateSelectAllCheckbox);
    });
}

// 체크박스 상태 업데이트 함수
function updateSelectAllCheckbox() {
    const selectAllCheckbox = document.querySelector('#inquiry-list-select-all');
    const checkboxes = document.querySelectorAll('#inquiry-id .apply-checkbox');
    selectAllCheckbox.checked = Array.from(checkboxes).every(cb => cb.checked);
}

// 미답변일 때 답변 가능한 모달창 열기
function noAnswerModalEvents() {
    const answerBtns = document.querySelectorAll('#inquiry-list .answered-btn');
    const noAnswerInquiryModal = document.getElementById('inquiry-modal');
    const answerForm = document.getElementById('answer-form');
    const modalComment = document.getElementById('modal-comment');

    answerBtns.forEach(answerBtn => {
        answerBtn.removeEventListener('click', handleAnswerButtonClick); // 기존 리스너 제거
        answerBtn.addEventListener('click', handleAnswerButtonClick);
    });

    // 답변 작성
    function handleAnswerButtonClick() {
        const inquiryRow = this.closest('.apply-table-row');
        const inquiryId = this.getAttribute('data-inquiry-id');
        const postTitle = inquiryRow.querySelector('.inquiry-title').getAttribute('data-original-title');
        const postContent = inquiryRow.querySelector('.inquiry-content').getAttribute('data-original-content');
        const isCompleted = this.classList.contains('completed');
        const answerContent = isCompleted ? inquiryRow.querySelector('.answer-content').textContent : '';

        document.getElementById('modal-post-title').value = postTitle;
        document.getElementById('modal-post-content').value = postContent;
        modalComment.value = answerContent.trimStart();
        modalComment.readOnly = isCompleted;
        document.getElementById('save-answer').style.display = isCompleted ? 'none' : 'block';

        noAnswerInquiryModal.style.display = 'block';

        if (!isCompleted) {
            answerForm.onsubmit = async function (e) {
                e.preventDefault();
                const newAnswerContent = modalComment.value.trim();

                if (newAnswerContent) {
                    try {
                        const response = await inquiryService.postAnswer(inquiryId, newAnswerContent);
                        inquiryLayout.updateInquiryStatus(inquiryId, {
                            answerContent: newAnswerContent,
                            answerDate: response.createdDate
                        });
                        modalComment.value = '';
                        noAnswerInquiryModal.style.display = 'none';
                    } catch (error) {
                        console.error("답변 등록 중 오류 발생:", error);
                        alert("답변 등록 중 오류가 발생했습니다.");
                    }
                    alert("답변 완료. ");
                } else {
                    alert("답변 내용을 입력해주세요.");
                }
            };
        } else {
            answerForm.onsubmit = null;
        }
    }

    document.getElementById('close-modal').addEventListener('click', closeModal);
    document.getElementById('modal-overlay').addEventListener('click', closeModal);

    // 모달 닫기
    function closeModal() {
        noAnswerInquiryModal.style.display = 'none';
        modalComment.value = '';
    }
}

//----------------------------------------------------------------------------------------------------------------------
// 회원관리

// 페이지네이션 클릭 이벤트
function setupPaginationEvent() {
    document.getElementById('pagination-members').addEventListener('click', async (event) => {
        event.preventDefault();

        const target = event.target.closest('li');
        if (!target) return;

        const currentPage = parseInt(document.querySelector('.pagination-page.active a')?.textContent || '1');
        const order = document.querySelector('.sort-filter-option.selected')?.dataset.order || 'createdDate';
        const search = document.querySelector('.filter-search-input')?.value || '';

        if (target.classList.contains('pagination-prev') && currentPage > 1) {
            await renderMembers(currentPage - 1, search, order);
        } else if (target.classList.contains('pagination-next') && currentPage < pagination.realEnd) {
            await renderMembers(currentPage + 1, search, order);
        } else if (target.classList.contains('pagination-page')) {
            const page = parseInt(target.textContent);
            if (!isNaN(page) && page !== currentPage) {
                await renderMembers(page, search, order);
            }
        }
    });
}

// 필터링 이벤트
function setupFilterEvent() {
    const filterOptions = document.querySelectorAll(".sort-filter-option");

    filterOptions.forEach(option => {
        option.addEventListener("click", async () => {
            filterOptions.forEach(opt => opt.classList.remove("selected"));
            option.classList.add("selected");

            let order = '';
            switch (option.textContent.trim()) {
                case '가입일 순':
                    order = 'createdDate';
                    break;
                case '활동 중':
                    order = 'statusYES';
                    break;
                case '탈퇴':
                    order = 'statusNO';
                    break;
            }

            await renderMembers(1, '', order);
        });
    });
}

// 검색 기능 설정
function setupSearchEvent() {
    const searchInput = document.querySelector('.filter-search-input');
    searchInput.addEventListener('keydown', async (event) => {
        // 검색 키워드 입력후 엔터키 칠때
        if (event.key === 'Enter') {
            const search = searchInput.value.trim();
            const order = document.querySelector('.sort-filter-option.selected')?.dataset.order || 'createdDate';
            await renderMembers(1, search, order);
        }
    });
}

// 모달 관련 요소 설정
const modal = document.getElementById('member-status-change-modal');
const overlay = modal.querySelector('.background-overlay');
const activeButton = modal.querySelector('.btn-active-member');
const terminatedButton = modal.querySelector('.btn-terminated-member');
const completeButton = modal.querySelector('.btn-complete');

let currentMemberId = null;
let newStatus = null;

// 모달 열기 함수
function openModal(memberId, currentStatus) {
    currentMemberId = memberId;
    newStatus = currentStatus;
    modal.style.display = 'block';
    activeButton.classList.toggle('selected', currentStatus === 'YES');
    terminatedButton.classList.toggle('selected', currentStatus === 'NO');
}

// 모달 닫기 함수
function closeMemberModal() {
    modal.style.display = 'none';
    currentMemberId = null;
    newStatus = null;
    activeButton.classList.remove('selected');
    terminatedButton.classList.remove('selected');
}

// 회원 수정 버튼 클릭 이벤트
function setupEditButtonEvent() {
    document.getElementById('member-list').addEventListener('click', (event) => {
        if (event.target.classList.contains('edit-btn')) {
            const memberId = event.target.dataset.memberId;
            const currentStatus = event.target.closest('.apply-table-row').querySelector('.active-member, .inactive-member').textContent.trim() === '활동 중' ? 'YES' : 'NO';
            openModal(memberId, currentStatus);
        }
    });
}

// 모달 내부 버튼 클릭 이벤트 설정
activeButton.addEventListener('click', () => {
    newStatus = 'YES';
    activeButton.classList.add('selected');
    terminatedButton.classList.remove('selected');
});

terminatedButton.addEventListener('click', () => {
    newStatus = 'NO';
    terminatedButton.classList.add('selected');
    activeButton.classList.remove('selected');
});

// "변경" 버튼 클릭 시 상태 변경
completeButton.addEventListener('click', async () => {
    if (currentMemberId && newStatus !== null) {
        try {
            const success = await updateMemberStatus(currentMemberId, newStatus);
            if (success) {
                updateMemberStatusUI(currentMemberId, newStatus);
                closeMemberModal();
            } else {
                console.error("회원 상태 변경에 실패했습니다.");
            }
        } catch (error) {
            console.error("회원 상태 업데이트 중 오류 발생:", error);
        }
    }
});

// 모달 외부를 클릭하면 모달 닫기
overlay.addEventListener('click', closeMemberModal);


// 전체 선택 및 개별 체크박스 이벤트
function setupCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#member-container .select-all');
    const memberCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        memberCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    memberCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(memberCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 삭제 버튼 클릭 이벤트
document.getElementById('delete-members').addEventListener('click', async () => {
    const selectedCheckboxes = document.querySelectorAll('.apply-checkbox:checked');
    const memberIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.dataset.memberId);

    if (memberIds.length === 0) {
        alert("삭제할 회원을 선택하세요.");
        return;
    }

    const confirmed = confirm("선택한 회원들을 삭제하시겠습니까?");
    if (!confirmed) return;

    try {
        const success = await deleteSelectedMembers(memberIds);
        if (success) {
            alert("회원이 성공적으로 삭제되었습니다.");
            renderMembers(); // 목록 갱신
        } else {
            console.error("회원 삭제에 실패했습니다.");
        }
    } catch (error) {
        console.error("회원 삭제 중 오류 발생:", error);
    }
});

// 회원 데이터를 화면에 렌더링하는 함수
async function renderMembers(page = 1, search = '', order = '') {
    const data = await fetchMembers(page, search, order);
    if (data) {
        const { members, pagination } = data;

        const memberList = document.getElementById('member-list');
        memberList.innerHTML = members.map(createMemberRow).join('');

        renderPagination(pagination, page, search, order);

        setupCheckboxEvents();
    }
}

// 초기화 함수
function init() {
    renderMembers();
    setupPaginationEvent();
    setupEditButtonEvent();
    setupFilterEvent();
    setupSearchEvent();
}

document.addEventListener('DOMContentLoaded', init);












