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
    if(e.target.id === "my-work-buyer-btn" ) {
        const myWorkPostId = e.target.classList[1];
        const workBuyerTable = document.querySelector(`.work-buyer-${myWorkPostId}`);

        if (// 작품 구매자 테이블이 화면에서 숨어있다면
            workBuyerTable.style.display === "none"
        ) {     // 1. 작품 구매자 테이블의 자식요소[배열]의 길이가 0이라면 (=목록이 안 불러졌다면)
            // 2. 작품 구매자 테이블의 자식요소[배열]의 길이가 0이 아니라면 (=목록이 불러진 상태라면)
            console.log("펀딩구매자테이블의 자식요소 길이 : ",workBuyerTable.children.length)
            if(workBuyerTable.children.length == 0) {
                // 1. 펀딩 구매자 테이블 html 에 목록을 추가해라.
                globalThis.myWorkBuyerPage = 1;
                workBuyerTable.innerHTML += await myPageService.getMyVideoWorkBuyerList(globalThis.myWorkBuyerPage, myWorkPostId, showMyWorkBuyerList);

                workBuyerTable.addEventListener('click', async (e) => {
                    e.preventDefault();
                    if(e.target.tagName === 'A') {
                        globalThis.myWorkBuyerPage = e.target.getAttribute("href");
                        workBuyerTable.innerHTML = await myPageService.getMyVideoWorkBuyerList(globalThis.myWorkBuyerPage, myWorkPostId, showMyWorkBuyerList);
                    }

                    if(e.target.classList[1] === "btn-public") {
                        const buyWorkId = e.target.classList[2];
                        const sendYes = "YES";

                        if(e.target.nextElementSibling.classList[2] === "active") {
                            e.target.classList.add("active");
                            e.target.nextElementSibling.classList.remove("active")
                            await myPageService.updateWorkSendStatus({
                                id: buyWorkId, workSendStatus: sendYes
                            });
                        }
                    }
                });
            }
            // 2. 펀딩 구매자 테이블에 목록을 추가하지 말고 펀딩 구매자 테이블을 화면에 보여줘라.
            // 펀딩 구매자 테이블을 화면에서 보여줘라.
            workBuyerTable.style.display = "block";
        } else {
            workBuyerTable.style.display = "none";
        }
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