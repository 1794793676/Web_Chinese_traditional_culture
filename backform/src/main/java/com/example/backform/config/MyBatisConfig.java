package com.example.backform.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.backform.mapper")
public class MyBatisConfig {
}
