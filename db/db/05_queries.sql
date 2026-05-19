USE heritage_culture_site;

-- 示例参数：后端实际调用时可替换为请求参数或预编译 SQL 参数。
SET @article_id := (SELECT id FROM articles WHERE slug = 'people-as-foundation');
SET @article_slug := 'people-as-foundation';
SET @category_slug := 'thought';
SET @user_id := (SELECT id FROM users WHERE username = 'demo');
SET @login_account := 'demo';
SET @captcha_key := 'login-demo-key-001';
SET @captcha_code := '8392';
SET @share_channel := 'link';
SET @new_comment_content := '这是一条用于联调的评论。';
SET @comment_status := NULL;
SET @target_comment_id_to_hide := 1;
SET @target_comment_id_to_delete := 3;

-- 1. 获取首页精选文章：已发布文章，带分类名和互动统计，按发布时间倒序取 6 条。
SELECT
  a.id,
  a.title,
  a.slug,
  a.summary,
  a.cover_image,
  a.published_at,
  s.category_id,
  s.category_name,
  s.like_count,
  s.comment_count,
  s.share_count,
  s.view_count
FROM articles a
INNER JOIN v_article_stats s ON s.article_id = a.id
WHERE a.status = 'published'
ORDER BY a.published_at DESC, a.view_count DESC
LIMIT 6;

-- 2. 获取分类页文章列表：根据 category slug 查询该分类下已发布文章，并返回互动统计。
SELECT
  a.id,
  a.title,
  a.slug,
  a.summary,
  a.cover_image,
  a.published_at,
  c.name AS category_name,
  s.like_count,
  s.comment_count,
  s.share_count,
  s.view_count
FROM articles a
INNER JOIN categories c ON c.id = a.category_id
INNER JOIN v_article_stats s ON s.article_id = a.id
WHERE c.slug = @category_slug
  AND c.status = 'published'
  AND a.status = 'published'
ORDER BY a.published_at DESC;

-- 3. 获取文章详情：根据 article slug 查询文章、分类、来源和统计数据。
SELECT
  a.id,
  a.title,
  a.slug,
  a.summary,
  a.content,
  a.cover_image,
  a.author,
  a.source_title,
  a.source_url,
  a.published_at,
  c.id AS category_id,
  c.name AS category_name,
  c.slug AS category_slug,
  s.like_count,
  s.comment_count,
  s.share_count,
  s.view_count,
  src.source_names
FROM articles a
INNER JOIN categories c ON c.id = a.category_id
INNER JOIN v_article_stats s ON s.article_id = a.id
LEFT JOIN (
  SELECT
    article_id,
    GROUP_CONCAT(source_name ORDER BY id SEPARATOR ' | ') AS source_names
  FROM article_sources
  GROUP BY article_id
) src ON src.article_id = a.id
WHERE a.slug = @article_slug
  AND a.status = 'published';

-- 4. 获取文章评论列表：只查询 visible 评论，包含用户名、头像和评论时间。
SELECT
  cm.id AS comment_id,
  cm.content,
  cm.created_at,
  u.id AS user_id,
  u.username,
  u.nickname,
  u.avatar_url
FROM comments cm
INNER JOIN users u ON u.id = cm.user_id
WHERE cm.article_id = @article_id
  AND cm.status = 'visible'
ORDER BY cm.created_at ASC;

-- 以下写入类示例放在事务中，整文件执行时会回滚，便于 DataGrip 直接测试语法。
START TRANSACTION;

-- 5. 用户点赞文章：重复点赞时不重复插入。
INSERT IGNORE INTO article_likes (article_id, user_id)
VALUES (@article_id, @user_id);

-- 6. 取消点赞：删除指定用户对指定文章的点赞记录。
DELETE FROM article_likes
WHERE article_id = @article_id
  AND user_id = @user_id;

-- 7. 记录转发：每次转发写入一条记录。
INSERT INTO article_shares (article_id, user_id, share_channel)
VALUES (@article_id, @user_id, @share_channel);

-- 8. 发布评论：新增评论，默认 visible；如需审核可把 status 改为 pending。
INSERT INTO comments (article_id, user_id, content, status)
VALUES (@article_id, @user_id, @new_comment_content, 'visible');

-- 12. 后台隐藏评论：软隐藏评论，前台 visible 查询不会再显示。
UPDATE comments
SET status = 'hidden',
    admin_note = '后台隐藏评论'
WHERE id = @target_comment_id_to_hide;

-- 13. 后台删除评论：软删除，不直接 DELETE。
UPDATE comments
SET status = 'deleted',
    admin_note = '后台软删除评论'
WHERE id = @target_comment_id_to_delete;

ROLLBACK;

-- 9. 后台数据总览：查询用户数、文章数、点赞数、评论数、转发数。
SELECT
  total_users,
  total_articles,
  total_likes,
  total_comments,
  total_shares,
  total_categories
FROM v_dashboard_stats;

-- 10. 后台热门文章排行：按点赞数、评论数、转发数综合排序，取前 5。
SELECT
  article_id,
  title,
  category_name,
  like_count,
  comment_count,
  share_count,
  view_count,
  (like_count + comment_count + share_count) AS hot_score
FROM v_article_stats
ORDER BY hot_score DESC, view_count DESC, published_at DESC
LIMIT 5;

-- 11. 后台评论管理列表：支持按状态筛选；@comment_status 为 NULL 时查询全部。
SELECT
  cm.id AS comment_id,
  cm.content,
  cm.status,
  cm.admin_note,
  cm.created_at,
  cm.updated_at,
  a.id AS article_id,
  a.title AS article_title,
  u.id AS user_id,
  u.username,
  u.nickname
FROM comments cm
INNER JOIN articles a ON a.id = cm.article_id
INNER JOIN users u ON u.id = cm.user_id
WHERE (@comment_status IS NULL OR cm.status = @comment_status)
ORDER BY cm.created_at DESC;

-- 14. 验证用户登录：根据用户名或邮箱查询 active 用户，后端再用 bcrypt 校验 password_hash。
SELECT
  id,
  username,
  email,
  password_hash,
  nickname,
  avatar_url,
  role,
  status
FROM users
WHERE (username = @login_account OR email = @login_account)
  AND status = 'active'
LIMIT 1;

-- 15. 验证验证码：根据 captcha_key 查询未使用、未过期且用途匹配的验证码。
SELECT
  id,
  captcha_key,
  captcha_code,
  purpose,
  expires_at
FROM captcha_codes
WHERE captcha_key = @captcha_key
  AND captcha_code = @captcha_code
  AND purpose = 'login'
  AND used = 0
  AND expires_at > NOW()
LIMIT 1;
