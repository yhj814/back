
myPageService.getMyFundingList(1, memberId, showMyFundingList);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.page = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.page, memberId, showMyFundingList);
    }
});


myFundingListLayout.addEventListener('click', async (e) => {
    if(e.target.id === "my-funding-buyer-btn" ) {
        const myFundingPostId = e.target.classList[1];
        const fundingBuyerTable = document.querySelector(`.setting-table.funding-buyer-${myFundingPostId}`);
        const temp = fundingBuyerTable.innerHTML;

        if (// 펀딩 구매자 테이블이 화면에서 숨어있다면
            fundingBuyerTable.style.display === "none"
        ) {     // 펀딩 구매자 테이블의 자식요소의 길이가 1이라면 (=목록이 안 불러졌다면)
            if(fundingBuyerTable.children.length == 1) {
                // 펀딩 구매자 테이블 html 에 목록을 추가해라.
                fundingBuyerTable.innerHTML += await myPageService.getFundingBuyerList(myFundingPostId, showFundingBuyerList);
            }
            // 펀딩 구매자 테이블을 화면에서 보여줘라.
            fundingBuyerTable.style.display = "block";
        } else {
            fundingBuyerTable.style.display = "none";
        }
    }

});

// myFundingListLayout.addEventListener('click', async (e) => {
//     if(e.target.id === "my-funding-buyer-btn" ) {
//         const myFundingPostId = e.target.classList[1];
//         const fundingBuyerTable  = document.querySelector(`.setting-table.funding-buyer-${myFundingPostId}`);
//         const fundingBuyerList = document.querySelector(`funding-${myFundingPostId}`);
//         console.log(fundingBuyerList);
//         const temp = fundingBuyerList.innerHTML =  '';
//
//         fundingBuyerTable.innerHTML += await myPageService.getFundingBuyerList(myFundingPostId, showFundingBuyerList);
//         if (
//             fundingBuyerTable.style.display === "none"
//         ) {
//             fundingBuyerTable.style.display = "block";
//         } else {
//             fundingBuyerTable.style.display = "none";
//             fundingBuyerTable.innerHTML = temp;
//         }
//
//     }
// });