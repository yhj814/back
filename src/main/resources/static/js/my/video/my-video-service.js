const myPageService = (() => {
    const getMyFundingList = async (memberId, callback) => {
        const response = await fetch(`/members/video/funding/${memberId}`);
        const members = await response.json();

        console.log(members)

        if(callback){
            callback(members);
        }
    }

    const getFundingBuyerList = async (memberId, callback) => {
        const response = await fetch(`/members/video/funding-buyer/${memberId}`);
        const members = await response.json();

        console.log(members)

        if(callback){
            callback(members);
        }
    }


    return {getMyFundingList: getMyFundingList, getFundingBuyerList: getFundingBuyerList}
})()