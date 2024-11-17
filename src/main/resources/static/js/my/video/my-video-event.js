// 내 작품
myPageService.getMyVideoWorkList(globalThis.myWorkPage, memberId, showMyWorkList);

myWorkListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
        globalThis.myWorkPage = e.target.getAttribute("href");
        myPageService.getMyVideoWorkList(globalThis.myWorkPage, memberId, showMyWorkList);
    }
});

myWorkListLayout.addEventListener('click', async (e) => {
    // 클릭한 타겟이 '구매한 사람들' 버튼이라면
    if(e.target.id === "my-work-buyer-btn" ) {

        console.log("1번째 : ", globalThis.myWorkBuyerPage);

        // 해당 버튼의 2번째 class 이름을 myWorkPostId 변수에 담아라
        // 즉, DB에 저장된 workId(=postId)번호를 변수에 담아라
        const myWorkPostId = e.target.classList[1];
        // 해당 workId의 구매자 테이블을 변수에 담아라
        const workBuyerTable = document.querySelector(`.work-buyer-${myWorkPostId}`);


        console.log("1번째 workBuyerTable.innerHTML", workBuyerTable.innerHTML);

        if (// 작품 구매자 테이블이 화면에서 숨어있다면
            workBuyerTable.style.display === "none"
        ) {  // 1. 작품 구매자 테이블의 자식요소[배열]의 길이가 0이라면 (=목록이 안 불러졌다면)

            console.log("2 : ", globalThis.myWorkBuyerPage);


            if(workBuyerTable.children.length == 0) {
                // 1. 작품 구매자 테이블 html 에 목록을 추가해라.
                // 시작페이지는 1 page 이다.
                globalThis.myWorkBuyerPage = 1;
                // 작품 구매자 테이블의 html(기존의 빈 테이블 안에)에 추가하라. fetch 한 myPageService의 getList( 1페이지, workId, 레이아웃 ) 내용을.???????
                workBuyerTable.innerHTML = await myPageService.getMyVideoWorkBuyerList(globalThis.myWorkBuyerPage, myWorkPostId, showMyWorkBuyerList);

                console.log("3 : ", globalThis.myWorkBuyerPage);

                let isRequestInProgress = false; // 상태 변수 추가

                workBuyerTable.addEventListener('click', async (e) => {
                    e.preventDefault();

                    if (isRequestInProgress) {
                        console.log("요청 진행 중입니다. 대기하세요.");
                        return; // 요청 진행 중이면 무시
                    }

                    if(e.target.tagName === 'A') {
                        globalThis.myWorkBuyerPage = e.target.getAttribute("href");
                        workBuyerTable.innerHTML = await myPageService.getMyVideoWorkBuyerList(globalThis.myWorkBuyerPage, myWorkPostId, showMyWorkBuyerList);

                        console.log("5 : ", globalThis.myWorkBuyerPage);
                    }

                    if (e.target.classList[1] === "btn-public" || e.target.classList[1] === "btn-secret") {

                        isRequestInProgress = true; // 요청 상태 시작
                        try {
                            const buyWorkId = e.target.classList[2];
                            const sendStatus = e.target.classList[1] === "btn-public" ? "YES" : "NO";
                            const sibling = e.target.classList[1] === "btn-public"
                                ? e.target.nextElementSibling
                                : e.target.previousElementSibling;

                            if (sibling?.classList.contains("active")) {
                                e.target.classList.add("active");
                                sibling.classList.remove("active");
                                const response = await myPageService.updateWorkSendStatus({
                                    id: buyWorkId,
                                    workSendStatus: sendStatus
                                });

                                if (response?.success) {
                                    console.log("DB 업데이트 성공");
                                } else {
                                    console.error("DB 업데이트 실패");
                                }
                            }
                        } catch (error) {
                            console.error("요청 중 오류 발생:", error);
                        } finally {
                            isRequestInProgress = false; // 요청 상태 초기화
                        }
                    }

                    // if(e.target.classList[1] === "btn-public") {
                    //     const buyWorkId = e.target.classList[2];
                    //     const sendYes = "YES";
                    //
                    //     if(e.target.nextElementSibling.classList[2] === "active" ||
                    //         e.target.nextElementSibling.classList[3] === "active") {
                    //         e.target.classList.add("active");
                    //         e.target.nextElementSibling.classList.remove("active")
                    //         try {
                    //             const response = await myPageService.updateWorkSendStatus({
                    //                 id: buyWorkId, workSendStatus: sendYes
                    //             });
                    //             if (response.success) {
                    //                 console.log("DB 업데이트 성공 - YES");
                    //             } else {
                    //                 console.error("DB 업데이트 실패 - YES");
                    //             }
                    //         } catch (error) {
                    //             console.error("요청 중 오류 발생 - YES :", error);
                    //         }
                    //     }
                    // } else if(e.target.classList[1] === "btn-secret") {
                    //     const buyWorkId = e.target.classList[2];
                    //     const sendNo = "NO";
                    //
                    //     if(e.target.previousElementSibling .classList[2] === "active" ||
                    //         e.target.previousElementSibling .classList[3] === "active") {
                    //         e.target.classList.add("active");
                    //         e.target.previousElementSibling.classList.remove("active")
                    //         try {
                    //             const response = await myPageService.updateWorkSendStatus({
                    //             id: buyWorkId, workSendStatus: sendNo
                    //         });
                    //             if (response.success) {
                    //                 console.log("DB 업데이트 성공 - NO");
                    //             } else {
                    //                 console.error("DB 업데이트 실패 - NO");
                    //             }
                    //         } catch (error) {
                    //             console.error("요청 중 오류 발생 - NO :", error);
                    //         }
                    //     }
                    // }
                });
            }
            // 2. 작품 구매자 테이블의 자식요소[배열]의 길이가 0이 아니라면 (=목록이 불러진 상태라면)
            // 작품 구매자 테이블에 목록을 추가하지 말고 작품 구매자 테이블을 화면에 보여줘라.
            workBuyerTable.style.display = "block";

            console.log("8 : ", globalThis.myWorkBuyerPage);
        } else {// 작품 구매자 테이블이 화면에 나타나있다면
                // 작품 구매자 테이블을 화면에서 숨겨라
            workBuyerTable.style.display = "none";
            globalThis.myWorkBuyerPage = 1;
            workBuyerTable.innerHTML = await myPageService.getMyVideoWorkBuyerList(globalThis.myWorkBuyerPage, myWorkPostId, showMyWorkBuyerList);

            console.log("9 : ", globalThis.myWorkBuyerPage);
        }
    }

});

