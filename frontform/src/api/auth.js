import request from './request'
export const getCaptcha=(purpose='login')=>request.get('/auth/captcha',{params:{purpose}})
export const register=(data)=>request.post('/auth/register',data)
export const login=(data)=>request.post('/auth/login',data)
export const logout=()=>request.post('/auth/logout')
export const getProfile=()=>request.get('/user/profile')
