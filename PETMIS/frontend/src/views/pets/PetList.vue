<template>
  <div class="pet-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>宠物列表</h3>
          <el-button type="primary" @click="handleAddPet">添加宠物</el-button>
        </div>
      </template>
      <el-table :data="pets" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="宠物名称"></el-table-column>
        <el-table-column prop="species" label="品种"></el-table-column>
        <el-table-column prop="breed" label="亚种"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="birthday" label="出生日期"></el-table-column>
        <el-table-column prop="weight" label="体重(kg)"></el-table-column>
        <el-table-column prop="height" label="身高(cm)"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditPet(scope.row.id)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDeletePet(scope.row.id)">删除</el-button>
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

const router = useRouter()
const petStore = usePetStore()

const pets = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const handleAddPet = () => {
  router.push('/pets/add')
}

const handleEditPet = (id) => {
  router.push(`/pets/edit/${id}`)
}

const handleDeletePet = (id) => {
  ElMessageBox.confirm('确定要删除这个宠物吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await petStore.deletePet(id)
    if (result) {
      ElMessage.success('删除成功')
      await fetchPets()
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  fetchPets()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchPets()
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
  total.value = pets.value.length
}

onMounted(() => {
  fetchPets()
})
</script>

<style scoped>
.pet-list-container {
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