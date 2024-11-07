// 공지사항 목록 로드
function loadAnnouncements(page, search = '',order= 'createdDate') {
    fetch(`/admin/announcements?page=${page}&order=${order}&search=${encodeURIComponent(search)}`)
        .then(response => response.json())
        .then(data => {
            const announcements = data.announcements;
            const pagination = data.pagination;

            const container = document.querySelector('#announcement-id #announcement-list');
            container.innerHTML = ''; // 기존 목록 초기화

            // 공지사항 목록을 HTML로 변환
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

            // 페이지 버튼
            updatePagination(pagination, page, search, order);

            // 수정 버튼 클릭 시 모달창 열기
            setEditButtonListeners();

            // 전체 선택 상태 초기화
            document.querySelector('#announcement-list-select-all').checked = false;

            // 개별 체크박스 리스너
            setIndividualCheckboxListeners();
        })
        .catch(error => console.error('Error fetching announcements:', error));
}

// 페이지 버튼 (검색과 정렬을 포함하여 호출)
function updatePagination(pagination, currentPage, search, order) {
    const paginationContainer = document.querySelector('#pagination-announcement .pagination-list');
    paginationContainer.innerHTML = ''; // 기존 페이지 버튼 초기화

    // 이전 페이지 버튼 (비활성화 조건 포함 1페이지에서 더이상 이동하지 않음)
    paginationContainer.innerHTML += `
        <li class="pagination-prev ${currentPage === 1 ? 'disabled' : ''}">
            <a href="#" onclick="loadAnnouncements(${Math.max(currentPage - 1, 1)}, '${search}', '${order}')">&lt;</a>
        </li>`;

    // 페이지 번호 버튼 생성
    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        paginationContainer.innerHTML += `
            <li class="pagination-page ${i === currentPage ? 'active' : ''}">
                <a href="#" onclick="loadAnnouncements(${i}, '${search}', '${order}')">${i}</a>
            </li>`;
    }

    // 다음 페이지 버튼 (비활성화 조건 포함 마지막 페이지에서 이동하지 않음)
    paginationContainer.innerHTML += `
        <li class="pagination-next ${currentPage === pagination.realEnd ? 'disabled' : ''}">
            <a href="#" onclick="loadAnnouncements(${Math.min(currentPage + 1, pagination.realEnd)}, '${search}', '${order}')">&gt;</a>
        </li>`;
}

