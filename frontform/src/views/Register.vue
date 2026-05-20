<template>
  <section class="auth-page">
    <div class="auth-card">
      <aside class="auth-aside">
        <h1>注册展馆账号</h1>
        <p>创建账号后可访问文化专题、文章详情与素材来源页。</p>
      </aside>
      <div class="auth-main">
        <h2>用户注册</h2>
        <form class="form" @submit.prevent="submit">
          <div class="field"><label>用户名</label><input v-model="form.username" /></div>
          <div class="field"><label>邮箱</label><input v-model="form.email" /></div>
          <div class="field"><label>昵称</label><input v-model="form.nickname" /></div>
          <div class="field"><label>密码</label><input v-model="form.password" type="password" /></div>
          <div class="field"><label>确认密码</label><input v-model="form.confirmPassword" type="password" /></div>
          <div class="field">
            <label>验证码</label>
            <input v-model="form.captchaCode" />
            <CaptchaBox ref="captchaRef" purpose="register" @update:key="form.captchaKey = $event" @refreshed="form.captchaCode = ''" />
          </div>
          <div class="form-actions">
            <button class="btn btn--primary" type="submit">注册</button>
            <router-link class="btn btn--outline" to="/login">已有账号？去登录</router-link>
          </div>
        </form>
        <p v-if="message" :class="ok ? 'form-success' : 'form-error'">{{ message }}</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/auth'
import CaptchaBox from '../components/CaptchaBox.vue'

const router = useRouter()
const captchaRef = ref(null)
const ok = ref(false)
const message = ref('')

const form = reactive({
  username: '',
  email: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  captchaCode: '',
  captchaKey: ''
})

const submit = async () => {
  ok.value = false
  message.value = ''
  if (form.password !== form.confirmPassword) {
    message.value = '两次密码不一致'
    return
  }
  try {
    await register(form)
    ok.value = true
    message.value = '注册成功，正在跳转登录页...'
    setTimeout(() => router.push('/login'), 800)
  } catch (err) {
    message.value = err.message
    captchaRef.value?.refresh()
  }
}
</script>
