<template>
  <article class="card article-card" @click="openArticle">
    <div v-if="showImage" class="article-card__image">
      <img :src="coverSrc" :alt="article.title" @error="handleImageError" />
    </div>
    <div v-else class="article-card__image article-card__image--fallback">
      <span>华夏文脉</span>
    </div>
    <div class="article-card__body">
      <span class="tag">{{ article.categoryName || '传统文化' }}</span>
      <h3>{{ article.title }}</h3>
      <p>{{ article.summary }}</p>
      <div class="article-card__meta">
        <span>{{ likeCount }} 喜欢 · {{ commentCount }} 评论 · {{ viewCount }} 浏览</span>
        <button class="btn btn--outline" type="button" @click.stop="openArticle">阅读</button>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { resolveMediaUrl } from '../utils/url'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const imageBroken = ref(false)

const coverSrc = computed(() => resolveMediaUrl(props.article?.coverUrl))
const showImage = computed(() => !!coverSrc.value && !imageBroken.value)

const likeCount = computed(() => props.article?.likeCount || 0)
const commentCount = computed(() => props.article?.commentCount || 0)
const viewCount = computed(() => props.article?.viewCount || 0)

const handleImageError = () => {
  imageBroken.value = true
}

watch(
  () => props.article?.coverUrl,
  () => {
    imageBroken.value = false
  }
)

const openArticle = () => {
  if (!props.article?.slug) return
  router.push(`/article/${props.article.slug}`)
}
</script>