//------------------------------------------------------------------------------------------------------------------
// 문의사항
const inquiryLayout = {
    // HTML ID 상수로 지정
    ORDER_INQUIRY_CREATED_DATE: "inquiry-created-date",
    ORDER_ANSWER_CREATED_DATE: "inquiry-answer-created-date",
    ORDER_NO_ANSWER: "no-answer-filter",

    renderInquiries(inquiries, order = this.ORDER_INQUIRY_CREATED_DATE) {
        const inquiryList = document.getElementById("inquiry-list");
        this.currentOrder = order; // 현재 필터 상태 저장
        inquiryList.innerHTML = ""; // 기존 목록 초기화

        if (!inquiries || inquiries.length === 0) {
            inquiryList.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
            return;
        }

        inquiries.forEach((inquiry) => {
            // 필터에 따른 데이터 표시 조건
            if (this.currentOrder === this.ORDER_NO_ANSWER && inquiry.inquiryStatus !== "NO") return;
            if (this.currentOrder === this.ORDER_ANSWER_CREATED_DATE && inquiry.inquiryStatus === "NO") return;

            // HTML 구조 생성
            const inquiryItem = document.createElement("div");
            inquiryItem.classList.add("apply-table-row");

            inquiryItem.innerHTML = `
            <div class="apply-table-cell">
                <input type="checkbox" class="apply-checkbox " data-id="${inquiry.postId}"/>
            </div>
            <div class="apply-table-cell">${inquiry.postId}</div>
            <div class="apply-table-cell">${inquiry.postCreatedDate}</div>
            <div class="apply-table-cell inquiry-title" 
                 data-original-title="${inquiry.postTitle}">
                ${this.truncateText(inquiry.postTitle, 8)}
            </div>
            <div class="apply-table-cell inquiry-content" 
                 data-original-content="${inquiry.postContent}">
                ${this.truncateText(inquiry.postContent, 8)}
            </div>
            <div class="apply-table-cell">${inquiry.profileName}</div>
            <div class="apply-table-cell">${inquiry.profileEmail}</div>
            <div class="apply-table-cell">
                <button class="answered-btn status ${
                inquiry.inquiryStatus === "NO" ? "unanswered" : "completed"
            }" data-inquiry-id="${inquiry.postId}">
                    ${inquiry.inquiryStatus === "NO" ? "미답변" : "답변 완료"}
                </button>
            </div>
            <div class="apply-table-cell answer-date">
                ${inquiry.inquiryStatus === "NO" ? "" : inquiry.answerDate || ""}
            </div>
            <div class="apply-table-cell answer-content" style="display: none">
                ${inquiry.inquiryStatus === "NO" ? "" : inquiry.answerContent || ""}
            </div>
        `;
            inquiryList.appendChild(inquiryItem);
        });

        // 체크박스 이벤트
        inquiryCheckboxListeners();

        // 미답변일 때 답변 가능한 모달창 열기
        noAnswerModalEvents();
    },

    // 답변 등록 후 화면 업데이트
    updateInquiryStatus(inquiryId, answerInfo) {
        const inquiryRow = document.querySelector(`.answered-btn[data-inquiry-id="${inquiryId}"]`).closest('.apply-table-row');

        const statusButton = inquiryRow.querySelector('.answered-btn');
        statusButton.textContent = "답변 완료";
        statusButton.classList.remove("unanswered");
        statusButton.classList.add("completed");

        const answerContentCell = inquiryRow.querySelector('.answer-content');
        answerContentCell.textContent = answerInfo.answerContent;

        const answerDateCell = inquiryRow.querySelector('.answer-date');
        answerDateCell.textContent  = answerInfo.createdDate;
    },

    // 텍스트 길이에 따라 잘라내는 함수
    truncateText(text, maxLength) {
        return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
    },

    // 페이지 네이션 (order 값 전달)
   renderPagination(pagination, currentPage = 1, currentOrder = inquiryEvent.ORDER_INQUIRY_CREATED_DATE, searchQuery = "") {
    const paginationContainer = document.querySelector("#pagination-inquiry .pagination-list");
    paginationContainer.innerHTML = ""; // 기존 페이지 버튼 초기화

    // 이전 페이지 버튼 (첫 페이지일 때 비활성화)
    paginationContainer.innerHTML += `
        <li class="pagination-prev ${currentPage === 1 ? 'disabled' : ''}">
            <a href="#" onclick="if(${currentPage} > 1) inquiryEvent.loadInquiries(${currentPage - 1}, '${currentOrder}', '${searchQuery}'); return false;">&lt;</a>
        </li>`;

    // 페이지 번호 버튼 생성 (startPage부터 endPage까지 표시)
    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        paginationContainer.innerHTML += `
            <li class="pagination-page ${i === currentPage ? 'active' : ''}">
                <a href="#" onclick="inquiryEvent.loadInquiries(${i}, '${currentOrder}', '${searchQuery}'); return false;">${i}</a>
            </li>`;
    }

    // 다음 페이지 버튼 (마지막 페이지일 때 비활성화)
    paginationContainer.innerHTML += `
        <li class="pagination-next ${currentPage === pagination.realEnd ? 'disabled' : ''}">
            <a href="#" onclick="if(${currentPage} < ${pagination.realEnd}) inquiryEvent.loadInquiries(${currentPage + 1}, '${currentOrder}', '${searchQuery}'); return false;">&gt;</a>
        </li>`;
    }

};

//-------------------------------------------------------------------------------------------------------------------------
// 회원관리

// 각 회원 정보를 HTML 요소로 변환하는 함수
function createMemberRow(member) {
    return `
        <div class="apply-table-row" data-member-id="${member.memberId}">
            <div class="apply-table-cell">
                <input type="checkbox" class="apply-checkbox" data-member-id="${member.memberId}" />
            </div>
            <div class="apply-table-cell">${member.memberId}</div>
            <div class="apply-table-cell">${member.profileName}</div>
            <div class="apply-table-cell">${member.createdDate}</div>
            <div class="apply-table-cell">${member.memberEmail}</div>
            <div class="apply-table-cell">${member.profilePhone}</div>
            <div class="apply-table-cell">${member.profileAge}</div>
            <div class="apply-table-cell">${member.profileGender}</div>
            <div class="apply-table-cell">${member.profileEmail}</div>
            <div class="apply-table-cell ${member.memberStatus === 'YES' ? 'active-member' : 'inactive-member'}">
                ${member.memberStatus === 'YES' ? '활동 중' : '탈퇴'}
            </div>
            <div class="apply-table-cell">
                <button class="edit-btn" data-member-id="${member.memberId}">
                    수정
                </button>
            </div>
        </div>
    `;
}

// 회원 상태 업데이트 후 UI 업데이트 함수
async function updateMemberStatusUI(memberId, newStatus) {
    const success = await updateMemberStatus(memberId, newStatus);
    if (success) {
        const memberRow = document.querySelector(`.apply-table-row[data-member-id="${memberId}"]`);
        if (memberRow) {
            const statusCell = memberRow.querySelector('.active-member, .inactive-member');
            statusCell.textContent = newStatus === 'YES' ? '활동 중' : '탈퇴';
            statusCell.className = `apply-table-cell ${newStatus === 'YES' ? 'active-member' : 'inactive-member'}`;
        }
    } else {
        console.error("회원 상태 변경에 실패했습니다.");
    }
}