// 구매한 작품
myPageService.getMyBuyVideoWorkList(globalThis.myBuyWorkPage, memberId, showMyBuyWorkList);

myBuyWorkListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
        globalThis.myBuyWorkPage = e.target.getAttribute("href");
        myPageService.getMyBuyVideoWorkList(globalThis.myBuyWorkPage, memberId, showMyBuyWorkList);
    }
});

// 구매한 작품 결제 내역 삭제
myBuyWorkListLayout.addEventListener('click', async (e) => {
    const deleteModal = document.querySelector(".delete-modal")

    // 구매내역 삭제 모달창 열기
    if(e.target.id === "delete-modal-open-btn" ) {
        deleteModal.style.display = "flex";
    }

    // 구매내역 삭제 모달창 닫기
    if(e.target.classList[0] === "delete-modal-close-btn"){
        console.log("close-btn : ", e.target)
        deleteModal.style.display = "none";
    }

    // 구매내역 삭제 버튼 눌렀을 때, 구매 내역삭제
    if (e.target.classList[1] === "btn-test-1") {
        const buyWorkId = e.target.classList[0];
        await myPageService.removeBuyWorkPost(buyWorkId);
        await myPageService.getMyBuyVideoWorkList(globalThis.myBuyWorkPage, memberId, showMyBuyWorkList);
    }
    // 구매내역 삭제 버튼 눌렀을 때, 구매 내역삭제
    if (e.target.classList[1] === "btn-test-2") {
        const buyWorkId = e.target.classList[0];
        await myPageService.removeBuyWorkPost(buyWorkId);
        await myPageService.getMyBuyVideoWorkList(globalThis.myBuyWorkPage, memberId, showMyBuyWorkList);
    }
});

