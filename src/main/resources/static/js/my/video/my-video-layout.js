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

// 나의 작품 목록, 페이징
const myWorkListLayout = document.getElementById("my-work-list");
const myWorkListPaging = document.getElementById("my-work-list-paging");

const showMyWorkList = ({myWorkPosts, workAndFundingPagination}) => {
    let text = ``;
    let pagingText = ``;

    myWorkPosts.forEach((myWorkPost) => {

        text += `
            <div class="list-item">
                <div class="products-list">
                    <div class="flex-box">
                        <div class="products-text">
                            <a
                                ><p
                                    class="my-products-title"
                                >
                                    ${myWorkPost.postTitle}
                                </p></a
                            >
                            <div
                                class="my-products-info"
                            >
                                <a
                                    ><p
                                        class="btn smooth my-products-category"
                                    >
                                         ${myWorkPost.genreType}
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
                                        ${timeForToday(myWorkPost.createdDate)}
                                    </div>
                                </div>
                            </div>
                            <a
                                ><p
                                    class="timeandcontent content products-description"
                                >
                                 ${myWorkPost.postContent}
                                </p></a
                            >
                        </div>
                        <div class="btn products-image">
                            <a 
                                ><img src="/text/display?fileName=${myWorkPost.thumbnailFilePath}/t_${myWorkPost.thumbnailFileName}"
                            </a>
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
                                src=${myWorkPost.profileImgUrl}
                            />
                            <p class="author-name">
                                ${myWorkPost.profileNickName}
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
                                            id="my-work-buyer-btn"
                                            class="icon-my-edit-off ${myWorkPost.id}"
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
                    <div class="setting-table work-buyer-${myWorkPost.id}" style="
                            border-top: 1px solid
                                rgb(224, 224, 224);
                            display: none;
                        ">
                        <!--내 작품 구매자 테이블-->
                    </div>
                </div>
            </div>
            `;
    });
    myWorkListLayout.innerHTML = text;


    if(workAndFundingPagination.prev){
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
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
    }

    myWorkListPaging.innerHTML = pagingText;
}

// 나의 작품 - 구매자 목록, 페이징
const showMyWorkBuyerList = ({myWorkBuyers, settingTablePagination}) => {

    let text = `<div class="price-member setting-th">
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
    text += `<div class="setting-tr-group" style="border-bottom: solid 1px #e0e0e0; padding-bottom: 0px;">`;

    myWorkBuyers.forEach((myWorkBuyer) => {
        text += `<div class="price-member setting-tr" style="padding-top: 7px">
                    <div class="setting-td with-sub size-l">
                        <div class="membername major-span">
                            ${myWorkBuyer.profileName}
                        </div>
                        <div class="memberemail sub-span">
                            ${myWorkBuyer.profileEmail}
                        </div>
                    </div>
                    <div class="center-text price-member setting-td with-text primary size-s"
                            style="margin-bottom: 35px;">
                        ${myWorkBuyer.workPrice}
                    </div>
                    <div class="center-text setting-td with-btn trueorfalse">
                        <div class="btn-group choice-group">`
        if(myWorkBuyer.workSendStatus === "YES") {
           text += `<div class="btn-choice btn-public active">`
        } else {
            text += `<div class="btn-choice btn-public ${myWorkBuyer.id}">`
        }
                        text += `<input
                                        checked=""
                                        class="radio-value"
                                        name="is_secret_employment"
                                        type="radio"
                                        value="YES"/>보냄
                            </div>`
        if(myWorkBuyer.workSendStatus === "NO") {
            text += `<div class="btn-choice btn-secret active">`
        } else {
            text += `<div class="btn-choice btn-secret">`
        }
             text +=          `<input
                                        class="radio-value"
                                        name="is_secret_employment"
                                        type="radio"
                                        value="NO"/>안보냄
                            </div>
                        </div>
                    </div>
                    <label
                            class="switch"
                            style="display: none;">
                        <input
                                class="media-checkbox"
                                type="checkbox"/>
                        <span class="slider round"></span>
                    </label>
                </div>
            `;

    });
    text += `    </div>`;

    text += `<ul class="pagination theme-yozm mypage-page back-or-next">`;

    if(settingTablePagination.prev){
        text += `
            <li class="page-item">
                <a href="${settingTablePagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
    }

    if(settingTablePagination.next) {
        text += `
            <li class="page-item">
                <a href="${settingTablePagination.endPage + 1}" class="page-link next"></a>
            </li>
        `
    }
    text += `    </ul>`;

    return text;
}

