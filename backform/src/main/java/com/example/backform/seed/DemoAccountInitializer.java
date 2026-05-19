package com.example.backform.seed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class DemoAccountInitializer implements CommandLineRunner {
 private final JdbcTemplate jdbc; @Value("${app.seed.fix-demo-passwords:true}") boolean fix;
 public DemoAccountInitializer(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public void run(String... args){ if(!fix) return; fixOne("admin","admin123456"); fixOne("demo","demo123456"); }
 private void fixOne(String u,String p){ var list=jdbc.queryForList("select id,password_hash from users where username=? limit 1",u); if(list.isEmpty()) return; String h=(String)list.get(0).get("password_hash"); BCryptPasswordEncoder e=new BCryptPasswordEncoder(); if(h==null||!h.startsWith("$2")||!e.matches(p,h)){ jdbc.update("update users set password_hash=? where username=?",e.encode(p),u);} }
}
