const myPageService = (() => {

    const getMyFundingList = async (page, memberId, callback) => {

        page = page || 1;
        const response = await fetch(`/members/video/myFunding/${memberId}/${page}`);
        const myFundingPosts = await response.json();

        if(callback){
            callback(myFundingPosts);
        }
    }

    const getFundingBuyerList = async (page, fundingPostId, callback) => {

        page = page || 1;
        const response = await fetch(`/members/video/fundingPost/${fundingPostId}/buyers/${page}`);
        const myFundingBuyers = await response.json();

        if(callback){
            return callback(myFundingBuyers);
        }
    }
    return {getMyFundingList: getMyFundingList, getFundingBuyerList: getFundingBuyerList}
})()