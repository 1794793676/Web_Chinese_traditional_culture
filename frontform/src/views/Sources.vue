<template>
  <section class="page-hero">
    <div class="container">
      <h1>素材来源说明</h1>
      <p>本页登录后可访问，用于集中说明素材来源和引用规范。</p>
    </div>
  </section>
  <section class="section">
    <div class="container grid grid--2">
      <article class="card article-body">
        <h3>文字来源说明</h3>
        <p>文章正文与摘要以课程数据源为准，引用时请标注来源。</p>
      </article>
      <article class="card article-body">
        <h3>图片来源说明</h3>
        <p>文章封面及配图来源详见对应文章详情页的 sources 字段。</p>
      </article>
      <article class="card article-body">
        <h3>图标与字体说明</h3>
        <p>页面图标与字体资源用于课程教学展示，不用于商业传播。</p>
      </article>
      <article class="card article-body">
        <h3>sources 字段说明</h3>
        <p>sourceTitle/sourceUrl/sourceType 为详情页引用依据。</p>
      </article>
    </div>
    <div class="container page-actions">
      <router-link class="btn btn--outline" to="/">返回首页</router-link>
      <button class="btn btn--primary" type="button" @click="$router.back()">返回上一页</button>
    </div>
  </section>
</template>
