<template>
  <article class="card article-card" @click="openArticle">
    <div v-if="article.coverUrl" class="article-card__image">
      <img :src="article.coverUrl" :alt="article.title" :style="{ display: coverFailed ? 'none' : 'block' }" @error="coverFailed = true" />
      <div v-if="coverFailed" class="image-fallback">华夏文脉</div>
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

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const likeCount = computed(() => props.article?.likeCount || 0)
const commentCount = computed(() => props.article?.commentCount || 0)
const viewCount = computed(() => props.article?.viewCount || 0)

const coverFailed = ref(false)

watch(
  () => props.article?.coverUrl,
  () => {
    coverFailed.value = false
  },
  { immediate: true }
)

const openArticle = () => {
  if (!props.article?.slug) return
  router.push(`/article/${props.article.slug}`)
}
</script>
