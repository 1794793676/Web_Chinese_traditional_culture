<template><section class="container"><h2>分类：{{slug}}</h2><div v-if="loading" class="loading"/><div v-else-if="error" class="error">{{error}}</div><div v-else-if="!items.length" class="empty">暂无文章</div><div v-else class="grid"><ArticleCard v-for="a in items" :key="a.id" :article="a"/></div><div class="pagination"><button :disabled="page<=1" @click="page--,load()">上一页</button><span>{{page}}</span><button :disabled="!hasNext" @click="page++,load()">下一页</button></div></section></template>
<script setup>
import { computed, ref, watch } from 'vue'; import { useRoute } from 'vue-router'; import { getArticlesByCategory } from '../api/article'; import ArticleCard from '../components/ArticleCard.vue'
const route=useRoute(); const slug=computed(()=>route.params.slug); const loading=ref(false); const error=ref(''); const page=ref(1); const size=ref(10); const items=ref([]); const hasNext=ref(false)
const load=async()=>{loading.value=true; error.value=''; try{const d=await getArticlesByCategory(slug.value,page.value,size.value); items.value=d.records||d.list||[]; hasNext.value=d.totalPages ? page.value<d.totalPages : items.value.length>=size.value}catch(e){error.value=e.message}finally{loading.value=false}}
watch(slug,()=>{page.value=1; load()},{immediate:true})
</script>
