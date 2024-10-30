document.addEventListener("DOMContentLoaded", () => {
    console.log("DOMContentLoaded 이벤트 호출됨");

    const workId = document.querySelector(".layout")?.getAttribute("data-work-id");
    console.log("workId 확인:", workId);

    if (!workId) {
        console.error("workId가 설정되지 않았습니다.");
        return;
    }


    const paginationContainer = document.getElementById("pagination");

    // 댓글 목록 및 페이지네이션 표시 함수
    const showList = ({ replies, pagination }) => {
        let text = ``;
        let pagingText = ``;


        replies.forEach((reply) => {
            text += `
            <div class="comment-group">
                <div class="comment-box">
                    <div class="comment-wrapper">
                        <div class="write-user">
                            <img src="https://yozm.wishket.com/static/img/default_avatar.png" class="comment-writer-img" />
                            <div class="comment-writer-name">
                                <div class="writer-name">${reply.profileNickname}</div>
                                <div class="btn-comment-action">
                                    <button class="btn-comment-delete cursor" onclick="deleteReply(${reply.id})">삭제</button>
                                    <img src="/images/video/report.jpg" />
                                    <button class="btn-comment-report cursor">신고</button>
                                </div>
                            </div>
                        </div>
                        <div class="comment">
                            <a class="comment-content">${reply.replyContent}</a>
                        </div>
                        <div class="comment-date-created">
                            ${reply.createDate}
                            <div class="comment-review">
                                <div class="review-box">
                                    <div class="star-box">
                                        <img src="https://www.wishket.com/static/renewal/img/project/wishket-project-portfolio/icon-review-star-full.png" />
                                    </div>
                                    <span class="review-score">${reply.star}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        });

        document.getElementById("comment-row").innerHTML = text;

        // 페이지네이션 요소 및 데이터 유효성 검사
        if (paginationContainer && pagination && Object.keys(pagination).length) {
            console.log("pagination 요소와 데이터가 유효합니다.");

            // 이전 버튼
            if (pagination.startPage > 1) {
                pagingText += `<li class="page-item prev"><a class="page-link" href="javascript:loadReplies(${pagination.startPage - 1})">  </a></li>`;
            }

            // 페이지 번호
            for (let i = pagination.startPage; i <= pagination.endPage; i++) {
                if (pagination.page === i) {
                    pagingText += `<li class="page-item page-link" style="color: #00a878">${i}</li>`;
                } else {
                    pagingText += `<li class="page-item"><a class="page-link" href="javascript:loadReplies(${i})">${i}</a></li>`;
                }
            }

            // 다음 버튼
            if (replies.length === pagination.rowCount && pagination.endPage < pagination.realEnd) {
                pagingText += `<li class="page-item next"><a class="page-link" href="javascript:loadReplies(${pagination.endPage + 1})">  </a></li>`;
            }

            paginationContainer.innerHTML = pagingText;
        } else {
            console.warn("pagination 요소를 찾을 수 없거나 pagination 데이터가 유효하지 않습니다.");
            paginationContainer.innerHTML = "";  // pagination 데이터가 없을 때 비우기
        }
    };
    //댓글 갯수 구하기. 맨 위에 있어야함
    const updateReplyCount = async (workId) => {
        try {
            // replyService를 통해 댓글 총 개수 조회
            const totalCount = await replyService.getReplyCount(workId);

            console.log("총 댓글 수:", totalCount); // 로그로 확인

            // `total-reply-count` 요소에 댓글 수 업데이트
            const totalReplyCountElement = document.getElementById("total-reply-count");
            if (totalReplyCountElement) {
                totalReplyCountElement.textContent = totalCount;
            } else {
                console.error("`total-reply-count` 요소를 찾을 수 없습니다.");
            }

            // `comment-count` 요소에 댓글 수 업데이트
            const commentCountElement = document.getElementById("comment-count");
            if (commentCountElement) {
                commentCountElement.textContent = totalCount;
            } else {
                console.error("`comment-count` 요소를 찾을 수 없습니다.");
            }
        } catch (error) {
            console.error("댓글 수를 불러오는 중 오류 발생:", error);
        }
    };
    updateReplyCount(workId);

    // 댓글 불러오기 함수
    const loadReplies = (page = 1) => {
        replyService.getList(page, workId, (data) => {
            const commentContainer = document.getElementById("comment-row");
            if (!commentContainer) {
                console.error("comment-row 요소가 존재하지 않습니다.");
                return;
            }

            if (data && data.pagination) {
                showList(data); // 댓글 목록과 페이지네이션 표시

                // 댓글 총합 개수 설정
                const totalReplyCountElement = document.getElementById("total-reply-count");
                const totalCount = data.totalCount || data.pagination.totalCount || 0; // 가능한 경로 확인

                console.log("총 댓글 개수:", totalCount); // 테스트 출력

                if (totalReplyCountElement) {
                    totalReplyCountElement.textContent = totalCount;
                    updateReplyCount(workId); // 댓글 총합 개수 업데이트
                } else {
                    console.error("total-reply-count 요소를 찾을 수 없습니다.");
                }

                // 신고 버튼 이벤트 추가
                addReportEvent();
            } else {
                console.warn("pagination 데이터가 없어 showList를 호출하지 않습니다.");
            }
        });
    };

    window.loadReplies = loadReplies;

    // 댓글 작성 이벤트
    document.querySelector(".btn-create-comment").addEventListener("click", async (event) => {
        event.preventDefault();

        const replyContent = document.getElementById("comment").value;

        if (!replyContent) return alert("댓글 내용을 입력하세요.");
        if (!selectedRating) return alert("별점을 선택하세요.");

        const replyData = { workId, replyContent, star: selectedRating };
        console.log("작성할 댓글 데이터:", replyData);

        await replyService.write(replyData);
        document.getElementById("comment").value = "";

        // 별점 초기화
        selectedRating = null;
        document.querySelectorAll(".star-rating .star").forEach((star) => {
            star.classList.remove("selected");
        });
        loadReplies();
        updateReplyCount(workId);
    });

    // 댓글 삭제 이벤트
    window.deleteReply = async (replyId) => {
        const success = await replyService.remove(replyId);

        // 성공적으로 삭제된 경우 댓글 목록 다시 로드
        if (success) {
            loadReplies();
            updateReplyCount(workId);// 새로고침 없이 댓글 목록을 다시 불러옵니다.
        } else {
            alert("댓글 삭제에 실패했습니다.");
        }
    };


    // 신고 버튼 이벤트 추가 함수
    const addReportEvent = () => {
        document.querySelectorAll(".btn-comment-report").forEach((btn) => {
            btn.addEventListener("click", () => {
                const replyModal = document.getElementById("report-modal-reply");
                if (replyModal) replyModal.style.display = "block";
            });
        });
    };

    let selectedRating = null;

    // 별점 클릭 이벤트
    document.querySelectorAll(".star-rating .star").forEach(function (star) {
        star.addEventListener("click", function () {
            selectedRating = parseInt(this.getAttribute("data-value"));

            // 모든 별의 선택 상태 초기화
            document.querySelectorAll(".star-rating .star").forEach(function (s) {
                s.classList.remove("selected");
            });

            // 클릭한 별과 그 이전 별들만 선택 상태로 변경
            for (let i = 1; i <= selectedRating; i++) {
                document.querySelector(`.star-rating .star[data-value="${i}"]`).classList.add("selected");
            }

            console.log("Selected rating:", selectedRating);
        });

        // 별점 마우스 오버 이벤트
        star.addEventListener("mouseover", function () {
            document.querySelectorAll(".star-rating .star").forEach(function (s) {
                s.classList.remove("hover");
            });
            this.classList.add("hover");
            let nextSiblings = getNextSiblings(this);
            nextSiblings.forEach(function (sibling) {
                sibling.classList.add("hover");
            });
        });

        // 별점 마우스 아웃 이벤트
        star.addEventListener("mouseout", function () {
            document.querySelectorAll(".star-rating .star").forEach(function (s) {
                s.classList.remove("hover");
            });
        });
    });

    // 클릭한 별의 이전 모든 별에 대한 `hover` 상태 관리
    function getNextSiblings(elem) {
        let siblings = [];
        siblings.push(elem);
        while ((elem = elem.previousElementSibling)) {
            siblings.push(elem);
        }
        return siblings;
    }

    loadReplies();
});