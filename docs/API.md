# API 文档

## 统一响应结构
```json
{"code":200,"message":"success","data":{}}
```

## 错误码与 HTTP 状态
- 400 参数错误
- 401 未登录/Token 无效/验证码错误
- 403 权限不足
- 404 资源不存在
- 409 数据冲突
- 500 服务器错误

## Token
请求头：`Authorization: Bearer <token>`

## 认证接口
### GET /api/auth/captcha?purpose=login
- 权限：免登录
- 示例响应：
```json
{"code":200,"message":"success","data":{"captchaKey":"xxx","captchaImage":"data:image/svg+xml;base64,...","expireAt":"2026-05-19T10:00:00"}}
```

### POST /api/auth/register
- 权限：免登录
- 请求体字段：username,email,password,confirmPassword,nickname,captchaKey,captchaCode
- 示例请求：
```json
{"username":"demo","password":"demo123456","captchaKey":"xxx","captchaCode":"1234","confirmPassword":"demo123456","email":"demo@test.com","nickname":"demo"}
```

### POST /api/auth/login
- 权限：免登录
- 请求体字段：username,password,captchaKey,captchaCode

### POST /api/auth/logout
- 权限：需登录

### GET /api/user/profile
- 权限：需登录

## 内容接口
- GET /api/categories（免登录）
- GET /api/articles/featured（免登录）
- GET /api/articles?category=slug&page=1&size=10（需登录）
- GET /api/articles/{slug}（需登录）
- POST /api/articles/{id}/view（免登录）
- POST /api/articles/{id}/like（需登录，幂等）
- DELETE /api/articles/{id}/like（需登录，幂等）
- GET /api/articles/{id}/comments（需登录）
- POST /api/articles/{id}/comments（需登录）
- POST /api/articles/{id}/share（需登录）

评论请求体：
```json
{"content":"写得很好"}
```
分享请求体：
```json
{"channel":"link"}
```

## 后台接口（均需 admin）
- GET /api/admin/dashboard
- GET /api/admin/comments
- PATCH /api/admin/comments/{id}/status
- GET /api/admin/interactions

PATCH 请求体：
```json
{"status":"hidden","adminNote":"违规"}
```

## curl 示例

### Linux / macOS (bash)
```bash
curl "http://localhost:8080/api/auth/captcha?purpose=login"
curl -X POST "http://localhost:8080/api/auth/register" -H "Content-Type: application/json" -d '{"username":"u1","email":"u1@test.com","password":"12345678","confirmPassword":"12345678","nickname":"u1","captchaKey":"xxx","captchaCode":"1234"}'
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d '{"username":"demo","password":"demo123456","captchaKey":"xxx","captchaCode":"1234"}'
```

### Windows CMD
> Windows CMD 不支持单引号包裹 JSON，请使用双引号并转义内部双引号。
```bat
curl "http://localhost:8080/api/auth/captcha?purpose=login"
curl -X POST "http://localhost:8080/api/auth/register" -H "Content-Type: application/json" -d "{\"username\":\"u1\",\"email\":\"u1@test.com\",\"password\":\"12345678\",\"confirmPassword\":\"12345678\",\"nickname\":\"u1\",\"captchaKey\":\"xxx\",\"captchaCode\":\"1234\"}"
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"demo\",\"password\":\"demo123456\",\"captchaKey\":\"xxx\",\"captchaCode\":\"1234\"}"
```
