<template>
  <section class="page-hero admin-page">
    <div class="container"><h1>评论管理</h1><p>审核与更新评论状态。</p></div>
  </section>
  <section class="section admin-page">
    <div class="container">
      <div class="page-actions">
        <select v-model="status" @change="page = 1; load()">
          <option value="">all</option><option value="visible">visible</option><option value="hidden">hidden</option><option value="deleted">deleted</option><option value="pending">pending</option>
        </select>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="table-wrap">
        <table class="data-table">
          <thead><tr><th>ID</th><th>内容</th><th>状态</th><th>操作</th></tr></thead>
          <tbody>
            <tr v-for="item in items" :key="item.id">
              <td>{{ item.id }}</td>
              <td>{{ item.content }}</td>
              <td><span class="status" :class="statusClass(item.status)">{{ item.status }}</span></td>
              <td>
                <select v-model="item._next"><option>visible</option><option>hidden</option><option>deleted</option><option>pending</option></select>
                <button class="btn btn--outline" @click="update(item)">提交</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="pagination">
        <button :disabled="page <= 1" @click="page--, load()">上一页</button>
        <span>第 {{ page }} 页</span>
        <button :disabled="!hasNext" @click="page++, load()">下一页</button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getAdminComments, updateCommentStatus } from '../../api/admin'

const loading = ref(false)
const error = ref('')
const status = ref('')
const page = ref(1)
const size = ref(10)
const hasNext = ref(false)
const items = ref([])

const statusClass = (value) => {
  if (value === 'pending') return 'status--pending'
  if (value === 'visible') return 'status--success'
  if (value === 'hidden' || value === 'deleted') return 'status--danger'
  return ''
}

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    const data = await getAdminComments({ status: status.value, page: page.value, size: size.value })
    items.value = (data.records || data.list || []).map((row) => ({ ...row, _next: row.status }))
    hasNext.value = data.totalPages ? page.value < data.totalPages : items.value.length >= size.value
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const update = async (item) => {
  await updateCommentStatus(item.id, { status: item._next, adminNote: '' })
  await load()
}

onMounted(load)
</script>
