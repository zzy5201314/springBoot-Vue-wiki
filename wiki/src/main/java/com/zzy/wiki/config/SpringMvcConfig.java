package com.zzy.wiki.config;

import com.zzy.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor logInterceptor;

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/redis/**",
                        "/user/login",
                        "/user/logout/**",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/vote/**",
                        "/doc/find-content/**",
                        "/ebook-snapshot/**"
                );
    }
}
