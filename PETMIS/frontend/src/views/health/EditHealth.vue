<template>
  <div class="edit-health-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>编辑健康记录</h3>
        </div>
      </template>
      <el-form :model="healthForm" :rules="rules" ref="healthFormRef" label-width="100px">
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="healthForm.petId" placeholder="请选择宠物">
            <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="记录类型" prop="recordType">
          <el-select v-model="healthForm.recordType" placeholder="请选择记录类型">
            <el-option label="疫苗" value="疫苗"></el-option>
            <el-option label="疾病" value="疾病"></el-option>
            <el-option label="体检" value="体检"></el-option>
            <el-option label="用药" value="用药"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="healthForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="healthForm.description" type="textarea" placeholder="请输入描述"></el-input>
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker v-model="healthForm.date" type="date" placeholder="请选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
          >
            <el-button type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png文件，且不超过2MB
              </div>
            </template>
          </el-upload>
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
import { useRouter, useRoute } from 'vue-router'
import { usePetStore } from '../../store/pet'
import { ElMessage } from 'element-plus'
import { healthApi } from '../../api/health'

const router = useRouter()
const route = useRoute()
const petStore = usePetStore()
const healthFormRef = ref(null)
const fileList = ref([])
const pets = ref([])

const healthForm = reactive({
  petId: '',
  recordType: '',
  title: '',
  description: '',
  date: '',
  attachments: ''
})

const rules = {
  petId: [
    { required: true, message: '请选择宠物', trigger: 'blur' }
  ],
  recordType: [
    { required: true, message: '请选择记录类型', trigger: 'blur' }
  ],
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' }
  ],
  date: [
    { required: true, message: '请选择日期', trigger: 'blur' }
  ]
}

const handleFileChange = (file, fileList) => {
  // 处理文件上传逻辑
  console.log('文件变更:', file, fileList)
}

const handleSubmit = async () => {
  if (!healthFormRef.value) return
  
  await healthFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await healthApi.updateHealthRecord(route.params.id, healthForm)
        if (response.data) {
          ElMessage.success('更新成功')
          router.push('/health')
        } else {
          ElMessage.error('更新失败')
        }
      } catch (error) {
        console.error('更新健康记录失败:', error)
        ElMessage.error('更新失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/health')
}

const fetchHealthRecord = async () => {
  try {
    const response = await healthApi.getHealthRecordById(route.params.id)
    if (response.data) {
      Object.assign(healthForm, response.data)
    }
  } catch (error) {
    console.error('获取健康记录失败:', error)
  }
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
}

onMounted(() => {
  fetchPets()
  fetchHealthRecord()
})
</script>

<style scoped>
.edit-health-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-demo {
  margin-top: 10px;
}
</style>