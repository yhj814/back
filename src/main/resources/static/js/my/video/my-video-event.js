
myPageService.getMyFundingList(1, memberId, showMyFundingList);

myFundingListPaging.addEventListener("click", (e)=>{
    e.preventDefault();
    if(e.target.tagName === "A") {
    globalThis.page = e.target.getAttribute("href");
    myPageService.getMyFundingList(globalThis.page, memberId, showMyFundingList);
    }
});
// const myFundingListToggleButton = document.querySelector(
//     `.my-funding-posts .btn-icon-edit-my.${member.id}`
// );
// console.log(myFundingListToggleButton);

// myFundingListLayout.addEventListener('click', (e) => {
//         if(e.target.id === "getMyFundingBuyer") {
//             alert("눌림")
//         }
// });