// 선택된 회원 삭제 함수
async function deleteSelectedMembers() {
    const selectedCheckboxes = document.querySelectorAll('.apply-checkbox:checked');
    const memberIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.dataset.memberId);

    // 선택한 회원없이 삭제할 경우
    if (memberIds.length === 0) {
        alert("삭제할 회원을 선택하세요.");
        return;
    }

    const success = await deleteMembersByIds(memberIds);
    if (success) {
        alert("회원이 성공적으로 삭제되었습니다.");
        renderMembers(); // 회원 목록 갱신
    } else {
        console.error("회원 삭제에 실패했습니다.");
    }
}

// 회원 페이지네이션 버튼 (검색과 정렬 포함)
function renderPagination(pagination, currentPage, search = '', order = '') {
    const { startPage, endPage, realEnd } = pagination;
    const paginationContainer = document.querySelector('#pagination-members .pagination-list');
    paginationContainer.innerHTML = ''; // 기존 페이지 버튼 초기화

    // 이전 페이지 버튼 (1페이지에서는 비활성화)
    paginationContainer.innerHTML += `
        <li class="pagination-prev ${currentPage === 1 ? 'disabled' : ''}">
            <a href="#" data-page="${currentPage > 1 ? currentPage - 1 : 1}">&lt;</a>
        </li>`;

    // 페이지 번호 버튼 생성
    for (let i = startPage; i <= endPage; i++) {
        paginationContainer.innerHTML += `
            <li class="pagination-page ${i === currentPage ? 'active' : ''}">
                <a href="#" data-page="${i}">${i}</a>
            </li>`;
    }

    // 다음 페이지 버튼 (마지막 페이지에서는 비활성화)
    paginationContainer.innerHTML += `
        <li class="pagination-next ${currentPage === realEnd ? 'disabled' : ''}">
            <a href="#" data-page="${currentPage < realEnd ? currentPage + 1 : realEnd}">&gt;</a>
        </li>`;

    // 페이지네이션 클릭 이벤트 추가
    paginationContainer.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            const page = parseInt(event.target.dataset.page);
            // (!isNaN(page) 페이지가 숫자인지
            if (!isNaN(page) && page > 0 && page <= realEnd && page !== currentPage) {
                renderMembers(page, search, order);
            }
        });
    });
}

// 전체 선택 및 개별 체크박스 설정
function setupCheckboxEvents() {
    const selectAllCheckbox = document.querySelector('#member-container .select-all');
    const memberCheckboxes = document.querySelectorAll('.apply-table-row .apply-checkbox');

    // 전체 선택 체크박스 클릭 시, 모든 개별 체크박스 선택/해제
    selectAllCheckbox.addEventListener('change', (event) => {
        const isChecked = event.target.checked;
        memberCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스 클릭 시, 전체 선택 체크박스 상태 업데이트
    memberCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(memberCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
        });
    });
}

// 삭제 버튼 클릭 이벤트 추가
document.getElementById('delete-members').addEventListener('click', deleteSelectedMembers);

// 회원 데이터를 화면에 랜더링
async function renderMembers(page = 1, search = '', order = '') {
    const data = await fetchMembers(page, search, order);
    if (data) {
        const { members, pagination } = data;

        // 회원 목록
        const memberList = document.getElementById('member-list');
        // join('') : map 함수로 생성된 문자열 배열을 하나로 합치기
        memberList.innerHTML = members.map(createMemberRow).join('');

        // 페이지네이션
        renderPagination(pagination, page, search, order);

        // 페이지 변경 시 체크박스 이벤트 다시 설정
        setupCheckboxEvents();
    }
}

// 초기화 함수
function init() {
    renderMembers(); // 첫 페이지 렌더링
    setupCheckboxEvents(); // 체크박스 이벤트 설정
}

document.addEventListener('DOMContentLoaded', init);


//----------------------------------------------------------------------------------------------------------

// 작품(영상)신고관리

