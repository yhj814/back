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
    const filterOptions = document.querySelectorAll(".sort-filter-option.member");

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

//----------------------------------------------------------------------------------------------------------
// 작품(영상)신고관리

// 선언
let currentOrder = 'postCreatedDate';
let currentSearch = '';
let selectedWorkId = null;
let selectedReportStatus = '';

// 페이지 변경
async function changePage(page) {
    const data = await fetchVideoReports(page, currentSearch, currentOrder);
    renderReportList(data.reports);
    renderVideoReportPagination(data.pagination);
}

// 전체 선택 및 개별 체크박스 이벤트
function videoReportCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#video-report-selectAll .select-all');
    const videoReportCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    // 전체 체크박스
    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        videoReportCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스
    videoReportCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(videoReportCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 신고 상태 모달 열기
function openReportModal(event) {
    const modal = document.getElementById("video-report-modal");
    modal.style.display = "flex";
    selectedWorkId = event.target.closest(".apply-table-row").querySelector(".apply-checkbox").dataset.id;
}

// 신고 상태 선택 및 저장
function setupReportModal() {
    const modal = document.getElementById("video-report-modal");
    const choiceButtons = modal.querySelectorAll(".choice-container input[type=button]");
    const saveButton = modal.querySelector(".btn-complete");
    const overlay = modal.querySelector(".background-overlay");

    choiceButtons.forEach(button => {
        button.addEventListener("click", () => {
            choiceButtons.forEach(btn => btn.classList.remove("on"));
            button.classList.add("on");
            selectedReportStatus = button.getAttribute("data-status");
        });
    });

    // 상태 선택 후 저장
    saveButton.addEventListener("click", async () => {
        if (selectedReportStatus) {
            const success = await updateVideoReportStatus(selectedWorkId, selectedReportStatus);
            if (success) {
                alert("상태가 업데이트되었습니다.");
                modal.style.display = "none";
                updateReportStatusInView(selectedWorkId, selectedReportStatus);
            }
        } else {
            alert("상태를 선택해주세요.");
        }
    });

    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 화면 상태 업데이트
function updateReportStatusInView(workId, newStatus) {
    const statusButton = document.querySelector(`.apply-checkbox[data-id="${workId}"]`)
        .closest(".apply-table-row")
        .querySelector(".report-management-btn.status");
    statusButton.textContent = newStatus;

    // 상태에 따른 배경색 변경
    switch (newStatus) {
        case "DELETE":
            statusButton.style.backgroundColor = "rgba(41, 153, 41, 0.818)";
            break;
        case "HOLD":
            statusButton.style.backgroundColor = "#ffa600";
            break;
        case "NOPROBLEM":
            statusButton.style.backgroundColor = "rgb(183, 183, 183)";
            break;
        default:
            statusButton.style.backgroundColor = "";
    }
}

// 초기화 함수
async function initPage() {
    const data = await fetchVideoReports();
    renderReportList(data.reports);
    renderVideoReportPagination(data.pagination);

    const searchInput = document.getElementById("video-report-search");
    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            currentSearch = searchInput.value;
            changePage(1);
        }
    });

    // 필터 처리
    document.querySelectorAll(".sort-filter-option.video").forEach((option) => {
        option.addEventListener("click", () => {
            document.querySelectorAll(".sort-filter-option.video").forEach(opt => opt.classList.remove("selected"));
            option.classList.add("selected");

            const selectedOrder = option.textContent.trim();
            currentOrder = selectedOrder === "작성일 순" ? "postCreatedDate"
                : selectedOrder === "조회수 순" ? "readCount"
                    : selectedOrder === "평점수 순" ? "star" : "reportStatus";

            changePage(1);
        });
    });

    setupReportModal();
}
// 신고 내역보기 모달
function setupReportDetailsModal() {
    const modal = document.querySelector(".reasons-report-modal.video"); // 모달 요소
    const overlay=document.querySelector(".background-overlay");
    const closeModalButton = modal.querySelector(".close-btn");           // 닫기 버튼

    // 모든 "보기" 버튼에 이벤트 리스너 추가
    document.querySelectorAll(".report-content-look").forEach(button => {
        button.addEventListener("click", () => {
            console.log("보기 버튼 클릭"); // 로그 추가

            // 버튼의 데이터 속성에서 정보를 읽어옴
            const name = button.getAttribute("data-name") || ' ';
            const email = button.getAttribute("data-email") || ' ';
            const time = button.getAttribute("data-time") || ' ';
            const content = button.getAttribute("data-content") || 'No content provided';

            // 모달 내부에 데이터를 설정
            modal.querySelector(".name").textContent = name;
            modal.querySelector(".email").textContent = email;
            modal.querySelector(".time").textContent = time;
            modal.querySelector("textarea[name='reason']").textContent = content;

            modal.style.display = "block"; // 모달 표시
            overlay.style.display="none";
        });
    });

    // 닫기 버튼 클릭 시 모달 닫기
    closeModalButton.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

document.addEventListener("DOMContentLoaded", () => {
    initPage();
    setupReportDetailsModal(); // 모달 이벤트 초기화
});

//----------------------------------------------------------------------------------------------------------
// 작품(글)신고관리

// 선언
let currentTextOrder = 'postCreatedDate';
let currentTextSearch = '';
let selectedWorkTextId = null;
let selectedTextReportStatus = '';

// 페이지 변경
async function changeTextPage(page) {
    const data = await fetchTextReports(page, currentTextSearch, currentTextOrder);
    renderTextReportList(data.reports);
    renderTextReportPagination(data.pagination);
}

// 전체 선택 및 개별 체크박스 이벤트
function textReportCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#text-report-selectAll .select-all');
    const textReportCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    // 전체 체크박스
    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        textReportCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스
    textReportCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(textReportCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 신고 상태 모달 열기
function openTextReportModal(event) {
    const modal = document.getElementById("text-report-modal");
    modal.style.display = "flex";
    selectedWorkTextId = event.target.closest(".apply-table-row").querySelector(".apply-checkbox").dataset.id;
}

// 신고 상태 선택 및 저장
function setupTextReportModal() {
    const modal = document.getElementById("text-report-modal");
    const choiceButtons = modal.querySelectorAll(".choice-container input[type=button]");
    const saveButton = modal.querySelector(".btn-complete");
    const overlay = modal.querySelector(".background-overlay");

    choiceButtons.forEach(button => {
        button.addEventListener("click", () => {
            choiceButtons.forEach(btn => btn.classList.remove("on"));
            button.classList.add("on");
            selectedTextReportStatus = button.getAttribute("data-status");
        });
    });

    // 상태 선택 후 저장
    saveButton.addEventListener("click", async () => {
        if (selectedTextReportStatus) {
            const success = await updateTextReportStatus(selectedWorkTextId, selectedTextReportStatus);
            if (success) {
                alert("상태가 업데이트되었습니다.");
                modal.style.display = "none";
                updateTextReportStatusInView(selectedWorkTextId, selectedTextReportStatus);
            }
        } else {
            alert("상태를 선택해주세요.");
        }
    });

    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 화면 상태 업데이트
function updateTextReportStatusInView(workId, newStatus) {
    const statusButton = document.querySelector(`.apply-checkbox[data-id="${workId}"]`)
        .closest(".apply-table-row")
        .querySelector(".report-management-btn.status");
    statusButton.textContent = newStatus;

    // 상태에 따른 배경색 변경
    switch (newStatus) {
        case "DELETE":
            statusButton.style.backgroundColor = "rgba(41, 153, 41, 0.818)";
            break;
        case "HOLD":
            statusButton.style.backgroundColor = "#ffa600";
            break;
        case "NOPROBLEM":
            statusButton.style.backgroundColor = "rgb(183, 183, 183)";
            break;
        default:
            statusButton.style.backgroundColor = "";
    }
}

// 초기화 함수
async function initTextPage() {
    const data = await fetchTextReports();
    renderTextReportList(data.reports);
    renderTextReportPagination(data.pagination);

    const searchInput = document.getElementById("text-report-search");
    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            currentTextSearch = searchInput.value;
            changeTextPage(1);
        }
    });

    // 필터 처리
    document.querySelectorAll(".sort-filter-option.text").forEach((option) => {
        option.addEventListener("click", () => {
            document.querySelectorAll(".sort-filter-option.text").forEach(opt => opt.classList.remove("selected"));
            option.classList.add("selected");

            const selectedOrder = option.textContent.trim();
            currentTextOrder = selectedOrder === "작성일 순" ? "postCreatedDate"
                : selectedOrder === "조회수 순" ? "readCount"
                    : selectedOrder === "평점수 순" ? "star" : "reportStatus";

            changeTextPage(1);
        });
    });

    setupTextReportModal();
}
// 신고 내역보기 모달
function setupTextReportDetailsModal() {
    const modal = document.querySelector(".reasons-report-modal.text"); // 모달 요소
    const overlay=document.querySelector(".background-overlay");
    const closeModalButton = modal.querySelector(".close-btn");           // 닫기 버튼

    // 모든 "보기" 버튼에 이벤트 리스너 추가
    document.querySelectorAll(".report-content-look-text").forEach(button => {
        button.addEventListener("click", () => {
            console.log("보기 버튼 클릭"); // 로그 추가

            // 버튼의 데이터 속성에서 정보를 읽어옴
            const name = button.getAttribute("data-name") || ' ';
            const email = button.getAttribute("data-email") || ' ';
            const time = button.getAttribute("data-time") || ' ';
            const content = button.getAttribute("data-content") || 'No content provided';

            // 모달 내부에 데이터를 설정
            modal.querySelector(".name").textContent = name;
            modal.querySelector(".email").textContent = email;
            modal.querySelector(".time").textContent = time;
            modal.querySelector("textarea[name='reason']").textContent = content;

            modal.style.display = "block"; // 모달 표시
            overlay.style.display="none";
        });
    });

    // 닫기 버튼 클릭 시 모달 닫기
    closeModalButton.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

document.addEventListener("DOMContentLoaded", () => {
    initTextPage();
    setupTextReportDetailsModal(); // 모달 이벤트 초기화
});

//--------------------------------------------------------------------------------------------------------
// 영상 댓글 신고관리

// 검색 정렬 선언
let currentVideoReplyOrder = 'replyCreatedDate';
let currentVideoReplySearch = '';

// 페이지 변경 함수
async function changeVideoReplyPage(page) {
    const data = await fetchVideoReplyReports(page, currentVideoReplySearch, currentVideoReplyOrder || 'replyCreatedDate');
    renderVideoReplyReportList(data.reports);
    renderVideoReplyReportPagination(data.pagination);
    setupVideoReplyContentClickEvents(); // 댓글 내용 클릭 이벤트 설정
}


// 신고 상태 업데이트 요청 후 목록 갱신
async function updateVideoReplyReportStatus(replyId, reportStatus) {
    const success = await fetchUpdateVideoReplyReportStatus(replyId, reportStatus);
    if (success) {
        alert("상태가 업데이트되었습니다."); // 상태 업데이트 알림
        changeVideoReplyPage(1); // 상태 변경 후 목록 갱신
        document.getElementById("video-reply-report-modal").style.display = "none"; // 모달 닫기
    }
}

// 전체 선택 및 개별 체크박스 이벤트
function videoReplyReportCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#video-reply-report-selectAll .select-all');
    const videoReplyReportCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    // 전체 체크박스
    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        videoReplyReportCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스
    videoReplyReportCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(videoReplyReportCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 신고 상태 모달 열기
function openVideoReplyReportModal(event) {
    const modal = document.getElementById("video-reply-report-modal");
    modal.style.display = "flex";
    selectedReplyId = event.target.closest(".apply-table-row").querySelector(".apply-checkbox").dataset.id;
}

// 신고 상태 선택 및 저장
function setupVideoReplyReportModal() {
    const modal = document.getElementById("video-reply-report-modal");
    const choiceButtons = modal.querySelectorAll(".choice-container input[type=button]");
    const saveButton = modal.querySelector(".btn-complete");
    const overlay = modal.querySelector(".background-overlay");

    choiceButtons.forEach(button => {
        button.addEventListener("click", () => {
            choiceButtons.forEach(btn => btn.classList.remove("on"));
            button.classList.add("on");
            selectedReportReplyStatus = button.getAttribute("data-status");
        });
    });

    // 상태 선택 후 저장
    saveButton.addEventListener("click", async () => {
        if (selectedReportReplyStatus) {
            const success = await updateVideoReplyReportStatus(selectedReplyId, selectedReportReplyStatus);
            if (success) {
                modal.style.display = "none";
                updateVideoReplyReportStatusInView(selectedReplyId, selectedReportReplyStatus);
            }
        } else {
            alert("상태를 선택해주세요.");
        }
    });

    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 화면 신고 상태 업데이트
function updateVideoReplyReportStatusInView(replyId, newStatus) {
    const statusButton = document.querySelector(`.apply-checkbox[data-id="${replyId}"]`)
        .closest(".apply-table-row")
        .querySelector(".report-management-btn.status");
    statusButton.textContent = newStatus;

    // 신고 상태에 따른 배경색 변경
    switch (newStatus) {
        case "DELETE":
            statusButton.style.backgroundColor = "rgba(41, 153, 41, 0.818)";
            break;
        case "HOLD":
            statusButton.style.backgroundColor = "#ffa600";
            break;
        case "NOPROBLEM":
            statusButton.style.backgroundColor = "rgb(183, 183, 183)";
            break;
        default:
            statusButton.style.backgroundColor = "";
    }
}

// 페이지 초기화
function initVideoReplyPage() {
    const searchInput = document.getElementById("video-reply-report-search");

    // 검색 입력 이벤트
    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            currentVideoReplySearch = e.target.value;
            // 검색어가 입력되면 첫 페이지로 이동
            changeVideoReplyPage(1);
        }
    });

    // 필터 버튼 클릭 이벤트
    document.querySelectorAll(".sort-filter-option.video-reply").forEach((option) => {
        option.addEventListener("click", () => {
            // 현재 클릭한 필터 버튼의 data-order 속성 값을 currentVideoReplyOrder에 설정
            const selectedOrder = option.getAttribute("data-order");
            currentVideoReplyOrder = selectedOrder || 'replyCreatedDate'; // 기본값 설정

            document.querySelectorAll(".sort-filter-option.video-reply").forEach(opt => opt.classList.remove("selected"));
            option.classList.add("selected");

            console.log(`Current Order set to: ${currentVideoReplyOrder}`); // 설정된 정렬 기준 로그

            // 변경된 정렬 기준으로 목록 갱신
            changeVideoReplyPage(1);
        });
    });

    // 페이지 초기 로드
    changeVideoReplyPage(1);
}

