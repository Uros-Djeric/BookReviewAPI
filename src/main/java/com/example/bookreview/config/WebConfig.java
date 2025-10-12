package com.example.bookreview.config;

import com.example.bookreview.security.RequestSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestSanitizer requestSanitizer;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestSanitizer);
    }
}