function renderReportList(reports) {
    const container = document.getElementById("work-video-report-list");
    container.innerHTML = '';

    if (!reports || reports.length === 0) {
        container.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
        return;
    }

    reports.forEach((report) => {
        const row = document.createElement("div");
        row.className = "apply-table-row";

        // 상태에 따른 배경색 결정
        let buttonBackgroundColor = "";
        switch (report.reportStatus) {
            case "DELETE":
                buttonBackgroundColor = "rgba(41, 153, 41, 0.818)";
                break;
            case "HOLD":
                buttonBackgroundColor = "#ffa600";
                break;
            case "NOPROBLEM":
                buttonBackgroundColor = "rgb(183, 183, 183)";
                break;
            default:
                buttonBackgroundColor = "";
        }

        row.innerHTML = `
            <div class="apply-table-cell">
                <input type="checkbox" class="apply-checkbox" data-id="${report.postId}"/>
            </div>
            <div class="apply-table-cell">${report.postId}</div>
            <div class="apply-table-cell">${report.profileName || ''}</div>
            <div class="apply-table-cell">${report.postCreatedDate || ''}</div>
            <div class="apply-table-cell">${report.genreType || ''}</div>
            <div class="apply-table-cell post-title">
                 <a href="#">
                    ${report.postTitle && report.postTitle.length > 7
                    ? report.postTitle.substring(0, 7) + '...'
                    : report.postTitle || ''}
                </a>
            </div>
            <div class="apply-table-cell">${report.readCount || 0}</div>
            <div class="apply-table-cell">${report.star || 0}</div>
            <div class="apply-table-cell">${report.workPrice || '0원'}원</div>
            <div class="apply-table-cell">
                <button class="report-management-btn status report" 
                        style="background-color: ${buttonBackgroundColor};"
                        onclick="openReportModal(event)">
                    ${report.reportStatus || '신고'}
                </button>
            </div>
            <div class="apply-table-cell">
                <button class="reasons-report-btn report-content-look" 
                    data-name="${report.reportProfileName || ''}" 
                    data-email="${report.reportProfileEmail || ''}" 
                    data-time="${report.reportCreatedDate || ''}" 
                    data-content="${report.reportContents || ''}">
                    보기
                </button>
            </div>
        `;

        container.appendChild(row);
    });

    videoReportCheckboxEvents();
    setupReportDetailsModal();
}

// 페이지네이션 HTML을 생성
function renderVideoReportPagination(pagination) {
    const paginationContainer = document.getElementById("pagination-video-report");
    paginationContainer.innerHTML = '';

    if (!pagination) return;

    // 이전 버튼
    const prevButton = document.createElement("li");
    prevButton.className = `pagination-prev ${pagination.page > 1 ? '' : 'disabled'}`;
    prevButton.innerHTML = `
        <a href="#" class="pagination-prev-link" rel="prev nofollow">
            <span class="pagination-prev-icon" aria-hidden="true">‹</span>
        </a>`;
    prevButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page > 1) changePage(pagination.page - 1);
    });
    paginationContainer.appendChild(prevButton);

    // 페이지 번호 버튼
    for (let i = pagination.startPage; i <= Math.min(pagination.endPage, pagination.realEnd); i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `pagination-page ${i === pagination.page ? 'active' : ''}`;
        pageButton.innerHTML = `<a href="#" class="pagination-page-link">${i}</a>`;
        pageButton.addEventListener("click", (e) => {
            e.preventDefault();
            changePage(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    // 다음 버튼
    const nextButton = document.createElement("li");
    nextButton.className = `pagination-next ${pagination.page < pagination.realEnd ? '' : 'disabled'}`;
    nextButton.innerHTML = `
        <a href="#" class="pagination-next-link" rel="next nofollow">
            <span class="pagination-next-icon" aria-hidden="true">›</span>
        </a>`;
    nextButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page < pagination.realEnd) changePage(pagination.page + 1);
    });
    paginationContainer.appendChild(nextButton);
}

//----------------------------------------------------------------------------------------------------------

// 작품(글)신고관리

function renderTextReportList(reports) {
    const container = document.getElementById("work-text-report-list");
    container.innerHTML = '';

    if (!reports || reports.length === 0) {
        container.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
        return;
    }

    reports.forEach((report) => {
        const row = document.createElement("div");
        row.className = "apply-table-row";

        // 상태에 따른 배경색 결정
        let buttonBackgroundColor = "";
        switch (report.reportStatus) {
            case "DELETE":
                buttonBackgroundColor = "rgba(41, 153, 41, 0.818)";
                break;
            case "HOLD":
                buttonBackgroundColor = "#ffa600";
                break;
            case "NOPROBLEM":
                buttonBackgroundColor = "rgb(183, 183, 183)";
                break;
            default:
                buttonBackgroundColor = "";
        }

        row.innerHTML = `
            <div class="apply-table-cell">
                <input type="checkbox" class="apply-checkbox" data-id="${report.postId}"/>
            </div>
            <div class="apply-table-cell">${report.postId}</div>
            <div class="apply-table-cell">${report.profileName || ''}</div>
            <div class="apply-table-cell">${report.postCreatedDate || ''}</div>
            <div class="apply-table-cell">${report.genreType || ''}</div>
            <div class="apply-table-cell post-title">
                 <a href="#">
                  ${report.postTitle && report.postTitle.length > 7
                  ? report.postTitle.substring(0, 7) + '...'
                  : report.postTitle || ''}
                </a>
            </div>
            <div class="apply-table-cell">${report.readCount || 0}</div>
            <div class="apply-table-cell">${report.star || 0}</div>
            <div class="apply-table-cell">${report.workPrice || '0원'}원</div>
            <div class="apply-table-cell">
                <button class="report-management-btn status report" 
                        style="background-color: ${buttonBackgroundColor};"
                        onclick="openTextReportModal(event)">
                    ${report.reportStatus || '신고'}
                </button>
            </div>
            <div class="apply-table-cell">
                <button class="reasons-report-btn report-content-look-text" 
                    data-name="${report.reportProfileName || ''}" 
                    data-email="${report.reportProfileEmail || ''}" 
                    data-time="${report.reportCreatedDate || ''}" 
                    data-content="${report.reportContents || ''}">
                    보기
                </button>
            </div>
        `;

        container.appendChild(row);
    });

    textReportCheckboxEvents();
    setupTextReportDetailsModal();
}

