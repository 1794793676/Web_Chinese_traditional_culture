package com.example.backform.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class CorsConfig implements WebMvcConfigurer {
 @Value("${app.cors.allowed-origins:*}") String origins;
 public void addCorsMappings(CorsRegistry reg){ var c=reg.addMapping("/**").allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS").allowedHeaders("Authorization","Content-Type"); if("*".equals(origins)) c.allowedOriginPatterns("*"); else c.allowedOrigins(origins.split(",")); }
}
