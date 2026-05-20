<template><section class="container"><h2>用户注册</h2><form @submit.prevent="submit"><input v-model="form.username" placeholder="用户名"/><input v-model="form.email" placeholder="邮箱"/><input v-model="form.nickname" placeholder="昵称"/><input type="password" v-model="form.password" placeholder="密码"/><input type="password" v-model="form.confirmPassword" placeholder="确认密码"/><input v-model="form.captchaCode" placeholder="验证码"/><CaptchaBox ref="capRef" purpose="register" @update:key="form.captchaKey=$event" @refreshed="form.captchaCode=''"/><button>注册</button></form><p v-if="msg" :class="ok?'form-success':'form-error'">{{msg}}</p></section></template>
<script setup>
import { reactive, ref } from 'vue'; import { useRouter } from 'vue-router'; import { register } from '../api/auth'; import CaptchaBox from '../components/CaptchaBox.vue'
const router=useRouter(); const capRef=ref(); const msg=ref(''); const ok=ref(false)
const form=reactive({username:'',email:'',nickname:'',password:'',confirmPassword:'',captchaCode:'',captchaKey:''})
const submit=async()=>{ok.value=false; if(form.password!==form.confirmPassword){msg.value='两次密码不一致'; return} try{await register(form); ok.value=true; msg.value='注册成功，即将跳转登录'; setTimeout(()=>router.push('/login'),800)}catch(e){msg.value=e.message; capRef.value?.refresh()}}
</script>
