<template>
  <AdminLayout>
    <section class="section">
      <h1>评论审核</h1>

      <div class="page-actions">
        <select v-model="status" @change="load">
          <option value="">全部</option>
          <option value="visible">已显示</option>
          <option value="pending">待审核</option>
          <option value="hidden">已隐藏</option>
          <option value="deleted">已删除</option>
        </select>
      </div>

      <div v-if="loading" class="loading">正在加载评论列表...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="!items.length" class="empty">暂无评论数据</div>
      <div v-else class="table-wrap">
        <table class="data-table">
          <thead>
            <tr>
              <th>评论ID</th>
              <th>关联文章</th>
              <th>用户昵称</th>
              <th>评论内容</th>
              <th>状态</th>
              <th>管理备注</th>
              <th>时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="it in items" :key="it.id">
              <td>{{ it.id }}</td>
              <td>{{ it.articleTitle }}</td>
              <td>{{ it.userNickname }}</td>
              <td>{{ it.content }}</td>
              <td>{{ formatStatus(it.status) }}</td>
              <td>{{ it.adminNote || '-' }}</td>
              <td>{{ it.createdAt }}</td>
              <td>
                <button class="btn btn--outline" @click="patch(it, 'visible', '审核通过')">通过</button>
                <button class="btn btn--outline" @click="patch(it, 'hidden', '管理员隐藏')">隐藏</button>
                <button class="btn btn--outline" @click="patch(it, 'pending', '待审核')">待审核</button>
                <button class="btn btn--outline" @click="patch(it, 'deleted', '管理员删除')">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </AdminLayout>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import AdminLayout from '../../components/AdminLayout.vue'
import { getAdminComments, updateCommentStatus } from '../../api/admin'
import { formatStatus, normalizeList } from '../../utils/format'

const loading = ref(false)
const error = ref('')
const items = ref([])
const status = ref('')

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    const data = await getAdminComments({ status: status.value, page: 1, size: 20 })
    items.value = normalizeList(data)
  } catch {
    error.value = '评论列表加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

const patch = async (item, next, note) => {
  try {
    await updateCommentStatus(item.id, { status: next, adminNote: note })
    await load()
  } catch {
    error.value = '评论状态更新失败，请稍后重试。'
  }
}

onMounted(load)
</script>
