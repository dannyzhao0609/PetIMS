<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <div class="header-left">
        <h2>日常监控</h2>
        <el-select v-model="selectedPetId" placeholder="选择宠物" style="width: 200px; margin-left: 20px" @change="handlePetChange">
          <el-option v-for="pet in petStore.petList" :key="pet.id" :label="pet.name" :value="pet.id" />
        </el-select>
      </div>
      <el-button type="primary" class="glow-button" @click="handleAdd" :disabled="!selectedPetId">
        <el-icon><Plus /></el-icon>添加记录
      </el-button>
    </div>
    <div class="page-content">
      <div class="monitoring-list" v-if="monitoringList.length > 0">
        <div class="monitoring-item glass-card" v-for="item in monitoringList" :key="item.id">
          <div class="monitoring-icon">
            <el-icon :size="32" color="#3b82f6"><Monitor /></el-icon>
          </div>
          <div class="monitoring-info">
            <h3>{{ item.recordType }}</h3>
            <p class="monitoring-meta">
              <span class="value">{{ item.value }} {{ item.unit }}</span>
              <span class="time">{{ formatDateTime(item.recordTime) }}</span>
            </p>
          </div>
          <div class="monitoring-actions">
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
      <el-empty v-else description="暂无监控记录，点击上方按钮添加~" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑监控记录' : '添加监控记录'"
      width="500px"
      class="monitoring-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="监控类型" prop="recordType">
          <el-select v-model="form.recordType" placeholder="请选择类型" style="width: 100%">
            <el-option label="体重" value="体重" />
            <el-option label="体温" value="体温" />
            <el-option label="心率" value="心率" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="数值" prop="value">
          <el-input v-model="form.value" placeholder="请输入数值" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="form.unit" placeholder="请选择单位" style="width: 100%">
            <el-option label="kg" value="kg" />
            <el-option label="g" value="g" />
            <el-option label="℃" value="℃" />
            <el-option label="次/分" value="次/分" />
            <el-option label="cm" value="cm" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录时间" prop="recordTime">
          <el-date-picker
            v-model="form.recordTime" type="datetime" placeholder="选择时间" style="width: 100%" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Monitor } from '@element-plus/icons-vue'
import { usePetStore } from '@/store/pet'
import { getMonitoringRecordsByPetId, createMonitoringRecord, updateMonitoringRecord, deleteMonitoringRecord } from '@/api/monitoring'

const petStore = usePetStore()
const monitoringList = ref([])
const selectedPetId = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  petId: null,
  recordType: '',
  value: '',
  unit: '',
  recordTime: null
})

const rules = {
  recordType: [{ required: true, message: '请选择监控类型', trigger: 'change' }],
  value: [{ required: true, message: '请输入数值', trigger: 'blur' }],
  unit: [{ required: true, message: '请选择单位', trigger: 'change' }],
  recordTime: [{ required: true, message: '请选择记录时间', trigger: 'change' }]
}

onMounted(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  petStore.fetchPetList(userInfo.id || 1)
})

const formatDateTime = (date) => {
  return date ? new Date(date).toLocaleString('zh-CN') : ''
}

const handlePetChange = () => {
  if (selectedPetId.value) {
    fetchMonitoringList()
  }
}

const fetchMonitoringList = async () => {
  try {
    const res = await getMonitoringRecordsByPetId(selectedPetId.value)
    monitoringList.value = res.data || []
  } catch (error) {
    console.error('获取监控记录失败:', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    petId: selectedPetId.value,
    recordType: '',
    value: '',
    unit: '',
    recordTime: new Date()
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
    await deleteMonitoringRecord(item.id)
    monitoringList.value = monitoringList.value.filter(m => m.id !== item.id)
    ElMessage.success('删除成功')
  } catch {
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    form.petId = selectedPetId.value
    if (isEdit.value) {
      await updateMonitoringRecord(form)
      ElMessage.success('更新成功')
    } else {
      await createMonitoringRecord(form)
      ElMessage.success('添加成功')
    }
    fetchMonitoringList()
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

  .monitoring-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .monitoring-item {
    padding: 20px;
    display: flex;
    align-items: flex-start;
    gap: 16px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateX(4px);
      box-shadow: 0 4px 20px rgba(59, 130, 246, 0.15);
    }
  }

  .monitoring-icon {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    background: linear-gradient(135deg, #3b82f6, #2563eb);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    .el-icon {
      color: white;
    }
  }

  .monitoring-info {
    flex: 1;

    h3 {
      margin: 0 0 8px 0;
      color: #f1f5f9;
      font-size: 16px;
    }

    .monitoring-meta {
      margin: 0;
      display: flex;
      gap: 12px;
      align-items: center;

      .value {
        padding: 2px 12px;
        border-radius: 20px;
        font-size: 12px;
        background: rgba(59, 130, 246, 0.2);
        color: #3b82f6;
      }

      .time {
        color: #64748b;
        font-size: 13px;
      }
    }
  }

  .monitoring-actions {
    display: flex;
    gap: 4px;
  }
}

:deep(.monitoring-dialog) {
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
