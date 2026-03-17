<template>
  <div class="add-share-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>添加日常分享</h3>
        </div>
      </template>
      <el-form :model="shareForm" :rules="rules" ref="shareFormRef" label-width="100px">
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="shareForm.petId" placeholder="请选择宠物">
            <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="shareForm.content" type="textarea" placeholder="请输入分享内容" rows="5"></el-input>
        </el-form-item>
        <el-form-item label="照片/视频">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            multiple
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
import { useRouter } from 'vue-router'
import { usePetStore } from '../../store/pet'
import { ElMessage } from 'element-plus'

const router = useRouter()
const petStore = usePetStore()
const shareFormRef = ref(null)
const fileList = ref([])
const pets = ref([])

const shareForm = reactive({
  petId: '',
  content: '',
  attachments: ''
})

const rules = {
  petId: [
    { required: true, message: '请选择宠物', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入分享内容', trigger: 'blur' }
  ]
}

const handleFileChange = (file, fileList) => {
  // 处理文件上传逻辑
  console.log('文件变更:', file, fileList)
}

const handleSubmit = async () => {
  if (!shareFormRef.value) return
  
  await shareFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 这里可以调用添加分享的API
        ElMessage.success('分享成功')
        router.push('/share')
      } catch (error) {
        console.error('添加分享失败:', error)
        ElMessage.error('分享失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/share')
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
.add-share-container {
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