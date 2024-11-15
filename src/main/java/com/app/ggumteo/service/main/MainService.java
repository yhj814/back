package com.app.ggumteo.service.main;

public interface MainService {
    /**
     * 총 회원 수를 조회합니다.
     */
    int getTotalMembers();

    /**
     * 총 작업 수를 조회합니다.
     */
    int getTotalWorks();

    /**
     * converge_price의 총 합계를 조회합니다.
     */
    int getTotalConvergePrice();

    /**
     * converge_price 퍼센트의 평균값을 조회합니다.
     */
    double getAverageConvergePricePercentage();
}