// 페이지네이션 HTML을 생성
function renderTextReportPagination(pagination) {
    const paginationContainer = document.getElementById("pagination-text-report");
    paginationContainer.innerHTML = '';

    if (!pagination) return;

    // 이전 버튼
    const prevButton = document.createElement("li");
    prevButton.className = `pagination-prev ${pagination.page > 1 ? '' : 'disabled'}`;
    prevButton.innerHTML = `
        <a href="#" class="pagination-prev-link" rel="prev nofollow">
            <span class="pagination-prev-icon" aria-hidden="true">‹</span>
        </a>`;
    prevButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page > 1) changeTextPage(pagination.page - 1);
    });
    paginationContainer.appendChild(prevButton);

    // 페이지 번호 버튼
    for (let i = pagination.startPage; i <= Math.min(pagination.endPage, pagination.realEnd); i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `pagination-page ${i === pagination.page ? 'active' : ''}`;
        pageButton.innerHTML = `<a href="#" class="pagination-page-link">${i}</a>`;
        pageButton.addEventListener("click", (e) => {
            e.preventDefault();
            changeTextPage(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    // 다음 버튼
    const nextButton = document.createElement("li");
    nextButton.className = `pagination-next ${pagination.page < pagination.realEnd ? '' : 'disabled'}`;
    nextButton.innerHTML = `
        <a href="#" class="pagination-next-link" rel="next nofollow">
            <span class="pagination-next-icon" aria-hidden="true">›</span>
        </a>`;
    nextButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page < pagination.realEnd) changeTextPage(pagination.page + 1);
    });
    paginationContainer.appendChild(nextButton);
}

//--------------------------------------------------------------------------------------------------

// 영상 댓글 신고 관리 목록
function renderVideoReplyReportList(replyReports) {
    const container = document.getElementById("video-reply-report-list");
    container.innerHTML = '';

    if (!replyReports || replyReports.length === 0) {
        container.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
        return;
    }

    replyReports.forEach((replyReport) => {
        const row = document.createElement("div");
        row.className = "apply-table-row";

        let buttonBackgroundColor = "";
        switch (replyReport.reportStatus) {
            case "DELETE": buttonBackgroundColor = "rgba(41, 153, 41, 0.818)"; break;
            case "HOLD": buttonBackgroundColor = "#ffa600"; break;
            case "NOPROBLEM": buttonBackgroundColor = "rgb(183, 183, 183)"; break;
            default: buttonBackgroundColor = "";
        }

        row.innerHTML = `
            <div class="apply-table-cell"><input type="checkbox" class="apply-checkbox" data-id="${replyReport.replyId}"/></div>
            <div class="apply-table-cell">${replyReport.replyId}</div>
            <div class="apply-table-cell post-title"><a href="#">${replyReport.postTitle || ''}</a></div>
            <div class="apply-table-cell video-reply-content" style="cursor: pointer" 
                 data-content="${replyReport.replyContent || ''}"
                    onclick="openVideoReplyModal(this)">
                    ${replyReport.replyContent && replyReport.replyContent.length > 6
                    ? replyReport.replyContent.substring(0, 6) + '...' : replyReport.replyContent || ''}</div>
            <div class="apply-table-cell">${replyReport.profileName || ''}</div>
            <div class="apply-table-cell">${replyReport.replyCreatedDate || ''}</div>
            <div class="apply-table-cell">
                <button class="report-management-btn status report" 
                        style="background-color: ${buttonBackgroundColor};"
                        onclick="openVideoReplyReportModal(event)">
                    ${replyReport.reportStatus || '신고'}
                </button>
            </div>
            <div class="apply-table-cell">
                <button class="reasons-report-btn report-content-look-text-reply" 
                    data-name="${replyReport.reportProfileName || ''}" 
                    data-email="${replyReport.reportProfileEmail || ''}" 
                    data-time="${replyReport.reportCreatedDate || ''}" 
                    data-content="${replyReport.reportContents || ''}">
                    보기
                </button>
            </div>
        `;

        container.appendChild(row);
    });

    // 체크박스 이벤트
    videoReplyReportCheckboxEvents();

    // 신고내역 보기 모달 이벤트
    setupVideoReplyReportDetailsModal();

}

