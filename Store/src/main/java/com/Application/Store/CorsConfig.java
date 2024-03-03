package com.Application.Store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${UI_URL}")
    private String UI_URL;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(UI_URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE");
        // .allowedHeaders("*") // Allow all headers
        // .allowCredentials(false); // Allow credentials (if needed)
    }
}
