// 로그아웃 버튼 클릭 시 실행될 함수
document.getElementById("logout-btn").addEventListener("click", function (event) {
    event.preventDefault(); // 기본 동작 막기

    // 로그아웃 요청 보내기
    fetch("/kakao/logout", { // 로그아웃 API 엔드포인트
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공 시, 원하는 경로로 이동
                window.location.href = "/main"; // 로그아웃 후 이동할 페이지
            } else {
                console.error("로그아웃 실패:", response.statusText);
            }
        })
        .catch(error => {
            console.error("로그아웃 중 오류 발생:", error);
        });
});
