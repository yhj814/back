

    Bootpay.requestPayment({
        application_id: "66c6a759a3175898bd6e499c",
        price: parseInt(workPrice, 10),
        order_name: workTitle,
        order_id: workId,
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
                id: workId,
                name: workTitle,
                qty: 1,
                price: parseInt(workPrice, 10)
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
            await completePayment(workId, memberProfileId);
            alert("결제가 성공적으로 완료되었습니다.");
            window.location.href = `/text/detail/${workId}`;
        })
        .catch((error) => {
            console.error("결제 요청 중 오류 발생:", error.message);
            alert("결제 중 오류가 발생했습니다. 다시 시도해주세요.");
        });

// 결제 완료 시 서버로 데이터 전송 함수
async function completePayment(workId, memberProfileId) {
    try {
        const response = await fetch("/text/order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                workId: workId,
                memberProfileId: memberProfileId
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
