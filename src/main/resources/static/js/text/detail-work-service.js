const replyService = (() => {
    // 댓글 작성 함수
    const write = async (replyData) => {
        try {
            const response = await fetch("/replies/write", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(replyData)
            });

            if (response.ok) {
                console.log("댓글 작성 성공");
            } else {
                console.error("댓글 작성 실패");
            }
        } catch (error) {
            console.error("댓글 작성 중 오류:", error);
        }
    };

    // 댓글 리스트 가져오기 함수
    const getList = async (page, workId, callback) => {
        try {
            const response = await fetch(`/replies/${workId}/${page}`);
            if (!response.ok) throw new Error("댓글 로딩 실패");

            const data = await response.json();
            console.log("댓글 리스트 불러오기 성공:", data);

            if (callback) callback(data);
            return data;
        } catch (error) {
            console.error("Error loading replies:", error);
        }
    };

    // 댓글 삭제 함수
    const remove = async (replyId) => {
        try {
            const response = await fetch(`/replies/${replyId}`, {
                method: "DELETE",
            });

            if (!response.ok) throw new Error("댓글 삭제 실패");
            console.log("댓글 삭제 성공");

            return true; // 삭제 성공
        } catch (error) {
            console.error("Error deleting reply:", error);
            return false; // 삭제 실패
        }
    };  // 댓글 수 조회 함수
    const getReplyCount = async (workId) => {
        try {
            const response = await fetch(`/replies/count/${workId}`);
            if (!response.ok) throw new Error("댓글 수 조회 실패");

            const count = await response.json();
            console.log("댓글 수:", count);
            return count;
        } catch (error) {
            console.error("Error fetching reply count:", error);
            return 0; // 오류 발생 시 댓글 수를 0으로 반환
        }
    };

    // 평균 별점 조회 함수
    const getAverageStar = async (workId) => {
        try {
            const response = await fetch(`/replies/average-star/${workId}`);
            if (!response.ok) throw new Error("평균 별점 조회 실패");

            const { averageStar } = await response.json();
            console.log("평균 별점:", averageStar);
            return averageStar;
        } catch (error) {
            console.error("Error fetching average star:", error);
            return 0; // 오류 발생 시 평균 별점을 0으로 반환
        }
    };

    return { write, getList, remove, getReplyCount, getAverageStar };
})();