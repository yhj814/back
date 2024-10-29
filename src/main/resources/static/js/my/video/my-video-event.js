myPageService.getMyFundingList(memberId, showMyFundingList);

// "구매한 사람들" 버튼 클릭 시 설정 테이블 표시/숨김
const myFundingListToggleButtons = document.querySelectorAll(
    '#my-funding-list .btn-icon-edit-my[name="toggle_btn"]'
);

// 버튼 클릭 시 설정 테이블을 표시/숨김 처리하는 이벤트 리스너
myFundingListToggleButtons.forEach((button, index) => {
    button.addEventListener("click", () => {
        const settingTable = settingTables[index];
        if (
            settingTable.style.display === "none" ||
            settingTable.style.display === ""
        ) {
            settingTable.style.display = "block"; // 보여줍니다.
        } else {
            settingTable.style.display = "none"; // 숨깁니다.
        }
    });
});