// 내 펀딩
myPageService.getMyFundingList(globalThis.myFundingPage, memberId, showMyFundingList);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.myFundingPage = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.myFundingPage, memberId, showMyFundingList);
    }
});

myFundingListLayout.addEventListener('click', async (e) => {
    if(e.target.id === "my-funding-buyer-btn" ) {
        const myFundingPostId = e.target.classList[1];
        const fundingBuyerTable = document.querySelector(`.funding-buyer-${myFundingPostId}`);

        if (// 펀딩 구매자 테이블이 화면에서 숨어있다면
            fundingBuyerTable.style.display === "none"
        ) {     // 1. 펀딩 구매자 테이블의 자식요소[배열]의 길이가 1이라면 (=목록이 안 불러졌다면)
                // 2. 펀딩 구매자 테이블의 자식요소[배열]의 길이가 1이 아니라면 (=목록이 불러진 상태라면)
            console.log("펀딩구매자테이블의 자식요소 길이 : ",fundingBuyerTable.children.length)
            if(fundingBuyerTable.children.length == 0) {
                // 1. 펀딩 구매자 테이블 html 에 목록을 추가해라.
                globalThis.myFundingBuyerPage = 1;
                fundingBuyerTable.innerHTML += await myPageService.getFundingBuyerList(globalThis.myFundingBuyerPage, myFundingPostId, showFundingBuyerList);

                fundingBuyerTable.addEventListener('click', async (e) => {
                    e.preventDefault();
                    if(e.target.tagName === 'A') {
                        globalThis.myFundingBuyerPage = e.target.getAttribute("href");
                        fundingBuyerTable.innerHTML = await myPageService.getFundingBuyerList(globalThis.myFundingBuyerPage, myFundingPostId, showFundingBuyerList);
                    }

                    if(e.target.classList[1] === "btn-public") {
                        const buyFundingProductId = e.target.classList[2];
                        const sendYes = "YES";

                        if(e.target.nextElementSibling.classList[2] === "active") {
                            e.target.classList.add("active");
                            e.target.nextElementSibling.classList.remove("active")
                            await myPageService.updateFundingSendStatus({
                                id: buyFundingProductId,
                                fundingSendStatus: sendYes
                            });
                        }
                    }
                });
            }
            // 2. 펀딩 구매자 테이블에 목록을 추가하지 말고 펀딩 구매자 테이블을 화면에 보여줘라.
            // 펀딩 구매자 테이블을 화면에서 보여줘라.
            fundingBuyerTable.style.display = "block";
        } else {
            fundingBuyerTable.style.display = "none";
        }
    }

});

// 결제한 펀딩
myPageService.getMyBuyFundingList(globalThis.myBuyFundingPage, memberId, showMyBuyFundingList);

myBuyFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
        globalThis.myBuyFundingPage = e.target.getAttribute("href");
        myPageService.getMyBuyFundingList(globalThis.myBuyFundingPage, memberId, showMyBuyFundingList);
    }
});

// 나의 모집
globalThis.myAuditionPage = 1;
myPageService.getMyVideoAuditionList(globalThis.myAuditionPage, memberId, showMyAuditionList);

myAuditionListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
        globalThis.myAuditionPage = e.target.getAttribute("href");
        myPageService.getMyVideoAuditionList(globalThis.myAuditionPage, memberId, showMyAuditionList);
    }
});

