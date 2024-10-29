document.addEventListener("DOMContentLoaded", function () {
    let isPhoneVerified = false;

    document.getElementById("requestVerificationCode").addEventListener("click", function () {
        const phoneNumber = document.getElementById("phoneNumber").value.trim();

        if (!phoneNumber) {
            document.getElementById("phoneError").style.display = "block";
            return;
        }

        fetch("/sms/send", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ phoneNumber })
        })
            .then(response => response.text())
            .then(message => {
                alert(message);
            })
            .catch(error => console.error("SMS 발송 오류:", error));
    });

    document.getElementById("verifyCode").addEventListener("click", function () {
        const phoneNumber = document.getElementById("phoneNumber").value.trim();
        const code = document.getElementById("verificationCode").value.trim();

        fetch("/sms/verify", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ phoneNumber, code })
        })
            .then(response => response.text())
            .then(message => {
                if (message === "인증에 성공하였습니다.") {
                    alert(message);
                    isPhoneVerified = true;
                } else {
                    alert(message);
                    isPhoneVerified = false;
                }
            })
            .catch(error => console.error("인증 오류:", error));
    });

    document.getElementById("consulting_apply_button").addEventListener("click", function (event) {
        if (!isPhoneVerified) {
            alert("전화번호 인증이 필요합니다.");
            return;
        }

        document.getElementById("base-edit-form").submit();
    });
});
