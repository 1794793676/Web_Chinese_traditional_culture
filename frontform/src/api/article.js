import request from './request'
import { resolveMediaUrl } from '../utils/url'
const arr=(d)=>Array.isArray(d)?d:(d?.items||d?.records||d?.list||[])
const normalizeSource=(s)=>({ ...s, sourceTitle:s?.sourceTitle||s?.sourceName||s?.source_name||'未命名来源', sourceUrl:s?.sourceUrl||s?.source_url||'#', sourceType:s?.sourceType||s?.source_type||'', licenseNote:s?.licenseNote||s?.license_note||'' })
const normalizeArticle=(a={})=>({ ...a, coverUrl:resolveMediaUrl(a.coverUrl||a.coverImage||a.cover_image), categoryName:a.categoryName||a.category||'传统文化', sourceTitle:a.sourceTitle||a.sourceName||'', sources:(a.sources||[]).map(normalizeSource) })
export const getCategories=()=>request.get('/categories')
export const getFeaturedArticles=async(limit=6)=>arr(await request.get('/articles/featured',{params:{limit}})).map(normalizeArticle)
export const getArticlesByCategory=async(category,page=1,size=10)=>{const data=await request.get('/articles',{params:{category,page,size}});const list=arr(data).map(normalizeArticle);return Array.isArray(data)?{items:list,total:list.length,page,size}:{...data,items:list,records:list,list}}
export const getArticleDetail=async(slug)=>normalizeArticle(await request.get(`/articles/${slug}`))
export const recordView=(id)=>request.post(`/articles/${id}/view`)
export const likeArticle=(id)=>request.post(`/articles/${id}/like`)
export const unlikeArticle=(id)=>request.delete(`/articles/${id}/like`)
export const getComments=(id,page=1,size=10)=>request.get(`/articles/${id}/comments`,{params:{page,size}})
export const createComment=(id,content)=>request.post(`/articles/${id}/comments`,{content})
export const shareArticle=(id,channel='link')=>request.post(`/articles/${id}/share`,{channel})
