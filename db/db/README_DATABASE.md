# 华夏文脉数据库设计说明

本目录提供《华夏文脉 · 中华优秀传统文化数字展馆》的 MySQL 8.0+ 数据库脚本。数据库名为 `heritage_culture_site`，字符集为 `utf8mb4`，排序规则为 `utf8mb4_unicode_ci`，所有表名和字段名均使用英文小写加下划线。

## 文件执行顺序

在 DataGrip 中连接 MySQL 后，按以下顺序执行：

1. `01_create_database.sql`
2. `02_schema.sql`
3. `03_seed.sql`
4. `04_views.sql`
5. `05_queries.sql`

`05_queries.sql` 是后端和后台常用 SQL 示例，文件顶部使用 MySQL 用户变量模拟参数。写入类示例被放入事务并回滚，方便整文件执行检查语法，不会破坏测试数据。实际后端接入时，可把变量替换为预编译 SQL 参数。

## 表结构说明

`users`：用户表，保存注册用户和管理员。`username`、`email` 均唯一，登录密码只保存 `password_hash`，`role` 区分普通用户和管理员，`status` 控制账号是否可用。

`captcha_codes`：验证码表，用于登录和注册验证码校验。`captcha_key` 负责前后端关联验证码，`expires_at` 判断是否过期，`used` 防止重复使用。

`categories`：文化专题分类表，保存思想理念、精神品格、器物与非遗、节日民俗、科学技艺、生态智慧 6 个专题。`slug` 用于 URL 或接口参数。

`articles`：文章表，保存专题文章的标题、摘要、正文、封面、作者、发布时间、素材来源和展示状态。`category_id` 外键关联 `categories.id`，删除分类时使用 `RESTRICT`，避免误删分类导致文章丢失。

`article_sources`：素材来源表，记录每篇文章的文字、图片、图标、字体或其他素材来源，便于课程论文和页脚展示出处。文章删除时来源记录级联删除。

`article_likes`：文章点赞表，记录用户点赞行为。通过唯一约束 `uk_article_likes_article_user` 保证同一用户对同一文章只能点赞一次。

`comments`：评论表，保存用户评论和后台审核状态。前台只展示 `status = 'visible'` 的评论；后台可将评论改为 `pending`、`hidden` 或 `deleted`。

`article_shares`：文章转发表，每次转发记录一条数据。`user_id` 允许为空，便于后续记录游客分享。

`page_views`：页面浏览记录表，用于后续统计访问量。文章页可关联 `article_id`，首页和后台页可只记录 `page_path`。

`admin_logs`：后台操作日志表，记录管理员隐藏评论、软删除评论等管理操作。

## 表关系说明

用户与评论、点赞是一对多关系：一个用户可以发表多条评论、点赞多篇文章。点赞表中 `article_id + user_id` 唯一，避免重复点赞。

分类与文章是一对多关系：一个分类下可以有多篇文章，一篇文章只能属于一个分类。分类删除时不级联删除文章，保证内容数据安全。

文章与来源、点赞、评论、转发是一对多关系：一篇文章可以有多个素材来源、多条点赞、多条评论和多条转发。文章删除时，这些强依赖数据使用级联删除。

用户与分享、浏览记录是弱关联关系：`article_shares.user_id` 和 `page_views.user_id` 允许为空，用户删除时设置为 `NULL`，保留统计记录。

## 统计视图

`v_article_stats`：按文章汇总分类名、点赞数、可见评论数、转发数、浏览量和发布时间。`comment_count` 只统计 `comments.status = 'visible'`。

`v_dashboard_stats`：后台总览单行统计，包含用户数、文章数、点赞数、评论数、转发数和分类数。

`v_latest_comments`：最新评论视图，包含评论、文章标题、用户名、状态和时间，按评论时间倒序用于后台管理。

## DataGrip 执行方法

1. 在 DataGrip 中新建 MySQL 数据源，填写主机、端口、用户名和密码。
2. 打开 `db/01_create_database.sql`，点击执行，创建并切换到 `heritage_culture_site`。
3. 依次执行 `02_schema.sql`、`03_seed.sql`、`04_views.sql`。
4. 执行 `05_queries.sql`，查看首页精选、分类文章、文章详情、评论列表、后台统计等查询结果。
5. 如果需要单独测试点赞、评论、隐藏评论等写入语句，可复制 `05_queries.sql` 中对应片段，把事务末尾的 `ROLLBACK` 改为 `COMMIT`。

## Express 后端连接说明

后续如果使用 Express，可以通过 `mysql2/promise` 连接数据库。建议在 `.env` 中保存数据库配置，不要把密码写死在代码中。

```js
import mysql from 'mysql2/promise';

const pool = mysql.createPool({
  host: process.env.DB_HOST,
  port: Number(process.env.DB_PORT || 3306),
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: 'heritage_culture_site',
  charset: 'utf8mb4',
  waitForConnections: true,
  connectionLimit: 10
});
```

用户登录时根据用户名或邮箱查询 `users`，只允许 `status = 'active'` 的用户登录，再用 bcrypt 校验提交密码与 `password_hash`。验证码校验应查询 `captcha_codes` 中未使用、未过期且用途匹配的记录，校验成功后把 `used` 更新为 `1`。

## 论文说明参考

数据库设计围绕“用户、内容、互动、统计、后台管理”五类数据展开。`users` 支撑注册、登录和角色权限控制；`categories` 与 `articles` 支撑文化专题和文章展示；`article_likes`、`comments`、`article_shares` 支撑点赞、评论和转发互动；`v_article_stats`、`v_dashboard_stats`、`v_latest_comments` 为后台总览、热门排行和评论管理提供直接数据来源；`admin_logs` 记录后台操作，增强系统管理说明的完整性。

功能设计上，游客只访问首页，登录用户可浏览专题和文章详情并进行点赞、评论、转发，管理员通过 `role = 'admin'` 进入后台查看统计数据并管理评论状态。评论采用软删除和隐藏机制，既满足前台展示控制，也保留后台审查记录。