// 구매한 작품 목록, 페이징
const myBuyWorkListLayout = document.getElementById("my-buy-work-list");
const myBuyWorkListPaging = document.getElementById("my-buy-work-list-paging");


const showMyBuyWorkList = ({myBuyWorkPosts, workAndFundingPagination}) => {
    let text = ``;
    let pagingText = ``;

    myBuyWorkPosts.forEach((myBuyWorkPost) => {
        text += ` <div class="list-item">
                    <div class="products-list">
                        <div class="flex-box">
                            <div class="products-text">
                                <a
                                    ><p
                                        class="my-products-title"
                                    >
                                        ${myBuyWorkPost.postTitle}
                                    </p></a
                                >
                                <div
                                    class="my-products-info"
                                >
                                    <a
                                        ><p
                                            class="btn smooth my-products-category"
                                        >
                                           ${myBuyWorkPost.genreType}
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
                                             ${timeForToday(myBuyWorkPost.createdDate)}
                                        </div>
                                    </div>
                                </div>
                                <a
                                    ><p
                                        class="timeandcontent content products-description"
                                    >
                                         ${myBuyWorkPost.postContent}
                                    </p></a
                                >
                            </div>
                            <div class="btn products-image">
                                <a
                                    ><img
                                        src="/text/display?fileName=${myBuyWorkPost.thumbnailFilePath}/t_${myBuyWorkPost.thumbnailFileName}"
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
                                    src=${myBuyWorkPost.profileImgUrl}
                                />
                                <p class="author-name">
                                     ${myBuyWorkPost.profileNickName}
                                </p>
                                <div
                                    class="divider"
                                    style="
                                        margin-left: 10px;
                                        margin-right: 10px;
                                    "
                                ></div>
    
                                <div
                                    class="timeandcontent smooth"
                                >
                                    ${myBuyWorkPost.workPrice}
                                </div>
                            </div>
                            <div class="flex-box">
                                <div class="btn-wrapper">
                                    <button
                                        class="btn btn-action icon-delete-read-products"
                                        type="button"
                                    >
                                        <div
                                            id="buy-work-delete-btn"
                                            class="icon-my-delete ${myBuyWorkPost.id}"
                                        ></div>
                                        <p
                                            class="action-tooltip bottom-action"
                                        >
                                            구매내역 삭제
                                        </p>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>`
    });

    myBuyWorkListLayout.innerHTML = text;

    if(workAndFundingPagination.prev){
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
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
    }

    myBuyWorkListPaging.innerHTML = pagingText;
}

// 나의 펀딩 목록, 페이징
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
    }

    myFundingListPaging.innerHTML = pagingText;
}

// 나의 펀딩 - 구매자 목록, 페이징
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
        text += `<div class="price-member setting-tr" style="padding-top: 7px">
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
            if(myFundingBuyer.fundingSendStatus === "YES") {
                        text += `
                                <div
                                        class="btn-choice btn-public active" style="cursor: unset"
                                >`
            }else{
                        text += `
                                <div
                                        class="btn-choice btn-public ${myFundingBuyer.id}"
                                >
                                `
            }
                        text +=  `<input
                                            checked=""
                                            class="radio-value"
                                            name="is_secret_employment"
                                            type="radio"
                                            value="YES"
                                    />
                                    보냄
                                </div>`

            if(myFundingBuyer.fundingSendStatus === "NO") {
                        text += `
                                <div
                                        class="btn-choice btn-secret active"
                                >`
            } else {
                        text += `
                                <div
                                        class="btn-choice btn-secret" style="cursor: unset"
                                >`
            }
                        text += `<input
                                            class="radio-value"
                                            name="is_secret_employment"
                                            type="radio"
                                            value="NO"
                                    />안보냄
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
    }

    if(settingTablePagination.next) {
        text += `
            <li class="page-item">
                <a href="${settingTablePagination.endPage + 1}" class="page-link next"></a>
            </li>
        `
    }
    text += `    </ul>`;

    return text;
}

// 결제한 펀딩 목록, 페이징
const myBuyFundingListLayout = document.getElementById("my-buy-funding-list");
const myBuyFundingListPaging = document.getElementById("my-buy-funding-list-paging");


