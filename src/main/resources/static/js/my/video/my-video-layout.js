const myFundingListLayout = document.getElementById("my-funding-list");
const myFundingListPaging = document.getElementById("my-funding-list-paging");

console.log("5 : ", myFundingListLayout);
console.log("6 : ", myFundingListPaging);

const showMyFundingList = ({myFundingPosts, myPagePagination}) => {
    let text = ``;
    let pagingText = ``;

    console.log("7 : ", showMyFundingList);
    console.log("8 : ", myFundingPosts);
    console.log("9 : ", myPagePagination);

    myFundingPosts.length;
    console.log("9.1 : ", myFundingPosts.length);

    myFundingPosts.forEach((myFundingPost, index) => {
        console.log("10 : ", index, myPagePagination);

        text += `
         <div class="list-item my-funding-posts">
            <div class="products-list">
                <div class="flex-box">
                    <div class="products-text">
                        <a
                        ><p
                                class="my-products-title"
                        >
                            ${myFundingPost.postTitle}
                        </p></a
                        >
                        <div
                                class="my-products-info"
                        >
                            <a
                            ><p
                                    class="btn smooth my-products-category"
                            >
                                ${myFundingPost.genreType}
                            </p></a
                            >
                            <div
                                    class="divider"
                            ></div>
                            <div class="flex-box">
                                <img
                                        class="time"
                                        src="/images/member/clock.png"
                                />
                                <div
                                        class="timeandcontent smooth"
                                >
                                   ${timeForToday(myFundingPost.createdDate)}
                                </div>
                            </div>
                        </div>
                        <a
                        ><p
                                class="timeandcontent content products-description"
                        >
                            ${myFundingPost.postContent}
                        </p></a
                        >
                    </div>
                    <div class="btn products-image">
                        <a
                            ><img
                                src="/images/member/thumnail.png"
                        /></a>
                    </div>
                </div>
                <div
                    class="flex-box products-author-box"
                >
                    <div
                        class="author-info flex-box"
                    >
                        <img
                            class="author-image"
                            src="/images/member/member-image.jpg"
                        />
                        <p class="author-name">
                            ${myFundingPost.profileNickname}
                        </p>
                    </div>
                    <div class="flex-box">
                        <div class="btn-wrapper">
                            <button
                                class="btn btn-action btn-icon-edit-my"
                                name="toggle_btn"
                                type="button"
                            >
                                <p
                                    class="action-tooltip bottom-action edit-my-off"
                                >
                                    구매한 사람들
                                </p>
                                <div
                                    class="edit-my-off"
                                >
                                    <div
                                        class="icon-my-edit-off"
                                    ></div>
                                </div>
                                <div
                                    class="edit-my-on"
                                    style="
                                        display: none;
                                    "
                                >
                                    <div
                                        class="icon-my-edit-on"
                                    ></div>
                                </div>
                            </button>
                        </div>
                        <div class="btn-wrapper">
                            <button
                                class="btn btn-action icon-delete-read-products"
                                type="button"
                            >
                                <div
                                    class="icon-my-delete"
                                ></div>
                                <p
                                    class="action-tooltip bottom-action"
                                >
                                    수정
                                </p>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="setting-table" style="border-top: 1px solid rgb(224, 224, 224); display: none;">
<!--                settin-table -->
                </div>
            </div>
        </div>
            `;
    });
    myFundingListLayout.innerHTML = text;

    console.log("12 : ", myFundingListLayout);


    if(myPagePagination.prev){
        pagingText += `
            <li class="page-item">
                <a href="${myPagePagination.startPage - 1}" class="page-link" id="back" style="bottom: -15px"></a>
            </li>
        `
    }
    for(let i=myPagePagination.startPage; i<=myPagePagination.endPage; i++){
        if(myPagePagination.page === i){
            pagingText += `<li class="page-item"><div class="page-link active">${i}</div></li>`
        }else{
            pagingText += `<li class="page-item"><a href="${i}" class="page-link">${i}</a></li>`
        }
    }

    if(myPagePagination.next) {
        pagingText += `
            <li class="page-item">
                <a href="${myPagePagination.endPage + 1}" class="page-link" style="bottom: -15px"></a>
            </li>
        `
    }

    myFundingListPaging.innerHTML = pagingText;
}

function timeForToday(datetime) {
    const today = new Date();
    const date = new Date(datetime);

    let gap = Math.floor((today.getTime() - date.getTime()) / 1000 / 60);

    if (gap < 1) {
        return "방금 전";
    }

    if (gap < 60) {
        return `${gap}분 전`;
    }

    gap = Math.floor(gap / 60);

    if (gap < 24) {
        return `${gap}시간 전`;
    }

    gap = Math.floor(gap / 24);

    if (gap < 31) {
        return `${gap}일 전`;
    }

    gap = Math.floor(gap / 31);

    if (gap < 12) {
        return `${gap}개월 전`;
    }

    gap = Math.floor(gap / 12);

    return `${gap}년 전`;
}


// const showFundingBuyerList = (members) => {
//     let text = ``;
//
//     members.forEach((member) => {
//         text += `
//             `;
//     });
//     myFundingListLayout.innerHTML += text;
//
// }
