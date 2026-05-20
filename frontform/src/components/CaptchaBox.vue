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

const refresh = async () => {
  const data = await getCaptcha(props.purpose)
  captchaImage.value = data.captchaImage
  emit('update:key', data.captchaKey)
  emit('refreshed')
}

onMounted(refresh)
defineExpose({ refresh })
</script>
