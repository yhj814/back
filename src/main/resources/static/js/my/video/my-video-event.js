
myPageService.getMyFundingList(1, memberId, showMyFundingList);

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
            console.log(e.target.classList[1]);
            console.log(e.target.className);
        };
});