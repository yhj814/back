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
    // 정렬 기준을 상수로 정의하여 HTML의 필터 ID와 매칭
    ORDER_INQUIRY_CREATED_DATE: "inquiry-created-date",
    ORDER_ANSWER_CREATED_DATE: "inquiry-answer-created-date",
    ORDER_NO_ANSWER: "no-answer-filter",

    // 정렬 기준(order), 검색어, 페이지가 적용된 문의사항 목록 조회
    async fetchInquiries(page = 1, order = this.ORDER_INQUIRY_CREATED_DATE, searchQuery = "") {
        try {
            const response = await fetch(`/admin/inquiries?page=${page}&order=${order}&search=${encodeURIComponent(searchQuery)}`);
            if (!response.ok) throw new Error("문의사항 조회 실패");

            const result = await response.json();
            console.log("정렬 기준 및 검색 결과:", order, searchQuery, result);
            return result;
        } catch (error) {
            console.error("문의사항 조회 중 오류 발생:", error);
            alert("문의사항 목록을 가져오는 중 오류가 발생했습니다.");
            throw error;
        }
    },

    // 답변 등록 (특정 inquiryId에 대한 답변 내용 전송)
    async postAnswer(inquiryId, answerContent) {
        try {
            const response = await fetch(`/admin/inquiries/${inquiryId}/answer`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ answerContent })
            });
            if (!response.ok) throw new Error("답변 등록 실패");

            const result = await response.json();
            console.log("답변 등록 결과:", result);
            return { createdDate: result.createdDate, answerContent };
        } catch (error) {
            console.error("답변 등록 중 오류 발생:", error);
            alert("답변 등록 중 오류가 발생했습니다.");
            throw error;
        }
    },

    // 선택된 문의사항 삭제
    async deleteSelectedInquiries(selectedIds) {
        try {
            const response = await fetch('/admin/inquiry/delete', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(selectedIds)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`문의사항 삭제 실패: ${errorText}`);
            }

            const resultMessage = await response.text();
            console.log("선택된 문의사항 삭제 성공:", selectedIds);
            alert(resultMessage);  // 성공 메시지 알림
            return resultMessage;
        } catch (error) {
            console.error("문의사항 삭제 중 오류 발생:", error);
            alert("문의사항 삭제 중 오류가 발생했습니다. 다시 시도해 주세요.");
            throw error;
        }
    }

};