// 신고 내역보기 모달
function setupVideoReplyReportDetailsModal() {
    const modal = document.querySelector(".reasons-report-modal.video-reply"); // 모달 요소
    const overlay=document.querySelector(".background-overlay");
    const closeModalButton = modal.querySelector(".close-btn");           // 닫기 버튼

    // 모든 "보기" 버튼에 이벤트 리스너 추가
    document.querySelectorAll(".report-content-look-video-reply").forEach(button => {
        button.addEventListener("click", () => {
            console.log("보기 버튼 클릭"); // 로그 추가

            // 버튼의 데이터 속성에서 정보를 읽어옴
            const name = button.getAttribute("data-name") || ' ';
            const email = button.getAttribute("data-email") || ' ';
            const time = button.getAttribute("data-time") || ' ';
            const content = button.getAttribute("data-content") || 'No content provided';

            // 모달 내부에 데이터를 설정
            modal.querySelector(".name").textContent = name;
            modal.querySelector(".email").textContent = email;
            modal.querySelector(".time").textContent = time;
            modal.querySelector("textarea[name='reason']").textContent = content;

            modal.style.display = "block"; // 모달 표시
            overlay.style.display="none";
        });
    });

    // 닫기 버튼 클릭 시 모달 닫기
    closeModalButton.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 댓글 내용 클릭 이벤트 설정
function setupVideoReplyContentClickEvents() {
    document.querySelectorAll(".apply-table-row .video-reply-content").forEach(cell => {
        cell.addEventListener("click", (e) => {
            const replyContent = e.target.textContent.trim();
            openVideoReplyModal(replyContent);
        });
    });
}


// 댓글 모달 열기 함수
function openVideoReplyModal(element) {
    const videoReplyModal = document.getElementById("reply-video-detail-modal");
    const replyContentTextarea = document.getElementById("reply-video-content-textarea");
    const replyContent = element.getAttribute("data-content"); // 원본 내용 가져오기
    replyContentTextarea.value = replyContent;
    videoReplyModal.style.display = "flex";
}

// 모달 외부 클릭 시 닫기
document.getElementById("modal-video-reply-overlay").addEventListener("click", () => {
    document.getElementById("reply-video-detail-modal").style.display = "none";
});

// 페이지 로드
document.addEventListener("DOMContentLoaded", () => {
    initVideoReplyPage();
    setupVideoReplyReportModal();
});

//--------------------------------------------------------------------------------------------------------
// 글 댓글 신고관리

// 검색 정렬 선언
let currentTextReplyOrder = 'replyCreatedDate';
let currentTextReplySearch = '';

// 페이지 변경 함수
async function changeTextReplyPage(page) {
    const data = await fetchTextReplyReports(page, currentTextReplySearch, currentTextReplyOrder || 'replyCreatedDate');
    renderTextReplyReportList(data.reports);
    renderTextReplyReportPagination(data.pagination);
    setupTextReplyContentClickEvents(); // 댓글 내용 클릭 이벤트 설정
}


// 신고 상태 업데이트 요청 후 목록 갱신
async function updateTextReplyReportStatus(replyId, reportStatus) {
    const success = await fetchUpdateTextReplyReportStatus(replyId, reportStatus);
    if (success) {
        alert("상태가 업데이트되었습니다."); // 상태 업데이트 알림
        changeTextReplyPage(1); // 상태 변경 후 목록 갱신
        document.getElementById("text-reply-report-modal").style.display = "none"; // 모달 닫기
    }
}

// 전체 선택 및 개별 체크박스 이벤트
function textReplyReportCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#text-reply-report-selectAll .select-all');
    const textReplyReportCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    // 전체 체크박스
    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        textReplyReportCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스
    textReplyReportCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(textReplyReportCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 신고 상태 모달 열기
function openTextReplyReportModal(event) {
    const modal = document.getElementById("text-reply-report-modal");
    modal.style.display = "flex";
    selectedReplyId = event.target.closest(".apply-table-row").querySelector(".apply-checkbox").dataset.id;
}

// 신고 상태 선택 및 저장
function setupTextReplyReportModal() {
    const modal = document.getElementById("text-reply-report-modal");
    const choiceButtons = modal.querySelectorAll(".choice-container input[type=button]");
    const saveButton = modal.querySelector(".btn-complete");
    const overlay = modal.querySelector(".background-overlay");

    choiceButtons.forEach(button => {
        button.addEventListener("click", () => {
            choiceButtons.forEach(btn => btn.classList.remove("on"));
            button.classList.add("on");
            selectedReportReplyStatus = button.getAttribute("data-status");
        });
    });

    // 상태 선택 후 저장
    saveButton.addEventListener("click", async () => {
        if (selectedReportReplyStatus) {
            const success = await updateTextReplyReportStatus(selectedReplyId, selectedReportReplyStatus);
            if (success) {
                modal.style.display = "none";
                updateTextReplyReportStatusInView(selectedReplyId, selectedReportReplyStatus);
            }
        } else {
            alert("상태를 선택해주세요.");
        }
    });

    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 화면 신고 상태 업데이트
function updateTextReplyReportStatusInView(replyId, newStatus) {
    const statusButton = document.querySelector(`.apply-checkbox[data-id="${replyId}"]`)
        .closest(".apply-table-row")
        .querySelector(".report-management-btn.status");
    statusButton.textContent = newStatus;

    // 신고 상태에 따른 배경색 변경
    switch (newStatus) {
        case "DELETE":
            statusButton.style.backgroundColor = "rgba(41, 153, 41, 0.818)";
            break;
        case "HOLD":
            statusButton.style.backgroundColor = "#ffa600";
            break;
        case "NOPROBLEM":
            statusButton.style.backgroundColor = "rgb(183, 183, 183)";
            break;
        default:
            statusButton.style.backgroundColor = "";
    }
}

// 페이지 초기화
function initTextReplyPage() {
    const searchInput = document.getElementById("text-reply-report-search");

    // 검색 입력 이벤트
    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            currentTextReplySearch = e.target.value;
            // 검색어가 입력되면 첫 페이지로 이동
            changeTextReplyPage(1);
        }
    });

    // 필터 버튼 클릭 이벤트
    document.querySelectorAll(".sort-filter-option.text-reply").forEach((option) => {
        option.addEventListener("click", () => {
            // 현재 클릭한 필터 버튼의 data-order 속성 값을 currentTextReplyOrder에 설정
            const selectedOrder = option.getAttribute("data-order");
            currentTextReplyOrder = selectedOrder || 'replyCreatedDate'; // 기본값 설정

            document.querySelectorAll(".sort-filter-option.text-reply").forEach(opt => opt.classList.remove("selected"));
            option.classList.add("selected");

            console.log(`Current Order set to: ${currentTextReplyOrder}`); // 설정된 정렬 기준 로그

            // 변경된 정렬 기준으로 목록 갱신
            changeTextReplyPage(1);
        });
    });

    // 페이지 초기 로드
    changeTextReplyPage(1);
}

