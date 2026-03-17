<template>
  <div class="add-post-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>发布帖子</h3>
        </div>
      </template>
      <el-form :model="postForm" :rules="rules" ref="postFormRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="postForm.petId" placeholder="请选择宠物">
            <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="postForm.content" type="textarea" placeholder="请输入内容" rows="5"></el-input>
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
import { useRouter } from 'vue-router'
import { usePetStore } from '../../store/pet'
import { ElMessage } from 'element-plus'
import { postApi } from '../../api/post'

const router = useRouter()
const petStore = usePetStore()
const postFormRef = ref(null)
const fileList = ref([])
const pets = ref([])

const postForm = reactive({
  title: '',
  petId: '',
  content: '',
  attachments: ''
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' }
  ]
}

const handleFileChange = (file, fileList) => {
  // 处理文件上传逻辑
  console.log('文件变更:', file, fileList)
}

const handleSubmit = async () => {
  if (!postFormRef.value) return
  
  await postFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 添加用户ID（实际项目中应该从登录状态获取）
        postForm.userId = 1
        const response = await postApi.addPost(postForm)
        if (response.data) {
          ElMessage.success('发布成功')
          router.push('/community')
        } else {
          ElMessage.error('发布失败')
        }
      } catch (error) {
        console.error('发布帖子失败:', error)
        ElMessage.error('发布失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/community')
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
.add-post-container {
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