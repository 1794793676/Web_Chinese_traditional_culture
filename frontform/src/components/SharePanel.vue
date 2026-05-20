<template>
  <div class="page-actions">
    <button v-for="channel in channels" :key="channel" class="btn btn--outline" type="button" @click="share(channel)">
      {{ channel }}
    </button>
    <span v-if="message" class="message-box">{{ message }}</span>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { shareArticle } from '../api/article'

const props = defineProps({ articleId: Number })
const emit = defineEmits(['shared'])

const channels = ['link', 'wechat', 'qq', 'weibo', 'other']
const message = ref('')

const share = async (channel) => {
  try {
    await shareArticle(props.articleId, channel)
    if (channel === 'link') {
      await navigator.clipboard?.writeText(window.location.href)
    }
    message.value = '转发记录成功'
    emit('shared')
  } catch (err) {
    message.value = err.message
  }
}
</script>
