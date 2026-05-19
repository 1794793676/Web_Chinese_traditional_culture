package com.example.backform.user;

import com.example.backform.auth.CurrentUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
  private final JdbcTemplate jdbc;
  public UserRepository(JdbcTemplate jdbc){this.jdbc=jdbc;}
  public Map<String,Object> findByAccount(String a){ var l=jdbc.queryForList("select * from users where username=? or email=? limit 1",a,a); return l.isEmpty()?null:l.get(0);} 
  public boolean existsUsername(String u){ return jdbc.queryForObject("select count(*) from users where username=?",Long.class,u)>0;}
  public boolean existsEmail(String e){ return jdbc.queryForObject("select count(*) from users where email=?",Long.class,e)>0;}
  public void insert(String u,String e,String p,String n){ jdbc.update("insert into users(username,email,password_hash,nickname,role,status) values(?,?,?,?, 'user','active')",u,e,p,n);} 
  public CurrentUser byId(Long id){ return jdbc.queryForObject("select id,username,nickname,email,role,avatar_url from users where id=?",(rs,rn)->new CurrentUser(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)),id);}
  public void updateLogin(Long id){ jdbc.update("update users set last_login_at=now() where id=?",id);} 
}
