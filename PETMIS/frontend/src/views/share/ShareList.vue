<template>
  <div class="share-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>日常分享</h3>
          <el-button type="primary" @click="handleAddShare">添加分享</el-button>
        </div>
      </template>
      <div class="share-grid">
        <el-card v-for="share in shares" :key="share.id" class="share-card">
          <div class="share-content">
            <div class="share-attachments" v-if="share.attachments">
              <img v-for="(attachment, index) in share.attachments.split(',')" :key="index" :src="attachment" class="share-image">
            </div>
            <div class="share-text">{{ share.content }}</div>
            <div class="share-meta">
              <span>{{ share.petName }}</span>
              <span>{{ share.createTime }}</span>
            </div>
          </div>
          <div class="share-actions">
            <el-button type="primary" size="small" @click="handleViewShare(share.id)">查看</el-button>
            <el-button type="danger" size="small" @click="handleDeleteShare(share.id)">删除</el-button>
          </div>
        </el-card>
      </div>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const shares = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const handleAddShare = () => {
  router.push('/share/add')
}

const handleViewShare = (id) => {
  router.push(`/share/detail/${id}`)
}

const handleDeleteShare = (id) => {
  ElMessageBox.confirm('确定要删除这个分享吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 这里可以调用删除分享的API
      ElMessage.success('删除成功')
      await fetchShares()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  fetchShares()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchShares()
}

const fetchShares = async () => {
  // 这里可以调用获取分享列表的API
  // 暂时使用模拟数据
  shares.value = [
    {
      id: 1,
      petName: '旺财',
      content: '今天带旺财去公园玩，它很开心！',
      attachments: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20dog%20playing%20in%20park&image_size=square',
      createTime: '2024-01-01 12:00:00'
    },
    {
      id: 2,
      petName: '喵喵',
      content: '喵喵今天睡了一整天，好可爱！',
      attachments: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20cat%20sleeping&image_size=square',
      createTime: '2024-01-02 10:00:00'
    }
  ]
  total.value = shares.value.length
}

onMounted(() => {
  fetchShares()
})
</script>

<style scoped>
.share-list-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.share-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin: 20px 0;
}

.share-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.share-content {
  flex: 1;
}

.share-attachments {
  margin-bottom: 10px;
}

.share-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.share-text {
  margin-bottom: 10px;
  line-height: 1.6;
}

.share-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
  margin-bottom: 10px;
}

.share-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>