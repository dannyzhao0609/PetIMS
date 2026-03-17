<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <div class="header-left">
        <h2>饮食管理</h2>
        <el-select v-model="selectedPetId" placeholder="选择宠物" style="width: 200px; margin-left: 20px" @change="handlePetChange">
          <el-option v-for="pet in petStore.petList" :key="pet.id" :label="pet.name" :value="pet.id" />
        </el-select>
      </div>
      <el-button type="primary" class="glow-button" @click="handleAdd" :disabled="!selectedPetId">
        <el-icon><Plus /></el-icon>添加记录
      </el-button>
    </div>
    <div class="page-content">
      <div class="feeding-list" v-if="feedingList.length > 0">
        <div class="feeding-item glass-card" v-for="item in feedingList" :key="item.id">
          <div class="feeding-icon">
            <el-icon :size="32" color="#f59e0b"><Food /></el-icon>
          </div>
          <div class="feeding-info">
            <h3>{{ item.foodName }}</h3>
            <p class="feeding-meta">
              <span class="amount">{{ item.amount }} {{ item.unit }}</span>
              <span class="time">{{ formatDateTime(item.feedingTime) }}</span>
            </p>
            <p class="feeding-notes" v-if="item.notes">{{ item.notes }}</p>
          </div>
          <div class="feeding-actions">
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
      <el-empty v-else description="暂无喂食记录，点击上方按钮添加~" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑喂食记录' : '添加喂食记录'"
      width="500px"
      class="feeding-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="食物名称" prop="foodName">
          <el-input v-model="form.foodName" placeholder="请输入食物名称" />
        </el-form-item>
        <el-form-item label="喂食数量" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="form.unit" placeholder="请选择单位" style="width: 100%">
            <el-option label="g" value="g" />
            <el-option label="kg" value="kg" />
            <el-option label="ml" value="ml" />
            <el-option label="L" value="L" />
            <el-option label="碗" value="碗" />
          </el-select>
        </el-form-item>
        <el-form-item label="喂食时间" prop="feedingTime">
          <el-date-picker
            v-model="form.feedingTime" type="datetime" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="form.notes" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { Plus, Edit, Delete, Food } from '@element-plus/icons-vue'
import { usePetStore } from '@/store/pet'
import { getFeedingRecordsByPetId, createFeedingRecord, updateFeedingRecord, deleteFeedingRecord } from '@/api/feeding'

const petStore = usePetStore()
const feedingList = ref([])
const selectedPetId = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  petId: null,
  foodName: '',
  amount: null,
  unit: 'g',
  feedingTime: null,
  notes: ''
})

const rules = {
  foodName: [{ required: true, message: '请输入食物名称', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入喂食数量', trigger: 'blur' }],
  unit: [{ required: true, message: '请选择单位', trigger: 'change' }],
  feedingTime: [{ required: true, message: '请选择喂食时间', trigger: 'change' }]
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
    fetchFeedingList()
  }
}

const fetchFeedingList = async () => {
  try {
    const res = await getFeedingRecordsByPetId(selectedPetId.value)
    feedingList.value = res.data || []
  } catch (error) {
    console.error('获取喂食记录失败:', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    petId: selectedPetId.value,
    foodName: '',
    amount: null,
    unit: 'g',
    feedingTime: new Date(),
    notes: ''
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
    await deleteFeedingRecord(item.id)
    feedingList.value = feedingList.value.filter(f => f.id !== item.id)
    ElMessage.success('删除成功')
  } catch {
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    form.petId = selectedPetId.value
    if (isEdit.value) {
      await updateFeedingRecord(form)
      ElMessage.success('更新成功')
    } else {
      await createFeedingRecord(form)
      ElMessage.success('添加成功')
    }
    fetchFeedingList()
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

  .feeding-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .feeding-item {
    padding: 20px;
    display: flex;
    align-items: flex-start;
    gap: 16px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateX(4px);
      box-shadow: 0 4px 20px rgba(245, 158, 11, 0.15);
    }
  }

  .feeding-icon {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    background: linear-gradient(135deg, #f59e0b, #d97706);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    .el-icon {
      color: white;
    }
  }

  .feeding-info {
    flex: 1;

    h3 {
      margin: 0 0 8px 0;
      color: #f1f5f9;
      font-size: 16px;
    }

    .feeding-meta {
      margin: 0 0 8px 0;
      display: flex;
      gap: 12px;
      align-items: center;

      .amount {
        padding: 2px 12px;
        border-radius: 20px;
        font-size: 12px;
        background: rgba(245, 158, 11, 0.2);
        color: #f59e0b;
      }

      .time {
        color: #64748b;
        font-size: 13px;
      }
    }

    .feeding-notes {
      margin: 0;
      color: #94a3b8;
      font-size: 14px;
    }
  }

  .feeding-actions {
    display: flex;
    gap: 4px;
  }
}

:deep(.feeding-dialog) {
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
