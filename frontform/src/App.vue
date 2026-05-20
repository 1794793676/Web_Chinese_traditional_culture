<template>
  <div class="site-shell">
    <NavBar />
    <main class="site-main">
      <router-view />
    </main>
    <FooterBar />
  </div>
</template>

<script setup>
import FooterBar from './components/FooterBar.vue'
import NavBar from './components/NavBar.vue'
</script>
