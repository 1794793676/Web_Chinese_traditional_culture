<template>
  <AdminLayout>
    <section class="section">
      <h1>数据总览</h1>

      <div class="kpi-grid">
        <div class="kpi-card" v-for="s in stats" :key="s.key">
          <span>{{ s.title }}</span>
          <strong>{{ s.value }}</strong>
        </div>
      </div>

      <h3>热门文章排行</h3>
      <div class="table-wrap">
        <table class="data-table">
          <thead>
            <tr><th>文章标题</th><th>所属分类</th><th>浏览量</th><th>点赞数</th><th>评论数</th><th>转发数</th><th>综合热度</th></tr>
          </thead>
          <tbody>
            <tr v-for="item in data.popularArticles || []" :key="item.articleId || item.title">
              <td>{{ item.title }}</td><td>{{ item.categoryName }}</td><td>{{ item.viewCount }}</td><td>{{ item.likeCount }}</td><td>{{ item.commentCount }}</td><td>{{ item.shareCount }}</td>
              <td><div class="score-bar"><span :style="{ width: `${((item.totalScore || 0) / maxScore) * 100}%` }" />{{ item.totalScore || 0 }}</div></td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </AdminLayout>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import AdminLayout from '../../components/AdminLayout.vue'
import { getDashboard } from '../../api/admin'
const data = ref({})
const maxScore = computed(() => Math.max(1, ...(data.value.popularArticles || []).map((i) => i.totalScore || 0)))
const stats = computed(() => [
  { key: 'user', title: '总用户数', value: data.value.userCount || 0 },
  { key: 'article', title: '总文章数', value: data.value.articleCount || 0 },
  { key: 'like', title: '总点赞数', value: data.value.likeCount || 0 },
  { key: 'comment', title: '总评论数', value: data.value.commentCount || 0 },
  { key: 'share', title: '总分享数', value: data.value.shareCount || 0 },
  { key: 'view', title: '总浏览数', value: data.value.viewCount || 0 }
])
onMounted(async () => { data.value = (await getDashboard()) || {} })
</script>
