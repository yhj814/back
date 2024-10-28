const myFundingListLayout = document.getElementById("my-funding-list");

const showList = (members) => {
    let text = ``;
    console.log("들어옴")
    console.log(members)

    members.forEach((member) => {
        text += `
         <div class="list-item">
            <div class="products-list">
                <div class="flex-box">
                    <div class="products-text">
                        <a
                        ><p
                                class="my-products-title"
                        >
                            ${member.postTitle}
                        </p></a
                        >
                        <div
                                class="my-products-info"
                        >
                            <a
                            ><p
                                    class="btn smooth my-products-category"
                            >
                                ${member.genreType}
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
                                   ${timeForToday(member.createdDate)}
                                </div>
                            </div>
                        </div>
                        <a
                        ><p
                                class="timeandcontent content products-description"
                        >
                            ${member.postContent}
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
                            ${member.memberName}
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
            </div>
        </div>
            `;
    });
    myFundingListLayout.innerHTML += text;

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