// 페이지네이션
function renderVideoReplyReportPagination(pagination) {
    const paginationContainer = document.getElementById("pagination-video-reply-report");
    paginationContainer.innerHTML = '';

    if (!pagination) return;

    const prevButton = document.createElement("li");
    prevButton.className = `pagination-prev ${pagination.page > 1 ? '' : 'disabled'}`;
    prevButton.innerHTML = `<a href="#" class="pagination-prev-link" rel="prev nofollow"><span class="pagination-prev-icon" aria-hidden="true">‹</span></a>`;
    prevButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page > 1) changeVideoReplyPage(pagination.page - 1);
    });
    paginationContainer.appendChild(prevButton);

    for (let i = pagination.startPage; i <= Math.min(pagination.endPage, pagination.realEnd); i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `pagination-page ${i === pagination.page ? 'active' : ''}`;
        pageButton.innerHTML = `<a href="#" class="pagination-page-link">${i}</a>`;
        pageButton.addEventListener("click", (e) => {
            e.preventDefault();
            changeVideoReplyPage(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    const nextButton = document.createElement("li");
    nextButton.className = `pagination-next ${pagination.page < pagination.realEnd ? '' : 'disabled'}`;
    nextButton.innerHTML = `<a href="#" class="pagination-next-link" rel="next nofollow"><span class="pagination-next-icon" aria-hidden="true">›</span></a>`;
    nextButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page < pagination.realEnd) changeVideoReplyPage(pagination.page + 1);
    });
    paginationContainer.appendChild(nextButton);
}


//--------------------------------------------------------------------------------------------------

// 글 댓글 신고 관리 목록
function renderTextReplyReportList(replyReports) {
    const container = document.getElementById("text-reply-report-list");
    container.innerHTML = '';

    if (!replyReports || replyReports.length === 0) {
        container.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
        return;
    }

    replyReports.forEach((replyReport) => {
        const row = document.createElement("div");
        row.className = "apply-table-row";

        let buttonBackgroundColor = "";
        switch (replyReport.reportStatus) {
            case "DELETE": buttonBackgroundColor = "rgba(41, 153, 41, 0.818)"; break;
            case "HOLD": buttonBackgroundColor = "#ffa600"; break;
            case "NOPROBLEM": buttonBackgroundColor = "rgb(183, 183, 183)"; break;
            default: buttonBackgroundColor = "";
        }

        row.innerHTML = `
            <div class="apply-table-cell"><input type="checkbox" class="apply-checkbox" data-id="${replyReport.replyId}"/></div>
            <div class="apply-table-cell">${replyReport.replyId}</div>
            <div class="apply-table-cell post-title"><a href="#">${replyReport.postTitle || ''}</a></div>
            <div class="apply-table-cell text-reply-content" style="cursor: pointer" 
                 data-content="${replyReport.replyContent || ''}"
                    onclick="openTextReplyModal(this)">
                    ${replyReport.replyContent && replyReport.replyContent.length > 6
                    ? replyReport.replyContent.substring(0, 6) + '...' : replyReport.replyContent || ''}</div>
            <div class="apply-table-cell">${replyReport.profileName || ''}</div>
            <div class="apply-table-cell">${replyReport.replyCreatedDate || ''}</div>
            <div class="apply-table-cell">
                <button class="report-management-btn status report" 
                        style="background-color: ${buttonBackgroundColor};"
                        onclick="openTextReplyReportModal(event)">
                    ${replyReport.reportStatus || '신고'}
                </button>
            </div>
            <div class="apply-table-cell">
                <button class="reasons-report-btn report-content-look-text-reply" 
                    data-name="${replyReport.reportProfileName || ''}" 
                    data-email="${replyReport.reportProfileEmail || ''}" 
                    data-time="${replyReport.reportCreatedDate || ''}" 
                    data-content="${replyReport.reportContents || ''}">
                    보기
                </button>
            </div>
        `;

        container.appendChild(row);
    });

    // 체크박스 이벤트
    textReplyReportCheckboxEvents();

    // 신고내역 보기 모달 이벤트
    setupTextReplyReportDetailsModal();

}

// 페이지네이션
function renderTextReplyReportPagination(pagination) {
    const paginationContainer = document.getElementById("pagination-text-reply-report");
    paginationContainer.innerHTML = '';

    if (!pagination) return;

    const prevButton = document.createElement("li");
    prevButton.className = `pagination-prev ${pagination.page > 1 ? '' : 'disabled'}`;
    prevButton.innerHTML = `<a href="#" class="pagination-prev-link" rel="prev nofollow"><span class="pagination-prev-icon" aria-hidden="true">‹</span></a>`;
    prevButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page > 1) changeTextReplyPage(pagination.page - 1);
    });
    paginationContainer.appendChild(prevButton);

    for (let i = pagination.startPage; i <= Math.min(pagination.endPage, pagination.realEnd); i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `pagination-page ${i === pagination.page ? 'active' : ''}`;
        pageButton.innerHTML = `<a href="#" class="pagination-page-link">${i}</a>`;
        pageButton.addEventListener("click", (e) => {
            e.preventDefault();
            changeTextReplyPage(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    const nextButton = document.createElement("li");
    nextButton.className = `pagination-next ${pagination.page < pagination.realEnd ? '' : 'disabled'}`;
    nextButton.innerHTML = `<a href="#" class="pagination-next-link" rel="next nofollow"><span class="pagination-next-icon" aria-hidden="true">›</span></a>`;
    nextButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page < pagination.realEnd) changeTextReplyPage(pagination.page + 1);
    });
    paginationContainer.appendChild(nextButton);
}

