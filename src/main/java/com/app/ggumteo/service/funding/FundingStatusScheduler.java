//package com.app.ggumteo.service.funding;
//
//import com.app.ggumteo.domain.funding.FundingDTO;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FundingStatusScheduler {
//
//    @Scheduled(cron = "0 0 0 1 * ?") // 매달 1일 자정에 실행
//    public void updateFundingStatus() {
//        List<FundingDTO> fundings = fundingService.getFundings(); // 펀딩 목록 가져오기
//        LocalDate now = LocalDate.now();
//
//        for (FundingDTO funding : fundings) {
//            if (funding.getCreatedDate().plusMonths(1).isBefore(now) && funding.getFundingStatus().equals("펀딩 중")) {
//                funding.setFundingStatus("펀딩 종료");
//                fundingService.updateFundingStatusToEnded(funding);
//            }
//        }
//    }
//}
