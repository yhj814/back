const div = document.getElementById("my-funding-texts");

console.log(memberId);
console.log(div)

let text = ``;

memberId.forEach((memberId) => {
    text += `
                <a
                    ><p
                        class="my-products-title"
                    >
                        ${memberId}
                    </p></a
                >
                <div
                    class="my-products-info"
                >
                    <a
                        ><p
                            class="btn smooth my-products-category"
                        >
                            펀딩 장르
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
                            몇 시간 전인지
                        </div>
                    </div>
                </div>
                <a
                    ><p
                        class="timeandcontent content products-description"
                    >
                        펀딩 포스트 내용
                    </p></a
                >
            `;
});
div.innerText += text;