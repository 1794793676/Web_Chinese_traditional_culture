<template>
  <article class="card article-card" :class="{ 'is-disabled': !article.slug }" @click="openArticle">
    <div class="article-card__image">
      <img :src="displayCover" :alt="safeText(article.title, '传统文化文章')" @error="onImageError" />
    </div>
    <div class="article-card__body">
      <span class="tag">{{ safeText(article.categoryName, '传统文化') }}</span>
      <h3>{{ safeText(article.title, '未命名文章') }}</h3>
      <p>{{ safeText(article.summary, '暂无摘要') }}</p>
      <p v-if="article.sourceTitle" class="article-card__source">来源：{{ article.sourceTitle }}</p>
      <div class="article-card__meta">
        <span>浏览 {{ formatNumber(article.viewCount) }} · 点赞 {{ formatNumber(article.likeCount) }} · 评论 {{ formatNumber(article.commentCount) }} · 转发 {{ formatNumber(article.shareCount) }}</span>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { resolveMediaUrl } from '../utils/url'
import { formatNumber, getFallbackCover, safeText } from '../utils/format'

const props = defineProps({ article: { type: Object, required: true } })
const router = useRouter()
const imgError = ref(false)

const displayCover = computed(() => {
  if (imgError.value || !props.article?.coverUrl) return getFallbackCover(props.article)
  return resolveMediaUrl(props.article.coverUrl)
})

watch(() => [props.article?.id, props.article?.coverUrl], () => (imgError.value = false), { immediate: true })

const onImageError = () => {
  imgError.value = true
}

const openArticle = () => {
  if (!props.article?.slug) return
  router.push(`/article/${props.article.slug}`)
}
</script>
