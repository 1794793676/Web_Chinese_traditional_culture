# Backform 后端说明

## 技术栈
Spring Boot + MyBatis + MySQL

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

## 测试账号
- admin / admin123456
- demo / demo123456

## API 文档
- docs/API.md

## 常见问题
- MySQL 密码错误：检查 DB_USERNAME、DB_PASSWORD。
- 端口占用：修改 server.port 或释放端口。
- 401/403：检查 Bearer Token 和用户角色。
- CORS：确认前端地址是 http://localhost:5173。
- 验证码错误：先获取新验证码，再提交登录/注册。
