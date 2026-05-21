<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <h2>文脉管理后台</h2>
      <div class="admin-user">
        <div class="avatar">管</div>
        <p>系统管理员</p>
      </div>
      <nav class="admin-nav">
        <router-link to="/admin/dashboard">数据总览</router-link>
        <router-link to="/admin/comments">评论审核</router-link>
        <router-link to="/admin/interactions">互动统计</router-link>
        <router-link to="/">返回首页</router-link>
        <button class="btn btn--outline" @click="exit">安全退出</button>
      </nav>
    </aside>
    <main class="admin-main">
      <slot />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()

const exit = async () => {
  await auth.logout()
  router.push('/login')
}
</script>
