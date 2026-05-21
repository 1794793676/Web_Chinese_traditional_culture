<template>
  <section class="auth-page">
    <div class="auth-card">
      <aside class="auth-aside"><h1>注册展馆账号</h1><p>创建账号后可访问文化专题、文章详情与素材来源页。</p></aside>
      <div class="auth-main">
        <h2>用户注册</h2>
        <form class="form" @submit.prevent="submit">
          <div class="field"><label>用户名</label><input v-model="form.username" placeholder="请输入用户名" /></div>
          <div class="field"><label>邮箱</label><input v-model="form.email" placeholder="请输入邮箱" /></div>
          <div class="field"><label>昵称</label><input v-model="form.nickname" placeholder="请输入昵称" /></div>
          <div class="field"><label>密码</label><input v-model="form.password" type="password" placeholder="至少6位密码" /></div>
          <div class="field"><label>确认密码</label><input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" /></div>
          <div class="field"><label>验证码</label><input v-model="form.captchaCode" placeholder="请输入验证码" /><CaptchaBox ref="captchaRef" purpose="register" @update:key="form.captchaKey = $event" @refreshed="form.captchaCode = ''" /></div>
          <label class="hint"><input v-model="form.agree" type="checkbox" /> 我已阅读并同意用户协议</label>
          <div class="form-actions"><button class="btn btn--primary" type="submit">注册</button><router-link class="btn btn--outline" to="/login">已有账号？去登录</router-link></div>
        </form>
        <p v-if="message" :class="ok ? 'form-success' : 'form-error'">{{ message }}</p>
      </div>
    </div>
  </section>
</template>
<script setup>
import { reactive, ref } from 'vue';import { useRouter } from 'vue-router';import { register } from '../api/auth';import CaptchaBox from '../components/CaptchaBox.vue'
const router=useRouter();const captchaRef=ref(null);const ok=ref(false);const message=ref('')
const form = reactive({ username: '', email: '', nickname: '', password: '', confirmPassword: '', captchaCode: '', captchaKey: '', agree: false })
const submit = async () => {ok.value=false;message.value='';if(!form.username.trim()) return (message.value='用户名不能为空');if(!/^\S+@\S+\.\S+$/.test(form.email)) return (message.value='邮箱格式不正确');if((form.password||'').length<6) return (message.value='密码至少 6 位');if(form.password!==form.confirmPassword) return (message.value='两次密码不一致');if(!form.captchaCode.trim()) return (message.value='请输入验证码');if(!form.agree) return (message.value='请先同意用户协议');try{await register(form);ok.value=true;message.value='注册成功，正在跳转登录页...';setTimeout(()=>router.push('/login'),800)}catch{message.value='注册失败，请检查输入信息。';captchaRef.value?.refresh()}}
</script>
