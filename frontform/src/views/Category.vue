<template>
  <section class="page-hero category-hero" :style="heroStyle">
    <div class="container">
      <span class="tag">文化专题</span>
      <h1>{{ categoryTitle }}</h1>
      <p>{{ categoryDescription }}</p>
    </div>
  </section>

  <section class="section">
    <div class="container">
      <div class="page-actions">
        <button
          v-for="cat in categories"
          :key="cat.slug"
          class="btn"
          :class="cat.slug === normalizedSlug ? 'btn--primary' : 'btn--outline'"
          @click="switchCategory(cat.slug)"
        >
          {{ cat.name }}
        </button>
      </div>

      <div v-if="loading" class="loading">正在加载专题文章...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!items.length" class="empty">此专题暂无文章</div>
      <div v-else class="grid grid--3">
        <ArticleCard v-for="article in items" :key="article.id" :article="article" />
      </div>

      <div class="pagination">
        <button :disabled="page <= 1" @click="changePage(page - 1)">上一页</button>
        <span>第 {{ page }} 页</span>
        <button :disabled="!hasNext" @click="changePage(page + 1)">下一页</button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticlesByCategory, getCategories } from '../api/article'
import ArticleCard from '../components/ArticleCard.vue'
import { getCategoryName, normalizeCategorySlug } from '../constants/category'
import { normalizeList } from '../utils/format'
import { getCategoryHeroImage } from '../utils/pictureMap'

const route = useRoute()
const router = useRouter()

const slug = computed(() => route.params.slug)
const normalizedSlug = computed(() => normalizeCategorySlug(slug.value))
const loading = ref(false)
const error = ref('')
const page = ref(1)
const size = ref(10)
const hasNext = ref(false)
const items = ref([])
const categories = ref([])

const categoryInfo = computed(() => categories.value.find((c) => c.slug === normalizedSlug.value) || {})
const categoryTitle = computed(() => categoryInfo.value.name || getCategoryName(normalizedSlug.value))
const categoryDescription = computed(() => categoryInfo.value.description || `${categoryTitle.value}专题文章 / 文化解读`)
const heroStyle = computed(() => ({ '--page-hero-bg': `url(${getCategoryHeroImage(normalizedSlug.value)})` }))

const loadList = async () => {
  loading.value = true
  error.value = ''
  try {
    const data = await getArticlesByCategory(normalizedSlug.value, page.value, size.value)
    const list = normalizeList(data)
    items.value = list
    hasNext.value = data?.total ? page.value * size.value < data.total : list.length >= size.value
  } catch (err) {
    error.value = err.message || '专题文章加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    categories.value = (await getCategories()) || []
  } catch {
    categories.value = []
  }
}

const switchCategory = (targetSlug) => {
  if (targetSlug === normalizedSlug.value) return
  router.push(`/category/${targetSlug}`)
}

const changePage = (target) => {
  page.value = target
  loadList()
}

watch(
  slug,
  async () => {
    page.value = 1
    await loadCategories()
    await loadList()
  },
  { immediate: true }
)
</script>