//---------------------------------------------------------------------------------------------------------------

// 영상 모집 신고 관리 목록
function renderVideoAuditionReportList(auditionReports) {
    const container = document.getElementById("video-audition-report-list");
    container.innerHTML = '';

    if (!auditionReports || auditionReports.length === 0) {
        container.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
        return;
    }

    auditionReports.forEach((auditionReports) => {
        const row = document.createElement("div");
        row.className = "apply-table-row";

        let buttonBackgroundColor = "";
        switch (auditionReports.reportStatus) {
            case "DELETE": buttonBackgroundColor = "rgba(41, 153, 41, 0.818)"; break;
            case "HOLD": buttonBackgroundColor = "#ffa600"; break;
            case "NOPROBLEM": buttonBackgroundColor = "rgb(183, 183, 183)"; break;
            default: buttonBackgroundColor = "";
        }

        // 숫자별 분야 화면 업로드
        let videoAuditionFieldText = '';
        switch (auditionReports.auditionField) {
            case 1:
                videoAuditionFieldText = '배우';
                break;
            case 2:
                videoAuditionFieldText = '스텝';
                break;
            case 3:
                videoAuditionFieldText = '감독';
                break;
            case 4:
                videoAuditionFieldText = '기타';
                break;
            default:
                videoAuditionFieldText = '';
        }

        row.innerHTML = `
            <div class="apply-table-cell"><input type="checkbox" class="apply-checkbox" data-id="${auditionReports.postId}"/></div>
            <div class="apply-table-cell">${auditionReports.postId}</div>
            <div class="apply-table-cell post-title">${auditionReports.profileName || ''}</div>
            <div class="apply-table-cell">${auditionReports.createdDate || ''}</div>
            <div class="apply-table-cell">${videoAuditionFieldText}</div>
            <div class="apply-table-cell" style="color: #002fff">
                 <a href="#">
                        ${auditionReports.postTitle && auditionReports.postTitle.length > 7
                        ? auditionReports.postTitle.substring(0, 7) + '...'
                        : auditionReports.postTitle || ''}
                </a>
            </div>
            <div class="apply-table-cell">${auditionReports.applicationCount || ''}</div>
            <div class="apply-table-cell">${auditionReports.auditionDeadline || ''}</div>
            <div class="apply-table-cell">
                <button class="report-management-btn status report" 
                        style="background-color: ${buttonBackgroundColor};"
                        onclick="openVideoAuditionReportModal(event)">
                    ${auditionReports.reportStatus || '신고'}
                </button>
            </div>
            <div class="apply-table-cell">
                <button class="reasons-report-btn report-content-look-video-audition" 
                    data-name="${auditionReports.reportProfileName || ''}" 
                    data-email="${auditionReports.reportProfileEmail || ''}" 
                    data-time="${auditionReports.reportCreatedDate || ''}" 
                    data-content="${auditionReports.reportContents || ''}">
                    보기
                </button>
            </div>
        `;

        container.appendChild(row);
    });

    // 체크박스 이벤트
    videoAuditionReportCheckboxEvents();

    // 신고내역 보기 모달 이벤트
    setupVideoAuditionReportDetailsModal();

}

// 페이지네이션
function renderVideoAuditionReportPagination(pagination) {
    const paginationContainer = document.getElementById("pagination-video-audition-report");
    paginationContainer.innerHTML = '';

    if (!pagination) return;

    const prevButton = document.createElement("li");
    prevButton.className = `pagination-prev ${pagination.page > 1 ? '' : 'disabled'}`;
    prevButton.innerHTML = `<a href="#" class="pagination-prev-link" rel="prev nofollow"><span class="pagination-prev-icon" aria-hidden="true">‹</span></a>`;
    prevButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page > 1) changeVideoAuditionPage(pagination.page - 1);
    });
    paginationContainer.appendChild(prevButton);

    for (let i = pagination.startPage; i <= Math.min(pagination.endPage, pagination.realEnd); i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `pagination-page ${i === pagination.page ? 'active' : ''}`;
        pageButton.innerHTML = `<a href="#" class="pagination-page-link">${i}</a>`;
        pageButton.addEventListener("click", (e) => {
            e.preventDefault();
            changeVideoAuditionPage(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    const nextButton = document.createElement("li");
    nextButton.className = `pagination-next ${pagination.page < pagination.realEnd ? '' : 'disabled'}`;
    nextButton.innerHTML = `<a href="#" class="pagination-next-link" rel="next nofollow"><span class="pagination-next-icon" aria-hidden="true">›</span></a>`;
    nextButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page < pagination.realEnd) changeVideoAuditionPage(pagination.page + 1);
    });
    paginationContainer.appendChild(nextButton);
}

