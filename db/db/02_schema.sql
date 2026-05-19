USE heritage_culture_site;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS admin_logs;
DROP TABLE IF EXISTS page_views;
DROP TABLE IF EXISTS article_shares;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS article_likes;
DROP TABLE IF EXISTS article_sources;
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS captcha_codes;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'User primary key',
  username VARCHAR(50) NOT NULL COMMENT 'Unique login username',
  email VARCHAR(100) NOT NULL COMMENT 'Unique user email',
  password_hash VARCHAR(255) NOT NULL COMMENT 'Hashed password only, never plain text',
  nickname VARCHAR(50) NULL COMMENT 'Display nickname',
  avatar_url VARCHAR(255) NULL COMMENT 'User avatar URL',
  role ENUM('user', 'admin') NOT NULL DEFAULT 'user' COMMENT 'Permission role',
  status ENUM('active', 'disabled') NOT NULL DEFAULT 'active' COMMENT 'User account status',
  last_login_at DATETIME NULL COMMENT 'Last successful login time',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
  PRIMARY KEY (id),
  CONSTRAINT uk_users_username UNIQUE (username),
  CONSTRAINT uk_users_email UNIQUE (email),
  INDEX idx_users_role (role),
  INDEX idx_users_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Registered users and administrators';

CREATE TABLE captcha_codes (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Captcha primary key',
  captcha_key VARCHAR(64) NOT NULL COMMENT 'Captcha key shared by client and server',
  captcha_code VARCHAR(10) NOT NULL COMMENT 'Captcha code value',
  purpose ENUM('login', 'register') NOT NULL DEFAULT 'login' COMMENT 'Captcha usage purpose',
  used TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Whether captcha has been used',
  expires_at DATETIME NOT NULL COMMENT 'Expiration time',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  PRIMARY KEY (id),
  CONSTRAINT uk_captcha_codes_captcha_key UNIQUE (captcha_key),
  INDEX idx_captcha_codes_expires_at (expires_at),
  INDEX idx_captcha_codes_used (used)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Login and registration captcha records';

CREATE TABLE categories (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Category primary key',
  name VARCHAR(50) NOT NULL COMMENT 'Category display name',
  slug VARCHAR(80) NOT NULL COMMENT 'Unique category slug',
  description TEXT NULL COMMENT 'Category description',
  cover_image VARCHAR(255) NULL COMMENT 'Category cover image URL',
  sort_order INT NOT NULL DEFAULT 0 COMMENT 'Display order',
  status ENUM('published', 'hidden') NOT NULL DEFAULT 'published' COMMENT 'Category display status',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
  PRIMARY KEY (id),
  CONSTRAINT uk_categories_slug UNIQUE (slug),
  INDEX idx_categories_status (status),
  INDEX idx_categories_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Traditional culture topic categories';

CREATE TABLE articles (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Article primary key',
  category_id BIGINT UNSIGNED NOT NULL COMMENT 'Related category ID',
  title VARCHAR(200) NOT NULL COMMENT 'Article title',
  slug VARCHAR(120) NOT NULL COMMENT 'Unique article slug',
  summary TEXT NULL COMMENT 'Article summary',
  content LONGTEXT NOT NULL COMMENT 'Article body content',
  cover_image VARCHAR(255) NULL COMMENT 'Article cover image URL',
  author VARCHAR(80) NULL DEFAULT '华夏文脉编辑组' COMMENT 'Article author',
  source_title VARCHAR(200) NULL COMMENT 'Main source title',
  source_url VARCHAR(255) NULL COMMENT 'Main source URL',
  status ENUM('published', 'draft', 'hidden') NOT NULL DEFAULT 'published' COMMENT 'Article publishing status',
  view_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Article view count',
  published_at DATETIME NULL COMMENT 'Publication time',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
  PRIMARY KEY (id),
  CONSTRAINT uk_articles_slug UNIQUE (slug),
  INDEX idx_articles_category_id (category_id),
  INDEX idx_articles_status (status),
  INDEX idx_articles_published_at (published_at),
  INDEX idx_articles_view_count (view_count),
  CONSTRAINT fk_articles_category_id
    FOREIGN KEY (category_id) REFERENCES categories (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Culture article content';

CREATE TABLE article_sources (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Source primary key',
  article_id BIGINT UNSIGNED NOT NULL COMMENT 'Related article ID',
  source_name VARCHAR(200) NOT NULL COMMENT 'Source name',
  source_url VARCHAR(255) NULL COMMENT 'Source URL',
  source_type ENUM('text', 'image', 'icon', 'font', 'other') NOT NULL DEFAULT 'text' COMMENT 'Source material type',
  license_note VARCHAR(255) NULL COMMENT 'License or usage note',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  PRIMARY KEY (id),
  INDEX idx_article_sources_article_id (article_id),
  INDEX idx_article_sources_source_type (source_type),
  CONSTRAINT fk_article_sources_article_id
    FOREIGN KEY (article_id) REFERENCES articles (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article material source records';

CREATE TABLE article_likes (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Like primary key',
  article_id BIGINT UNSIGNED NOT NULL COMMENT 'Related article ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT 'Liking user ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  PRIMARY KEY (id),
  CONSTRAINT uk_article_likes_article_user UNIQUE (article_id, user_id),
  INDEX idx_article_likes_user_id (user_id),
  CONSTRAINT fk_article_likes_article_id
    FOREIGN KEY (article_id) REFERENCES articles (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT fk_article_likes_user_id
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article likes';

CREATE TABLE comments (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Comment primary key',
  article_id BIGINT UNSIGNED NOT NULL COMMENT 'Related article ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT 'Commenting user ID',
  content TEXT NOT NULL COMMENT 'Comment content',
  status ENUM('visible', 'pending', 'hidden', 'deleted') NOT NULL DEFAULT 'visible' COMMENT 'Comment moderation status',
  admin_note VARCHAR(255) NULL COMMENT 'Administrator moderation note',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
  PRIMARY KEY (id),
  INDEX idx_comments_article_id (article_id),
  INDEX idx_comments_user_id (user_id),
  INDEX idx_comments_status (status),
  INDEX idx_comments_created_at (created_at),
  CONSTRAINT fk_comments_article_id
    FOREIGN KEY (article_id) REFERENCES articles (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT fk_comments_user_id
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article comments and moderation status';

CREATE TABLE article_shares (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Share primary key',
  article_id BIGINT UNSIGNED NOT NULL COMMENT 'Related article ID',
  user_id BIGINT UNSIGNED NULL COMMENT 'Sharing user ID, nullable for guest shares',
  share_channel ENUM('wechat', 'qq', 'link', 'weibo', 'other') NOT NULL DEFAULT 'link' COMMENT 'Share channel',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  PRIMARY KEY (id),
  INDEX idx_article_shares_article_id (article_id),
  INDEX idx_article_shares_user_id (user_id),
  INDEX idx_article_shares_share_channel (share_channel),
  INDEX idx_article_shares_created_at (created_at),
  CONSTRAINT fk_article_shares_article_id
    FOREIGN KEY (article_id) REFERENCES articles (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT fk_article_shares_user_id
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Article share records';

CREATE TABLE page_views (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Page view primary key',
  article_id BIGINT UNSIGNED NULL COMMENT 'Related article ID, nullable for non-article pages',
  user_id BIGINT UNSIGNED NULL COMMENT 'Viewing user ID, nullable for guests',
  page_path VARCHAR(255) NOT NULL COMMENT 'Visited page path',
  ip_address VARCHAR(45) NULL COMMENT 'IPv4 or IPv6 address',
  user_agent VARCHAR(255) NULL COMMENT 'Browser user agent',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  PRIMARY KEY (id),
  INDEX idx_page_views_article_id (article_id),
  INDEX idx_page_views_user_id (user_id),
  INDEX idx_page_views_page_path (page_path),
  INDEX idx_page_views_created_at (created_at),
  CONSTRAINT fk_page_views_article_id
    FOREIGN KEY (article_id) REFERENCES articles (id)
    ON UPDATE CASCADE
    ON DELETE SET NULL,
  CONSTRAINT fk_page_views_user_id
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Page visit tracking records';

CREATE TABLE admin_logs (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Admin log primary key',
  admin_id BIGINT UNSIGNED NOT NULL COMMENT 'Administrator user ID',
  action VARCHAR(100) NOT NULL COMMENT 'Operation action',
  target_type VARCHAR(50) NULL COMMENT 'Target object type',
  target_id BIGINT UNSIGNED NULL COMMENT 'Target object ID',
  detail TEXT NULL COMMENT 'Operation detail',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  PRIMARY KEY (id),
  INDEX idx_admin_logs_admin_id (admin_id),
  INDEX idx_admin_logs_action (action),
  INDEX idx_admin_logs_target (target_type, target_id),
  INDEX idx_admin_logs_created_at (created_at),
  CONSTRAINT fk_admin_logs_admin_id
    FOREIGN KEY (admin_id) REFERENCES users (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Administrator operation logs';
