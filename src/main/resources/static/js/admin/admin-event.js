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

    // 답변 하기 버튼 이벤트
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
