//---------------------------------------------------------------------------------------------------------------

// 글 모집 신고 관리 목록
function renderTextAuditionReportList(auditionReports) {
    const container = document.getElementById("text-audition-report-list");
    container.innerHTML = '';

    if (!auditionReports || auditionReports.length === 0) {
        container.innerHTML = '<p>조회할 데이터가 없습니다.</p>';
        return;
    }

    auditionReports.forEach((auditionReports) => {
        const row = document.createElement("div");
        row.className = "apply-table-row";

        let buttonBackgroundColor = "";
        switch (auditionReports.reportStatus) {
            case "DELETE": buttonBackgroundColor = "rgba(41, 153, 41, 0.818)"; break;
            case "HOLD": buttonBackgroundColor = "#ffa600"; break;
            case "NOPROBLEM": buttonBackgroundColor = "rgb(183, 183, 183)"; break;
            default: buttonBackgroundColor = "";
        }

        // 숫자별 분야 화면 업로드
        let textAuditionFieldText = '';
        switch (auditionReports.auditionField) {
            case 1:
                textAuditionFieldText = '썸네일';
                break;
            case 2:
                textAuditionFieldText = '일러스트';
                break;
            case 3:
                textAuditionFieldText = '웹툰';
                break;
            case 4:
                textAuditionFieldText = '기타';
                break;
            default:
                textAuditionFieldText = '';
        }

        row.innerHTML = `
            <div class="apply-table-cell"><input type="checkbox" class="apply-checkbox" data-id="${auditionReports.postId}"/></div>
            <div class="apply-table-cell">${auditionReports.postId}</div>
            <div class="apply-table-cell post-title">${auditionReports.profileName || ''}</div>
            <div class="apply-table-cell">${auditionReports.createdDate || ''}</div>
            <div class="apply-table-cell">${textAuditionFieldText}</div>
            <div class="apply-table-cell" style="color: #002fff">
                 <a href="#">
                        ${auditionReports.postTitle && auditionReports.postTitle.length > 7
            ? auditionReports.postTitle.substring(0, 7) + '...'
            : auditionReports.postTitle || ''}
                </a>
            </div>
            <div class="apply-table-cell">${auditionReports.applicationCount || ''}</div>
            <div class="apply-table-cell">${auditionReports.auditionDeadline || ''}</div>
            <div class="apply-table-cell">
                <button class="report-management-btn status report" 
                        style="background-color: ${buttonBackgroundColor};"
                        onclick="openTextAuditionReportModal(event)">
                    ${auditionReports.reportStatus || '신고'}
                </button>
            </div>
            <div class="apply-table-cell">
                <button class="reasons-report-btn report-content-look-text-audition" 
                    data-name="${auditionReports.reportProfileName || ''}" 
                    data-email="${auditionReports.reportProfileEmail || ''}" 
                    data-time="${auditionReports.reportCreatedDate || ''}" 
                    data-content="${auditionReports.reportContents || ''}">
                    보기
                </button>
            </div>
        `;

        container.appendChild(row);
    });

    // 체크박스 이벤트
    textAuditionReportCheckboxEvents();

    // 신고내역 보기 모달 이벤트
    setupTextAuditionReportDetailsModal();

}

// 페이지네이션
function renderTextAuditionReportPagination(pagination) {
    const paginationContainer = document.getElementById("pagination-text-audition-report");
    paginationContainer.innerHTML = '';

    if (!pagination) return;

    const prevButton = document.createElement("li");
    prevButton.className = `pagination-prev ${pagination.page > 1 ? '' : 'disabled'}`;
    prevButton.innerHTML = `<a href="#" class="pagination-prev-link" rel="prev nofollow"><span class="pagination-prev-icon" aria-hidden="true">‹</span></a>`;
    prevButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page > 1) changeTextAuditionPage(pagination.page - 1);
    });
    paginationContainer.appendChild(prevButton);

    for (let i = pagination.startPage; i <= Math.min(pagination.endPage, pagination.realEnd); i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `pagination-page ${i === pagination.page ? 'active' : ''}`;
        pageButton.innerHTML = `<a href="#" class="pagination-page-link">${i}</a>`;
        pageButton.addEventListener("click", (e) => {
            e.preventDefault();
            changeTextAuditionPage(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    const nextButton = document.createElement("li");
    nextButton.className = `pagination-next ${pagination.page < pagination.realEnd ? '' : 'disabled'}`;
    nextButton.innerHTML = `<a href="#" class="pagination-next-link" rel="next nofollow"><span class="pagination-next-icon" aria-hidden="true">›</span></a>`;
    nextButton.addEventListener("click", (e) => {
        e.preventDefault();
        if (pagination.page < pagination.realEnd) changeTextAuditionPage(pagination.page + 1);
    });
    paginationContainer.appendChild(nextButton);
}