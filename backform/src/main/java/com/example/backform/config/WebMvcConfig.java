package com.example.backform.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 private final AuthInterceptor i; public WebMvcConfig(AuthInterceptor i){this.i=i;}
 public void addInterceptors(InterceptorRegistry r){ r.addInterceptor(i).addPathPatterns("/api/**"); }
}
