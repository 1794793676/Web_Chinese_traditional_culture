package com.example.backform.auth;
import com.example.backform.common.BusinessException;
import com.example.backform.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class AuthService {
 private final UserMapper userMapper; private final CaptchaService cap; private final TokenService token; private final BCryptPasswordEncoder enc=new BCryptPasswordEncoder();
 public AuthService(UserMapper userMapper, CaptchaService cap, TokenService token){this.userMapper=userMapper;this.cap=cap;this.token=token;}
 public Map<String,Object> register(Map<String,String> r){ if(!r.get("password").equals(r.get("confirmPassword"))) throw new BusinessException(400,"两次密码不一致"); if(r.get("captchaKey")!=null&&!r.get("captchaKey").isBlank()) cap.verify(r.get("captchaKey"),r.get("captchaCode"),"register"); if(userMapper.countByUsername(r.get("username"))>0) throw new BusinessException(409,"用户名已存在"); if(userMapper.countByEmail(r.get("email"))>0) throw new BusinessException(409,"邮箱已存在"); userMapper.insertUser(r.get("username"),r.get("email"),enc.encode(r.get("password")),r.get("nickname")); var u=userMapper.findByAccount(r.get("username")); return Map.of("id",u.get("id"),"username",u.get("username"),"email",u.get("email"),"nickname",u.get("nickname"),"role",u.get("role"),"createdAt",u.get("created_at").toString()); }
 public Map<String,Object> login(Map<String,String> req){ cap.verify(req.get("captchaKey"),req.get("captchaCode"),"login"); var u=userMapper.findByAccount(req.get("account")); if(u==null) throw new BusinessException(401,"用户名或密码错误"); if(!"active".equals(u.get("status"))) throw new BusinessException(403,"该账号已被禁用"); if(!enc.matches(req.get("password"),(String)u.get("password_hash"))) throw new BusinessException(401,"用户名或密码错误"); CurrentUser cu=new CurrentUser(((Number)u.get("id")).longValue(),(String)u.get("username"),(String)u.get("nickname"),(String)u.get("email"),(String)u.get("role"),(String)u.get("avatar_url")); String t=token.issue(cu); userMapper.updateLastLogin(cu.id()); return Map.of("token",t,"user",cu); }
}
