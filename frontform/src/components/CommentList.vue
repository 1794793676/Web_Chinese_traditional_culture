<template><div><div v-if="loading" class="loading">评论加载中...</div><div v-else-if="error" class="error">{{error}}</div><div v-else-if="!items.length" class="empty">暂无评论</div><ul v-else><li v-for="c in items" :key="c.id"><b>{{c.nickname||c.username}}</b>：{{c.content}}</li></ul><div class="pagination"><button :disabled="page<=1" @click="page--,load()">上一页</button><span>{{page}}</span><button :disabled="!hasNext" @click="page++,load()">下一页</button></div></div></template>
<script setup>
import { ref, watch } from 'vue'; import { getComments } from '../api/article'
const props=defineProps({articleId:Number}); const loading=ref(false); const error=ref(''); const items=ref([]); const page=ref(1); const size=ref(10); const hasNext=ref(false)
const load=async()=>{if(!props.articleId) return; loading.value=true; error.value=''; try{const d=await getComments(props.articleId,page.value,size.value); items.value=d.records||d.list||[]; hasNext.value=(d.totalPages? page.value<d.totalPages: (items.value.length>=size.value));}catch(e){error.value=e.message}finally{loading.value=false}}
watch(()=>props.articleId,()=>{page.value=1; load()},{immediate:true}); defineExpose({load})
</script>
