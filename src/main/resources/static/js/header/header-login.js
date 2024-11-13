document.addEventListener("DOMContentLoaded", function() {
    // **스크롤 기능 구현
    const header = document.querySelector(".header");
    if (!header) {
        console.warn("Header element not found.");
        return;
    }
    const headerHeight = header.offsetHeight;
    const headerNav = document.querySelector(".header-nav");
    if (!headerNav) {
        console.warn("Header-nav element not found.");
        return;
    }

    window.addEventListener("scroll", function () {
        let windowTop = window.scrollY;
        if (windowTop >= headerHeight) {
            headerNav.classList.add("sticky");
        } else {
            headerNav.classList.remove("sticky");
        }
    });

    // **검색창 기능 구현
    const searchBox = document.getElementById("search-box");
    const searchFullModal = document.getElementById("search-full-modal");
    const iconClose = document.getElementById("icon-close");
    const backgroundOverlay = document.getElementById("background-overlay");
    const searchInput = document.getElementById("search-input");

    if (searchBox && searchFullModal && searchInput) {
        searchBox.addEventListener("click", () => {
            // 검색창 나옴.
            searchFullModal.style.display = "block";
            // 바로 커서 깜박임 기능
            searchInput.focus();
            window.scrollTo({ top: 0 });
        });
    } else {
        console.warn("Search elements not found.");
    }

    if (iconClose && searchFullModal) {
        iconClose.addEventListener("click", () => {
            // 검색창 사라짐.
            searchFullModal.style.display = "none";
        });
    }

    if (backgroundOverlay && searchFullModal) {
        backgroundOverlay.addEventListener("click", () => {
            // 검색창 사라짐.
            searchFullModal.style.display = "none";
        });
    }

    // **알람 모달 기능구현
    const notiContainer1 = document.getElementById("noti-bell-container-1");
    const notiContainer2 = document.getElementById("noti-bell-container-2");

    const notiModal1 = document.getElementById("noti-modal-1");
    const notiModal2 = document.getElementById("noti-modal-2");

    if (notiContainer1 && notiModal1) {
        notiContainer1.addEventListener("mouseenter", (e) => {
            notiModal1.style.display = "block";
        });

        notiModal1.addEventListener("mouseleave", (e) => {
            notiModal1.style.display = "none";
        });
    }

    if (notiContainer2 && notiModal2) {
        notiContainer2.addEventListener("mouseenter", (e) => {
            notiModal2.style.display = "block";
        });

        notiModal2.addEventListener("mouseleave", (e) => {
            notiModal2.style.display = "none";
        });
    }

    if (header && notiModal1 && notiModal2) {
        header.addEventListener("mouseleave", (e) => {
            notiModal1.style.display = "none";
            notiModal2.style.display = "none";
        });
    }
});
