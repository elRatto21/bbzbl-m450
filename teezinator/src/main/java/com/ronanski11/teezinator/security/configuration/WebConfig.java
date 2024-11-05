package com.ronanski11.teezinator.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // This will apply CORS to all the paths in your application
                .allowedOrigins("*") // Specifies the allowed origin(s)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specifies the allowed HTTP method(s)
                .allowedHeaders("*"); // Allows all headers
    }
}