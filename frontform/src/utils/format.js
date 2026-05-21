export const formatStatus = (status = '') => {
  const map = {
    visible: '已显示',
    pending: '待审核',
    hidden: '已隐藏',
    deleted: '已删除'
  }
  return map[status] || '未知状态'
}

export const statusClass = (status = '') => `status--${status || 'default'}`

export const formatNumber = (num) => Number(num || 0).toLocaleString('zh-CN')

export const normalizeList = (data) => (Array.isArray(data) ? data : data?.items || data?.records || data?.list || [])

export const normalizePage = (data) => {
  const items = normalizeList(data)
  return {
    ...data,
    items,
    total: data?.total,
    totalPages: data?.totalPages
  }
}

export const safeText = (value, fallback = '暂无') => {
  const text = String(value ?? '').trim()
  return text || fallback
}

export const getFallbackCover = (article = {}) => {
  const text = `${article.title || ''} ${article.categoryName || ''}`
  if (text.includes('书法')) return '/assets/images/article-calligraphy.svg'
  if (text.includes('端午') || text.includes('节日')) return '/assets/images/article-dragon-boat.svg'
  if (text.includes('革故鼎新') || text.includes('创新')) return '/assets/images/article-innovation.svg'
  if (text.includes('榫卯') || text.includes('器物') || text.includes('非遗')) return '/assets/images/article-mortise.svg'
  if (text.includes('民为邦本') || text.includes('思想')) return '/assets/images/article-people.svg'
  if (text.includes('节气') || text.includes('生态')) return '/assets/images/article-solar-terms.svg'
  return '/assets/images/article-innovation.svg'
}
