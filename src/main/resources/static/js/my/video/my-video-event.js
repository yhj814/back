
myPageService.getMyFundingList(1, memberId, showMyFundingList);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.page = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.page, memberId, showMyFundingList);
    }
});

myFundingListLayout.addEventListener('click', async (e) => {
        if(e.target.id === "my-funding-buyer-btn") {
            const myFundingPostId = e.target.classList[1];
            console.log("myFundingPostId :",myFundingPostId);

            const fundingBuyerTable  = document.querySelector(`.setting-table.funding-buyer-${myFundingPostId}`);

            fundingBuyerTable.style.display = "block";

            fundingBuyerTable.innerHTML += await myPageService.getFundingBuyerList(myFundingPostId, showFundingBuyerList);

            console.log("1??", fundingBuyerTable);
            console.log("3??", myPageService.getFundingBuyerList(myFundingPostId, showFundingBuyerList));
            console.log("4??", fundingBuyerTable.innerHTML);
        }
});