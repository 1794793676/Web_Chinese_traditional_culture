# API 文档（冻结版）
统一返回：`{"code":200,"message":"success","data":...}`。
鉴权头：`Authorization: Bearer <token>`。

## 认证
- GET `/api/auth/captcha?purpose=login|register`（免登录）
- POST `/api/auth/register`（免登录）
- POST `/api/auth/login`（免登录）
- POST `/api/auth/logout`（需登录）
- GET `/api/user/profile`（需登录）

### curl 示例
```bash
curl "http://localhost:8080/api/auth/captcha?purpose=login"
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"u1","email":"u1@test.com","password":"12345678","confirmPassword":"12345678","nickname":"u1","captchaKey":"xxx","captchaCode":"1234"}'
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"account":"demo","password":"demo123456","captchaKey":"xxx","captchaCode":"1234"}'
```

## 内容
- GET `/api/categories`（免登录）
- GET `/api/articles/featured?limit=6`（免登录）
- GET `/api/articles?category=thought&page=1&size=10`（需登录）
- GET `/api/articles/{slug}`（需登录）
- POST `/api/articles/{id}/view`（可免登录）
- POST `/api/articles/{id}/like`（需登录）
- DELETE `/api/articles/{id}/like`（需登录）
- GET `/api/articles/{id}/comments?page=1&size=10`（需登录）
- POST `/api/articles/{id}/comments`（需登录，默认 visible）
- POST `/api/articles/{id}/share`（需登录）

### 请求体示例
```json
{"content":"写得很好"}
```
```json
{"channel":"link"}
```

### 错误说明
- 400 参数错误
- 401 未登录/验证码错误
- 403 无权限
- 404 资源不存在
- 409 冲突（如重复键）
- 500 服务器错误

## 后台（均需登录+admin）
- GET `/api/admin/dashboard`
- GET `/api/admin/comments?status=&page=1&size=10`
- PATCH `/api/admin/comments/{id}/status`
- GET `/api/admin/interactions?period=30d&limit=20`

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/admin/dashboard
curl -X PATCH http://localhost:8080/api/admin/comments/1/status -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"status":"hidden","adminNote":"违规"}'
```

> 默认测试账号：`admin/admin123456`、`demo/demo123456`。
