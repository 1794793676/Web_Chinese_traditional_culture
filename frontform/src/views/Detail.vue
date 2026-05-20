<template>
  <section class="page-hero">
    <div class="container">
      <p><span class="tag">{{ article.categoryName || '传统文化' }}</span></p>
      <h1>{{ article.title }}</h1>
      <p>{{ article.summary }}</p>
    </div>
  </section>

  <section class="section section--soft">
    <div class="container content-layout">
      <div class="article-detail card">
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <template v-else>
          <div v-if="coverSrc" class="cover-frame">
            <img :src="coverSrc" :alt="article.title" @error="coverError = true" />
          </div>
          <div class="article-content" v-html="article.content"></div>

          <h3>素材来源</h3>
          <ul class="source-list">
            <li v-for="source in article.sources || []" :key="source.id">
              <a :href="source.sourceUrl" target="_blank" rel="noreferrer">{{ source.sourceTitle }}</a>
            </li>
          </ul>

          <h3>发表评论</h3>
          <form class="form" @submit.prevent="submitComment">
            <div class="field">
              <label>评论内容</label>
              <textarea v-model="comment" rows="4" />
            </div>
            <div class="form-actions">
              <button class="btn btn--primary" type="submit">提交评论</button>
            </div>
          </form>

          <p v-if="message" class="message-box">{{ message }}</p>
          <CommentList ref="commentListRef" :article-id="article.id" />
        </template>
      </div>

      <aside class="sidebar card">
        <h3>互动数据</h3>
        <ul class="metric-list">
          <li><span>浏览</span><strong>{{ article.viewCount || 0 }}</strong></li>
          <li><span>点赞</span><strong>{{ article.likeCount || 0 }}</strong></li>
          <li><span>评论</span><strong>{{ article.commentCount || 0 }}</strong></li>
          <li><span>转发</span><strong>{{ article.shareCount || 0 }}</strong></li>
        </ul>
        <div class="page-actions">
          <button class="btn btn--primary" type="button" @click="toggleLike">
            {{ article.likedByCurrentUser ? '取消点赞' : '点赞' }}
          </button>
        </div>
        <SharePanel :article-id="article.id" @shared="refreshDetail" />
      </aside>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { resolveMediaUrl } from '../utils/url'
import { useRoute } from 'vue-router'
import {
  createComment,
  getArticleDetail,
  likeArticle,
  recordView,
  unlikeArticle
} from '../api/article'
import CommentList from '../components/CommentList.vue'
import SharePanel from '../components/SharePanel.vue'

const route = useRoute()
const slug = computed(() => route.params.slug)

const loading = ref(false)
const error = ref('')
const message = ref('')
const comment = ref('')
const article = ref({})
const commentListRef = ref(null)
const coverError = ref(false)

const coverSrc = computed(() => {
  if (coverError.value) return ''
  return resolveMediaUrl(article.value?.coverUrl)
})

const refreshDetail = async () => {
  article.value = await getArticleDetail(slug.value)
  coverError.value = false
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
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const toggleLike = async () => {
  try {
    const request = article.value.likedByCurrentUser ? unlikeArticle : likeArticle
    const counts = await request(article.value.id)
    article.value = {
      ...article.value,
      ...counts,
      likedByCurrentUser: !article.value.likedByCurrentUser
    }
  } catch (err) {
    message.value = err.message
  }
}

const submitComment = async () => {
  if (!comment.value.trim()) return
  try {
    await createComment(article.value.id, comment.value)
    comment.value = ''
    message.value = '评论成功'
    await commentListRef.value?.load()
    await refreshDetail()
  } catch (err) {
    message.value = err.message
  }
}

onMounted(loadDetail)
</script>
