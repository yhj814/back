document.addEventListener("DOMContentLoaded", function() {
    // body 태그에서 data-* 속성을 통해 정보를 가져옵니다.
    const bodyElement = document.querySelector("body");
    const fundingId = bodyElement.dataset.workId;
    const memberProfileId = bodyElement.dataset.memberId;
    const profileName = bodyElement.dataset.profileName || "기본 이름";
    const profilePhone = bodyElement.dataset.profilePhone || "010-0000-0000";
    const profileEmail = bodyElement.dataset.profileEmail || "default@example.com";

    console.log("Funding ID:", fundingId);
    console.log("Member Profile ID:", memberProfileId);
    console.log("Profile Name:", profileName);
    console.log("Profile Phone:", profilePhone);
    console.log("Profile Email:", profileEmail);

    // 모든 상품에 클릭 이벤트를 추가합니다.
    const productElements = document.querySelectorAll(".funding-product");

    productElements.forEach((productElement) => {
        productElement.addEventListener("click", function() {
            // 상품 정보를 데이터 속성으로 저장하고 이를 통해 가져옵니다.
            const productName = productElement.querySelector(".product-name span").innerText;
            const productPrice = parseInt(productElement.querySelector(".product-price .price").innerText.replace(',', ''), 10);
            const productAmount = parseInt(productElement.querySelector(".product-number .number").innerText, 10);
            const productId = productElement.getAttribute("data-product-id"); // 변경된 부분: dataset 대신 getAttribute 사용

            console.log("Product ID:", productId);
            console.log("Product Name:", productName);
            console.log("Product Price:", productPrice);
            console.log("Product Amount:", productAmount);

            // 데이터 누락 체크
            if (!fundingId) {
                console.error("Funding ID is missing.");
            }
            if (!memberProfileId) {
                console.error("Member Profile ID is missing.");
            }
            if (!productId) {
                console.error("Product ID is missing.");
            }
            if (isNaN(productAmount)) {
                console.error("Product Amount is missing or invalid.");
            }

            // 결제에 필요한 정보가 모두 존재하는지 확인
            if (!fundingId || !memberProfileId || !productId || isNaN(productAmount)) {
                alert("결제에 필요한 정보가 누락되었습니다. 다시 확인해 주세요.");
                return;
            }

            // Bootpay 결제 요청 부분
            Bootpay.requestPayment({
                application_id: "66c6a759a3175898bd6e499c",
                price: productPrice,
                order_name: productName,
                order_id: "funding_" + new Date().getTime(),  // 고유 주문 번호
                pg: "카카오페이",
                method: "간편",
                tax_free: 0,
                user: {
                    id: memberProfileId,
                    username: profileName,
                    phone: profilePhone,
                    email: profileEmail
                },
                items: [
                    {
                        id: productId,  // 상품 ID로 구분
                        name: productName,
                        qty: 1,
                        price: productPrice
                    }
                ],
                extra: {
                    open_type: "iframe",
                    card_quota: "0,2,3",
                    escrow: false
                }
            })
                .then(async (response) => {
                    console.log("결제 응답:", response); // 결제 성공 응답 로그 출력
                    // 결제 성공 시 서버에 데이터 전송
                    await completePayment(fundingId, memberProfileId, productId, productAmount);
                    alert("결제가 성공적으로 완료되었습니다.");
                    window.location.href = `/funding/detail/${fundingId}`; // 성공 후 상세 페이지로 리다이렉트
                })
                .catch((error) => {
                    console.error("결제 요청 중 오류 발생:", error.message);
                    alert("결제 중 오류가 발생했습니다. 다시 시도해주세요.");
                });
        });
    });
});


// 결제 완료 시 서버로 데이터 전송 함수
async function completePayment(fundingId, memberProfileId, productId, amount) {
    try {
        const response = await fetch("/text/funding/order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                fundingId: fundingId,
                memberProfileId: memberProfileId,
                productId: productId,
                amount: amount
            })
        });

        if (!response.ok) {
            console.error("서버에 결제 정보 전송 실패:", await response.text());
            alert("결제 정보 저장에 실패했습니다.");
        } else {
            console.log("서버에 결제 정보 전송 성공");
        }
    } catch (error) {
        console.error("결제 완료 데이터 전송 중 오류 발생:", error);
    }
}
