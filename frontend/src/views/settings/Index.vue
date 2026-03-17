<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>系统设置</h2>
    </div>
    <div class="page-content">
      <el-card class="settings-card glass-card">
        <template #header>
          <div class="card-header">
            <span>基本设置</span>
          </div>
        </template>
        
        <el-form :model="form" label-width="120px">
          <el-form-item label="主题模式">
            <el-radio-group v-model="form.theme">
              <el-radio label="dark">深色主题</el-radio>
              <el-radio label="light">浅色主题</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="语言">
            <el-select v-model="form.language" style="width: 200px">
              <el-option label="简体中文" value="zh-CN" />
              <el-option label="English" value="en-US" />
            </el-select>
          </el-form-item>
          <el-form-item label="通知设置">
            <el-switch v-model="form.notifications" />
          </el-form-item>
          <el-form-item label="自动刷新">
            <el-switch v-model="form.autoRefresh" />
          </el-form-item>
          <el-form-item label="刷新间隔">
            <el-select v-model="form.refreshInterval" style="width: 200px">
              <el-option label="30秒" :value="30" />
              <el-option label="1分钟" :value="60" />
              <el-option label="5分钟" :value="300" />
              <el-option label="10分钟" :value="600" />
            </el-select>
          </el-form-item>
        </el-form>
      </el-card>
      
      <el-card class="settings-card glass-card" style="margin-top: 20px;">
        <template #header>
          <div class="card-header">
            <span>安全设置</span>
          </div>
        </template>
        
        <el-form label-width="120px">
          <el-form-item label="修改密码">
            <el-button type="primary" @click="showPasswordDialog = true">
              <el-icon><Lock /></el-icon>
              修改密码
            </el-button>
          </el-form-item>
          <el-form-item label="清除缓存">
            <el-button type="warning" @click="handleClearCache">
              <el-icon><Delete /></el-icon>
              清除缓存
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
      
      <div class="settings-actions">
        <el-button type="primary" @click="handleSave" :loading="loading">
          <el-icon><Check /></el-icon>
          保存设置
        </el-button>
        <el-button @click="handleReset">
          <el-icon><RefreshLeft /></el-icon>
          重置设置
        </el-button>
      </div>
    </div>
    
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px">
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="80px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请确认新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Lock, Delete, Check, RefreshLeft } from '@element-plus/icons-vue'

const loading = ref(false)
const passwordLoading = ref(false)
const showPasswordDialog = ref(false)
const passwordFormRef = ref(null)

const form = reactive({
  theme: 'dark',
  language: 'zh-CN',
  notifications: true,
  autoRefresh: true,
  refreshInterval: 60
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

onMounted(() => {
  const savedSettings = localStorage.getItem('settings')
  if (savedSettings) {
    Object.assign(form, JSON.parse(savedSettings))
  }
})

const handleSave = async () => {
  loading.value = true
  
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    localStorage.setItem('settings', JSON.stringify(form))
    ElMessage.success('设置已保存')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  ElMessageBox.confirm('确定要重置所有设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    Object.assign(form, {
      theme: 'dark',
      language: 'zh-CN',
      notifications: true,
      autoRefresh: true,
      refreshInterval: 60
    })
    localStorage.removeItem('settings')
    ElMessage.success('设置已重置')
  }).catch(() => {})
}

const handleClearCache = () => {
  ElMessageBox.confirm('确定要清除所有缓存吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.clear()
    ElMessage.success('缓存已清除，请重新登录')
    setTimeout(() => {
      window.location.reload()
    }, 1000)
  }).catch(() => {})
}

const handleChangePassword = async () => {
  await passwordFormRef.value.validate()
  passwordLoading.value = true
  
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('密码修改成功')
    showPasswordDialog.value = false
    Object.assign(passwordForm, {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
  } catch (error) {
    ElMessage.error('密码修改失败')
  } finally {
    passwordLoading.value = false
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
    max-width: 800px;
    
    .settings-card {
      .card-header {
        color: #f1f5f9;
      }
    }
    
    .settings-actions {
      display: flex;
      gap: 12px;
      margin-top: 24px;
    }
  }
}

:deep(.settings-card) {
  .el-form-item__label {
    color: #94a3b8;
  }

  .el-input__wrapper,
  .el-select__wrapper {
    background: rgba(15, 23, 42, 0.8);
    border-color: rgba(255, 255, 255, 0.1);

    &:hover {
      border-color: rgba(0, 212, 255, 0.5);
    }

    .el-input__inner,
    .el-select__selected-item {
      color: #f1f5f9;
    }
  }
  
  .el-radio {
    .el-radio__label {
      color: #f1f5f9;
    }
  }
}
</style>
