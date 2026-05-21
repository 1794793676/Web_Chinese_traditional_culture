export const CATEGORY_SLUG_ALIAS = {
  thought: 'thought',
  spirit: 'spirit',
  object: 'craft',
  craft: 'craft',
  festival: 'festival',
  science: 'science',
  ecology: 'ecology'
}

export const CATEGORY_NAME_BY_SLUG = {
  thought: '思想理念',
  spirit: '精神品格',
  craft: '器物与非遗',
  festival: '节日民俗',
  science: '科学技艺',
  ecology: '生态智慧'
}

export const normalizeCategorySlug = (slug) => CATEGORY_SLUG_ALIAS[slug] || slug

export const getCategoryName = (slug) => CATEGORY_NAME_BY_SLUG[normalizeCategorySlug(slug)] || slug
