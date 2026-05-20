<template><div><img v-if="captchaImage" :src="captchaImage" alt="captcha" @click="refresh" style="cursor:pointer;height:40px"/><button type="button" @click="refresh">刷新验证码</button></div></template>
<script setup>
import { onMounted, ref } from 'vue'; import { getCaptcha } from '../api/auth'
const props=defineProps({purpose:{type:String,default:'login'}}); const emit=defineEmits(['update:key','refreshed'])
const captchaImage=ref('')
const refresh=async()=>{const d=await getCaptcha(props.purpose); captchaImage.value=d.captchaImage; emit('update:key',d.captchaKey); emit('refreshed')}
onMounted(refresh)
defineExpose({refresh})
</script>
