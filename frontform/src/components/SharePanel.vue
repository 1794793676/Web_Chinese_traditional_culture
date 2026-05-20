<template><div class="page-actions"><button v-for="c in channels" :key="c" @click="share(c)">{{c}}</button><span v-if="msg" class="message-box">{{msg}}</span></div></template>
<script setup>
import { ref } from 'vue'; import { shareArticle } from '../api/article'
const props=defineProps({articleId:Number}); const emit=defineEmits(['shared']); const msg=ref(''); const channels=['link','wechat','qq','weibo','other']
const share=async(channel)=>{try{await shareArticle(props.articleId,channel); if(channel==='link') await navigator.clipboard?.writeText(window.location.href); msg.value='转发记录成功'; emit('shared')}catch(e){msg.value=e.message}}
</script>
