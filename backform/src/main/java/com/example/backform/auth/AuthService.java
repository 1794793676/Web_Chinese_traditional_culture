package com.example.backform.auth;
import com.example.backform.common.BusinessException;
import com.example.backform.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class AuthService {
 private final UserRepository repo; private final CaptchaService cap; private final TokenService token; private final BCryptPasswordEncoder enc=new BCryptPasswordEncoder();
 public AuthService(UserRepository repo, CaptchaService cap, TokenService token){this.repo=repo;this.cap=cap;this.token=token;}
 public Map<String,Object> register(Map<String,String> r){ if(!r.get("password").equals(r.get("confirmPassword"))) throw new BusinessException(400,"两次密码不一致"); if(r.get("captchaKey")!=null&&!r.get("captchaKey").isBlank()) cap.verify(r.get("captchaKey"),r.get("captchaCode"),"register"); if(repo.existsUsername(r.get("username"))) throw new BusinessException(409,"用户名已存在"); if(repo.existsEmail(r.get("email"))) throw new BusinessException(409,"邮箱已存在"); repo.insert(r.get("username"),r.get("email"),enc.encode(r.get("password")),r.get("nickname")); var u=repo.findByAccount(r.get("username")); return Map.of("id",u.get("id"),"username",u.get("username"),"email",u.get("email"),"nickname",u.get("nickname"),"role",u.get("role"),"createdAt",u.get("created_at").toString()); }
 public Map<String,Object> login(Map<String,String> req){ cap.verify(req.get("captchaKey"),req.get("captchaCode"),"login"); var u=repo.findByAccount(req.get("account")); if(u==null) throw new BusinessException(401,"用户名或密码错误"); if(!"active".equals(u.get("status"))) throw new BusinessException(403,"该账号已被禁用"); if(!enc.matches(req.get("password"),(String)u.get("password_hash"))) throw new BusinessException(401,"用户名或密码错误"); CurrentUser cu=new CurrentUser(((Number)u.get("id")).longValue(),(String)u.get("username"),(String)u.get("nickname"),(String)u.get("email"),(String)u.get("role"),(String)u.get("avatar_url")); String t=token.issue(cu); repo.updateLogin(cu.id()); return Map.of("token",t,"user",cu); }
}
