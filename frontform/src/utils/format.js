import { getFallbackCoverFromText } from './pictureMap'

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
  return { ...data, items, total: data?.total, totalPages: data?.totalPages }
}

export const safeText = (value, fallback = '暂无') => {
  const text = String(value ?? '').trim()
  return text || fallback
}

export const getFallbackCover = (article = {}) => getFallbackCoverFromText(`${article.title || ''} ${article.categoryName || ''}`)
