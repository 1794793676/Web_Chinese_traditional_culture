# 华夏文脉 · 中华优秀传统文化数字展馆

《Web 开发技术》课程设计第一阶段重构成果。

## 项目结构

```text
heritage-culture-site/
├── index.html
├── pages/
│   ├── login.html
│   ├── register.html
│   ├── category.html
│   ├── detail.html
│   ├── no-access.html
│   └── sources.html
├── admin/
│   ├── dashboard.html
│   ├── comments.html
│   └── interactions.html
├── assets/
│   ├── css/
│   │   ├── base.css
│   │   ├── layout.css
│   │   ├── components.css
│   │   └── responsive.css
│   ├── js/
│   │   ├── main.js
│   │   └── auth-placeholder.js
│   └── images/
└── README.md
```

## 页面整理关系

- `desktop/code.html` 与 `mobile/code.html` 已合并为响应式 `index.html`
- `_1/code.html` 对应 `pages/register.html`
- `_2/code.html` 对应 `pages/login.html`
- `_3/code.html` 对应 `pages/category.html`
- `_4/code.html` 对应 `pages/detail.html`
- `_5/code.html` 对应 `pages/no-access.html`
- `_6/code.html` 对应 `admin/dashboard.html`
- `_7/code.html` 对应 `admin/comments.html`
- `_8/code.html` 对应 `admin/interactions.html`

## 当前阶段完成情况

- 已建立正式语义化目录，不再以 `_1`、`_2`、`desktop`、`mobile` 作为正式页面目录。
- 首页已合并为单一响应式页面，使用 CSS media query 适配桌面、平板和移动端。
- 首页栏目统一为：思想理念、精神品格、器物与非遗、节日民俗、科学技艺、生态智慧。
- 首页标题、副标题、精选文章和时间轴内容已统一。
- 公共样式已拆分到 `assets/css/`。
- 基础脚本已拆分到 `assets/js/`，包含移动端汉堡菜单和游客状态占位。
- 页面导航、登录、注册、专题、详情、后台总览等链接已改为正式相对路径。

## 后续阶段建议

- 将远程示意图替换为本地授权图片，统一放入 `assets/images/`。
- 增加真实内容数据、权限控制和后端接口。
- 为表单与后台操作补充真实业务逻辑。
