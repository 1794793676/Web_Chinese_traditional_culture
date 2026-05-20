<template>
  <section class="page-hero">
    <div class="container">
      <h1>无访问权限</h1>
      <p v-if="auth.isLoggedIn">当前账号无后台权限。</p>
      <p v-else>根据课程要求，游客只能访问首页，其余页面需登录后查看。</p>
      <div class="hero__actions">
        <router-link class="btn btn--primary" to="/login">去登录</router-link>
        <router-link class="btn btn--outline" to="/">返回首页</router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
auth.loadFromStorage()
</script>
