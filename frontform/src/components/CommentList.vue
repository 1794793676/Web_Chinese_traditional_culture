<template>
  <div class="comment-list">
    <div v-if="loading" class="loading">评论加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="!items.length" class="empty">暂无评论，欢迎发表第一条见解。</div>
    <ul v-else>
      <li v-for="item in items" :key="item.id" class="comment-item">
        <div class="comment-item__head">
          <strong>{{ item.nickname || '匿名用户' }}</strong>
          <span class="muted">@{{ item.username || 'unknown' }}</span>
          <span class="tag" :class="statusClass(item.status)">{{ formatStatus(item.status) }}</span>
        </div>
        <p class="muted">{{ item.createdAt || '-' }}</p>
        <p>{{ item.content }}</p>
      </li>
    </ul>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
import { getComments } from '../api/article'
import { formatStatus, normalizeList, statusClass } from '../utils/format'
const props = defineProps({ articleId: Number })
const loading = ref(false)
const error = ref('')
const items = ref([])
const load = async () => {
  if (!props.articleId) return
  loading.value = true
  error.value = ''
  try {
    const data = await getComments(props.articleId, 1, 10)
    items.value = normalizeList(data)
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
watch(() => props.articleId, load, { immediate: true })
defineExpose({ load })
</script>
