package com.example.backform.seed;

import com.example.backform.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DemoAccountInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DemoAccountInitializer.class);

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.seed.fix-demo-passwords:true}")
    private boolean enabled;

    public DemoAccountInitializer(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void run(String... args) {
        if (!enabled) {
            return;
        }
        fixPassword("admin", "admin123456");
        fixPassword("demo", "demo123456");
    }

    private void fixPassword(String username, String rawPassword) {
        var userRow = userMapper.findByAccount(username);
        if (userRow == null) {
            return;
        }
        String passwordHash = String.valueOf(userRow.get("password_hash"));
        if (!passwordHash.startsWith("$2") || !passwordEncoder.matches(rawPassword, passwordHash)) {
            userMapper.updatePasswordHashByUsername(username, passwordEncoder.encode(rawPassword));
            log.info("已修复演示账号密码: {}", username);
        }
    }
}
