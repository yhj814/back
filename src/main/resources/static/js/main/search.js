const searchVideoDiv = document.querySelector(".list-cover.video");
const searchTextDiv = document.querySelector(".list-cover.text");
const searchVideo = document.querySelector(".search-video");
const searchText = document.querySelector(".search-text");

searchVideo.addEventListener("click", () => {
    searchTextDiv.style.display = "none";
    searchVideoDiv.style.display = "block";

    searchVideo.classList.add("active");
    searchText.classList.remove("active");
});

searchText.addEventListener("click", () => {
    searchTextDiv.style.display = "block";
    searchVideoDiv.style.display = "none";

    searchText.classList.add("active");
    searchVideo.classList.remove("active");
});
