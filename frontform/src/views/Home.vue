<template>
  <section class="hero">
    <div class="container hero__content">
      <div class="hero__icon">文</div>
      <h1>华夏文脉 · 中华优秀传统文化数字展馆</h1>
      <p>在现代网页中重读传统文化的思想、器物与精神</p>
      <div class="hero__actions">
        <button class="btn btn--primary" type="button" @click="enterMuseum">进入展馆</button>
        <router-link class="btn btn--outline" to="/register">注册体验</router-link>
      </div>
    </div>
  </section>

  <section class="section">
    <div class="container">
      <div class="section-heading">
        <h2>文化专题</h2>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!categories.length" class="empty">暂无专题分类</div>
      <div v-else class="grid grid--3">
        <button
          v-for="category in categories"
          :key="category.slug"
          class="card category-card"
          type="button"
          @click="goCategory(category.slug)"
        >
          <div class="category-card__head">
            <div class="category-card__icon">{{ categoryIconMap[category.slug] || '文' }}</div>
            <h3>{{ category.name }}</h3>
          </div>
          <p>{{ category.description || '登录后查看完整内容' }}</p>
          <div class="category-card__foot">
            <span>{{ auth.isLoggedIn ? '登录后可互动' : '登录后查看完整内容' }}</span>
            <span>查看专题 →</span>
          </div>
        </button>
      </div>
    </div>
  </section>

  <section id="featured" class="section section--soft">
    <div class="container">
      <div class="section-heading"><h2>典藏精品</h2></div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!featured.length" class="empty">暂无精选文章</div>
      <div v-else class="grid grid--3">
        <ArticleCard v-for="item in featured" :key="item.id" :article="item" />
      </div>
    </div>
  </section>

  <section class="section section--soft">
    <div class="container">
      <div class="section-heading"><h2>文脉纪元</h2></div>
      <div class="timeline card">
        <div v-for="item in timeline" :key="item" class="timeline__item"><span class="timeline__dot" /><span>{{ item }}</span></div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories, getFeaturedArticles } from '../api/article'
import ArticleCard from '../components/ArticleCard.vue'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()

const loading = ref(false)
const error = ref('')
const categories = ref([])
const featured = ref([])
const timeline = ['先秦思想', '汉唐气象', '宋元审美', '明清技艺', '当代表达']
const categoryIconMap = {
  thought: '思',
  spirit: '魂',
  craft: '匠',
  festival: '礼',
  science: '知',
  ecology: '生'
}

const loadData = async () => {
  loading.value = true
  error.value = ''
  try {
    const [categoryData, featuredData] = await Promise.all([getCategories(), getFeaturedArticles(6)])
    categories.value = categoryData || []
    featured.value = featuredData || []
  } catch (err) {
    error.value = err.message || '首页数据加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

const goCategory = (slug) => {
  if (auth.isLoggedIn) return router.push(`/category/${slug}`)
  router.push({ path: '/login', query: { redirect: `/category/${slug}` } })
}

const enterMuseum = () => {
  const first = categories.value[0]
  if (first?.slug) goCategory(first.slug)
}

onMounted(() => {
  auth.loadFromStorage()
  loadData()
})
</script>
