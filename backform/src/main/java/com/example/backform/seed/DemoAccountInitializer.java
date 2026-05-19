package com.example.backform.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DemoAccountInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DemoAccountInitializer.class);

    private final JdbcTemplate jdbc;

    @Value("${app.seed.fix-demo-passwords:true}")
    private boolean fix;

    public DemoAccountInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        if (!fix) {
            return;
        }
        try {
            fixOne("admin", "admin123456");
            fixOne("demo", "demo123456");
        } catch (CannotGetJdbcConnectionException ex) {
            log.warn("跳过演示账号密码修复：数据库连接失败，请检查 spring.datasource.username/password 配置。{}", ex.getMostSpecificCause().getMessage());
        }
    }

    private void fixOne(String username, String rawPassword) {
        var list = jdbc.queryForList("select id,password_hash from users where username=? limit 1", username);
        if (list.isEmpty()) {
            return;
        }
        String hash = (String) list.get(0).get("password_hash");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (hash == null || !hash.startsWith("$2") || !encoder.matches(rawPassword, hash)) {
            jdbc.update("update users set password_hash=? where username=?", encoder.encode(rawPassword), username);
        }
    }
}
