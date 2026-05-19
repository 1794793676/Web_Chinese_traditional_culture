package com.example.backform.auth;

import com.example.backform.common.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CaptchaService {
 private final JdbcTemplate jdbc; private final Random r=new Random(); @Value("${app.captcha.expire-minutes:5}") int exp;
 public CaptchaService(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public Map<String,Object> create(String purpose){ String code=""+(1000+r.nextInt(9000)); String key=UUID.randomUUID().toString(); jdbc.update("insert into captcha_codes(captcha_key,captcha_code,purpose,expires_at) values(?,?,?,date_add(now(), interval ? minute))",key,code,purpose,exp); String svg="<svg xmlns='http://www.w3.org/2000/svg' width='120' height='40'><text x='15' y='28' font-size='24'>"+code+"</text></svg>"; String b64="data:image/svg+xml;base64,"+Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8)); return Map.of("captchaKey",key,"imageBase64",b64,"expiresIn",exp*60); }
 public void verify(String key,String code,String purpose){ var list=jdbc.queryForList("select * from captcha_codes where captcha_key=? and purpose=? and used=0 and expires_at>now() limit 1",key,purpose); if(list.isEmpty()) throw new BusinessException(401,"验证码错误或已过期"); String real=(String)list.get(0).get("captcha_code"); if(!real.equalsIgnoreCase(code)) throw new BusinessException(401,"验证码错误或已过期"); jdbc.update("update captcha_codes set used=1 where captcha_key=?",key); }
}
