package com.zsl.upmall.intercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
        registry.addInterceptor(centerAdminLoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/order/balance-notify/**")
                .excludePathPatterns("/order/pay-notify/**")
                .excludePathPatterns("/groupon/test/**")
                .excludePathPatterns("/groupon/push/**")
                .excludePathPatterns("/groupon/shoudong/**")
                .excludePathPatterns("/order/refund-notify/**")
                .excludePathPatterns("/test/send/**");
    }

    @Bean
    public CenterAdminLoginInterceptor centerAdminLoginInterceptor() {
        return new CenterAdminLoginInterceptor();
    }
}
