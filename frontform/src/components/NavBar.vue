<template><header class="topbar"><div class="container"><router-link to="/">华夏文脉</router-link><nav class="page-actions"><router-link to="/">首页</router-link><router-link to="/sources">素材来源</router-link><router-link v-if="auth.isAdmin" to="/admin/dashboard">后台</router-link><template v-if="!auth.isLoggedIn"><router-link to="/login">登录</router-link><router-link to="/register">注册</router-link></template><template v-else><span>{{auth.user?.nickname||auth.user?.username}}（{{auth.user?.role}}）</span><button @click="doLogout">退出</button></template></nav></div></header></template>
<script setup>
import { useRouter } from 'vue-router'; import { useAuthStore } from '../stores/auth'
const router=useRouter(); const auth=useAuthStore(); auth.loadFromStorage();
const doLogout=async()=>{await auth.logout(); router.push('/login')}
</script>
