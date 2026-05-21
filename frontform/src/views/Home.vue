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
        <p>从思想、精神、器物、节日、科技、生态等角度理解传统文化。</p>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!categories.length" class="empty">暂无分类</div>
      <div v-else class="grid grid--3">
        <button
          v-for="category in categories"
          :key="category.slug"
          class="card category-card"
          type="button"
          @click="goCategory(category.slug)"
        >
          <div class="category-card__head">
            <h3>{{ category.name }}</h3>
          </div>
          <p>{{ category.description || '登录后查看完整内容。' }}</p>
        </button>
      </div>
    </div>
  </section>

  <section class="section section--soft">
    <div class="container">
      <div class="section-heading"><h2>精选文章</h2></div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!featured.length" class="empty">暂无精选文章</div>
      <div v-else class="grid grid--3">
        <ArticleCard v-for="item in featured" :key="item.id" :article="item" />
      </div>
    </div>
  </section>

  <section class="section section--soft" id="featured">
    <div class="container">
      <div class="section-heading"><h2>文脉纪元</h2></div>
      <div class="card" style="padding:16px;">先秦思想 → 汉唐气象 → 宋元审美 → 明清技艺 → 当代表达</div>
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

const loadData = async () => {
  loading.value = true
  error.value = ''
  try {
    const [categoryData, featuredData] = await Promise.all([getCategories(), getFeaturedArticles(6)])
    categories.value = categoryData || []
    featured.value = featuredData || []
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const goCategory = (slug) => {
  if (auth.isLoggedIn) {
    router.push(`/category/${slug}`)
    return
  }
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
