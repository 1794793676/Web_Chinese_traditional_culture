package com.example.backform.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CaptchaMapper {
    int insertCaptcha(@Param("captchaKey") String captchaKey, @Param("captchaCode") String captchaCode, @Param("purpose") String purpose, @Param("expireMinutes") int expireMinutes);
    Map<String, Object> findValidCaptcha(@Param("captchaKey") String captchaKey, @Param("purpose") String purpose);
    int markUsed(@Param("captchaKey") String captchaKey);
}