// 나의 모집 지원자 목록
myAuditionListLayout.addEventListener('click', async (e) => {

    if(e.target.id === "my-audition-applicant-btn" ) {
        const myAuditionId = e.target.classList[1];
        const auditionApplicantTable = document.querySelector(`.audition-applicant-${myAuditionId}`);

        if (
            auditionApplicantTable.style.display === "none"
        ) {
            if(auditionApplicantTable.children.length == 0) {
                globalThis.myAuditionApplicantPage = 1;
                auditionApplicantTable.innerHTML = await myPageService.getMyVideoAuditionApplicantList(globalThis.myAuditionApplicantPage, myAuditionId, showMyAuditionApplicantList);

                auditionApplicantTable.addEventListener('click', async (e) => {
                    e.preventDefault();
                    if(e.target.tagName === 'A') {
                        globalThis.myAuditionApplicantPage = e.target.getAttribute("href");
                        auditionApplicantTable.innerHTML = await myPageService.getMyVideoAuditionApplicantList(globalThis.myAuditionApplicantPage, myAuditionId, showMyAuditionApplicantList);
                    }

                    if(e.target.classList[1] === "btn-public") {
                        const auditionApplicantId = e.target.classList[2];
                        const confirmOk = "YES";

                        if(e.target.nextElementSibling.classList[2] === "active" ||
                            e.target.nextElementSibling.classList[3] === "active") {
                            e.target.classList.add("active");
                            e.target.nextElementSibling.classList.remove("active")
                            await myPageService.updateConfirmStatus({
                                id: auditionApplicantId,
                                confirmStatus : confirmOk
                            });
                        }
                    }

                    if(e.target.classList[1] === "btn-secret") {
                        const auditionApplicantId = e.target.classList[2];
                        const confirmNo = "NO";

                        if(e.target.previousElementSibling .classList[2] === "active"  ||
                            e.target.previousElementSibling .classList[3] === "active") {
                            e.target.classList.add("active");
                            e.target.previousElementSibling.classList.remove("active")
                            await myPageService.updateConfirmStatus({
                                id: auditionApplicantId,
                                confirmStatus: confirmNo
                            });
                        }
                    }

                    // if (e.target.id === "resume-open") {
                    //     const auditionApplicationId = e.target.classList[1];
                    //     window.open(`/audition/video/application/${auditionApplicationId}`, "PopupWin", "width=500,height=600");
                    // }

                });
            }
            auditionApplicantTable.style.display = "block";
        } else {
            auditionApplicantTable.style.display = "none";
            globalThis.myAuditionApplicantPage = 1;
            auditionApplicantTable.innerHTML = await myPageService.getMyVideoAuditionApplicantList(globalThis.myAuditionApplicantPage, myAuditionId, showMyAuditionApplicantList);
        }
    }

});

// 내가 신청한 모집
globalThis.myApplicationAuditionPage = 1;
myPageService.getMyVideoApplicationAuditionList(globalThis.myApplicationAuditionPage, memberId, showMyApplicationAuditionList);

myApplicationAuditionListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
        globalThis.myApplicationAuditionPage = e.target.getAttribute("href");
        myPageService.getMyVideoApplicationAuditionList(globalThis.myApplicationAuditionPage, memberId, showMyApplicationAuditionList);
    }
});

// 문의 내역
myPageService.getMyInquiryHistoryList(globalThis.myInquiryHistoryPage, memberId, showMyInquiryHistoryList);

myInquiryHistoryListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
        globalThis.myInquiryHistoryPage = e.target.getAttribute("href");
        myPageService.getMyInquiryHistoryList(globalThis.myInquiryHistoryPage, memberId, showMyInquiryHistoryList)
    }
});

myInquiryHistoryListLayout.addEventListener('click', async (e) => {
    if(e.target.id === "admin-answer-btn" ) {
        const myInquiryId = e.target.classList[1];
        console.log("event-e.target: ",e.target);
        const adminAnswer = document.querySelector(`.inquiry-${myInquiryId}`);

        if (
            adminAnswer.style.display === "none"
        ) {
            console.log("펀딩구매자테이블의 자식요소 길이 : ",adminAnswer.children.length)
            if(adminAnswer.children.length == 0) {
                adminAnswer.innerHTML += await myPageService.getAdminAnswerByInquiryId(myInquiryId, showAdminAnswer)
                console.log("event-myInquiryId: ",myInquiryId);
                console.log("event-adminAnswer: ",adminAnswer);
            }
            adminAnswer.style.display = "block";
        } else {
            adminAnswer.style.display = "none";
        }
    }
});