// 신고 내역보기 모달
function setupTextReplyReportDetailsModal() {
    const modal = document.querySelector(".reasons-report-modal.text-reply"); // 모달 요소
    const overlay=document.querySelector(".background-overlay");
    const closeModalButton = modal.querySelector(".close-btn");           // 닫기 버튼

    // 모든 "보기" 버튼에 이벤트 리스너 추가
    document.querySelectorAll(".report-content-look-text-reply").forEach(button => {
        button.addEventListener("click", () => {
            console.log("보기 버튼 클릭"); // 로그 추가

            // 버튼의 데이터 속성에서 정보를 읽어옴
            const name = button.getAttribute("data-name") || ' ';
            const email = button.getAttribute("data-email") || ' ';
            const time = button.getAttribute("data-time") || ' ';
            const content = button.getAttribute("data-content") || 'No content provided';

            // 모달 내부에 데이터를 설정
            modal.querySelector(".name").textContent = name;
            modal.querySelector(".email").textContent = email;
            modal.querySelector(".time").textContent = time;
            modal.querySelector("textarea[name='reason']").textContent = content;

            modal.style.display = "block"; // 모달 표시
            overlay.style.display="none";
        });
    });

    // 닫기 버튼 클릭 시 모달 닫기
    closeModalButton.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 댓글 내용 클릭 이벤트 설정
function setupTextReplyContentClickEvents() {
    document.querySelectorAll(".apply-table-row .text-reply-content").forEach(cell => {
        cell.addEventListener("click", (e) => {
            const replyContent = e.target.textContent.trim();
            openTextReplyModal(replyContent);
        });
    });
}

// 댓글 모달 열기 함수
function openTextReplyModal(element) {
    const textReplyModal = document.getElementById("reply-text-detail-modal");
    const replyContentTextarea = document.getElementById("reply-text-content-textarea");
    const replyContent = element.getAttribute("data-content"); // 원본 내용 가져오기
    replyContentTextarea.value = replyContent;
    textReplyModal.style.display = "flex";
}


// 모달 외부 클릭 시 닫기
document.getElementById("modal-text-reply-overlay").addEventListener("click", () => {
    document.getElementById("reply-text-detail-modal").style.display = "none";
});

// 페이지 로드
document.addEventListener("DOMContentLoaded", () => {
    initTextReplyPage();
    setupTextReplyReportModal();
});

//--------------------------------------------------------------------------------------------------------
// 영상 모집 신고관리

// 검색 정렬 선언
let currentVideoAuditionOrder = 'createdDate';
let currentVideoAuditionSearch = '';

// 페이지 변경 함수
async function changeVideoAuditionPage(page) {
    const data = await fetchVideoAuditionReports(page, currentVideoAuditionSearch, currentVideoAuditionOrder || 'createdDate');
    renderVideoAuditionReportList(data.reports);
    renderVideoAuditionReportPagination(data.pagination);
}


// 신고 상태 업데이트 요청 후 목록 갱신
async function updateVideoAuditionReportStatus(auditionId, reportStatus) {
    const success = await fetchUpdateVideoAuditionReportStatus(auditionId, reportStatus);
    if (success) {
        alert("상태가 업데이트되었습니다."); // 상태 업데이트 알림
        changeVideoAuditionPage(1); // 상태 변경 후 목록 갱신
        document.getElementById("video-audition-report-modal").style.display = "none"; // 모달 닫기
    }
}

// 전체 선택 및 개별 체크박스 이벤트
function videoAuditionReportCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#video-audition-report-selectAll .select-all');
    const videoAuditionReportCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    // 전체 체크박스
    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        videoAuditionReportCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스
    videoAuditionReportCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(videoAuditionReportCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 신고 상태 모달 열기
function openVideoAuditionReportModal(event) {
    const modal = document.getElementById("video-audition-report-modal");
    modal.style.display = "flex";
    selectedAuditionId = event.target.closest(".apply-table-row").querySelector(".apply-checkbox").dataset.id;
}

// 신고 상태 선택 및 저장
function setupVideoAuditionReportModal() {
    const modal = document.getElementById("video-audition-report-modal");
    const choiceButtons = modal.querySelectorAll(".choice-container input[type=button]");
    const saveButton = modal.querySelector(".btn-complete");
    const overlay = modal.querySelector(".background-overlay");

    choiceButtons.forEach(button => {
        button.addEventListener("click", () => {
            choiceButtons.forEach(btn => btn.classList.remove("on"));
            button.classList.add("on");
            selectedReportAuditionStatus = button.getAttribute("data-status");
        });
    });

    // 상태 선택 후 저장
    saveButton.addEventListener("click", async () => {
        if (selectedReportAuditionStatus) {
            const success = await updateVideoAuditionReportStatus(selectedAuditionId, selectedReportAuditionStatus);
            if (success) {
                modal.style.display = "none";
                updateVideoAuditionReportStatusInView(selectedAuditionId, selectedReportAuditionStatus);
            }
        } else {
            alert("상태를 선택해주세요.");
        }
    });

    overlay.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

// 화면 신고 상태 업데이트
function updateVideoAuditionReportStatusInView(replyId, newStatus) {
    const statusButton = document.querySelector(`.apply-checkbox[data-id="${replyId}"]`)
        .closest(".apply-table-row")
        .querySelector(".report-management-btn.status");
    statusButton.textContent = newStatus;

    // 신고 상태에 따른 배경색 변경
    switch (newStatus) {
        case "DELETE":
            statusButton.style.backgroundColor = "rgba(41, 153, 41, 0.818)";
            break;
        case "HOLD":
            statusButton.style.backgroundColor = "#ffa600";
            break;
        case "NOPROBLEM":
            statusButton.style.backgroundColor = "rgb(183, 183, 183)";
            break;
        default:
            statusButton.style.backgroundColor = "";
    }
}

// 페이지 초기화
function initVideoAuditionPage() {
    const searchInput = document.getElementById("video-audition-report-search");

    // 검색 입력 이벤트
    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            let searchValue = e.target.value.trim();

            // 검색어를 숫자 코드로 변환
            switch (searchValue) {
                case "배우":
                    currentVideoAuditionSearch = 1;
                    break;
                case "스텝":
                    currentVideoAuditionSearch = 2;
                    break;
                case "감독":
                    currentVideoAuditionSearch = 3;
                    break;
                case "기타":
                    currentVideoAuditionSearch = 4;
                    break;
                default:
                    currentVideoAuditionSearch = searchValue; // 숫자가 아닌 경우 그대로 사용
                    break;
            }

            // 검색어가 입력되면 첫 페이지로 이동
            changeVideoAuditionPage(1);
        }
    });

    // 필터 버튼 클릭 이벤트
    document.querySelectorAll(".sort-filter-option.video-audition").forEach((option) => {
        option.addEventListener("click", () => {
            // 현재 클릭한 필터 버튼의 data-order 속성 값을 currentVideoAuditionOrder에 설정
            const selectedOrder = option.getAttribute("data-order");

            // 선택한 정렬 기준에 따라 currentVideoAuditionOrder를 설정
            if (selectedOrder === "createdDate") {
                currentVideoAuditionOrder = "작성순";
            } else if (selectedOrder === "video-application-count") {
                currentVideoAuditionOrder = "지원자순";
            } else if (selectedOrder === "reportStatus") {
                currentVideoAuditionOrder = "신고관리";
            } else {
                currentVideoAuditionOrder = "작성순"; // 기본값
            }

            // 모든 필터 버튼의 'selected' 클래스 제거 후 현재 선택한 옵션에 추가
            document.querySelectorAll(".sort-filter-option.video-audition").forEach(opt => opt.classList.remove("selected"));
            option.classList.add("selected");

            console.log(`Current Order set to: ${currentVideoAuditionOrder}`); // 설정된 정렬 기준 로그

            // 변경된 정렬 기준으로 목록 갱신
            changeVideoAuditionPage(1);
        });
    });


    // 페이지 초기 로드
    changeVideoAuditionPage(1);
}

