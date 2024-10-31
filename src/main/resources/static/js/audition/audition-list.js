document.addEventListener("DOMContentLoaded", () => {
    const searchInputs = document.querySelectorAll(
        ".default-input-wishket input"
    );

    searchInputs.forEach((input) => {
        input.addEventListener("focus", () => {
            // 기본 focus outline 제거
            input.style.outline = "none";
        });
    });



});
function searchAuditions() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    const url = `/audition/video/audition-list?page=1&keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
}