// 내 정보
myPageService.getMemberProfileByMemberId(memberId, showMyProfile);

myProfileLayout.addEventListener('click', async (e) => {
    if(e.target.id === 'btn-update') {
        e.preventDefault();

        let isValid = true;
        let selectedProfileGender = null;

        const memberId = e.target.classList[3];
        const InputProfileName = document.querySelector("input[name=profileName]");
        const InputProfileNickName = document.querySelector("input[name=profileNickName]");
        const InputProfileAge = document.querySelector("input[name=profileAge]");
        const textareaProfileEtc = document.querySelector("textarea[name=profileEtc]");

        const profileGenderMan = document.getElementById('gender-1');
        const profileGenderWoman = document.getElementById('gender-2');

        if (profileGenderMan.checked) {
            selectedProfileGender = profileGenderMan.value; // profileGenderMan radio 버튼 선택 시
        } else if (profileGenderWoman.checked) {
            selectedProfileGender = profileGenderWoman.value; // profileGenderWoman radio 버튼 선택 시
        }

        // 기타 필드 유효성 검사 (인증번호 필드는 제외)
        document.querySelectorAll(".form-control:not(#emailVerificationCode):not(#verificationCode)").forEach((input) => {
            const errorMessage = input.closest(".input-gap")
                ? input.closest(".input-gap").parentElement.querySelector(".error-message")
                : input.parentElement.querySelector(".error-message");

            if (!input.value.trim() && !input.matches(".temp-upload-resume") && !input.matches("textarea")) {
                input.classList.add("error");
                if (errorMessage) {
                    errorMessage.style.display = "block";
                }
                isValid = false;
            } else {
                input.classList.remove("error");
                if (errorMessage) {
                    errorMessage.style.display = "none";
                }
            }
        });

        // 폼 제출 처리
        if (isValid) {
            alert("회원정보가 수정되었습니다.")
            await myPageService.updateMemberProfileByMemberId({
                memberId: memberId, profileName: InputProfileName.value,
                profileNickName: InputProfileNickName.value, profileGender: selectedProfileGender,
                profileAge: InputProfileAge.value, profileEtc: textareaProfileEtc.value
            });
            await myPageService.getMemberProfileByMemberId(memberId, showMyProfile);
        } else {
            alert("예시 형식에 맞게 필수 항목을 모두 작성해 주세요.")
        }
    }

    if(e.target.id === 'btn-cancle') {
        const memberId = e.target.classList[3];
        const originalProfileName = document.querySelector(".original-profileName");
        const originalProfileNickName = document.querySelector(".original-profileNickName");
        const originalProfileGender = document.querySelector(".original-profileGender");
        const originalProfileAge = document.querySelector(".original-profileAge");
        const originalProfileEtc = document.querySelector(".original-profileEtc");

        console.log(originalProfileName.innerText);
        alert("회원정보 수정이 취소되었습니다.")

        await myPageService.updateMemberProfileByMemberId({
            memberId: memberId, profileName: originalProfileName.innerText,
            profileNickName: originalProfileNickName.innerText, profileGender: originalProfileGender.innerText,
            profileAge: originalProfileAge.innerText, profileEtc: originalProfileEtc.innerText
        });
        await myPageService.getMemberProfileByMemberId(memberId, showMyProfile);
    }
})
globalThis.MyAlarmPage = 1
myPageService.getMyAlarmsByMemberProfileId(MyAlarmPage, (data) => {
    renderNotificationHeader();
    showMyNotification(data);
});

// 새로고침 버튼 클릭 이벤트 처리
myNotificationListLayout.addEventListener('click', (e) => {
    if (e.target.classList.contains('refresh-btn') || e.target.classList.contains('refresh-img')) {

    }
});

function refresh() { // genreType을 빈 문자열로 설정
    const url = `/alarm/video/my-page`;
    window.location.href = url;
}

moreButton.addEventListener("click", (e) => {
    myPageService.getMyAlarmsByMemberProfileId(++globalThis.MyAlarmPage, (data) => {
        renderNotificationHeader();
        showMyNotification(data);
    });
});