# frontform 前端说明

## 1. 技术栈
Vue 3 + Vite + Vue Router + Pinia + Axios。

## 2. 目录结构
见 `src/` 下 api/router/stores/components/views 分层。

## 3. 安装依赖
```bash
npm install
```

## 4. 启动
```bash
npm run dev
```

## 5. 构建
```bash
npm run build
```

## 6. 预览
```bash
npm run preview
```

## 7. API 代理说明
Vite 已配置 `/api -> http://localhost:8080`。

## 8. 后端启动要求
先启动 `backform`（Spring Boot）再启动前端。

## 9. 登录状态说明
`heritage_token`、`heritage_user` 存储在 localStorage，刷新后可恢复。

## 10. 路由权限矩阵
- public: `/` `/login` `/register` `/no-access`
- requiresAuth: `/category/:slug` `/article/:slug` `/sources`
- requiresAdmin: `/admin/dashboard` `/admin/comments` `/admin/interactions`

> 课程要求游客只能访问首页，因此 `/sources` 明确为 requiresAuth。

## 11. 页面功能说明
首页分类/精选、登录注册验证码、分类列表、文章详情互动、后台总览/评论管理/互动排行。

## 12. 默认测试账号
请使用后端初始化账号（普通用户+admin）。

## 13. 前端验收流程
按课程清单执行：游客访问、登录、互动、后台权限、退出后拦截。

## 14. 常见问题
- 端口占用：修改 Vite 端口或释放 5173。
- 401：token 失效，系统会清理并跳登录。
- 403：无权限，自动跳 no-access。
- 验证码不显示：检查后端 captcha 接口与图片字段。
- 后端未启动：前端会报网络错误。
- CORS/proxy：确认 `vite.config.js` 代理与后端跨域配置。


## 15. 图片资源加载说明
- 课程展示图片统一放在 `frontform/public/assets/images/`，前端直接通过 `/assets/images/...` 访问。
- `src/utils/url.js` 会对 `/assets/` 路径原样返回，不再拼接后端地址。
- 首页与登录页背景图使用本地 SVG：`hero-heritage.svg`、`auth-heritage.svg`，不依赖外链。

## 16. 已初始化数据库的修复步骤
如果数据库已经执行过旧版本 seed（包含 `.jpg/.png` 路径），请额外执行：

```bash
mysql -u <user> -p heritage_culture_site < db/db/06_fix_image_paths.sql
```

该脚本仅更新图片路径，不会修改用户、文章正文、评论、点赞、转发等业务数据。
