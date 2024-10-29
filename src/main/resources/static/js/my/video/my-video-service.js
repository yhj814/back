const myPageService = (() => {
    const getMyFundingList = async (page, memberId, callback) => {

        console.log("1 : ", getMyFundingList);

        page = page || 1;

        console.log("2 : ", page);

        const response = await fetch(`/members/video/myFunding/${memberId}/${page}`);
        const myFundingPosts = await response.json();

        console.log("3 : ", response);
        console.log("4 : ", myFundingPosts);

        if(callback){
            callback(myFundingPosts);
        }
    }

    return {getMyFundingList: getMyFundingList}
})()