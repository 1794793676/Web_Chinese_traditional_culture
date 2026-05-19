USE heritage_culture_site;

-- 注意：02_schema.sql 已包含 uk_article_likes_article_user，
-- 本脚本不再重复 ADD 同名唯一约束，避免标准初始化失败。

ALTER TABLE comments
    MODIFY status ENUM ('visible', 'pending', 'hidden', 'deleted') NOT NULL DEFAULT 'visible';

ALTER TABLE article_shares
    MODIFY share_channel ENUM ('wechat', 'qq', 'link', 'weibo', 'other') NOT NULL DEFAULT 'link';

ALTER TABLE page_views
    MODIFY user_id BIGINT UNSIGNED NULL;

CREATE INDEX idx_comments_article_status_created_at
    ON comments (article_id, status, created_at);

CREATE INDEX idx_article_shares_article_created_at
    ON article_shares (article_id, created_at);

CREATE INDEX idx_page_views_article_created_at
    ON page_views (article_id, created_at);

-- 修复默认演示账号密码（BCrypt）
-- admin / admin123456
-- demo  / demo123456
UPDATE users
SET password_hash = '$2a$10$4KLIQj6s2qmJ2N2NhQSl2.x4YvSXxNAX72EHnkh6.gM9W2aqh0vqy'
WHERE username = 'admin';

UPDATE users
SET password_hash = '$2a$10$daA48IUx2HQAlnXrhIRbkecw0vV1M2f4iMOkvAcLf3xWP8sQS4sEO'
WHERE username = 'demo';
