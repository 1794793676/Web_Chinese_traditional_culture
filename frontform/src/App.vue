<template>
  <div class="site-shell">
    <template v-if="!isAdminRoute">
      <NavBar />
      <main class="site-main"><router-view /></main>
      <FooterBar />
    </template>
    <main v-else class="site-main"><router-view /></main>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import FooterBar from './components/FooterBar.vue'
import NavBar from './components/NavBar.vue'
const route = useRoute()
const isAdminRoute = computed(() => route.path.startsWith('/admin'))
</script>
