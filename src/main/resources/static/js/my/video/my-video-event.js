
myPageService.getMyFundingList(1, memberId, showMyFundingList);
myPageService.getFundingBuyerList( 1, showFundingBuyerList);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.page = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.page, memberId, showMyFundingList);
    }
});

myFundingListLayout.addEventListener('click', (e) => {
        if(e.target.id === "my-funding-buyer-btn") {
            console.log(e.target);
            const myFundingPostId = e.target.classList[1];
            console.log("myFundingPostId :",myFundingPostId);
            // console.log(document.querySelector(`.funding-buyer-${myFundingPostId}`));
            const fundingBuyerTable  = document.querySelector(`.funding-buyer-${myFundingPostId}`);
            console.log(fundingBuyerTable);
            fundingBuyerTable.style.display = "block";
        }
});