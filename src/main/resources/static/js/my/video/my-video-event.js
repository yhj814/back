
myPageService.getMyFundingList(1, memberId, showMyFundingList);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.page = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.page, memberId, showMyFundingList);
    }
});

myFundingListLayout.addEventListener('click', (e) => {
    // console.log("4: ", myFundingListLayout);
    console.log("5: ", e.target);
    // console.log("6: ", e.target.id === "my-funding-Buyer-button");
    //     if(e.target.id === "my-funding-Buyer-button") {
    //         console.log("id: 들어옴");
    //         console.log(e.target.classList);
    //     };

    // if(e.target.className === "my-funding-Buyer-button") {
    //     console.log("className: 들어옴");
    // };
});