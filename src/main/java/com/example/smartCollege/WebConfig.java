package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Updated path pattern to match your new "/college" prefix
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/college/students/**")
                .addPathPatterns("/college/academic/**"); // Also protect academic records
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Adding the photo resource handler here as well
        registry.addResourceHandler("/user-photos/**")
                .addResourceLocations("file:user-photos/");
    }
}