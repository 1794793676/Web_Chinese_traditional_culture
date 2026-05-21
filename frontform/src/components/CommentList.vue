<template><div class="comment-list"><div v-if="loading" class="loading">评论加载中...</div><div v-else-if="error" class="error">{{ error }}</div><div v-else-if="!items.length" class="empty">暂无评论，欢迎发表第一条见解。</div><ul v-else><li v-for="item in items" :key="item.id" class="comment-item"><div><strong>{{ item.nickname||item.username||'匿名用户' }}</strong><span class="tag">{{ item.status||'visible' }}</span></div><p>{{ item.content }}</p></li></ul></div></template>
<script setup>
import { ref,watch } from 'vue';import { getComments } from '../api/article'
const props=defineProps({articleId:Number});const loading=ref(false),error=ref(''),items=ref([])
const load=async()=>{if(!props.articleId)return;loading.value=true;error.value='';try{const data=await getComments(props.articleId,1,10);items.value=Array.isArray(data)?data:(data.items||data.records||data.list||[])}catch(e){error.value=e.message}finally{loading.value=false}}
watch(()=>props.articleId,load,{immediate:true});defineExpose({load})
</script>
