package com.example.backform.auth;

import com.example.backform.common.BusinessException;
import com.example.backform.mapper.CaptchaMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class CaptchaService {
 private final CaptchaMapper captchaMapper; private final Random r=new Random(); @Value("${app.captcha.expire-minutes:5}") int exp;
 public CaptchaService(CaptchaMapper captchaMapper){this.captchaMapper=captchaMapper;}
 public Map<String,Object> create(String purpose){ String code=""+(1000+r.nextInt(9000)); String key= UUID.randomUUID().toString(); captchaMapper.insertCaptcha(key,code,purpose,exp); String svg="<svg xmlns='http://www.w3.org/2000/svg' width='120' height='40'><text x='15' y='28' font-size='24'>"+code+"</text></svg>"; String b64="data:image/svg+xml;base64,"+ Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8)); return Map.of("captchaKey",key,"imageBase64",b64,"expiresIn",exp*60); }
 public void verify(String key,String code,String purpose){ var row=captchaMapper.findValidCaptcha(key,purpose); if(row==null) throw new BusinessException(401,"验证码错误或已过期"); String real=(String)row.get("captcha_code"); if(!real.equalsIgnoreCase(code)) throw new BusinessException(401,"验证码错误或已过期"); captchaMapper.markUsed(key); }
}
