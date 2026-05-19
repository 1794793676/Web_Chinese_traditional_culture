# README_BACKEND

## 技术栈
- Spring Boot
- MyBatis
- MySQL
- Gradle
- Java 17

## 目录结构
- `backform/`：后端工程
- `db/db/`：数据库脚本
- `docs/API.md`：接口文档

## 数据库初始化顺序
1. `01_create_database.sql`
2. `02_schema.sql`
3. `03_seed.sql`
4. `04_views.sql`
5. `05_queries.sql`
6. `06_mybatis_adjustments.sql`

## 数据库环境变量
- `DB_USERNAME`
- `DB_PASSWORD`

## 启动命令
### Windows
- `gradlew.bat test`
- `gradlew.bat bootRun`

### Linux/macOS
- `chmod +x gradlew`
- `./gradlew test`
- `./gradlew bootRun`

## 默认账号
- demo / demo123456
- admin / admin123456

## API 文档位置
- `docs/API.md`

## 常见问题
- MySQL 连接失败：检查 `spring.datasource.url` 与数据库是否启动。
- 数据库密码错误：检查 `DB_USERNAME/DB_PASSWORD`。
- 端口占用：修改 `server.port` 或释放 8080。
- CORS 问题：检查 `app.cors.allowed-origins` 与前端端口。
- 401/403：确认是否携带 Bearer Token、角色是否为 admin。
- Mapper 扫描重复告警：确认只保留单一 `@MapperScan`。
- 验证码错误：确认 `captchaKey/captchaCode` 匹配且未过期。

## 课程验收接口测试流程
按顺序执行：
1. GET `/api/auth/captcha?purpose=login`
2. POST `/api/auth/login`
3. GET `/api/categories`
4. GET `/api/articles/featured`
5. 未登录 GET `/api/articles/{slug}`（应 401）
6. 登录后 GET `/api/articles/{slug}`
7. POST `/api/articles/{id}/view`
8. POST `/api/articles/{id}/like`
9. 重复 POST `/api/articles/{id}/like`（不应 500）
10. DELETE `/api/articles/{id}/like`
11. POST `/api/articles/{id}/comments`
12. GET `/api/articles/{id}/comments`
13. POST `/api/articles/{id}/share`
14. demo 访问 `/api/admin/dashboard`（应 403）
15. admin 访问 `/api/admin/dashboard`
16. admin PATCH `/api/admin/comments/{id}/status`
