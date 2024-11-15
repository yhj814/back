package com.app.ggumteo.mapper.main;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

    int countMembers();

    int countWorks();

    int sumConvergePrice();

    double getAverageConvergePricePercentage();
}
