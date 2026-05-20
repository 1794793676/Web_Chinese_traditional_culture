import request from './request'
import { resolveMediaUrl } from '../utils/url'

const normalizeArticle = (article) => ({
  ...article,
  coverUrl: resolveMediaUrl(article?.coverUrl || article?.cover_image || article?.coverImage)
})

export const getCategories = () => request.get('/categories')
export const getFeaturedArticles = async (limit = 6) => {
  const data = await request.get('/articles/featured', { params: { limit } })
  return (data || []).map(normalizeArticle)
}
export const getArticlesByCategory = async (category, page = 1, size = 10) => {
  const data = await request.get('/articles', { params: { category, page, size } })
  const records = (data.records || data.list || []).map(normalizeArticle)
  if (Array.isArray(data)) return data.map(normalizeArticle)
  return { ...data, records, list: records }
}
export const getArticleDetail = async (slug) => normalizeArticle(await request.get(`/articles/${slug}`))
export const recordView = (id) => request.post(`/articles/${id}/view`)
export const likeArticle = (id) => request.post(`/articles/${id}/like`)
export const unlikeArticle = (id) => request.delete(`/articles/${id}/like`)
export const getComments = (id, page = 1, size = 10) => request.get(`/articles/${id}/comments`, { params: { page, size } })
export const createComment = (id, content) => request.post(`/articles/${id}/comments`, { content })
export const shareArticle = (id, channel) => request.post(`/articles/${id}/share`, { channel })
