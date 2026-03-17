<template>
  <div class="hygiene-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>卫生记录</h3>
          <el-button type="primary" @click="handleAddHygiene">添加卫生记录</el-button>
        </div>
      </template>
      <el-select v-model="petId" placeholder="选择宠物" style="margin-bottom: 20px; width: 200px;">
        <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
      </el-select>
      <el-select v-model="recordType" placeholder="记录类型" style="margin-left: 10px; margin-bottom: 20px; width: 150px;">
        <el-option label="全部" value=""></el-option>
        <el-option label="洗澡" value="洗澡"></el-option>
        <el-option label="美容" value="美容"></el-option>
        <el-option label="驱虫" value="驱虫"></el-option>
      </el-select>
      <el-button type="primary" @click="fetchHygieneRecords" style="margin-left: 10px;">查询</el-button>
      <el-table :data="hygieneRecords" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="recordType" label="记录类型"></el-table-column>
        <el-table-column prop="date" label="日期"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditHygiene(scope.row.id)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDeleteHygiene(scope.row.id)">删除</el-button>
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
import { hygieneApi } from '../../api/hygiene'

const router = useRouter()
const petStore = usePetStore()

const pets = ref([])
const petId = ref('')
const recordType = ref('')
const hygieneRecords = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const handleAddHygiene = () => {
  router.push('/hygiene/add')
}

const handleEditHygiene = (id) => {
  router.push(`/hygiene/edit/${id}`)
}

const handleDeleteHygiene = (id) => {
  ElMessageBox.confirm('确定要删除这个卫生记录吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await hygieneApi.deleteHygieneRecord(id)
      ElMessage.success('删除成功')
      await fetchHygieneRecords()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  fetchHygieneRecords()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchHygieneRecords()
}

const fetchHygieneRecords = async () => {
  try {
    const response = await hygieneApi.getHygieneRecords(petId.value, recordType.value)
    hygieneRecords.value = response.data
    total.value = hygieneRecords.value.length
  } catch (error) {
    console.error('获取卫生记录失败:', error)
  }
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
}

onMounted(() => {
  fetchPets()
  fetchHygieneRecords()
})
</script>

<style scoped>
.hygiene-list-container {
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