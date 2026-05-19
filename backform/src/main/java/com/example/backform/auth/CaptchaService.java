package com.example.backform.auth;

import com.example.backform.auth.dto.CaptchaResponse;
import com.example.backform.common.BusinessException;
import com.example.backform.mapper.CaptchaMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class CaptchaService {

    private static final Set<String> VALID_PURPOSES = Set.of("login", "register");
    private static final String CAPTCHA_INVALID_MESSAGE = "验证码错误或已过期";

    private final CaptchaMapper captchaMapper;
    private final Random random = new Random();

    @Value("${app.captcha.expire-minutes:5}")
    private int expireMinutes;

    public CaptchaService(CaptchaMapper captchaMapper) {
        this.captchaMapper = captchaMapper;
    }

    public CaptchaResponse create(String purpose) {
        validatePurpose(purpose);
        String captchaCode = String.valueOf(1000 + random.nextInt(9000));
        String captchaKey = UUID.randomUUID().toString();
        captchaMapper.insertCaptcha(captchaKey, captchaCode, purpose, expireMinutes);

        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='120' height='40'><text x='15' y='28' font-size='24'>"
                + captchaCode
                + "</text></svg>";
        String captchaImage = "data:image/svg+xml;base64,"
                + Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
        String expireAt = LocalDateTime.now().plusMinutes(expireMinutes).toString();
        return new CaptchaResponse(captchaKey, captchaImage, expireAt);
    }

    public void verify(String captchaKey, String captchaCode, String purpose) {
        validatePurpose(purpose);
        Map<String, Object> captchaRow = captchaMapper.findValidCaptcha(captchaKey, purpose);
        if (captchaRow == null) {
            throw new BusinessException(401, CAPTCHA_INVALID_MESSAGE, HttpStatus.UNAUTHORIZED);
        }
        String realCode = String.valueOf(captchaRow.get("captcha_code"));
        if (!realCode.equalsIgnoreCase(captchaCode)) {
            throw new BusinessException(401, CAPTCHA_INVALID_MESSAGE, HttpStatus.UNAUTHORIZED);
        }
        captchaMapper.markUsed(captchaKey);
    }

    private void validatePurpose(String purpose) {
        if (!VALID_PURPOSES.contains(purpose)) {
            throw new BusinessException(400, "purpose 只允许 login 或 register", HttpStatus.BAD_REQUEST);
        }
    }
}
