// // 인증번호 모달창
// // Enter Modal 창
// document.addEventListener("DOMContentLoaded", () => {
//     const correctCode = "123456"; // 올바른 인증번호
//     const enterModal = document.getElementById("enter-modal"); // 모달 창 요소
//     const enterVerifyBtn = document.getElementById("enterVerify_btn"); // 인증번호 확인 버튼
//     const inputs = document.querySelectorAll(".enter-input"); // 인증번호 입력 필드들
//     const enterModalContent = document.querySelector(".enterModal_content");
//     const modalWrap = document.querySelector(".modal-wrap");
//     // 모달을 여는 함수
//     const openEnterModal = () => {
//         enterModal.style.display = "flex"; // 모달을 화면에 보이도록 설정
//         modalWrap.style.animation = "popUp 0.5s"; // 애니메이션을 적용하여 모달을 열기
//         inputs[0].focus(); // 첫 번째 입력 필드에 포커스를 맞춤
//     };
//
//     // 모달을 닫는 함수
//     const closeEnterModal = () => {
//         modalWrap.style.animation = "popDown 0.5s"; // 애니메이션을 적용하여 모달을 닫기
//         setTimeout(() => {
//             enterModal.style.display = "none"; // 애니메이션이 끝난 후 모달을 화면에서 숨김
//         }, 450); // 애니메이션의 지속 시간과 맞추기
//     };
//
//     // 페이지 로드 시 모달을 자동으로 열기
//     openEnterModal();
//
//     // 입력 필드 처리
//     inputs.forEach((input, index) => {
//         // 입력이 발생할 때
//         input.addEventListener("input", () => {
//             // 입력 필드에 한 글자가 입력되면 다음 입력 필드로 포커스를 이동
//             if (input.value.length === 1 && index < inputs.length - 1) {
//                 inputs[index + 1].focus();
//             }
//         });
//
//         // 키다운 이벤트 처리
//         input.addEventListener("keydown", (e) => {
//             if (
//                 e.key === "Backspace" &&
//                 input.value.length === 0 &&
//                 index > 0
//             ) {
//                 // 백스페이스 키가 눌렸을 때 이전 입력 필드로 포커스를 이동
//                 inputs[index - 1].focus();
//             } else if (e.key === "Enter") {
//                 // 엔터 키가 눌렸을 때 확인 버튼 클릭
//                 enterVerifyBtn.click();
//             }
//         });
//     });
//
//     // 확인 버튼 클릭 시 인증번호 확인
//     enterVerifyBtn.addEventListener("click", () => {
//         // 모든 입력 필드의 값을 합쳐서 인증번호 생성
//         const enteredCode = Array.from(inputs)
//             .map((input) => input.value)
//             .join("");
//
//         // 입력된 인증번호의 길이가 올바른 인증번호의 길이와 같을 때
//         if (enteredCode.length === correctCode.length) {
//             if (enteredCode === correctCode) {
//                 // 인증번호가 올바를 때
//                 closeEnterModal(); // 모달 닫기
//             } else {
//                 // 인증번호가 틀릴 때
//                 enterModalContent.classList.add("vibration");
//
//                 // 모든 입력 필드에 'vibration' 클래스 추가
//                 inputs.forEach((input) => {
//                     input.value = ""; // 모든 입력 필드 초기화
//                     input.classList.add("vibration"); // 진동 클래스 추가
//                 });
//
//                 // 첫 번째 입력 필드에 포커스 맞춤
//                 inputs[0].focus();
//
//                 // 일정 시간 후에 'vibration' 클래스 제거
//                 setTimeout(() => {
//                     inputs.forEach((input) => {
//                         input.classList.remove("vibration");
//                         enterModalContent.classList.remove("vibration");
//                     });
//                 }, 400);
//             }
//         }
//     });
// });

// 인증번호 모달창
document.addEventListener("DOMContentLoaded", () => {
    const enterModal = document.getElementById("enter-modal"); // 모달 창 요소
    const enterVerifyBtn = document.getElementById("enterVerify_btn"); // 인증번호 확인 버튼
    const inputs = document.querySelectorAll(".enter-input"); // 인증번호 입력 필드들
    const enterModalContent = document.querySelector(".enterModal_content");
    const modalWrap = document.querySelector(".modal-wrap");
    const verifyForm = document.getElementById("verify-form");

    // 모달을 여는 함수
    const openEnterModal = () => {
        enterModal.style.display = "flex"; // 모달을 화면에 보이도록 설정
        modalWrap.style.animation = "popUp 0.5s"; // 애니메이션을 적용하여 모달을 열기
        inputs[0].focus(); // 첫 번째 입력 필드에 포커스를 맞춤
    };

    // 모달을 닫는 함수
    const closeEnterModal = () => {
        modalWrap.style.animation = "popDown 0.5s"; // 애니메이션을 적용하여 모달을 닫기
        setTimeout(() => {
            enterModal.style.display = "none"; // 애니메이션이 끝난 후 모달을 화면에서 숨김
        }, 450); // 애니메이션의 지속 시간과 맞추기
    };

    // 페이지 로드 시 모달을 자동으로 열기
    openEnterModal();

    // 입력 필드 처리
    inputs.forEach((input, index) => {
        // 입력이 발생할 때
        input.addEventListener("input", () => {
            // 입력 필드에 한 글자가 입력되면 다음 입력 필드로 포커스를 이동
            if (input.value.length === 1 && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        // 키다운 이벤트 처리
        input.addEventListener("keydown", (e) => {
            if (e.key === "Backspace" && input.value.length === 0 && index > 0) {
                // 백스페이스 키가 눌렸을 때 이전 입력 필드로 포커스를 이동
                inputs[index - 1].focus();
            } else if (e.key === "Enter") {
                // 엔터 키가 눌렸을 때 확인 버튼 클릭
                enterVerifyBtn.click();
            }
        });
    });

    verifyForm.addEventListener("submit", (event) => {
        // 폼 기본 제출 동작 방지
        event.preventDefault();

        // 모든 입력 필드의 값을 합쳐서 인증번호 생성
        const enteredCode = Array.from(inputs)
            .map((input) => input.value)
            .join("");

        // 인증번호를 서버로 POST 요청
        fetch("/admin/verify", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                adminVerifyCode: enteredCode,
            }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success && data.redirect) {
                    // 인증 성공 시 리다이렉트 처리
                    // 서버에서 받은 리다이렉트 URL로 이동
                    window.location.href = data.redirect;
                } else {
                    // 인증 실패 시 입력 필드에 진동 효과 추가
                    enterModalContent.classList.add("vibration");
                    inputs.forEach((input) => {
                        // 모든 입력 필드 초기화
                        input.value = "";
                        // 진동 클래스 추가
                        input.classList.add("vibration");
                    });
                    // 첫 번째 입력 필드에 포커스 맞춤
                    inputs[0].focus();
                    setTimeout(() => {
                        inputs.forEach((input) => {
                            input.classList.remove("vibration");
                            enterModalContent.classList.remove("vibration");
                        });
                    }, 400);
                }
            })
            .catch((error) => {
                console.error("Error verifying code:", error);
            });
    });
});


