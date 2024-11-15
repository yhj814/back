package com.app.ggumteo.repository.main;

import com.app.ggumteo.domain.main.MainDTO;
import com.app.ggumteo.mapper.main.MainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MainDAO {

    public final MainMapper mainMapper;

    public int countMembers() {
        return mainMapper.countMembers();
    }

    public int countWorks(){
        return mainMapper.countWorks();
    }

    public int sumConvergePrice(){
        return mainMapper.sumConvergePrice();
    }

    public double getAverageConvergePricePercentage(){
        return mainMapper.getAverageConvergePricePercentage();
    }

}
