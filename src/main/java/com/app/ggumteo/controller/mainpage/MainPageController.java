//package com.app.ggumteo.controller.mainpage;
//
//import com.app.ggumteo.service.main.MainService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class MainPageController {
//
//    private final MainService mainService;
//
//    @GetMapping("/main")
//    public String getMainPage(Model model) {
//        Long totalMembers = mainService.getTotalMembers();
//        Long totalWorks = mainService.getTotalWorks();
//        Long totalConvergePrice = mainService.getTotalConvergePrice();
//        double averageConvergePricePercentage = mainService.getAverageConvergePricePercentage();
//
//        // Converge Price를 억 단위로 변환 (예: 8143000000 -> 81억)
//        double totalConvergePriceEok = totalConvergePrice / 100_000_000.0;
//        String totalConvergePriceFormatted = String.format("%.0f억", totalConvergePriceEok);
//
//        // 평균 퍼센트 포맷 (소수점 2자리)
//        String averageConvergePricePercentageFormatted = String.format("%.2f", averageConvergePricePercentage);
//
//        model.addAttribute("totalMembers", totalMembers);
//        model.addAttribute("totalWorks", totalWorks);
//        model.addAttribute("totalConvergePrice", totalConvergePriceFormatted);
//        model.addAttribute("averageConvergePricePercentage", averageConvergePricePercentageFormatted);
//
//        return "main"; // main.html 템플릿 이름
//    }
//}