// 신고 내역보기 모달
function setupVideoAuditionReportDetailsModal() {
    const modal = document.querySelector(".reasons-report-modal.video-audition"); // 모달 요소
    const overlay=document.querySelector(".background-overlay");
    const closeModalButton = modal.querySelector(".close-btn");           // 닫기 버튼

    // 모든 "보기" 버튼에 이벤트 리스너 추가
    document.querySelectorAll(".report-content-look-video-audition").forEach(button => {
        button.addEventListener("click", () => {
            console.log("보기 버튼 클릭"); // 로그 추가

            // 버튼의 데이터 속성에서 정보를 읽어옴
            const name = button.getAttribute("data-name") || ' ';
            const email = button.getAttribute("data-email") || ' ';
            const time = button.getAttribute("data-time") || ' ';
            const content = button.getAttribute("data-content") || 'No content provided';

            // 모달 내부에 데이터를 설정
            modal.querySelector(".name").textContent = name;
            modal.querySelector(".email").textContent = email;
            modal.querySelector(".time").textContent = time;
            modal.querySelector("textarea[name='reason']").textContent = content;

            modal.style.display = "block"; // 모달 표시
            overlay.style.display="none";
        });
    });

    // 닫기 버튼 클릭 시 모달 닫기
    closeModalButton.addEventListener("click", () => {
        modal.style.display = "none";
    });
}


// 페이지 로드
document.addEventListener("DOMContentLoaded", () => {
    initVideoAuditionPage();
    setupVideoAuditionReportModal();
});




