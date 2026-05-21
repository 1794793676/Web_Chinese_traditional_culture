<template>
  <article class="card article-card" @click="openArticle">
    <div class="article-card__image">
      <img :src="displayCoverUrl" :alt="article.title" :style="{ display: coverFailed ? 'none' : 'block' }" @error="coverFailed = true" />
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
import coverDefault from '../picture/华夏文脉数字展馆背景图.png'
import coverCalligraphy from '../picture/华夏文脉数字展馆背景图-书法之美.png'
import coverSolarTerms from '../picture/华夏文脉数字展馆背景图-二十四节气.png'
import coverMortise from '../picture/华夏文脉数字展馆背景图-榫卯建构.png'
import coverPeople from '../picture/华夏文脉数字展馆背景图-民为邦本.png'
import coverDragonBoat from '../picture/华夏文脉数字展馆背景图-端午年俗.png'
import coverInnovation from '../picture/华夏文脉数字展馆背景图-革故鼎新.png'

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

const localCoverMap = [
  { matcher: /书法/, cover: coverCalligraphy },
  { matcher: /端午|年俗/, cover: coverDragonBoat },
  { matcher: /革故鼎新|创新/, cover: coverInnovation },
  { matcher: /二十四节气|节气/, cover: coverSolarTerms },
  { matcher: /榫卯|建构/, cover: coverMortise },
  { matcher: /民为邦本|邦本|民本/, cover: coverPeople }
]

const localCoverUrl = computed(() => {
  const text = `${props.article?.title || ''} ${props.article?.summary || ''} ${props.article?.categoryName || ''}`
  const hit = localCoverMap.find((item) => item.matcher.test(text))
  return hit ? hit.cover : coverDefault
})

const displayCoverUrl = computed(() => props.article?.coverUrl || localCoverUrl.value)

watch(
  () => [props.article?.coverUrl, props.article?.title],
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
