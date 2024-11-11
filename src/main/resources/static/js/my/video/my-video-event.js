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

                // 구매자 테이블을 클릭했을 때
                workBuyerTable.addEventListener('click', async (e) => {
                    e.preventDefault();
                    console.log("4 : ", globalThis.myWorkBuyerPage);

                    // 만약 a 태그를 클릭한다면
                    if(e.target.tagName === 'A') {
                        // 클릭한 링크(의 번호)를 페이지 변수?에 담아라
                        globalThis.myWorkBuyerPage = e.target.getAttribute("href");
                        // 구매자 테이블 html 을
                        workBuyerTable.innerHTML = await myPageService.getMyVideoWorkBuyerList(globalThis.myWorkBuyerPage, myWorkPostId, showMyWorkBuyerList);

                        console.log("5 : ", globalThis.myWorkBuyerPage);
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
            // 2. 작품 구매자 테이블의 자식요소[배열]의 길이가 0이 아니라면 (=목록이 불러진 상태라면)
            // 작품 구매자 테이블에 목록을 추가하지 말고 작품 구매자 테이블을 화면에 보여줘라.
            workBuyerTable.style.display = "block";

            console.log("8 : ", globalThis.myWorkBuyerPage);
        } else {// 작품 구매자 테이블이 화면에 나타나있다면
                // 작품 구매자 테이블을 화면에서 숨겨라
            workBuyerTable.style.display = "none";

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
    // 클릭한 타겟이 '결제 내역 삭제' 버튼이라면
    if(e.target.id === "buy-work-delete-btn" ) {
        console.log("들어옴")
        const buyWorkId = e.target.classList[1];
        console.log(buyWorkId)
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

myPageService.getMemberProfile(memberId, showMyProfile);

console.log("myPageService.getMemberProfile(memberId, showMyProfile) : ", myPageService.getMemberProfile(memberId, showMyProfile))