const myPageService = (() => {
    const getMyFundingList = async (memberId, callback) => {
        const response = await fetch(`/members/${memberId}`);
        const members = await response.json();

        console.log(members)

        if(callback){
            callback(members);
        }
    }
    return {getMyFundingList: getMyFundingList}
})()