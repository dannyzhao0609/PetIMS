<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>个人中心</h2>
    </div>
    <div class="page-content">
      <el-card class="profile-card glass-card">
        <div class="profile-header">
          <el-avatar :size="100" style="background: linear-gradient(135deg, #3b82f6, #8b5cf6)">
            {{ userInfo.username?.charAt(0)?.toUpperCase() || 'U' }}
          </el-avatar>
          <div class="profile-info">
            <h2>{{ userInfo.username }}</h2>
            <p class="user-email">{{ userInfo.email || '暂无邮箱' }}</p>
            <p class="user-phone">{{ userInfo.phone || '暂无手机号' }}</p>
          </div>
        </div>
        
        <el-divider />
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-form>
        
        <div class="profile-actions">
          <el-button type="primary" @click="handleSave" :loading="loading">
            <el-icon><Edit /></el-icon>
            保存修改
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'

const userInfo = ref({})
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  email: '',
  phone: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }]
}

onMounted(() => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
    Object.assign(form, userInfo.value)
  }
})

const handleSave = async () => {
  await formRef.value.validate()
  loading.value = true
  
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    Object.assign(userInfo.value, form)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.page-container {
  .page-header {
    padding: 20px 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      color: #f1f5f9;
    }
  }

  .page-content {
    .profile-card {
      max-width: 600px;
      margin: 0 auto;
      
      .profile-header {
        display: flex;
        gap: 24px;
        align-items: center;
        
        .profile-info {
          h2 {
            margin: 0 0 8px 0;
            color: #f1f5f9;
            font-size: 24px;
          }
          
          .user-email,
          .user-phone {
            margin: 4px 0;
            color: #94a3b8;
            font-size: 14px;
          }
        }
      }
      
      .profile-actions {
        display: flex;
        justify-content: flex-end;
        margin-top: 20px;
      }
    }
  }
}

:deep(.profile-card) {
  .el-form-item__label {
    color: #94a3b8;
  }

  .el-input__wrapper {
    background: rgba(15, 23, 42, 0.8);
    border-color: rgba(255, 255, 255, 0.1);

    &:hover {
      border-color: rgba(0, 212, 255, 0.5);
    }

    .el-input__inner {
      color: #f1f5f9;
    }
  }
}
</style>
