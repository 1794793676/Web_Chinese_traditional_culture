USE heritage_culture_site;

UPDATE users
SET avatar_url = CASE avatar_url
  WHEN '/assets/images/avatar-admin.png' THEN '/assets/images/avatar-admin.svg'
  WHEN '/assets/images/avatar-demo.png' THEN '/assets/images/avatar-demo.svg'
  ELSE avatar_url
END
WHERE avatar_url IN ('/assets/images/avatar-admin.png', '/assets/images/avatar-demo.png');

UPDATE categories
SET cover_image = CASE cover_image
  WHEN '/assets/images/category-thought.jpg' THEN '/assets/images/category-thought.svg'
  WHEN '/assets/images/category-spirit.jpg' THEN '/assets/images/category-spirit.svg'
  WHEN '/assets/images/category-craft.jpg' THEN '/assets/images/category-craft.svg'
  WHEN '/assets/images/category-festival.jpg' THEN '/assets/images/category-festival.svg'
  WHEN '/assets/images/category-science.jpg' THEN '/assets/images/category-science.svg'
  WHEN '/assets/images/category-ecology.jpg' THEN '/assets/images/category-ecology.svg'
  ELSE cover_image
END
WHERE cover_image IN (
  '/assets/images/category-thought.jpg', '/assets/images/category-spirit.jpg', '/assets/images/category-craft.jpg',
  '/assets/images/category-festival.jpg', '/assets/images/category-science.jpg', '/assets/images/category-ecology.jpg'
);

UPDATE articles
SET cover_image = CASE cover_image
  WHEN '/assets/images/article-people.jpg' THEN '/assets/images/article-people.svg'
  WHEN '/assets/images/article-mortise.jpg' THEN '/assets/images/article-mortise.svg'
  WHEN '/assets/images/article-solar-terms.jpg' THEN '/assets/images/article-solar-terms.svg'
  WHEN '/assets/images/article-innovation.jpg' THEN '/assets/images/article-innovation.svg'
  WHEN '/assets/images/article-dragon-boat.jpg' THEN '/assets/images/article-dragon-boat.svg'
  WHEN '/assets/images/article-calligraphy.jpg' THEN '/assets/images/article-calligraphy.svg'
  ELSE cover_image
END
WHERE cover_image IN (
  '/assets/images/article-people.jpg', '/assets/images/article-mortise.jpg', '/assets/images/article-solar-terms.jpg',
  '/assets/images/article-innovation.jpg', '/assets/images/article-dragon-boat.jpg', '/assets/images/article-calligraphy.jpg'
);
