package com.vs.wuyijobrecruitspider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/user/login").setViewName("login");
        registry.addViewController("/job/show").setViewName("query");
        registry.addViewController("/job/region").setViewName("statistics-region");
        //registry.addViewController("/job/salary").setViewName("fw");
        registry.addViewController("/job/salary").setViewName("statistics-salary");
    }
}
