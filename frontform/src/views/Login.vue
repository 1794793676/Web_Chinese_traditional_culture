<template>
  <section class="auth-page">
    <div class="auth-card">
      <aside class="auth-aside">
        <h1>登录数字展馆</h1>
        <p>在现代网页中重读传统文化的思想、器物与精神。</p>
      </aside>
      <div class="auth-main">
        <h2>用户登录</h2>
        <form class="form" @submit.prevent="submit">
          <div class="field"><label>用户名</label><input v-model="form.username" /></div>
          <div class="field"><label>密码</label><input v-model="form.password" type="password" /></div>
          <div class="field">
            <label>验证码</label>
            <input v-model="form.captchaCode" />
            <CaptchaBox ref="captchaRef" purpose="login" @update:key="form.captchaKey = $event" @refreshed="form.captchaCode = ''" />
          </div>
          <div class="form-actions">
            <button class="btn btn--primary" type="submit">登录</button>
            <router-link class="btn btn--outline" to="/register">没有账号？去注册</router-link>
          </div>
        </form>
        <p v-if="message" class="form-error">{{ message }}</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { login } from '../api/auth'
import CaptchaBox from '../components/CaptchaBox.vue'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const captchaRef = ref(null)
const message = ref('')
const form = reactive({ username: '', password: '', captchaCode: '', captchaKey: '' })

const submit = async () => {
  message.value = ''
  try {
    const data = await login(form)
    auth.setAuth(data.token, data.user)
    const redirect = route.query.redirect
    router.push(redirect || (data.user?.role === 'admin' ? '/admin/dashboard' : '/'))
  } catch (err) {
    message.value = err.message
    captchaRef.value?.refresh()
  }
}
</script>
