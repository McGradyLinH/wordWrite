package com.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Component
public class MyWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/login",
                "/js/**","/css/**","/images/**","/fonts/**","/kaptcha","/addUser","/register");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/correctindex").setViewName("teacher/Index");
    	registry.addViewController("/check").setViewName("student/Stucheck");
    }
}
