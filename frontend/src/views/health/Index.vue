<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <div class="header-left">
        <h2>健康管理</h2>
        <el-select v-model="selectedPetId" placeholder="选择宠物" style="width: 200px; margin-left: 20px" @change="handlePetChange">
          <el-option v-for="pet in petStore.petList" :key="pet.id" :label="pet.name" :value="pet.id" />
        </el-select>
      </div>
      <el-button type="primary" class="glow-button" @click="handleAdd" :disabled="!selectedPetId">
        <el-icon><Plus /></el-icon>添加记录
      </el-button>
    </div>
    <div class="page-content">
      <div class="health-list" v-if="healthList.length > 0">
        <div class="health-item glass-card" v-for="item in healthList" :key="item.id">
          <div class="health-icon" :class="getRecordTypeClass(item.recordType)">
            <el-icon :size="32"><FirstAidKit /></el-icon>
          </div>
          <div class="health-info">
            <h3>{{ item.title }}</h3>
            <p class="health-meta">
              <span class="type-tag" :class="getRecordTypeClass(item.recordType)">{{ item.recordType }}</span>
              <span class="date">{{ formatDate(item.date) }}</span>
            </p>
            <p class="health-desc" v-if="item.description">{{ item.description }}</p>
          </div>
          <div class="health-actions">
            <el-button link type="primary" size="small" @click="handleEdit(item)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(item)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>
      </div>
      <el-empty v-else-if="!selectedPetId" description="请先选择宠物~" />
      <el-empty v-else description="暂无健康记录，点击上方按钮添加~" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑健康记录' : '添加健康记录'"
      width="500px"
      class="health-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="记录类型" prop="recordType">
          <el-select v-model="form.recordType" placeholder="请选择类型" style="width: 100%">
            <el-option label="疫苗" value="疫苗" />
            <el-option label="疾病" value="疾病" />
            <el-option label="体检" value="体检" />
            <el-option label="用药" value="用药" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker v-model="form.date" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, FirstAidKit } from '@element-plus/icons-vue'
import { usePetStore } from '@/store/pet'
import { getHealthRecordsByPetId, createHealthRecord, updateHealthRecord, deleteHealthRecord } from '@/api/health'

const petStore = usePetStore()
const healthList = ref([])
const selectedPetId = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  petId: null,
  recordType: '',
  title: '',
  date: null,
  description: ''
})

const rules = {
  recordType: [{ required: true, message: '请选择记录类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }]
}

onMounted(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  petStore.fetchPetList(userInfo.id || 1)
})

const formatDate = (date) => {
  return date ? new Date(date).toLocaleDateString('zh-CN') : ''
}

const getRecordTypeClass = (type) => {
  const map = {
    '疫苗': 'type-vaccine',
    '疾病': 'type-disease',
    '体检': 'type-checkup',
    '用药': 'type-medication'
  }
  return map[type] || 'type-default'
}

const handlePetChange = () => {
  if (selectedPetId.value) {
    fetchHealthList()
  }
}

const fetchHealthList = async () => {
  try {
    const res = await getHealthRecordsByPetId(selectedPetId.value)
    healthList.value = res.data || []
  } catch (error) {
    console.error('获取健康记录失败:', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    petId: selectedPetId.value,
    recordType: '',
    title: '',
    date: null,
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (item) => {
  isEdit.value = true
  Object.assign(form, item)
  dialogVisible.value = true
}

const handleDelete = async (item) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteHealthRecord(item.id)
    healthList.value = healthList.value.filter(h => h.id !== item.id)
    ElMessage.success('删除成功')
  } catch {
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    form.petId = selectedPetId.value
    if (isEdit.value) {
      await updateHealthRecord(form)
      ElMessage.success('更新成功')
    } else {
      await createHealthRecord(form)
      ElMessage.success('添加成功')
    }
    fetchHealthList()
    dialogVisible.value = false
  } catch (error) {
    console.error(error)
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

    .header-left {
      display: flex;
      align-items: center;
    }

    h2 {
      margin: 0;
      color: #f1f5f9;
    }
  }

  .health-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .health-item {
    padding: 20px;
    display: flex;
    align-items: flex-start;
    gap: 16px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateX(4px);
      box-shadow: 0 4px 20px rgba(0, 212, 255, 0.15);
    }
  }

  .health-icon {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    &.type-vaccine {
      background: linear-gradient(135deg, #10b981, #059669);
    }
    &.type-disease {
      background: linear-gradient(135deg, #ef4444, #dc2626);
    }
    &.type-checkup {
      background: linear-gradient(135deg, #3b82f6, #2563eb);
    }
    &.type-medication {
      background: linear-gradient(135deg, #f59e0b, #d97706);
    }
    &.type-default {
      background: linear-gradient(135deg, #6b7280, #4b5563);
    }

    .el-icon {
      color: white;
    }
  }

  .health-info {
    flex: 1;

    h3 {
      margin: 0 0 8px 0;
      color: #f1f5f9;
      font-size: 16px;
    }

    .health-meta {
      margin: 0 0 8px 0;
      display: flex;
      gap: 12px;
      align-items: center;

      .type-tag {
        padding: 2px 12px;
        border-radius: 20px;
        font-size: 12px;
        color: white;

        &.type-vaccine {
          background: rgba(16, 185, 129, 0.2);
          color: #10b981;
        }
        &.type-disease {
          background: rgba(239, 68, 68, 0.2);
          color: #ef4444;
        }
        &.type-checkup {
          background: rgba(59, 130, 246, 0.2);
          color: #3b82f6;
        }
        &.type-medication {
          background: rgba(245, 158, 11, 0.2);
          color: #f59e0b;
        }
      }

      .date {
        color: #64748b;
        font-size: 13px;
      }
    }

    .health-desc {
      margin: 0;
      color: #94a3b8;
      font-size: 14px;
    }
  }

  .health-actions {
    display: flex;
    gap: 4px;
  }
}

:deep(.health-dialog) {
  .el-dialog {
    background: rgba(30, 41, 59, 0.95);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.1);

    .el-dialog__header {
      .el-dialog__title {
        color: #f1f5f9;
      }
    }

    .el-form-item__label {
      color: #94a3b8;
    }

    .el-input__wrapper,
    .el-select__wrapper,
    .el-textarea__inner,
    .el-date-editor {
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
  }
}
</style>
