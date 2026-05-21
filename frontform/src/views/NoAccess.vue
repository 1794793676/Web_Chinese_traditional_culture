<template>
  <section class="section">
    <div class="container">
      <div class="access-panel">
        <h1>无访问权限</h1>
        <p v-if="auth.isLoggedIn">当前账号没有后台管理权限。</p>
        <p v-else>你当前以游客身份访问，请登录后进入完整数字展馆。</p>
        <div class="hero__actions">
          <router-link class="btn btn--primary" to="/login">去登录</router-link>
          <router-link class="btn btn--outline" to="/">返回首页</router-link>
        </div>
      </div>
    </div>
  </section>
</template>
<script setup>
import { useAuthStore } from '../stores/auth'
const auth = useAuthStore()
auth.loadFromStorage()
</script>
