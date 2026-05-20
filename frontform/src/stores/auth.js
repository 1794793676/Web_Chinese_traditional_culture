import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { getProfile, logout as logoutApi } from '../api/auth'
const TOKEN_KEY='heritage_token'; const USER_KEY='heritage_user'
export const useAuthStore=defineStore('auth',()=>{
 const token=ref(''); const user=ref(null)
 const isLoggedIn=computed(()=>!!token.value)
 const isAdmin=computed(()=>user.value?.role==='admin')
 const loadFromStorage=()=>{token.value=localStorage.getItem(TOKEN_KEY)||''; try{user.value=JSON.parse(localStorage.getItem(USER_KEY)||'null')}catch{user.value=null}}
 const setAuth=(t,u)=>{token.value=t||''; user.value=u||null; localStorage.setItem(TOKEN_KEY,token.value); localStorage.setItem(USER_KEY,JSON.stringify(user.value))}
 const clearAuth=()=>{token.value='';user.value=null;localStorage.removeItem(TOKEN_KEY);localStorage.removeItem(USER_KEY)}
 const fetchProfile=async()=>{const p=await getProfile(); user.value=p; localStorage.setItem(USER_KEY,JSON.stringify(p)); return p}
 const logout=async()=>{try{await logoutApi()}catch{} finally{clearAuth()}}
 return {token,user,isLoggedIn,isAdmin,loadFromStorage,setAuth,clearAuth,fetchProfile,logout}
})
