package com.app.ggumteo.aspect;

import com.app.ggumteo.domain.funding.MyFundingListDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Slf4j
@Configuration
public class LogAspect {
    @AfterReturning(value = "@annotation(com.app.ggumteo.aspect.annotation.MyPageLogStatus)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, MyFundingListDTO returnValue) {
        log.info("method: {}", joinPoint.getSignature().getName());
        log.info("arguments: {}", joinPoint.getArgs().toString()); //단일객체일 때는...?
        log.info("returnValue: {}", returnValue);
    }
}
