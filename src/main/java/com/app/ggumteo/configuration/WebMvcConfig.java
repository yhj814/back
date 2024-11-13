package com.app.ggumteo.configuration;

import com.app.ggumteo.interceptor.AlarmIntercepter;
import com.app.ggumteo.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final AlarmService alarmService;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AlarmIntercepter(alarmService)).addPathPatterns("/**");
    }
}
