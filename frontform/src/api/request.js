import axios from 'axios'
const request=axios.create({baseURL:'/api',timeout:15000})
request.interceptors.request.use((config)=>{const t=localStorage.getItem('heritage_token'); if(t){config.headers.Authorization=`Bearer ${t}`} return config})
request.interceptors.response.use(async(res)=>{const body=res.data; if(body?.code===200) return body.data; throw new Error(body?.message||'请求失败')}, async(err)=>{
 const status=err?.response?.status
 if(status===401){ const { useAuthStore } = await import('../stores/auth'); const { default:router } = await import('../router'); const store=useAuthStore(); store.clearAuth(); if(router.currentRoute.value.path!=='/login') router.push({path:'/login',query:{redirect:router.currentRoute.value.fullPath}})}
 if(status===403){ const { default:router } = await import('../router'); if(router.currentRoute.value.path!=='/no-access') router.push('/no-access')}
 throw new Error(err?.response?.data?.message||err.message||'网络错误')
})
export default request
