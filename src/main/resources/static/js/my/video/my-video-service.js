const myPageService = (() => {
    const getMyFundingList = async (memberId, callback) => {
        const response = await fetch(`/members/video/myFunding/${memberId}`);
        const fundingPostsByMember = await response.json();

        console.log(fundingPostsByMember)

        if(callback){
            callback(fundingPostsByMember);
        }
    }

    // const getFundingBuyerList = async (fundingPostId) => {
    //     const response = await fetch(`/members/video/myFunding/${fundingPostId}`);
    //     const buyersByFundingPost = await response.json();
    //
    //     console.log(buyersByFundingPost)
    // }


    return {getMyFundingList: getMyFundingList}
})()