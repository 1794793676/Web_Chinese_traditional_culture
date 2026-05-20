<template><section class="container"><h2>用户登录</h2><form @submit.prevent="submit"><input v-model="form.username" placeholder="用户名"/><input v-model="form.password" type="password" placeholder="密码"/><input v-model="form.captchaCode" placeholder="验证码"/><CaptchaBox ref="capRef" purpose="login" @update:key="form.captchaKey=$event" @refreshed="form.captchaCode=''"/><button>登录</button></form><p v-if="msg" class="form-error">{{msg}}</p></section></template>
<script setup>
import { reactive, ref } from 'vue'; import { useRoute,useRouter } from 'vue-router'; import { login } from '../api/auth'; import { useAuthStore } from '../stores/auth'; import CaptchaBox from '../components/CaptchaBox.vue'
const route=useRoute(); const router=useRouter(); const auth=useAuthStore(); const capRef=ref(); const msg=ref('');
const form=reactive({username:'',password:'',captchaCode:'',captchaKey:''})
const submit=async()=>{msg.value=''; try{const d=await login(form); auth.setAuth(d.token,d.user); router.push(route.query.redirect|| (d.user?.role==='admin'?'/admin/dashboard':'/'))}catch(e){msg.value=e.message; capRef.value?.refresh()}}
</script>
