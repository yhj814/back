globalThis.pageA = 1;
myPageService.getMyFundingList(globalThis.pageA, memberId, showMyFundingList);
console.log("내 펀딩 게시물 처음 페이지: ", globalThis.pageA);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.pageA = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.pageA, memberId, showMyFundingList);
        console.log("내 펀딩 게시물 페이지 : ",globalThis.pageA);
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
                globalThis.pageB = 1;
                fundingBuyerTable.innerHTML += await myPageService.getFundingBuyerList(globalThis.pageB, myFundingPostId, showFundingBuyerList);
                console.log("들어옴 : ", fundingBuyerTable.innerHTML);
                const sendButton = document.querySelector(".btn-choice.btn-public");

                fundingBuyerTable.addEventListener('click', async (e) => {
                    e.preventDefault();
                    if(e.target.tagName === 'A') {
                        globalThis.pageB = e.target.getAttribute("href");
                        const fundingBuyerListWrapper = fundingBuyerTable.children[1]
                        const fundingBuyerList = fundingBuyerListWrapper.children[0]
                        fundingBuyerTable.innerHTML = await myPageService.getFundingBuyerList(globalThis.pageB, myFundingPostId, showFundingBuyerList);
                        console.log("내 펀딩 구매자 목록 페이지: ", globalThis.pageB);
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