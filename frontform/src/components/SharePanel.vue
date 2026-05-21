<template>
  <div class="page-actions">
    <button
      v-for="channel in channels"
      :key="channel.value"
      class="btn btn--outline"
      type="button"
      :disabled="!articleId"
      @click="share(channel.value)"
    >
      {{ channel.label }}
    </button>
    <span v-if="message" class="message-box">{{ message }}</span>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { shareArticle } from '../api/article'

const props = defineProps({ articleId: Number })
const emit = defineEmits(['shared'])

const message = ref('')
const channels = [
  { value: 'link', label: '复制链接' },
  { value: 'wechat', label: '微信' },
  { value: 'qq', label: 'QQ' },
  { value: 'weibo', label: '微博' },
  { value: 'other', label: '其他' }
]

const share = async (channel) => {
  if (!props.articleId) return

  try {
    await shareArticle(props.articleId, channel)

    if (channel === 'link') {
      try {
        await navigator?.clipboard?.writeText(window.location.href)
        message.value = '转发记录已保存'
      } catch {
        message.value = '转发记录已保存，复制链接失败，请手动复制地址栏链接。'
      }
    } else {
      message.value = '转发记录已保存'
    }

    emit('shared')
  } catch {
    message.value = '转发失败，请稍后再试。'
  }
}
</script>
