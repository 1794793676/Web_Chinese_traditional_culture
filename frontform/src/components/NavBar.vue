<template>
  <header class="site-header">
    <div class="site-header__inner">
      <router-link class="brand" to="/">华夏文脉</router-link>
      <button class="icon-btn nav-toggle" @click="menuOpen=!menuOpen" type="button">☰</button>
      <nav class="site-nav" :class="{ 'is-open': menuOpen }" aria-label="主导航">
        <router-link class="nav-link" active-class="is-active" to="/" @click="menuOpen=false">首页</router-link>
        <button class="nav-link nav-btn" @click="goFirstCategory">文化专题</button>
        <a class="nav-link" href="#featured" @click="menuOpen=false">典藏精品</a>
        <router-link class="nav-link" to="/sources" @click="menuOpen=false">素材来源</router-link>
        <router-link v-if="auth.isAdmin" class="nav-link" to="/admin/dashboard" @click="menuOpen=false">后台入口</router-link>
      </nav>
      <div class="site-actions">
        <template v-if="!auth.isLoggedIn">
          <router-link class="btn btn--outline" to="/login">登录</router-link>
          <router-link class="btn btn--primary" to="/register">注册</router-link>
        </template>
        <template v-else>
          <span class="user-chip">{{ displayName }}（{{ auth.isAdmin ? '管理员' : '用户' }}）</span>
          <button class="btn btn--outline" @click="handleLogout">退出</button>
        </template>
      </div>
    </div>
  </header>
</template>
<script setup>
import { computed, ref } from 'vue';import { useRouter } from 'vue-router';import { useAuthStore } from '../stores/auth';import { getCategories } from '../api/article'
const auth=useAuthStore();auth.loadFromStorage();const router=useRouter();const menuOpen=ref(false)
const displayName=computed(()=>auth.user?.nickname||auth.user?.username||'用户')
const handleLogout=async()=>{await auth.logout();menuOpen.value=false;router.push('/login')}
const goFirstCategory=async()=>{menuOpen.value=false;let slug='thought'
try{const list=await getCategories();slug=list?.[0]?.slug||slug}catch{}
if(!auth.isLoggedIn)return router.push({path:'/login',query:{redirect:`/category/${slug}`}})
router.push(`/category/${slug}`)
}
</script>
