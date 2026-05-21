<template>
  <article class="card article-card" @click="openArticle">
    <div v-if="displayCoverUrl" class="article-card__image">
      <img :src="displayCoverUrl" :alt="article.title" :style="{ display: coverFailed ? 'none' : 'block' }" @error="handleCoverError" />
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
import coverCalligraphy from '../picture/华夏文脉数字展馆背景图-书法之美.png'
import coverSolarTerms from '../picture/华夏文脉数字展馆背景图-二十四节气.png'
import coverPeopleFirst from '../picture/华夏文脉数字展馆背景图-民为邦本.png'
import coverJoinery from '../picture/华夏文脉数字展馆背景图-榫卯建构.png'
import coverDragonBoat from '../picture/华夏文脉数字展馆背景图-端午年俗.png'
import coverInnovation from '../picture/华夏文脉数字展馆背景图-革故鼎新.png'
import coverDefault from '../picture/华夏文脉数字展馆背景图.png'

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
const useLocalFallback = ref(false)

const localCoverMap = [
  { keywords: ['书法'], image: coverCalligraphy },
  { keywords: ['端午'], image: coverDragonBoat },
  { keywords: ['革故鼎新'], image: coverInnovation },
  { keywords: ['二十四节气'], image: coverSolarTerms },
  { keywords: ['榫卯'], image: coverJoinery },
  { keywords: ['民为邦本'], image: coverPeopleFirst }
]

const localCoverUrl = computed(() => {
  const title = props.article?.title || ''
  const matched = localCoverMap.find((item) => item.keywords.some((keyword) => title.includes(keyword)))
  return matched?.image || coverDefault
})

const displayCoverUrl = computed(() => {
  if (useLocalFallback.value) return localCoverUrl.value
  return props.article?.coverUrl || localCoverUrl.value
})

watch(
  () => [props.article?.id, props.article?.coverUrl, props.article?.title],
  () => {
    useLocalFallback.value = false
    coverFailed.value = false
  },
  { immediate: true }
)

const handleCoverError = () => {
  if (!useLocalFallback.value && props.article?.coverUrl) {
    useLocalFallback.value = true
    coverFailed.value = false
    return
  }
  coverFailed.value = true
}

const openArticle = () => {
  if (!props.article?.slug) return
  router.push(`/article/${props.article.slug}`)
}
</script>