const showMyBuyFundingList = ({myBuyFundingPosts, workAndFundingPagination}) => {
    let text = ``;
    let pagingText = ``;

    myBuyFundingPosts.forEach((myBuyFundingPost) => {
        text += ` <div class="list-item">
                      <div class="products-list">
                            <div class="flex-box">
                                <div class="products-text">
                                    <a
                                        ><p
                                            class="my-products-title"
                                        >
                                            ${myBuyFundingPost.postTitle}
                                        </p></a
                                    >
                                    <div
                                        class="my-products-info"
                                    >
                                        <a
                                            ><p
                                                class="btn smooth my-products-category"
                                            >
                                                ${myBuyFundingPost.genreType}
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
                                                ${timeForToday(myBuyFundingPost.createdDate)}
                                            </div>
                                        </div>
                                    </div>
                                    <a
                                        ><p
                                            class="timeandcontent content products-description"
                                        >
                                             ${myBuyFundingPost.postContent}
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
                                        ${myBuyFundingPost.postContent}
                                    </p>
                                    <div
                                        class="divider"
                                        style="
                                            margin-left: 10px;
                                            margin-right: 10px;
                                        "
                                    ></div>
                                    <div
                                        class="timeandcontent smooth"
                                    >
                                        ${myBuyFundingPost.productName}
                                    </div>
                                    <div
                                        class="divider"
                                        style="
                                            margin-left: 10px;
                                            margin-right: 10px;
                                        "
                                    ></div>
                                    <div
                                        class="timeandcontent smooth"
                                    >
                                        ${myBuyFundingPost.productPrice}
                                    </div>
                                </div>
    
                                <div class="flex-box">
                                    <div
                                        class="btn-wrapper"
                                    ></div>
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
                                                결제내역 삭제
                                            </p>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                  </div>`
    });

    myBuyFundingListLayout.innerHTML = text;

//     콘솔창에 오류가 남. 확인 필요
// if(myBuyFundingPosts[0].id !== null)  {}

    if (workAndFundingPagination.prev) {
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
    }
    for (let i = workAndFundingPagination.startPage; i <= workAndFundingPagination.endPage; i++) {
        if (workAndFundingPagination.page === i) {
            pagingText += `<li class="page-item"><div class="page-link active">${i}</div></li>`
        } else {
            pagingText += `<li class="page-item"><a href="${i}" class="page-link">${i}</a></li>`
        }
    }

    if (workAndFundingPagination.next) {
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.endPage + 1}" class="page-link next"></a>
            </li>
        `
    }

    myBuyFundingListPaging.innerHTML = pagingText;
}

// 문의내역, 페이징
const myInquiryHistoryListLayout = document.getElementById("my-inquiry-history-list");
const myInquiryHistoryListPaging = document.getElementById("my-inquiry-history-list-paging");


const showMyInquiryHistoryList = ({myInquiryHistories, workAndFundingPagination}) => {
let text = ``;
let pagingText = ``;

    myInquiryHistories.forEach((myInquiryHistory) => {
        text += `<div class="list-item">
            <div class="inquiry-list">
                <div class="flex-box">
                    <div class="inquiry-text">
                        <a
                            ><p
                                class="my-inquiry-title"
                            >
                                ${myInquiryHistory.postTitle}
                            </p></a
                        >
                        <div
                            class="my-inquiry-info"
                        >
                            <div class="flex-box">
                                <img
                                    class="time"
                                    src="/images/member/clock.png"
                                />
                                <div
                                    class="timecontent-font"
                                >
                                     ${timeForToday(myInquiryHistory.postCreatedDate)}
                                </div>
                            </div>
                        </div>
                        <a
                            ><p
                                class="timecontent-font content inquiry-description"
                            >
                                 ${myInquiryHistory.postContent}
                            </p></a
                        >
                    </div>
                </div>
                <div
                    class="flex-box inquiry-author-box"
                >
                    <div
                        class="author-info flex-box"
                    >
                        <img
                            class="author-image"
                            src="/images/member/member-image.jpg"
                        />
                        <p class="author-name">
                            ${myInquiryHistory.profileNickname}
                        </p>
                    </div>
                    <div
                        class="flex-box"
                        style="margin-left: 370px"
                    >
                        <div class="btn-wrapper">
                            <button
                                class="btn btn-action btn-icon-edit-my"
                                name="toggle_btn"
                                type="button"
                                style="
                                    background-color: white;
                                "
                            >
                                <p
                                    class="action-tooltip bottom-action edit-my-off"
                                >
                                    관리자 답변 보기
                                </p>
                                <div
                                    class="edit-my-off"
                                >
                                    <div
                                        id="admin-answer-btn"
                                        class="icon-my-edit-off ${myInquiryHistory.postId}"
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
    
                    </div>
                </div>
                <div
                    class="setting-table inquiry-${myInquiryHistory.postId}""
                    style="
                        border-top: 1px solid
                            rgb(224, 224, 224);
                        display: none;
                    " 
                >
<!--답변 테이블 -->
                </div>
            </div>
        </div>`;
    });
    myInquiryHistoryListLayout.innerHTML = text;


    if(workAndFundingPagination.prev){
        pagingText += `
            <li class="page-item">
                <a href="${workAndFundingPagination.startPage - 1}" class="page-link back"></a>
            </li>
        `
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
    }
    myInquiryHistoryListPaging.innerHTML = pagingText;
}

// 내 문의 내역 관리자 답변
const showAdminAnswer = (adminAnswer) => {
    let text =``;
    console.log("layout-adminAnswer: ",adminAnswer);

        text =  `<div class="setting-th">
                <div
                    class="setting-td manager"
                >
                    관리자 답변
                </div>
            </div>
            <div
                class="setting-tr-group"
                style="
                    border-bottom: solid 1px
                        #e0e0e0;
                    padding-bottom: 0px;
                "
            >
                <div
                    class="setting-tr"
                    style="padding-top: 7px"
                >
                    <div
                        class="setting-td with-sub manager"
                        style="width: 700px"
                    >`
            if(adminAnswer.inquiryStatus === 'YES')  {
               text +=  `<div class="sub-span">
                            ${adminAnswer.adminAnswerContent}
                        </div>`
            }
            else {

                text +=  `<div class="sub-span">
                            아직 미답변 상태입니다.
                        </div>`

            }
    text +=    `</div>
                </div>
            </div>`

    return text;
}

// 내 정보
const myProfileLayout = document.getElementById("my-profile");

console.log("myProfileLayout", myProfileLayout)

const showMyProfile = (memberProfile) => {
    let text = ``;

    console.log("memberProfile : ", memberProfile)

    text = `<form class="form-horizontal has-validation-callback" id="base-edit-form">
                <div class="form-group">
                    <label class="control-label required" id="full_name_label"><span></span> 이름</label>
                    <div class="control-wrapper">
                        <input class="form-control" id="full_name" placeholder="이름을 입력해 주세요." name="profileName" type="text" value="${memberProfile.profileName}">
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            이 항목을 채워주십시오.
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label required" id="full_nickname_label"><span></span> 닉네임</label>
                    <div class="control-wrapper">
                        <input class="form-control" id="full_nickname" placeholder="사용할 닉네임을 입력해주세요." name="profileNickName" type="text" value="${memberProfile.profileNickName}">
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            이 항목을 채워주십시오.
                        </div>
                    </div>
                </div>
                <div class="form-group" style="margin-top: -5px">
                    <label class="control-label" for="gender">성별</label>
                    <div class="control-wrapper">
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            이 항목을 채워주십시오.
                        </div>
                    </div>
                    <div class="control-wrapper">
                        <ul class="list-unstyled" style="margin-bottom: 0">
                            <li>
                                <label class="radio-inline" for="gender_1"><input data-exception="yes" id="gender_1" name="profileGender" type="radio" value="남성" checked="${memberProfile.profileGender == '남성'}">남성</label>
                            </li>
                            <li>
                                <label class="radio-inline" for="gender_2"><input data-exception="yes" id="gender_2" name="profileGender" type="radio" value="여성" checked="${memberProfile.profileGender == '여성'}">여성</label>
                            </li>
                        </ul>
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            이 항목을 채워주십시오.
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label" for="date_of_birth" id="date_of_birth_label">나이</label>
                    <div class="control-wrapper">
                        <input class="form-control" id="member-age" name="profileAge}" placeholder="만 나이를 입력해주세요." type="text" value="${memberProfile.profileAge}">

                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            이 항목을 채워주십시오.
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">이메일</label>

                    <div class="control-wrapper">
                        <div class="input-gap loading-icon-wrap">
                            <input class="form-control" data-exception="yes" placeholder="이메일을 입력하세요." name="profileEmail" type="text" value="${memberProfile.profileEmail}">
                            <button class="btn btn-certification-select" type="button" id="requestEmailCode">인증번호 요청</button>
                            <img class="loading-icon" id="loadingGif" src="/images/main/loading.gif" alt="Loading...">
                        </div>
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            올바른 이메일 주소를
                            입력해주세요.
                        </div>
                    </div>
                </div>
                <div class="form-group certification-input-email">
                    <label class="control-label required">이메일 인증</label>
                    <div class="control-wrapper ">
                        <div class="input-gap">
                            <input class="form-control " id="emailVerificationCode" placeholder="인증번호를 입력하세요." type="text">
                            <button class="btn btn-certification-select" type="button" id="verifyEmailCode">인증번호 확인</button>
                        </div>
                        <div class="error-message" id="emailVerificationError" style="display: none;"></div>
                        <div class="email-timer-container" style="display: none;">
                            <p>인증 유효 시간: <span id="emailTimerDisplay">3:00</span></p>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">연락처</label>
                    <div class="control-wrapper">
                        <div class="input-gap">
                            <input class="form-control" data-exception="yes" placeholder="전화번호를 입력하세요." name="profilePhone" type="text" value="${memberProfile.profilePhone}">
                            <button class="btn btn-certification-select" type="button" id="requestVerificationCode">인증번호 요청</button>
                        </div>
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            올바른 전화번호를 입력해주세요.
                        </div>
                    </div>
                </div>
                <div class="form-group certification-input-phone">
                    <label class="control-label required">전화번호 인증</label>
                    <div class="control-wrapper ">
                        <div class="input-gap">
                            <input class="form-control " id="verificationCode" placeholder="인증번호를 입력하세요." type="text">
                            <button class="btn btn-certification-select" type="button" id="verifyCode">인증번호 확인</button>
                        </div>
                        <div class="error-message" id="verificationError">인증번호를 입력하세요.</div>
                        <!-- 타이머 표시 영역 -->
                        <div class="timer-container" style="display: none;">
                            <p>인증번호 유효 시간: <span id="timerDisplay">3:00</span></p>
                        </div>
                    </div>


                </div>
                <div class="form-group">
                    <label class="control-label">추가 작성사항</label>
                    <div class="control-wrapper">
                        <textarea class="form-control form-textarea" placeholder="간단한 자기소개를 입력해주세요." name="profileEtc" value="${memberProfile.profileEtc}" style="height: 340px"></textarea>
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            간단한 자기소개를 입력해주세요.
                        </div>
                    </div>
                </div>
                <div class="form-group" style="margin-top: 360px">
                    <label class="control-label" style="margin-top: 25px;">첨부파일</label>
                    <div class="control-wrapper" style="margin-top: 25px;">
                        <input accept=".pdf, .docx, .doc, .hwp" class="temp-upload-resume" name="profileFile" hidden="" type="file">

                        <div class="selected-file-viewer">
                            <input class="remove-request" hidden="" name="fileContent" type="checkbox">
                            <p class="placeholder-attachment">
                                이력서를 제출해주세요.
                            </p>
                            <div class="tooltip-group">
                                <img class="viewer-file-icon" src="/images/audition/attachment_file_icon@2x.png" style="width: 15px"><a download="" id="resume_download"><p class="file-name-viewer text300 body-2-medium" id="selected_file_name_viewer"></p></a><img class="btn-delete-resume" id="btn_delete_resume" src="/images/audition/chip_btn_delete_normal@2x.png" style="width: 15px">
                            </div>
                        </div>
                        <button class="btn btn-file-select" id="file_select_btn" type="button">
                            <img src="/images/audition/btn_icon_add@2x.png" style="width: 15px">이력서 선택
                        </button>
                        <div class="error-message">
                            <i class="fa fa-exclamation-circle"></i>
                            첨부파일을 선택해주세요.
                        </div>
                    </div>
                </div>
            </form>`;

    myProfileLayout.innerHTML = text;

    console.log("myProfileLayout.innerHTML : ", myProfileLayout.innerHTML)

}

