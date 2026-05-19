USE heritage_culture_site;

DROP VIEW IF EXISTS v_latest_comments;
DROP VIEW IF EXISTS v_dashboard_stats;
DROP VIEW IF EXISTS v_article_stats;

CREATE VIEW v_article_stats AS
SELECT
  a.id AS article_id,
  a.title AS title,
  a.category_id AS category_id,
  c.name AS category_name,
  COALESCE(l.like_count, 0) AS like_count,
  COALESCE(cm.comment_count, 0) AS comment_count,
  COALESCE(s.share_count, 0) AS share_count,
  a.view_count AS view_count,
  a.published_at AS published_at
FROM articles a
INNER JOIN categories c ON c.id = a.category_id
LEFT JOIN (
  SELECT article_id, COUNT(*) AS like_count
  FROM article_likes
  GROUP BY article_id
) l ON l.article_id = a.id
LEFT JOIN (
  SELECT article_id, COUNT(*) AS comment_count
  FROM comments
  WHERE status = 'visible'
  GROUP BY article_id
) cm ON cm.article_id = a.id
LEFT JOIN (
  SELECT article_id, COUNT(*) AS share_count
  FROM article_shares
  GROUP BY article_id
) s ON s.article_id = a.id;

CREATE VIEW v_dashboard_stats AS
SELECT
  (SELECT COUNT(*) FROM users) AS total_users,
  (SELECT COUNT(*) FROM articles) AS total_articles,
  (SELECT COUNT(*) FROM article_likes) AS total_likes,
  (SELECT COUNT(*) FROM comments) AS total_comments,
  (SELECT COUNT(*) FROM article_shares) AS total_shares,
  (SELECT COUNT(*) FROM categories) AS total_categories;

CREATE VIEW v_latest_comments AS
SELECT
  cm.id AS comment_id,
  cm.article_id AS article_id,
  a.title AS article_title,
  cm.user_id AS user_id,
  u.username AS username,
  cm.content AS content,
  cm.status AS status,
  cm.created_at AS created_at
FROM comments cm
INNER JOIN articles a ON a.id = cm.article_id
INNER JOIN users u ON u.id = cm.user_id
ORDER BY cm.created_at DESC;
