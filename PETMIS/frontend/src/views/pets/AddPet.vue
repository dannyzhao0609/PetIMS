<template>
  <div class="add-pet-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>添加宠物</h3>
        </div>
      </template>
      <el-form :model="petForm" :rules="rules" ref="petFormRef" label-width="100px">
        <el-form-item label="宠物名称" prop="name">
          <el-input v-model="petForm.name" placeholder="请输入宠物名称"></el-input>
        </el-form-item>
        <el-form-item label="品种" prop="species">
          <el-input v-model="petForm.species" placeholder="请输入宠物品种"></el-input>
        </el-form-item>
        <el-form-item label="亚种" prop="breed">
          <el-input v-model="petForm.breed" placeholder="请输入宠物亚种"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="petForm.gender">
            <el-radio label="公">公</el-radio>
            <el-radio label="母">母</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker v-model="petForm.birthday" type="date" placeholder="请选择出生日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="体重(kg)" prop="weight">
          <el-input v-model.number="petForm.weight" type="number" placeholder="请输入体重"></el-input>
        </el-form-item>
        <el-form-item label="身高(cm)" prop="height">
          <el-input v-model.number="petForm.height" type="number" placeholder="请输入身高"></el-input>
        </el-form-item>
        <el-form-item label="宠物照片">
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { usePetStore } from '../../store/pet'
import { ElMessage } from 'element-plus'

const router = useRouter()
const petStore = usePetStore()
const petFormRef = ref(null)
const fileList = ref([])

const petForm = reactive({
  name: '',
  species: '',
  breed: '',
  gender: '公',
  birthday: '',
  weight: '',
  height: ''
})

const rules = {
  name: [
    { required: true, message: '请输入宠物名称', trigger: 'blur' }
  ],
  species: [
    { required: true, message: '请输入宠物品种', trigger: 'blur' }
  ],
  birthday: [
    { required: true, message: '请选择出生日期', trigger: 'blur' }
  ],
  weight: [
    { required: true, message: '请输入体重', trigger: 'blur' },
    { type: 'number', message: '请输入有效的体重值', trigger: 'blur' }
  ],
  height: [
    { required: true, message: '请输入身高', trigger: 'blur' },
    { type: 'number', message: '请输入有效的身高值', trigger: 'blur' }
  ]
}

const handleFileChange = (file, fileList) => {
  // 处理文件上传逻辑
  console.log('文件变更:', file, fileList)
}

const handleSubmit = async () => {
  if (!petFormRef.value) return
  
  await petFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const result = await petStore.addPet(petForm)
        if (result) {
          ElMessage.success('添加成功')
          router.push('/pets')
        } else {
          ElMessage.error('添加失败')
        }
      } catch (error) {
        console.error('添加宠物失败:', error)
        ElMessage.error('添加失败')
      }
    }
  })
}

const handleCancel = () => {
  router.push('/pets')
}
</script>

<style scoped>
.add-pet-container {
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