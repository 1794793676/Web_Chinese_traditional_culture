USE heritage_culture_site;

-- 保持原结构不破坏，仅补充约束和索引
ALTER TABLE article_likes
  ADD CONSTRAINT uk_article_likes_article_user UNIQUE (article_id, user_id);

-- 兼容旧库：若枚举值不完整则统一为课程要求值
ALTER TABLE comments
  MODIFY status ENUM('visible','pending','hidden','deleted') NOT NULL DEFAULT 'visible';

ALTER TABLE article_shares
  MODIFY share_channel ENUM('wechat','qq','link','weibo','other') NOT NULL DEFAULT 'link';

ALTER TABLE page_views
  MODIFY user_id BIGINT UNSIGNED NULL;

-- MyBatis 查询优化索引
CREATE INDEX idx_comments_article_status_created_at ON comments(article_id, status, created_at);
CREATE INDEX idx_article_shares_article_created_at ON article_shares(article_id, created_at);
CREATE INDEX idx_page_views_article_created_at ON page_views(article_id, created_at);

-- 说明：admin/demo 密码由应用启动时 DemoAccountInitializer 自动修复为可登录 BCrypt
-- admin / admin123456
-- demo  / demo123456
