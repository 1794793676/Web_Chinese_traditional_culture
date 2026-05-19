# README_BACKEND

## 技术栈
Spring Boot 3.5.14, Java 17, Gradle, MySQL 8, JdbcTemplate, Bean Validation, BCrypt。

## 数据库脚本顺序
1. db/db/01_create_database.sql
2. db/db/02_schema.sql
3. db/db/03_seed.sql
4. db/db/04_views.sql
5. db/db/05_queries.sql

## MySQL 配置
修改 `backform/src/main/resources/application.properties` 中的 `spring.datasource.*`。

## 启动
```bash
cd backform
./gradlew bootRun
```
Windows:
```bat
cd backform
gradlew.bat bootRun
```

## 演示账号
管理员：admin / admin123456
普通用户：demo / demo123456

## 主要接口
- /api/auth/captcha
- /api/auth/register
- /api/auth/login
- /api/auth/logout
- /api/user/profile
- /api/categories
- /api/articles/featured
- /api/articles
- /api/admin/dashboard

## curl 示例
```bash
curl "http://localhost:8080/api/auth/captcha?purpose=login"

curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"account":"demo","password":"demo123456","captchaKey":"替换为验证码key","captchaCode":"替换为验证码"}'
```
