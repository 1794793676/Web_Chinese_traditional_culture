import request from './request'
export const getDashboard=()=>request.get('/admin/dashboard')
export const getAdminComments=(params)=>request.get('/admin/comments',{params})
export const updateCommentStatus=(id,data)=>request.patch(`/admin/comments/${id}/status`,data)
export const getInteractions=(params)=>request.get('/admin/interactions',{params})
