<template>
  <section class="page-hero">
    <div class="container">
      <h1>{{ categoryTitle }}</h1>
      <p>分类专题文章列表</p>
    </div>
  </section>

  <section class="section">
    <div class="container">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!items.length" class="empty">暂无文章</div>
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
import { useRoute } from 'vue-router'
import { getArticlesByCategory, getCategories } from '../api/article'
import ArticleCard from '../components/ArticleCard.vue'
import { getCategoryName, normalizeCategorySlug } from '../constants/category'

const route = useRoute()
const slug = computed(() => route.params.slug)
const normalizedSlug = computed(() => normalizeCategorySlug(slug.value))
const loading = ref(false)
const error = ref('')
const page = ref(1)
const size = ref(10)
const hasNext = ref(false)
const items = ref([])
const categories = ref([])

const categoryTitle = computed(
  () =>
    categories.value.find((c) => c.slug === normalizedSlug.value)?.name || getCategoryName(normalizedSlug.value)
)

const loadList = async () => {
  loading.value = true
  error.value = ''
  try {
    const data = await getArticlesByCategory(normalizedSlug.value, page.value, size.value)
    items.value = data.items || data.records || data.list || []
    hasNext.value = data.total ? page.value * size.value < data.total : items.value.length >= size.value
  } catch (err) {
    error.value = err.message
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
