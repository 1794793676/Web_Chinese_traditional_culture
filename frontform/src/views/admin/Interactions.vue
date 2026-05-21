<template>
  <AdminLayout>
    <section class="section">
      <h1>互动统计</h1>

      <div v-if="loading" class="loading">正在加载互动统计...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <template v-else>
        <div class="grid grid--4">
          <div class="kpi-card">总浏览量 {{ summary.view }}</div>
          <div class="kpi-card">总点赞量 {{ summary.like }}</div>
          <div class="kpi-card">总评论数 {{ summary.comment }}</div>
          <div class="kpi-card">总转发数 {{ summary.share }}</div>
        </div>

        <div v-if="!items.length" class="empty">暂无互动统计数据</div>
        <div v-else class="table-wrap">
          <table class="data-table">
            <thead>
              <tr>
                <th>文章ID</th>
                <th>文章标题</th>
                <th>所属分类</th>
                <th>浏览量</th>
                <th>点赞数</th>
                <th>评论数</th>
                <th>转发数</th>
                <th>综合热度</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in items" :key="item.articleId">
                <td>{{ item.articleId }}</td>
                <td>{{ item.title }}</td>
                <td>{{ item.categoryName }}</td>
                <td>{{ item.viewCount }}</td>
                <td>{{ item.likeCount }}</td>
                <td>{{ item.commentCount }}</td>
                <td>{{ item.shareCount }}</td>
                <td>
                  <div class="score-bar">
                    <span :style="{ width: `${((item.totalScore || 0) / maxScore) * 100}%` }" />
                    {{ item.totalScore || 0 }}
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </template>
    </section>
  </AdminLayout>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import AdminLayout from '../../components/AdminLayout.vue'
import { getInteractions } from '../../api/admin'

const loading = ref(false)
const error = ref('')
const items = ref([])

const maxScore = computed(() => Math.max(1, ...items.value.map((i) => i.totalScore || 0)))
const summary = computed(() =>
  items.value.reduce(
    (acc, cur) => ({
      view: acc.view + (cur.viewCount || 0),
      like: acc.like + (cur.likeCount || 0),
      comment: acc.comment + (cur.commentCount || 0),
      share: acc.share + (cur.shareCount || 0)
    }),
    { view: 0, like: 0, comment: 0, share: 0 }
  )
)

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    items.value = (await getInteractions({ limit: 20 })) || []
  } catch {
    error.value = '互动统计加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
