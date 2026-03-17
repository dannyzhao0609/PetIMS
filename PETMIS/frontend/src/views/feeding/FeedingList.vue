<template>
  <div class="feeding-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>饮食记录</h3>
          <el-button type="primary" @click="handleAddFeeding">添加饮食记录</el-button>
        </div>
      </template>
      <el-select v-model="petId" placeholder="选择宠物" style="margin-bottom: 20px; width: 200px;">
        <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="margin-left: 10px; margin-bottom: 20px; width: 300px;"
      />
      <el-button type="primary" @click="fetchFeedingRecords" style="margin-left: 10px;">查询</el-button>
      <el-table :data="feedingRecords" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="foodName" label="食物名称"></el-table-column>
        <el-table-column prop="amount" label="数量"></el-table-column>
        <el-table-column prop="unit" label="单位"></el-table-column>
        <el-table-column prop="feedingTime" label="喂食时间"></el-table-column>
        <el-table-column prop="notes" label="备注" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="danger" size="small" @click="handleDeleteFeeding(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
import { usePetStore } from '../../store/pet'
import { ElMessage, ElMessageBox } from 'element-plus'
import { feedingApi } from '../../api/feeding'

const router = useRouter()
const petStore = usePetStore()

const pets = ref([])
const petId = ref('')
const dateRange = ref([])
const feedingRecords = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const handleAddFeeding = () => {
  router.push('/feeding/add')
}

const handleDeleteFeeding = (id) => {
  ElMessageBox.confirm('确定要删除这个饮食记录吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await feedingApi.deleteFeedingRecord(id)
      ElMessage.success('删除成功')
      await fetchFeedingRecords()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  fetchFeedingRecords()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchFeedingRecords()
}

const fetchFeedingRecords = async () => {
  try {
    let startDate = null
    let endDate = null
    if (dateRange.value && dateRange.value.length === 2) {
      startDate = dateRange.value[0].getTime()
      endDate = dateRange.value[1].getTime()
    }
    const response = await feedingApi.getFeedingRecords(petId.value, startDate, endDate)
    feedingRecords.value = response.data
    total.value = feedingRecords.value.length
  } catch (error) {
    console.error('获取饮食记录失败:', error)
  }
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
}

onMounted(() => {
  fetchPets()
  fetchFeedingRecords()
})
</script>

<style scoped>
.feeding-list-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>