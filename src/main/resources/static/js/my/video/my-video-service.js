const myPageService = (() => {

    const getMyFundingList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/video/my/funding/${page}`);
        const myFundingPosts = await response.json();

        if(callback){
            callback(myFundingPosts);
        }
    }

    const getFundingBuyerList = async (page, fundingPostId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/video/my/funding/${fundingPostId}/buyers/${page}`);
        const myFundingBuyers = await response.json();

        if(callback){
            return callback(myFundingBuyers);
        }
    }

    const updateFundingSendStatus = async (buyFundingProduct) => {
        await fetch(`/members/video/my/funding/buyers/sendStatus/update`, {
            method: "put",
            body: JSON.stringify(buyFundingProduct),
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        });
    }

    const getMyBuyFundingList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/video/my/buy/funding/${page}`);
        const myBuyFundingPosts = await response.json();

        if(callback){
            callback(myBuyFundingPosts);
        }
    }

    const getMyInquiryHistoryList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/inquiry-histories/${page}`);
        const myInquiryHistories = await response.json();

        console.log("response", response)
        console.log("service: myInquiryHistories", myInquiryHistories)


        if(callback) {
            callback(myInquiryHistories);
        }
    }
    console.log("getMyInquiryHistoryList",getMyInquiryHistoryList)

    return {
        getMyFundingList: getMyFundingList,
        getFundingBuyerList: getFundingBuyerList,
        updateFundingSendStatus: updateFundingSendStatus,
        getMyBuyFundingList: getMyBuyFundingList,
        getMyInquiryHistoryList: getMyInquiryHistoryList}
})()