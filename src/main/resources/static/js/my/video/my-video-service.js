const myPageService = (() => {

//************* 작품 ***************

    const getMyVideoWorkList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/video/my/work/${page}`);
        const myWorkPosts = await response.json();

        if(callback){
            callback(myWorkPosts);
        }
    }

    const getMyVideoWorkBuyerList = async (page, workPostId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/video/my/work/${workPostId}/buyers/${page}`);
        const myWorkBuyers = await response.json();

        if(callback){
            return callback(myWorkBuyers);
        }
    }

    const updateWorkSendStatus = async (buyWork) => {
        await fetch(`/members/video/my/work/buyers/sendStatus/update`, {
            method: "put",
            body: JSON.stringify(buyWork),
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        });
    }

    const getMyBuyVideoWorkList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/video/my/buy/work/${page}`);
        const myBuyWorkPosts = await response.json();

        if(callback){
            callback(myBuyWorkPosts);
        }
    }

    const removeBuyWorkPost = async (id) => {
        await fetch(`/members/video/my/buy/work/${id}`, {
            method: "delete"
        });
    }

//************* 펀딩 ***************

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

//************* 모집 ***************

    const getMyVideoAuditionList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/video/my/audition/${page}`);
        const myAuditionPosts = await response.json();

        console.log("myAuditionPosts : ", myAuditionPosts)

        if(callback){
            callback(myAuditionPosts);
        }
    }

    const getMyVideoAuditionApplicantList = async (page, auditionId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/video/my/audition/${auditionId}/applicants/${page}`);
        const myAuditionApplicants = await response.json();

        console.log("myAuditionApplicants : ", myAuditionApplicants)

        if(callback){
            return callback(myAuditionApplicants);
        }
    }

    const updateConfirmStatus = async (auditionApplication) => {
        await fetch(`/members/video/my/audition/applicants/confirm-status/update`, {
            method: "put",
            body: JSON.stringify(auditionApplication),
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        });
    }
    //
    const getMyVideoApplicationAuditionList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/video/my/application/audition/${page}`);
        const myApplicationAuditionPosts = await response.json();

        if(callback){
            callback(myApplicationAuditionPosts);
        }
    }
//************* 내 정보 ***************
    const getMemberProfileByMemberId = async (memberId, callback) => {
        const response = await fetch(`/members/${memberId}/profile`);
        const myProfile = await response.json();

        console.log("myProfile : ", myProfile)

        if(callback){
            callback(myProfile);
        }
    }

    const updateMemberProfileByMemberId = async (memberProfile) => {
        await fetch(`/members/profile/update`, {
            method: "put",
            body: JSON.stringify(memberProfile),
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        });
    }

//************* 문의 ***************

    const getMyInquiryHistoryList = async (page, memberId, callback) => {
        page = page || 1;
        const response = await fetch(`/members/${memberId}/inquiry/${page}`);
        const myInquiryHistories = await response.json();

        if(callback) {
            callback(myInquiryHistories);
        }
    }

    const getAdminAnswerByInquiryId = async (inquiryId, callback) => {
        const response = await fetch(`/members/inquiry/${inquiryId}/admin-answer`);
        const adminAnswer = await response.json();

        if (callback) {
            return callback(adminAnswer)
        }
    }

    const getUnreadAlarmsByCurrentMember = async ()

    return {
        getMyVideoWorkList: getMyVideoWorkList,
        getMyVideoWorkBuyerList: getMyVideoWorkBuyerList,
        updateWorkSendStatus: updateWorkSendStatus,
        getMyBuyVideoWorkList: getMyBuyVideoWorkList,
        removeBuyWorkPost: removeBuyWorkPost,
        getMyFundingList: getMyFundingList,
        getFundingBuyerList: getFundingBuyerList,
        updateFundingSendStatus: updateFundingSendStatus,
        getMyBuyFundingList: getMyBuyFundingList,
        getMyVideoAuditionList: getMyVideoAuditionList,
        getMyVideoAuditionApplicantList: getMyVideoAuditionApplicantList,
        updateConfirmStatus: updateConfirmStatus,
        getMyVideoApplicationAuditionList: getMyVideoApplicationAuditionList,
        getMemberProfileByMemberId: getMemberProfileByMemberId,
        updateMemberProfileByMemberId: updateMemberProfileByMemberId,
        getMyInquiryHistoryList: getMyInquiryHistoryList,
        getAdminAnswerByInquiryId: getAdminAnswerByInquiryId
    }
})()