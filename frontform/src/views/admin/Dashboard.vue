<template>
  <section class="page-hero admin-page">
    <div class="container">
      <h1>后台数据总览</h1>
      <p>实时查看平台互动与内容运营状态。</p>
    </div>
  </section>

  <section class="section admin-page">
    <div class="container">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <template v-else>
        <div class="grid grid--3">
          <div class="kpi-card" v-for="item in stats" :key="item.key">
            <span>{{ item.label }}</span>
            <strong>{{ item.value }}</strong>
          </div>
        </div>

        <div class="grid grid--2" style="margin-top: 16px">
          <div class="card chart-card">
            <h3>热门文章</h3>
            <ul><li v-for="item in data.popularArticles || []" :key="item.id">{{ item.title }}</li></ul>
          </div>
          <div class="table-wrap">
            <table class="data-table">
              <thead><tr><th>用户</th><th>最新评论</th></tr></thead>
              <tbody>
                <tr v-for="item in data.latestComments || []" :key="item.id">
                  <td>{{ item.nickname || item.username }}</td>
                  <td>{{ item.content }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { getDashboard } from '../../api/admin'

const loading = ref(false)
const error = ref('')
const data = ref({})

const DASHBOARD_STAT_LABELS = {
  userCount: '用户数量',
  articleCount: '文章数量',
  likeCount: '点赞数量',
  commentCount: '评论数量',
  shareCount: '分享数量',
  viewCount: '浏览数量'
}

const stats = computed(() =>
  Object.entries(DASHBOARD_STAT_LABELS).map(([key, label]) => ({
    key,
    label,
    value: data.value[key] ?? 0
  }))
)

onMounted(async () => {
  loading.value = true
  try {
    data.value = await getDashboard()
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
})
</script>
