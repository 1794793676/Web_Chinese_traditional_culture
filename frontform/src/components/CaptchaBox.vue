<template>
  <div class="captcha-box">
    <img
      v-if="captchaImage"
      :src="captchaImage"
      alt="captcha"
      class="captcha-box__image"
      @click="refresh"
    />
    <button class="btn btn--outline" type="button" @click="refresh">刷新验证码</button>
    <p v-if="error" class="form-error">{{ error }}</p>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getCaptcha } from '../api/auth'

const props = defineProps({
  purpose: {
    type: String,
    default: 'login'
  }
})

const emit = defineEmits(['update:key', 'refreshed'])
const captchaImage = ref('')
const error = ref('')

const refresh = async () => {
  try {
    const data = await getCaptcha(props.purpose)
    captchaImage.value = data.captchaImage
    error.value = ''
    emit('update:key', data.captchaKey)
    emit('refreshed')
  } catch {
    error.value = '验证码加载失败，请点击刷新重试。'
  }
}

onMounted(refresh)
defineExpose({ refresh })
</script>
