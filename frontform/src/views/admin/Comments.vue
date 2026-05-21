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

      <div class="table-wrap">
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

const items = ref([])
const status = ref('')

const load = async () => {
  const data = await getAdminComments({ status: status.value, page: 1, size: 20 })
  items.value = normalizeList(data)
}

const patch = async (item, next, note) => {
  await updateCommentStatus(item.id, { status: next, adminNote: note })
  await load()
}

onMounted(load)
</script>
