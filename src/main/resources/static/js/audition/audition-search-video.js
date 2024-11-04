function searchAuditions() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    const url = `/audition/video/list?page=1&keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
}