USE heritage_culture_site;

INSERT INTO users
  (username, email, password_hash, nickname, avatar_url, role, status, last_login_at)
VALUES
  ('admin', 'admin@example.com', '$2b$10$examplehashforadmin', '管理员', '/assets/images/avatar-admin.png', 'admin', 'active', '2026-05-18 09:10:00'),
  ('demo', 'demo@example.com', '$2b$10$examplehashfordemo', '演示用户', '/assets/images/avatar-demo.png', 'user', 'active', '2026-05-18 09:30:00');

INSERT INTO captcha_codes
  (captcha_key, captcha_code, purpose, used, expires_at)
VALUES
  ('login-demo-key-001', '8392', 'login', 0, '2026-05-18 23:59:59'),
  ('register-demo-key-001', '5180', 'register', 1, '2026-05-18 10:00:00');

INSERT INTO categories
  (name, slug, description, cover_image, sort_order, status)
VALUES
  ('思想理念', 'thought', '展示中华优秀传统文化中的民本思想、礼法观念与治国智慧。', '/assets/images/category-thought.jpg', 10, 'published'),
  ('精神品格', 'spirit', '展示自强不息、厚德载物、革故鼎新等精神品格。', '/assets/images/category-spirit.jpg', 20, 'published'),
  ('器物与非遗', 'craft', '展示传统器物、非遗技艺与东方审美。', '/assets/images/category-craft.jpg', 30, 'published'),
  ('节日民俗', 'festival', '展示传统节日、民俗记忆与家国情怀。', '/assets/images/category-festival.jpg', 40, 'published'),
  ('科学技艺', 'science', '展示古代科技、工程智慧与工艺方法。', '/assets/images/category-science.jpg', 50, 'published'),
  ('生态智慧', 'ecology', '展示顺时而作、天人合一与生态观念。', '/assets/images/category-ecology.jpg', 60, 'published');

INSERT INTO articles
  (category_id, title, slug, summary, content, cover_image, author, source_title, source_url, status, view_count, published_at)
VALUES
  (
    (SELECT id FROM categories WHERE slug = 'thought'),
    '民为邦本：传统政治智慧中的人民观',
    'people-as-foundation',
    '从“民为邦本”的传统表达出发，理解中华政治文化中重视民生、安定社会、以人为本的思想脉络。',
    '“民为邦本”强调国家治理应以百姓生活为根基。传统政治智慧并非只关注制度秩序，也关注民生安顿、社会教化与公共责任。将这一思想放入数字展馆中展示，可以帮助用户理解传统文化中以人民为中心的价值追求，并思考其在现代公共服务、社会治理和网页内容表达中的转化方式。',
    '/assets/images/article-people.jpg',
    '华夏文脉编辑组',
    '中华优秀传统文化相关公开资料',
    'https://www.gov.cn/',
    'published',
    326,
    '2026-05-01 09:00:00'
  ),
  (
    (SELECT id FROM categories WHERE slug = 'craft'),
    '榫卯结构：不用一钉一铆的东方技艺',
    'mortise-and-tenon',
    '榫卯结构以构件咬合完成连接，体现了传统木作中的力学经验、材料理解和精密审美。',
    '榫卯结构是中国传统木作技艺的重要代表。它通过凹凸构件相互咬合，使建筑和家具在不用金属钉的情况下保持稳定。其价值不仅在于结构牢固，也在于对材料纹理、受力方向和空间比例的深刻把握。数字展馆可通过图文、动画和案例说明，让用户直观看到传统工艺背后的科学方法。',
    '/assets/images/article-mortise.jpg',
    '华夏文脉编辑组',
    '中国非物质文化遗产网公开资料',
    'https://www.ihchina.cn/',
    'published',
    512,
    '2026-05-03 10:30:00'
  ),
  (
    (SELECT id FROM categories WHERE slug = 'ecology'),
    '二十四节气：顺时而作的生态智慧',
    'twenty-four-solar-terms',
    '二十四节气体现了古人观察自然、安排农事、调和生活节律的生态智慧。',
    '二十四节气来自长期的天象观测和农业实践，反映了气候、物候与生产生活之间的关系。它提醒人们尊重自然节律，顺应季节变化安排耕作、饮食与生活。作为数字展馆内容，节气主题适合结合时间轴、节令图像和知识卡片呈现，帮助用户建立传统文化与当代生态意识之间的联系。',
    '/assets/images/article-solar-terms.jpg',
    '华夏文脉编辑组',
    '联合国教科文组织非物质文化遗产相关资料',
    'https://ich.unesco.org/',
    'published',
    688,
    '2026-05-05 14:00:00'
  ),
  (
    (SELECT id FROM categories WHERE slug = 'spirit'),
    '革故鼎新：传统文化中的创新精神',
    'renewal-and-innovation',
    '“革故鼎新”体现了在继承中更新、在变化中发展的文化精神。',
    '中华优秀传统文化并不是静止不变的符号集合，而是在历史演进中不断吸收、调整和创造。“革故鼎新”强调去除不合时宜的旧弊，建立更适应现实的新秩序。这一精神适合用于课程设计主题阐释：网页展示传统文化时，也应通过现代交互、结构化信息和清晰视觉语言实现创造性转化。',
    '/assets/images/article-innovation.jpg',
    '华夏文脉编辑组',
    '中华思想文化术语传播工程公开资料',
    'https://www.chinesethought.cn/',
    'published',
    274,
    '2026-05-07 08:40:00'
  ),
  (
    (SELECT id FROM categories WHERE slug = 'festival'),
    '端午节俗：家国情怀与民间记忆',
    'dragon-boat-festival-customs',
    '端午节融合纪念、祈福、竞技与饮食民俗，承载着家国情怀和地方记忆。',
    '端午节有赛龙舟、食粽、挂艾草等丰富习俗。不同地区的节俗形态各有差异，但都体现了人们对健康、团圆、纪念和共同体情感的重视。数字展馆可以通过节日故事、民俗图像和互动问答呈现端午文化，使用户在浏览中理解传统节日的历史厚度和现实温度。',
    '/assets/images/article-dragon-boat.jpg',
    '华夏文脉编辑组',
    '中国传统节日公开资料',
    'https://www.ihchina.cn/',
    'published',
    439,
    '2026-05-09 16:20:00'
  ),
  (
    (SELECT id FROM categories WHERE slug = 'science'),
    '书法之美：线条中的精神气韵',
    'beauty-of-calligraphy',
    '书法通过点画、结构和章法呈现汉字之美，也体现书写者的精神气韵。',
    '书法是文字书写、审美创造和精神表达的结合。线条的提按顿挫、字形的疏密开合、篇章的节奏布局，都体现了中国传统艺术对气韵与秩序的追求。在数字展馆中，书法主题可以结合高清图片、作品赏析和字体演变说明，帮助用户理解汉字文化与审美精神。',
    '/assets/images/article-calligraphy.jpg',
    '华夏文脉编辑组',
    '中国国家博物馆公开资料',
    'https://www.chnmuseum.cn/',
    'published',
    371,
    '2026-05-11 11:15:00'
  );

