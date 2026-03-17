<template>
  <div class="monitoring-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>日常监控</h3>
          <el-button type="primary" @click="handleAddMonitoring">添加监控记录</el-button>
        </div>
      </template>
      <el-select v-model="petId" placeholder="选择宠物" style="margin-bottom: 20px; width: 200px;">
        <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
      </el-select>
      <el-select v-model="recordType" placeholder="记录类型" style="margin-left: 10px; margin-bottom: 20px; width: 150px;">
        <el-option label="全部" value=""></el-option>
        <el-option label="活动量" value="活动量"></el-option>
        <el-option label="睡眠" value="睡眠"></el-option>
        <el-option label="位置" value="位置"></el-option>
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="margin-left: 10px; margin-bottom: 20px; width: 300px;"
      />
      <el-button type="primary" @click="fetchMonitoringRecords" style="margin-left: 10px;">查询</el-button>
      <el-table :data="monitoringRecords" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="recordType" label="记录类型"></el-table-column>
        <el-table-column prop="value" label="数值"></el-table-column>
        <el-table-column prop="unit" label="单位"></el-table-column>
        <el-table-column prop="recordTime" label="记录时间"></el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="danger" size="small" @click="handleDeleteMonitoring(scope.row.id)">删除</el-button>
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
import { monitoringApi } from '../../api/monitoring'

const router = useRouter()
const petStore = usePetStore()

const pets = ref([])
const petId = ref('')
const recordType = ref('')
const dateRange = ref([])
const monitoringRecords = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const handleAddMonitoring = () => {
  router.push('/monitoring/add')
}

const handleDeleteMonitoring = (id) => {
  ElMessageBox.confirm('确定要删除这个监控记录吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await monitoringApi.deleteMonitoringRecord(id)
      ElMessage.success('删除成功')
      await fetchMonitoringRecords()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  fetchMonitoringRecords()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchMonitoringRecords()
}

const fetchMonitoringRecords = async () => {
  try {
    let startDate = null
    let endDate = null
    if (dateRange.value && dateRange.value.length === 2) {
      startDate = dateRange.value[0].getTime()
      endDate = dateRange.value[1].getTime()
    }
    const response = await monitoringApi.getMonitoringRecords(petId.value, recordType.value, startDate, endDate)
    monitoringRecords.value = response.data
    total.value = monitoringRecords.value.length
  } catch (error) {
    console.error('获取监控记录失败:', error)
  }
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
}

onMounted(() => {
  fetchPets()
  fetchMonitoringRecords()
})
</script>

<style scoped>
.monitoring-list-container {
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