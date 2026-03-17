<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>宠物管理</h2>
      <el-button type="primary" class="glow-button" @click="handleAdd">
        <el-icon><Plus /></el-icon>添加宠物
      </el-button>
    </div>
    <div class="page-content">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="pet in petStore.petList" :key="pet.id">
          <div class="pet-card glass-card" @click="handleSelect(pet)">
            <div class="pet-avatar">
              <el-avatar :size="80" style="background: linear-gradient(135deg, #3b82f6, #8b5cf6)">
                {{ pet.name?.charAt(0)?.toUpperCase() || 'P' }}
              </el-avatar>
            </div>
            <div class="pet-info">
              <h3>{{ pet.name }}</h3>
              <p class="pet-meta">
                <span>{{ pet.species }} / {{ pet.breed }}</span>
              </p>
              <p class="pet-detail">
                <span>性别: {{ pet.gender === '公' ? '♂' : '♀' }}</span>
                <span v-if="pet.weight">{{ pet.weight }}kg</span>
              </p>
            </div>
            <div class="pet-actions">
              <el-button link type="primary" size="small" @click.stop="handleEdit(pet)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button link type="danger" size="small" @click.stop="handleDelete(pet)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-empty v-if="petStore.petList.length === 0" description="暂无宠物，点击上方按钮添加~" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑宠物' : '添加宠物'"
      width="500px"
      class="pet-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="宠物名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入宠物名称" />
        </el-form-item>
        <el-form-item label="品种" prop="species">
          <el-select v-model="form.species" placeholder="请选择品种" style="width: 100%">
            <el-option label="狗" value="狗" />
            <el-option label="猫" value="猫" />
            <el-option label="仓鼠" value="仓鼠" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="亚种" prop="breed">
          <el-input v-model="form.breed" placeholder="请输入亚种" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="公">公</el-radio>
            <el-radio label="母">母</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="form.birthday" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="体重(kg)" prop="weight">
          <el-input-number v-model="form.weight" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="身高(cm)" prop="height">
          <el-input-number v-model="form.height" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" />
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
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { usePetStore } from '@/store/pet'
import { createPet, updatePet, deletePet } from '@/api/pet'

const petStore = usePetStore()
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  species: '',
  breed: '',
  gender: '公',
  birthday: null,
  weight: null,
  height: null,
  remarks: ''
})

const rules = {
  name: [{ required: true, message: '请输入宠物名称', trigger: 'blur' }],
  species: [{ required: true, message: '请选择品种', trigger: 'change' }]
}

onMounted(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  petStore.fetchPetList(userInfo.id || 1)
})

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    name: '',
    species: '',
    breed: '',
    gender: '公',
    birthday: null,
    weight: null,
    height: null,
    remarks: ''
  })
  dialogVisible.value = true
}

const handleEdit = (pet) => {
  isEdit.value = true
  Object.assign(form, pet)
  dialogVisible.value = true
}

const handleSelect = (pet) => {
  petStore.setSelectedPet(pet)
}

const handleDelete = async (pet) => {
  try {
    await ElMessageBox.confirm(`确定要删除 ${pet.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePet(pet.id)
    petStore.removePet(pet.id)
    ElMessage.success('删除成功')
  } catch {
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    form.userId = userInfo.id || 1
    
    if (isEdit.value) {
      await updatePet(form)
      petStore.updatePetInList(form)
      ElMessage.success('更新成功')
    } else {
      await createPet(form)
      ElMessage.success('添加成功')
      petStore.fetchPetList(userInfo.id || 1)
    }
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

    h2 {
      margin: 0;
      color: #f1f5f9;
    }
  }

  .page-content {
    padding: 0;
  }

  .pet-card {
    padding: 24px;
    margin-bottom: 20px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 40px rgba(0, 212, 255, 0.2);
      border-color: rgba(0, 212, 255, 0.3);
      .pet-actions {
        opacity: 1;
      }
    }
  }

  .pet-avatar {
    margin-bottom: 16px;
  }

  .pet-info {
    h3 {
      margin: 0 0 8px 0;
      color: #f1f5f9;
      font-size: 18px;
    }

    .pet-meta {
      margin: 0 0 8px 0;
      color: #94a3b8;
      font-size: 14px;
    }

    .pet-detail {
      margin: 0;
      color: #64748b;
      font-size: 13px;
      display: flex;
      gap: 16px;
      justify-content: center;
    }
  }

  .pet-actions {
    position: absolute;
    top: 12px;
    right: 12px;
    opacity: 0;
    transition: opacity 0.3s ease;
    display: flex;
    gap: 4px;
  }
}

:deep(.pet-dialog) {
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
