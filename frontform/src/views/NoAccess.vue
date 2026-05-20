<template><section class="container"><h2>无访问权限</h2><p v-if="!auth.isLoggedIn">请先登录后访问子页面。</p><p v-else>当前账号无后台权限。</p><div class="page-actions"><router-link to="/login">去登录</router-link><router-link to="/">返回首页</router-link></div></section></template>
<script setup>import { useAuthStore } from '../stores/auth'; const auth=useAuthStore(); auth.loadFromStorage()</script>
