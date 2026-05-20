<template>
  <section class="page-hero admin-page"><div class="container"><h1>互动排行</h1><p>按综合分查看互动热度。</p></div></section>
  <section class="section admin-page">
    <div class="container">
      <div class="page-actions">
        <select v-model="limit" @change="load"><option :value="10">10</option><option :value="20">20</option><option :value="50">50</option></select>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="table-wrap">
        <table class="data-table">
          <thead><tr><th>articleId</th><th>title</th><th>category</th><th>view</th><th>like</th><th>comment</th><th>share</th><th>score</th></tr></thead>
          <tbody>
            <tr v-for="item in items" :key="item.articleId">
              <td>{{ item.articleId }}</td><td>{{ item.title }}</td><td>{{ item.categoryName }}</td>
              <td><span class="tag">{{ item.viewCount }}</span></td><td><span class="tag">{{ item.likeCount }}</span></td>
              <td><span class="tag">{{ item.commentCount }}</span></td><td><span class="tag">{{ item.shareCount }}</span></td>
              <td><span class="status status--success">{{ item.totalScore }}</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getInteractions } from '../../api/admin'

const loading = ref(false)
const error = ref('')
const items = ref([])
const limit = ref(10)

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    items.value = await getInteractions({ limit: limit.value })
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