INSERT INTO article_sources
  (article_id, source_name, source_url, source_type, license_note)
VALUES
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), '中华优秀传统文化相关公开资料', 'https://www.gov.cn/', 'text', '用于课程设计学习展示，请在论文中注明来源'),
  ((SELECT id FROM articles WHERE slug = 'mortise-and-tenon'), '中国非物质文化遗产网公开资料', 'https://www.ihchina.cn/', 'text', '用于课程设计学习展示，请在论文中注明来源'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), '联合国教科文组织非物质文化遗产相关资料', 'https://ich.unesco.org/', 'text', '用于课程设计学习展示，请在论文中注明来源'),
  ((SELECT id FROM articles WHERE slug = 'renewal-and-innovation'), '中华思想文化术语传播工程公开资料', 'https://www.chinesethought.cn/', 'text', '用于课程设计学习展示，请在论文中注明来源'),
  ((SELECT id FROM articles WHERE slug = 'dragon-boat-festival-customs'), '中国传统节日公开资料', 'https://www.ihchina.cn/', 'text', '用于课程设计学习展示，请在论文中注明来源'),
  ((SELECT id FROM articles WHERE slug = 'beauty-of-calligraphy'), '中国国家博物馆公开资料', 'https://www.chnmuseum.cn/', 'text', '用于课程设计学习展示，请在论文中注明来源');

INSERT INTO comments
  (article_id, user_id, content, status, admin_note, created_at)
VALUES
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), (SELECT id FROM users WHERE username = 'demo'), '这篇文章让我更直观地理解了传统文化中的人民观。', 'visible', NULL, '2026-05-12 09:10:00'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), (SELECT id FROM users WHERE username = 'demo'), '页面中的时间轴设计很清晰，适合文化主题展示。', 'visible', NULL, '2026-05-12 10:15:00'),
  ((SELECT id FROM articles WHERE slug = 'mortise-and-tenon'), (SELECT id FROM users WHERE username = 'demo'), '非遗内容如果能加入更多图片会更有吸引力。', 'pending', '等待管理员审核', '2026-05-12 11:20:00'),
  ((SELECT id FROM articles WHERE slug = 'renewal-and-innovation'), (SELECT id FROM users WHERE username = 'admin'), '传统文化和现代网页设计结合得比较自然。', 'visible', NULL, '2026-05-12 13:25:00'),
  ((SELECT id FROM articles WHERE slug = 'dragon-boat-festival-customs'), (SELECT id FROM users WHERE username = 'demo'), '这个专题适合作为课程设计展示内容。', 'visible', NULL, '2026-05-12 15:30:00'),
  ((SELECT id FROM articles WHERE slug = 'beauty-of-calligraphy'), (SELECT id FROM users WHERE username = 'demo'), '书法内容可以作为视觉设计的重点模块。', 'visible', NULL, '2026-05-13 08:50:00');

