package com.mxworld.mxworld.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     // Serve files from the uploads directory with proper caching
    //     registry.addResourceHandler("/files/**")
    //             .addResourceLocations("file:uploads/")
    //             .setCachePeriod(3600)
    //             .resourceChain(true);
    // }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}