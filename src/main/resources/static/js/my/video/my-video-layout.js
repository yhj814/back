const myFundingListLayout = document.getElementById("my-funding-list");
const myFundingListPaging = document.getElementById("my-funding-list-paging");

const showMyFundingList = ({myFundingPosts, workAndFundingPagination}) => {
    let text = ``;
    let pagingText = ``;

    myFundingPosts.forEach((myFundingPost) => {

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
                                        id="my-funding-buyer-btn"
                                        class="icon-my-edit-off ${myFundingPost.id}"
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
                <div class="setting-table funding-buyer-${myFundingPost.id}" style="border-top: 1px solid rgb(224, 224, 224); display: none;">
                 </div>    
            </div>
        </div>
            `;
    });
    myFundingListLayout.innerHTML = text;


    if(workAndFundingPagination.prev){
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
        console.log("펀딩 게시물 목록/이전 : ", workAndFundingPagination.prev);
        console.log("펀딩 게시물 목록/이전 : ", pagingText);
    }
    for(let i=workAndFundingPagination.startPage; i<=workAndFundingPagination.endPage; i++){
        if(workAndFundingPagination.page === i){
            pagingText += `<li class="page-item"><div class="page-link active">${i}</div></li>`
        }else{
            pagingText += `<li class="page-item"><a href="${i}" class="page-link">${i}</a></li>`
        }
    }

    if(workAndFundingPagination.next) {
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.endPage + 1}" class="page-link next"></a>
            </li>
        `
        console.log("펀딩 게시물 목록/다음 : ", workAndFundingPagination.next);
        console.log("펀딩 게시물 목록/다음 : ", pagingText);
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


const showFundingBuyerList = ({myFundingBuyers, settingTablePagination}) => {

    let text = `<div class="setting-th">
                                <div class="setting-td size-l">
                                    이름/이메일
                                </div>
                                <div class="center-text setting-td size-s">
                                    금액
                                </div>
                                <div class="center-text setting-td trueorfalse">
                                    발송 여부
                                </div>
                        </div>
                       `
    text += `<div>
                            <div
                                class="setting-tr-group"
                                style="
                                border-bottom: solid 1px
                                    #e0e0e0;
                                padding-bottom: 0px;
                            "
                    >`;

    myFundingBuyers.forEach((myFundingBuyer) => {
        text += `<div class="price-member setting-tr buy-funding-product-${myFundingBuyer.id}" style="padding-top: 7px">
                        <div
                                class="setting-td with-sub size-l"
                        >
                            <div
                                    class="membername major-span"
                            >
                                ${myFundingBuyer.profileName}
                            </div>
                            <div
                                    class="memberemail sub-span"
                            >
                                ${myFundingBuyer.profileEmail}
                            </div>
                        </div>
                        <div
                                class="center-text price-member setting-td with-text primary size-s"
                                style="
                                margin-bottom: 35px;
                            "
                        >
                             ${myFundingBuyer.productPrice}원
                        </div>
                        <div
                                class="center-text setting-td with-btn trueorfalse"
                        >
                            <div
                                    class="btn-group choice-group"
                            >
                            `
        console.log(myFundingBuyer.fundingSendStatus);
            if(myFundingBuyer.fundingSendStatus === "YES") {
                        text += `
                                <div
                                        class="btn-choice btn-public active"
                                >`
                              }else{
                        text += `
                                <div
                                        class="btn-choice btn-public"
                                >
                                `
                            }
                        text +=  `<input
                                            checked=""
                                            class="radio-value"
                                            name="is_secret_employment"
                                            type="radio"
                                            value="YES"
                                    /><span
                                        class="name"
                                >보냄</span
                                >
                                </div>`
        if(myFundingBuyer.fundingSendStatus === "NO") {
            text += `
                                <div
                                        class="btn-choice btn-secret active"
                                >`
        } else {
            text += `
                                <div
                                        class="btn-choice btn-secret"
                                >`
        }
            text +=        `<input
                                            class="radio-value"
                                            name="is_secret_employment"
                                            type="radio"
                                            value="NO"
                                    /><span
                                        class="name"
                                >안보냄</span
                                >
                                </div>
                            </div>
                        </div>
                        <label
                                class="switch"
                                style="
                                display: none;
                            "
                        >
                            <input
                                    class="media-checkbox"
                                    type="checkbox"
                            />
                            <span
                                    class="slider round"
                            ></span>
                        </label>
                   </div>
            `;

    });
    text += `    </div>
            </div>`;

    text += `<ul class="pagination theme-yozm mypage-page back-or-next">`;

    if(settingTablePagination.prev){
        text += `
            <li class="page-item">
                <a href="${settingTablePagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
        console.log("구매자 목록/이전 : ", settingTablePagination.prev);
        console.log("구매자 목록/이전 : ", text);
    }

    if(settingTablePagination.next) {
        text += `
            <li class="page-item">
                <a href="${settingTablePagination.endPage + 1}" class="page-link next"></a>
            </li>
        `
        console.log("구매자 목록/다음 : ",settingTablePagination.next);
        console.log("구매자 목록/다음 : ",text);
    }
    text += `    </ul>`;

    return text;
}
