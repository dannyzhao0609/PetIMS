<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>传感器管理</h2>
      <el-button type="primary" class="glow-button" @click="handleAddSensor">
        <el-icon><Plus /></el-icon>
        添加传感器
      </el-button>
    </div>
    <div class="page-content">
      <el-tabs v-model="activeTab" class="glass-card">
        <el-tab-pane label="传感器列表" name="sensors">
          <el-table :data="sensors" style="width: 100%" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="sensorCode" label="传感器编码" width="180" />
            <el-table-column prop="sensorName" label="传感器名称" width="180" />
            <el-table-column prop="sensorType" label="传感器类型" width="120">
              <template #default="{ row }">
                <el-tag :type="row.sensorType === 'GPS' ? 'success' : 'primary'">
                  {{ row.sensorType }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
                  {{ row.status === 'ACTIVE' ? '活跃' : '离线' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="batteryLevel" label="电量" width="100">
              <template #default="{ row }">
                <el-progress :percentage="row.batteryLevel || 0" :color="getBatteryColor(row.batteryLevel)" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="handleEditSensor(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDeleteSensor(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane label="宠物绑定" name="bindings">
          <div class="binding-header">
            <el-button type="primary" @click="handleAddBinding">
              <el-icon><Link /></el-icon>
              绑定宠物
            </el-button>
          </div>
          <el-table :data="bindings" style="width: 100%" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="petName" label="宠物名称" width="150" />
            <el-table-column prop="sensorName" label="传感器名称" width="180" />
            <el-table-column prop="sensorCode" label="传感器编码" width="180" />
            <el-table-column prop="bindingTime" label="绑定时间" width="180" />
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button size="small" type="danger" @click="handleDeleteBinding(row)">解绑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog
      v-model="sensorDialogVisible"
      :title="isEditSensor ? '编辑传感器' : '添加传感器'"
      width="500px"
      class="sensor-dialog"
    >
      <el-form :model="sensorForm" :rules="sensorRules" ref="sensorFormRef" label-width="100px">
        <el-form-item label="传感器编码" prop="sensorCode">
          <el-input v-model="sensorForm.sensorCode" placeholder="请输入传感器编码" />
        </el-form-item>
        <el-form-item label="传感器名称" prop="sensorName">
          <el-input v-model="sensorForm.sensorName" placeholder="请输入传感器名称" />
        </el-form-item>
        <el-form-item label="传感器类型" prop="sensorType">
          <el-select v-model="sensorForm.sensorType" placeholder="请选择传感器类型" style="width: 100%">
            <el-option label="GPS" value="GPS" />
            <el-option label="BLE" value="BLE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="sensorForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="离线" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="电量" prop="batteryLevel">
          <el-slider v-model="sensorForm.batteryLevel" :min="0" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="sensorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSensor">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="bindingDialogVisible"
      title="绑定宠物"
      width="500px"
      class="binding-dialog"
    >
      <el-form :model="bindingForm" :rules="bindingRules" ref="bindingFormRef" label-width="100px">
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="bindingForm.petId" placeholder="请选择宠物" style="width: 100%">
            <el-option 
              v-for="pet in pets" 
              :key="pet.id" 
              :label="pet.name" 
              :value="pet.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择传感器" prop="sensorId">
          <el-select v-model="bindingForm.sensorId" placeholder="请选择传感器" style="width: 100%">
            <el-option 
              v-for="sensor in availableSensors" 
              :key="sensor.id" 
              :label="`${sensor.sensorName} (${sensor.sensorCode})`" 
              :value="sensor.id" 
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bindingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveBinding">绑定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Link } from '@element-plus/icons-vue'
import { getSensors, createSensor, updateSensor, deleteSensor, getBindings, createBinding, deleteBinding } from '@/api/sensor'
import { getPetsByUserId } from '@/api/pet'

const activeTab = ref('sensors')
const sensors = ref([])
const bindings = ref([])
const pets = ref([])
const sensorDialogVisible = ref(false)
const bindingDialogVisible = ref(false)
const isEditSensor = ref(false)
const sensorFormRef = ref(null)
const bindingFormRef = ref(null)

const sensorForm = reactive({
  id: null,
  sensorCode: '',
  sensorName: '',
  sensorType: 'GPS',
  status: 'ACTIVE',
  batteryLevel: 100
})

const bindingForm = reactive({
  petId: null,
  sensorId: null
})

const sensorRules = {
  sensorCode: [{ required: true, message: '请输入传感器编码', trigger: 'blur' }],
  sensorName: [{ required: true, message: '请输入传感器名称', trigger: 'blur' }],
  sensorType: [{ required: true, message: '请选择传感器类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const bindingRules = {
  petId: [{ required: true, message: '请选择宠物', trigger: 'change' }],
  sensorId: [{ required: true, message: '请选择传感器', trigger: 'change' }]
}

const availableSensors = computed(() => {
  const boundSensorIds = bindings.value.map(b => b.sensorId)
  return sensors.value.filter(s => !boundSensorIds.includes(s.id))
})

const getBatteryColor = (percentage) => {
  if (percentage > 70) return '#67c23a'
  if (percentage > 30) return '#e6a23c'
  return '#f56c6c'
}

const loadSensors = async () => {
  try {
    const res = await getSensors()
    sensors.value = res.data || []
  } catch (error) {
    console.error('加载传感器列表失败:', error)
  }
}

const loadBindings = async () => {
  try {
    const res = await getBindings()
    bindings.value = res.data || []
  } catch (error) {
    console.error('加载绑定列表失败:', error)
  }
}

const loadPets = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const res = await getPetsByUserId(userInfo.id)
    pets.value = res.data || []
  } catch (error) {
    console.error('加载宠物列表失败:', error)
  }
}

const handleAddSensor = () => {
  isEditSensor.value = false
  Object.assign(sensorForm, {
    id: null,
    sensorCode: '',
    sensorName: '',
    sensorType: 'GPS',
    status: 'ACTIVE',
    batteryLevel: 100
  })
  sensorDialogVisible.value = true
}

const handleEditSensor = (row) => {
  isEditSensor.value = true
  Object.assign(sensorForm, row)
  sensorDialogVisible.value = true
}

const handleSaveSensor = async () => {
  try {
    await sensorFormRef.value.validate()
    if (isEditSensor.value) {
      await updateSensor(sensorForm)
      ElMessage.success('更新成功')
    } else {
      await createSensor(sensorForm)
      ElMessage.success('添加成功')
    }
    sensorDialogVisible.value = false
    loadSensors()
  } catch (error) {
    if (error !== false) {
      console.error('保存失败:', error)
    }
  }
}

const handleDeleteSensor = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这个传感器吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteSensor(row.id)
    ElMessage.success('删除成功')
    loadSensors()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleAddBinding = () => {
  Object.assign(bindingForm, {
    petId: null,
    sensorId: null
  })
  bindingDialogVisible.value = true
}

const handleSaveBinding = async () => {
  try {
    await bindingFormRef.value.validate()
    await createBinding(bindingForm)
    ElMessage.success('绑定成功')
    bindingDialogVisible.value = false
    loadBindings()
  } catch (error) {
    if (error !== false) {
      console.error('绑定失败:', error)
    }
  }
}

const handleDeleteBinding = async (row) => {
  try {
    await ElMessageBox.confirm('确定要解绑吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteBinding(row.id)
    ElMessage.success('解绑成功')
    loadBindings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('解绑失败:', error)
    }
  }
}

onMounted(() => {
  loadSensors()
  loadBindings()
  loadPets()
})
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
    .binding-header {
      margin-bottom: 16px;
    }
  }
}

:deep(.sensor-dialog),
:deep(.binding-dialog) {
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
  }
}
</style>
