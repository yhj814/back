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
            <div class="apply-table-cell answer-content" style="display: none">
                ${inquiry.inquiryStatus === "NO" ? "" : inquiry.answerContent || ""}
            </div>
            <div class="apply-table-cell answer-date">
                ${inquiry.inquiryStatus === "NO" ? "" : inquiry.answerDate || ""}
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







