<template><AdminLayout><section class="section"><h1>数据总览</h1><div class="grid grid--3"><div class="kpi-card" v-for="s in stats" :key="s.k">{{s.t}} {{s.v}}</div></div><h3>热门文章</h3><div class="table-wrap"><table class="data-table"><thead><tr><th>标题</th><th>分类</th><th>浏览</th><th>点赞</th><th>评论</th><th>转发</th><th>热度</th></tr></thead><tbody><tr v-for="p in data.popularArticles||[]" :key="p.articleId||p.title"><td>{{p.title}}</td><td>{{p.categoryName}}</td><td>{{p.viewCount}}</td><td>{{p.likeCount}}</td><td>{{p.commentCount}}</td><td>{{p.shareCount}}</td><td><div class="score-bar"><span :style="{width:(p.totalScore/max*100||0)+'%'}"></span>{{p.totalScore}}</div></td></tr></tbody></table></div></section></AdminLayout></template>
<script setup>
import { computed,onMounted,ref } from 'vue';import AdminLayout from '../../components/AdminLayout.vue';import { getDashboard } from '../../api/admin';
const data=ref({}); onMounted(async()=>{data.value=await getDashboard()||{}})
const stats=computed(()=>[{k:'u',t:'用户数量',v:data.value.userCount||0},{k:'a',t:'文章数量',v:data.value.articleCount||0},{k:'l',t:'点赞数量',v:data.value.likeCount||0},{k:'c',t:'评论数量',v:data.value.commentCount||0},{k:'s',t:'分享数量',v:data.value.shareCount||0},{k:'v',t:'浏览数量',v:data.value.viewCount||0}])
const max=computed(()=>Math.max(1,...(data.value.popularArticles||[]).map(i=>i.totalScore||0)))
</script>
