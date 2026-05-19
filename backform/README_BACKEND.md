# Backform 后端说明

## 技术栈
- Spring Boot 3
- MyBatis（XML Mapper）
- MySQL 8
- BCrypt

## 数据库初始化顺序
1. db/db/01_create_database.sql
2. db/db/02_schema.sql
3. db/db/03_seed.sql
4. db/db/04_views.sql
5. db/db/05_queries.sql
6. db/db/06_mybatis_adjustments.sql

## 环境变量
- DB_USERNAME
- DB_PASSWORD

## 启动
- Windows: `gradlew.bat bootRun`
- Linux/macOS: `./gradlew bootRun`

## 默认测试账号
- admin / admin123456
- demo / demo123456

## API 文档
- `docs/API.md`

## curl 示例
```bash
curl "http://localhost:8080/api/auth/captcha?purpose=login"
```

## 常见问题
- MySQL 密码错误：检查 DB_USERNAME/DB_PASSWORD。
- 端口占用：修改 `server.port`。
- CORS：确保前端开发地址为 `http://localhost:5173`。
- 验证码错误：先请求新验证码再登录。
- 401/403：检查 token 或管理员角色。
