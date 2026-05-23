<template>
  <section class="page-hero detail-hero" :style="heroStyle">
    <div class="container">
      <p class="muted">首页 / {{ article.categoryName || '文化专题' }} / {{ article.title || '文章详情' }}</p>
      <p><span class="tag">{{ article.categoryName || '传统文化' }}</span></p>
      <h1>{{ article.title }}</h1>
      <p>{{ article.summary }}</p>
    </div>
  </section>

  <section class="section section--soft">
    <div class="container content-layout">
      <div class="article-detail card article-body">
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <template v-else>
          <div class="cover-frame">
            <img v-if="!coverFailed" :src="displayCover" :alt="article.title" @error="coverFailed = true" />
            <img v-else :src="fallbackDetailCover" :alt="article.title || '默认封面'" class="cover-fallback-image" />
          </div>

          <div v-if="isHtml" class="article-content" v-html="article.content" />
          <div v-else class="article-content">
            <p v-for="(line, idx) in paragraphLines" :key="idx">{{ line }}</p>
          </div>

          <h3>素材来源</h3>
          <ul v-if="article.sources?.length" class="source-list">
            <li v-for="source in article.sources" :key="source.id">
              <template v-if="source.sourceUrl && source.sourceUrl !== '#'">
                <a :href="source.sourceUrl" target="_blank" rel="noreferrer">{{ source.sourceTitle }}</a>
              </template>
              <template v-else>{{ source.sourceTitle }}</template>
              <span class="muted">（{{ source.sourceType || '未标注类型' }}）</span>
            </li>
          </ul>
          <p v-else class="empty">暂无来源信息</p>

          <h3>发表评论</h3>
          <form class="form" @submit.prevent="submitComment">
            <div class="field">
              <label>评论内容</label>
              <textarea v-model="comment" rows="4" maxlength="500" placeholder="请输入你的观点（最多500字）" />
            </div>
            <div class="form-actions">
              <button class="btn btn--primary" type="submit">提交评论</button>
            </div>
          </form>

          <p v-if="message" class="message-box">{{ message }}</p>
          <CommentList ref="commentListRef" :article-id="article.id" />
        </template>
      </div>

      <aside class="sidebar card sidebar-box">
        <h3>互动数据</h3>
        <ul class="metric-list">
          <li><span>浏览</span><strong>{{ article.viewCount || 0 }}</strong></li>
          <li><span>点赞</span><strong>{{ article.likeCount || 0 }}</strong></li>
          <li><span>评论</span><strong>{{ article.commentCount || 0 }}</strong></li>
          <li><span>转发</span><strong>{{ article.shareCount || 0 }}</strong></li>
        </ul>
        <button class="btn btn--primary" type="button" @click="toggleLike">
          {{ article.likedByCurrentUser ? '取消点赞' : '点赞' }}
        </button>
        <SharePanel :article-id="article.id" @shared="refreshDetail" />
      </aside>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { createComment, getArticleDetail, likeArticle, recordView, unlikeArticle } from '../api/article'
import { getFallbackCover } from '../utils/format'
import { fallbackDetailCover, getCategoryHeroImage } from '../utils/pictureMap'
import CommentList from '../components/CommentList.vue'
import SharePanel from '../components/SharePanel.vue'

const route = useRoute()
const slug = computed(() => route.params.slug)
const loading = ref(false)
const error = ref('')
const message = ref('')
const comment = ref('')
const article = ref({})
const coverFailed = ref(false)
const commentListRef = ref(null)

const isHtml = computed(() => /<[^>]+>/.test(article.value?.content || ''))
const paragraphLines = computed(() => String(article.value?.content || '').split('\n').filter(Boolean))
const displayCover = computed(() => article.value.coverUrl || getFallbackCover(article.value) || fallbackDetailCover)
const heroStyle = computed(() => ({ '--page-hero-bg': `url(${getCategoryHeroImage(article.value?.categorySlug || 'thought')})` }))

const refreshDetail = async () => {
  article.value = await getArticleDetail(slug.value)
  coverFailed.value = false
}

const loadDetail = async () => {
  loading.value = true
  error.value = ''
  try {
    await refreshDetail()
    if (article.value?.id) {
      const counts = await recordView(article.value.id)
      article.value = { ...article.value, ...counts }
    }
  } catch (err) {
    error.value = err.message || '文章加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

const toggleLike = async () => {
  try {
    const request = article.value.likedByCurrentUser ? unlikeArticle : likeArticle
    const counts = await request(article.value.id)
    article.value = { ...article.value, ...counts, likedByCurrentUser: !article.value.likedByCurrentUser }
  } catch {
    message.value = '点赞操作失败，请稍后再试。'
  }
}

const submitComment = async () => {
  if (!comment.value.trim()) {
    message.value = '评论内容不能为空。'
    return
  }
  try {
    await createComment(article.value.id, comment.value.trim())
    comment.value = ''
    message.value = '评论提交成功。'
    await commentListRef.value?.load()
    await refreshDetail()
  } catch {
    message.value = '评论提交失败，请稍后再试。'
  }
}

onMounted(loadDetail)
</script>
