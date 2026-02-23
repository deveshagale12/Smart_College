package com.smartCollege;

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
		/* registry.addInterceptor(apiKeyInterceptor) */
                //.addPathPatterns("/college/students/**"); 
                // REMOVE .addPathPatterns("/college/academic/**")
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Adding the photo resource handler here as well
        registry.addResourceHandler("/user-photos/**")
                .addResourceLocations("file:user-photos/");
    }
    
    
}