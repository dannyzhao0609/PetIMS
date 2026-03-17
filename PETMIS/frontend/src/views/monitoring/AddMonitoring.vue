<template>
  <div class="add-monitoring-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>添加监控记录</h3>
        </div>
      </template>
      <el-form :model="monitoringForm" :rules="rules" ref="monitoringFormRef" label-width="100px">
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="monitoringForm.petId" placeholder="请选择宠物">
            <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="记录类型" prop="recordType">
          <el-select v-model="monitoringForm.recordType" placeholder="请选择记录类型">
            <el-option label="活动量" value="活动量"></el-option>
            <el-option label="睡眠" value="睡眠"></el-option>
            <el-option label="位置" value="位置"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数值" prop="value">
          <el-input v-model="monitoringForm.value" placeholder="请输入数值"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="monitoringForm.unit" placeholder="请选择单位">
            <el-option label="步" value="步"></el-option>
            <el-option label="分钟" value="分钟"></el-option>
            <el-option label="小时" value="小时"></el-option>
            <el-option label="米" value="米"></el-option>
            <el-option label="公里" value="公里"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="记录时间" prop="recordTime">
          <el-date-picker v-model="monitoringForm.recordTime" type="datetime" placeholder="请选择记录时间"></el-date-picker>
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
import { monitoringApi } from '../../api/monitoring'

const router = useRouter()
const petStore = usePetStore()
const monitoringFormRef = ref(null)
const pets = ref([])

const monitoringForm = reactive({
  petId: '',
  recordType: '',
  value: '',
  unit: '',
  recordTime: ''
})

const rules = {
  petId: [
    { required: true, message: '请选择宠物', trigger: 'blur' }
  ],
  recordType: [
    { required: true, message: '请选择记录类型', trigger: 'blur' }
  ],
  value: [
    { required: true, message: '请输入数值', trigger: 'blur' }
  ],
  unit: [
    { required: true, message: '请选择单位', trigger: 'blur' }
  ],
  recordTime: [
    { required: true, message: '请选择记录时间', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!monitoringFormRef.value) return
  
  await monitoringFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await monitoringApi.addMonitoringRecord(monitoringForm)
        if (response.data) {
          ElMessage.success('添加成功')
          router.push('/monitoring')
        } else {
          ElMessage.error('添加失败')
        }
      } catch (error) {
        console.error('添加监控记录失败:', error)
        ElMessage.error('添加失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/monitoring')
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
.add-monitoring-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>