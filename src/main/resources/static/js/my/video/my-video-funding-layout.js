const myFundingListLayout = document.getElementById("my-funding-list");

const showList = ({members}) => {
    let text = ``;

    members.forEach((member) => {
        text += `<div style="display: flex">
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
                                <!--몇 시간 전인지-->
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
            `;
    });
    myFundingListLayout.innerText += text;

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
