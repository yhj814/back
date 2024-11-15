package com.app.ggumteo.service.main;

import com.app.ggumteo.repository.main.MainDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final MainDAO mainDAO;

    @Override
    public int getTotalMembers() {
        return mainDAO.countMembers();
    }

    @Override
    public int getTotalWorks() {
        return mainDAO.countWorks();
    }

    @Override
    public int getTotalConvergePrice() {
        return mainDAO.sumConvergePrice();
    }

    @Override
    public double getAverageConvergePricePercentage() {
        return mainDAO.getAverageConvergePricePercentage();
    }
}
