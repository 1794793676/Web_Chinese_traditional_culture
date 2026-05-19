# API 文档（后端字段冻结版）

## 1. 统一响应结构 ApiResponse
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 2. HTTP 状态码与 JSON code 对照
- 200：成功
- 400：参数错误
- 401：未登录 / Token 无效 / 验证码错误
- 403：权限不足
- 404：资源不存在
- 409：冲突
- 500：服务端错误

## 3. Token 使用方式
`Authorization: Bearer <token>`

## 4. 权限说明
- `public`：免登录
- `login`：需登录
- `admin`：需管理员

## 5. 请求字段冻结
- LoginRequest：`username,password,captchaKey,captchaCode`
- RegisterRequest：`username,email,password,confirmPassword,nickname,captchaKey,captchaCode`
- CommentCreateRequest：`content`
- ShareCreateRequest：`channel`
- CommentStatusUpdateRequest：`status,adminNote`

## 6. 响应字段冻结
以 DTO 为准；核心：`UserProfileResponse.avatarUrl`、`ArticleSourceResponse.id/articleId/sourceTitle/sourceUrl/sourceType`。

---

> 说明：下列每个接口均给出用途、Method、URL、权限、参数、示例与 curl。

## Auth
### GET /api/auth/captcha
- 用途：获取验证码
- Method：GET
- URL：`/api/auth/captcha?purpose=login`
- 权限：public
- Query：`purpose` (login/register)
- Body：无
- Success：`data.captchaKey,captchaImage,expireAt`
- Error：400/500
- curl：`curl "http://localhost:8080/api/auth/captcha?purpose=login"`

### POST /api/auth/register
- 用途：注册
- Method：POST
- URL：`/api/auth/register`
- 权限：public
- Body 字段：`username,email,password,confirmPassword,nickname,captchaKey,captchaCode`
- Request 示例：`{"username":"u1","email":"u1@test.com","password":"12345678","confirmPassword":"12345678","nickname":"u1","captchaKey":"k","captchaCode":"1234"}`
- Success：200
- Error：400/409
- curl：`curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"u1","email":"u1@test.com","password":"12345678","confirmPassword":"12345678","nickname":"u1","captchaKey":"k","captchaCode":"1234"}'`

### POST /api/auth/login
- 用途：登录获取 token
- Method：POST
- URL：`/api/auth/login`
- 权限：public
- Body：`username,password,captchaKey,captchaCode`
- Success：`data.token,data.user`
- Error：400/401
- curl：`curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"demo","password":"demo123456","captchaKey":"k","captchaCode":"1234"}'`

### POST /api/auth/logout
- 用途：退出登录
- Method：POST
- URL：`/api/auth/logout`
- 权限：login
- Header：`Authorization: Bearer <token>`
- Body：无
- Success：200
- Error：401
- curl：`curl -X POST http://localhost:8080/api/auth/logout -H "Authorization: Bearer $TOKEN"`

## User
### GET /api/user/profile
- 用途：当前用户资料
- Method：GET
- URL：`/api/user/profile`
- 权限：login
- Success 字段：`id,username,nickname,email,role,avatarUrl,createdAt,lastLoginAt`
- Error：401/404
- curl：`curl http://localhost:8080/api/user/profile -H "Authorization: Bearer $TOKEN"`

## Content
### GET /api/categories
public；返回 `id,name,slug,description`

### GET /api/articles/featured
public；返回 `ArticleCardResponse[]`

### GET /api/articles
- URL：`/api/articles?category={slug}&page=1&size=10`
- 权限：login
- 返回：分页 `ArticleCardResponse`

### GET /api/articles/{slug}
- 权限：login
- 返回：`ArticleDetailResponse`（含 `sources`）

### POST /api/articles/{id}/view
- 权限：public
- 返回：`InteractionCountResponse`

### POST /api/articles/{id}/like
- 权限：login
- 幂等：重复点赞不 500

### DELETE /api/articles/{id}/like
- 权限：login
- 幂等：重复取消不 500

### GET /api/articles/{id}/comments
- 权限：login
- Query：`page,size`
- 返回：分页 `CommentResponse`

### POST /api/articles/{id}/comments
- 权限：login
- Body：`{"content":"评论内容"}`

### POST /api/articles/{id}/share
- 权限：login
- Body：`{"channel":"link"}`

## Admin
### GET /api/admin/dashboard
admin；返回 `DashboardResponse`

### GET /api/admin/comments
admin；Query：`status,page,size`

### PATCH /api/admin/comments/{id}/status
admin；Body：`status,adminNote`

### GET /api/admin/interactions
admin；Query：`limit`

## TOKEN 与管理员 curl 示例
```bash
# 先登录普通用户
TOKEN="<login返回token>"
# 管理员 token
ADMIN_TOKEN="<admin登录返回token>"

curl http://localhost:8080/api/categories
curl http://localhost:8080/api/articles/featured
curl http://localhost:8080/api/articles/slug-1 -H "Authorization: Bearer $TOKEN"
curl -X POST http://localhost:8080/api/articles/1/like -H "Authorization: Bearer $TOKEN"
curl -X DELETE http://localhost:8080/api/articles/1/like -H "Authorization: Bearer $TOKEN"
curl -X PATCH http://localhost:8080/api/admin/comments/1/status -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" -d '{"status":"hidden","adminNote":"违规"}'
```
