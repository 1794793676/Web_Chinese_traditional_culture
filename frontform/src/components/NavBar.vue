<template>
  <header class="site-header">
    <div class="site-header__inner">
      <router-link class="brand" to="/">华夏文脉</router-link>

      <nav class="site-nav" aria-label="主导航">
        <router-link class="nav-link" active-class="is-active" to="/">首页</router-link>
        <router-link class="nav-link" active-class="is-active" to="/sources">素材来源</router-link>
        <router-link
          v-if="auth.isAdmin"
          class="nav-link"
          active-class="is-active"
          to="/admin/dashboard"
        >
          后台
        </router-link>
      </nav>

      <div class="site-actions">
        <template v-if="!auth.isLoggedIn">
          <router-link class="btn btn--outline" to="/login">登录</router-link>
          <router-link class="btn btn--primary" to="/register">注册</router-link>
        </template>
        <template v-else>
          <span class="user-chip">{{ displayName }}（{{ auth.user?.role }}）</span>
          <button class="btn btn--outline" type="button" @click="handleLogout">退出</button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
auth.loadFromStorage()

const displayName = computed(() => auth.user?.nickname || auth.user?.username || '用户')

const handleLogout = async () => {
  await auth.logout()
  router.push('/login')
}
</script>
