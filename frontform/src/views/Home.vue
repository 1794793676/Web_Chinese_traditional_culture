<template><section class="container"><h1>华夏文脉 · 数字展馆</h1><div v-if="loading" class="loading">加载中...</div><div v-else-if="error" class="error">{{error}}</div><template v-else><h2>文化专题</h2><div class="page-actions"><button v-for="c in categories" :key="c.slug" @click="goCategory(c.slug)">{{c.name}}</button></div><h2>精选文章</h2><div class="grid"><ArticleCard v-for="a in featured" :key="a.id" :article="a"/></div><section class="card"><h3>素材来源摘要</h3><p>完整素材来源页需登录后访问。</p><button @click="$router.push('/sources')">查看完整素材来源</button></section></template></section></template>
<script setup>
import { onMounted, ref } from 'vue'; import { useRouter } from 'vue-router'; import { useAuthStore } from '../stores/auth'; import { getCategories,getFeaturedArticles } from '../api/article'; import ArticleCard from '../components/ArticleCard.vue'
const router=useRouter(); const auth=useAuthStore(); const loading=ref(false); const error=ref(''); const categories=ref([]); const featured=ref([])
const load=async()=>{loading.value=true; try{[categories.value,featured.value]=await Promise.all([getCategories(),getFeaturedArticles(6)])}catch(e){error.value=e.message}finally{loading.value=false}}
const goCategory=(slug)=> auth.isLoggedIn ? router.push(`/category/${slug}`):router.push({path:'/login',query:{redirect:`/category/${slug}`}})
onMounted(()=>{auth.loadFromStorage(); load()})
</script>