INSERT INTO article_likes
  (article_id, user_id, created_at)
VALUES
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), (SELECT id FROM users WHERE username = 'admin'), '2026-05-12 09:40:00'),
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), (SELECT id FROM users WHERE username = 'demo'), '2026-05-12 09:42:00'),
  ((SELECT id FROM articles WHERE slug = 'mortise-and-tenon'), (SELECT id FROM users WHERE username = 'admin'), '2026-05-12 10:00:00'),
  ((SELECT id FROM articles WHERE slug = 'mortise-and-tenon'), (SELECT id FROM users WHERE username = 'demo'), '2026-05-12 10:05:00'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), (SELECT id FROM users WHERE username = 'admin'), '2026-05-12 10:30:00'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), (SELECT id FROM users WHERE username = 'demo'), '2026-05-12 10:31:00'),
  ((SELECT id FROM articles WHERE slug = 'renewal-and-innovation'), (SELECT id FROM users WHERE username = 'admin'), '2026-05-12 11:00:00'),
  ((SELECT id FROM articles WHERE slug = 'dragon-boat-festival-customs'), (SELECT id FROM users WHERE username = 'demo'), '2026-05-12 11:30:00'),
  ((SELECT id FROM articles WHERE slug = 'beauty-of-calligraphy'), (SELECT id FROM users WHERE username = 'admin'), '2026-05-12 12:00:00'),
  ((SELECT id FROM articles WHERE slug = 'beauty-of-calligraphy'), (SELECT id FROM users WHERE username = 'demo'), '2026-05-12 12:05:00');

INSERT INTO article_shares
  (article_id, user_id, share_channel, created_at)
VALUES
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), (SELECT id FROM users WHERE username = 'demo'), 'link', '2026-05-13 09:00:00'),
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), NULL, 'wechat', '2026-05-13 09:05:00'),
  ((SELECT id FROM articles WHERE slug = 'mortise-and-tenon'), (SELECT id FROM users WHERE username = 'demo'), 'qq', '2026-05-13 09:30:00'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), (SELECT id FROM users WHERE username = 'admin'), 'weibo', '2026-05-13 10:00:00'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), (SELECT id FROM users WHERE username = 'demo'), 'link', '2026-05-13 10:15:00'),
  ((SELECT id FROM articles WHERE slug = 'renewal-and-innovation'), NULL, 'link', '2026-05-13 10:40:00'),
  ((SELECT id FROM articles WHERE slug = 'dragon-boat-festival-customs'), (SELECT id FROM users WHERE username = 'demo'), 'wechat', '2026-05-13 11:10:00'),
  ((SELECT id FROM articles WHERE slug = 'beauty-of-calligraphy'), (SELECT id FROM users WHERE username = 'admin'), 'other', '2026-05-13 11:20:00');

INSERT INTO page_views
  (article_id, user_id, page_path, ip_address, user_agent, created_at)
VALUES
  (NULL, NULL, '/', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:00:00'),
  ((SELECT id FROM articles WHERE slug = 'people-as-foundation'), (SELECT id FROM users WHERE username = 'demo'), '/articles/people-as-foundation', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:05:00'),
  ((SELECT id FROM articles WHERE slug = 'mortise-and-tenon'), NULL, '/articles/mortise-and-tenon', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:12:00'),
  ((SELECT id FROM articles WHERE slug = 'twenty-four-solar-terms'), (SELECT id FROM users WHERE username = 'demo'), '/articles/twenty-four-solar-terms', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:20:00'),
  ((SELECT id FROM articles WHERE slug = 'renewal-and-innovation'), (SELECT id FROM users WHERE username = 'admin'), '/articles/renewal-and-innovation', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:30:00'),
  ((SELECT id FROM articles WHERE slug = 'dragon-boat-festival-customs'), NULL, '/articles/dragon-boat-festival-customs', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:40:00'),
  ((SELECT id FROM articles WHERE slug = 'beauty-of-calligraphy'), (SELECT id FROM users WHERE username = 'demo'), '/articles/beauty-of-calligraphy', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 08:50:00'),
  (NULL, (SELECT id FROM users WHERE username = 'admin'), '/admin/dashboard', '127.0.0.1', 'Mozilla/5.0 Demo Browser', '2026-05-14 09:00:00');

INSERT INTO admin_logs
  (admin_id, action, target_type, target_id, detail, created_at)
VALUES
  ((SELECT id FROM users WHERE username = 'admin'), 'review_comment', 'comment', 3, 'Seed data: comment marked as pending for moderation demo.', '2026-05-14 09:30:00');
