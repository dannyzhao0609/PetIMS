<template>
  <div class="add-feeding-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>添加饮食记录</h3>
        </div>
      </template>
      <el-form :model="feedingForm" :rules="rules" ref="feedingFormRef" label-width="100px">
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="feedingForm.petId" placeholder="请选择宠物">
            <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="食物名称" prop="foodName">
          <el-input v-model="feedingForm.foodName" placeholder="请输入食物名称"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="amount">
          <el-input v-model.number="feedingForm.amount" type="number" placeholder="请输入数量"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="feedingForm.unit" placeholder="请选择单位">
            <el-option label="克" value="克"></el-option>
            <el-option label="千克" value="千克"></el-option>
            <el-option label="毫升" value="毫升"></el-option>
            <el-option label="升" value="升"></el-option>
            <el-option label="份" value="份"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="喂食时间" prop="feedingTime">
          <el-date-picker v-model="feedingForm.feedingTime" type="datetime" placeholder="请选择喂食时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="feedingForm.notes" type="textarea" placeholder="请输入备注"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePetStore } from '../../store/pet'
import { ElMessage } from 'element-plus'
import { feedingApi } from '../../api/feeding'

const router = useRouter()
const petStore = usePetStore()
const feedingFormRef = ref(null)
const pets = ref([])

const feedingForm = reactive({
  petId: '',
  foodName: '',
  amount: '',
  unit: '',
  feedingTime: '',
  notes: ''
})

const rules = {
  petId: [
    { required: true, message: '请选择宠物', trigger: 'blur' }
  ],
  foodName: [
    { required: true, message: '请输入食物名称', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入数量', trigger: 'blur' },
    { type: 'number', message: '请输入有效的数量值', trigger: 'blur' }
  ],
  unit: [
    { required: true, message: '请选择单位', trigger: 'blur' }
  ],
  feedingTime: [
    { required: true, message: '请选择喂食时间', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!feedingFormRef.value) return
  
  await feedingFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await feedingApi.addFeedingRecord(feedingForm)
        if (response.data) {
          ElMessage.success('添加成功')
          router.push('/feeding')
        } else {
          ElMessage.error('添加失败')
        }
      } catch (error) {
        console.error('添加饮食记录失败:', error)
        ElMessage.error('添加失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/feeding')
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
}

onMounted(() => {
  fetchPets()
})
</script>

<style scoped>
.add-feeding-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>