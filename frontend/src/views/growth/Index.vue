<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <div class="header-left">
        <h2>成长分析</h2>
        <el-select v-model="selectedPetId" placeholder="选择宠物" style="width: 200px; margin-left: 20px" @change="handlePetChange">
          <el-option v-for="pet in petStore.petList" :key="pet.id" :label="pet.name" :value="pet.id" />
        </el-select>
      </div>
    </div>
    <div class="page-content">
      <el-row :gutter="20" class="charts-row">
        <el-col :xs="24" :lg="12">
          <div class="chart-card glass-card">
            <div class="card-header">
              <h3 class="card-title">体重变化趋势</h3>
            </div>
            <div ref="weightChartRef" class="chart-container"></div>
          </div>
        </el-col>
        <el-col :xs="24" :lg="12">
          <div class="chart-card glass-card">
            <div class="card-header">
              <h3 class="card-title">身高变化趋势</h3>
            </div>
            <div ref="heightChartRef" class="chart-container"></div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="stats-row">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card glass-card">
            <div class="stat-icon">
              <el-icon :size="32"><TrendCharts /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.weightChange }}</div>
              <div class="stat-label">体重变化</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card glass-card">
            <div class="stat-icon">
              <el-icon :size="32"><DataAnalysis /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.heightChange }}</div>
              <div class="stat-label">身高变化</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card glass-card">
            <div class="stat-icon">
              <el-icon :size="32"><Monitor /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.recordCount }}</div>
              <div class="stat-label">记录次数</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card glass-card">
            <div class="stat-icon">
              <el-icon :size="32"><Calendar /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.days }}</div>
              <div class="stat-label">记录天数</div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-empty v-if="!selectedPetId" description="请先选择宠物~" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import { TrendCharts, DataAnalysis, Monitor, Calendar } from '@element-plus/icons-vue'
import { usePetStore } from '@/store/pet'
import { getMonitoringRecordsByPetId } from '@/api/monitoring'

const petStore = usePetStore()
const selectedPetId = ref(null)
const weightChartRef = ref(null)
const heightChartRef = ref(null)
let weightChart = null
let heightChart = null

const monitoringList = ref([])

const stats = computed(() => {
  const weightRecords = monitoringList.value.filter(r => r.recordType === '体重')
  const heightRecords = monitoringList.value.filter(r => r.recordType === '身高' || r.recordType === '体长')
  
  let weightChange = '-'
  let heightChange = '-'
  
  if (weightRecords.length >= 2) {
    const first = parseFloat(weightRecords[weightRecords.length - 1].value)
    const last = parseFloat(weightRecords[0].value)
    const change = last - first
    weightChange = change > 0 ? `+${change.toFixed(2)}kg` : `${change.toFixed(2)}kg`
  }
  
  if (heightRecords.length >= 2) {
    const first = parseFloat(heightRecords[heightRecords.length - 1].value)
    const last = parseFloat(heightRecords[0].value)
    const change = last - first
    heightChange = change > 0 ? `+${change.toFixed(2)}cm` : `${change.toFixed(2)}cm`
  }
  
  return {
    weightChange,
    heightChange,
    recordCount: monitoringList.value.length,
    days: monitoringList.value.length > 0 ? Math.ceil((new Date() - new Date(monitoringList.value[monitoringList.value.length - 1].recordTime)) / (1000 * 60 * 60 * 24)) : 0
  }
})

onMounted(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  petStore.fetchPetList(userInfo.id || 1)
})

onUnmounted(() => {
  if (weightChart) {
    weightChart.dispose()
  }
  if (heightChart) {
    heightChart.dispose()
  }
  window.removeEventListener('resize', handleResize)
})

const handleResize = () => {
  if (weightChart) {
    weightChart.resize()
  }
  if (heightChart) {
    heightChart.resize()
  }
}

const handlePetChange = () => {
  if (selectedPetId.value) {
    fetchMonitoringList()
  }
}

const fetchMonitoringList = async () => {
  try {
    const res = await getMonitoringRecordsByPetId(selectedPetId.value)
    monitoringList.value = (res.data || []).sort((a, b) => new Date(b.recordTime) - new Date(a.recordTime))
    initWeightChart()
    initHeightChart()
    window.addEventListener('resize', handleResize)
  } catch (error) {
    console.error('获取监控记录失败:', error)
  }
}

const initWeightChart = () => {
  if (!weightChartRef.value) return
  
  const weightRecords = monitoringList.value.filter(r => r.recordType === '体重')
  
  weightChart = echarts.init(weightChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(15, 23, 42, 0.9)',
      borderColor: 'rgba(255, 255, 255, 0.1)',
      textStyle: { color: '#f1f5f9' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: weightRecords.map(r => new Date(r.recordTime).toLocaleDateString('zh-CN')),
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } },
      axisLabel: { color: '#94a3b8' }
    },
    yAxis: {
      type: 'value',
      name: 'kg',
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } },
      axisLabel: { color: '#94a3b8' },
      splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.05)' } }
    },
    series: [
      {
        name: '体重',
        type: 'line',
        smooth: true,
        data: weightRecords.map(r => parseFloat(r.value)),
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(168, 85, 247, 0.3)' },
            { offset: 1, color: 'rgba(168, 85, 247, 0)' }
          ])
        },
        lineStyle: { color: '#a855f7', width: 3 },
        itemStyle: { color: '#a855f7' }
      }
    ]
  }
  weightChart.setOption(option)
}

const initHeightChart = () => {
  if (!heightChartRef.value) return
  
  const heightRecords = monitoringList.value.filter(r => r.recordType === '身高' || r.recordType === '体长')
  
  heightChart = echarts.init(heightChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(15, 23, 42, 0.9)',
      borderColor: 'rgba(255, 255, 255, 0.1)',
      textStyle: { color: '#f1f5f9' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: heightRecords.map(r => new Date(r.recordTime).toLocaleDateString('zh-CN')),
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } },
      axisLabel: { color: '#94a3b8' }
    },
    yAxis: {
      type: 'value',
      name: 'cm',
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } },
      axisLabel: { color: '#94a3b8' },
      splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.05)' } }
    },
    series: [
      {
        name: '身高',
        type: 'line',
        smooth: true,
        data: heightRecords.map(r => parseFloat(r.value)),
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(245, 158, 11, 0.3)' },
            { offset: 1, color: 'rgba(245, 158, 11, 0)' }
          ])
        },
        lineStyle: { color: '#f59e0b', width: 3 },
        itemStyle: { color: '#f59e0b' }
      }
    ]
  }
  heightChart.setOption(option)
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

  .charts-row {
    margin-bottom: 24px;
  }

  .chart-card {
    padding: 24px;
    height: 100%;

    .card-header {
      margin-bottom: 20px;

      .card-title {
        font-size: 18px;
        font-weight: 600;
        color: #f1f5f9;
        margin: 0;
      }
    }

    .chart-container {
      height: 300px;
    }
  }

  .stats-row {
    .stat-card {
      padding: 24px;
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 20px;

      .stat-icon {
        width: 64px;
        height: 64px;
        border-radius: 16px;
        background: linear-gradient(135deg, rgba(0, 212, 255, 0.2), rgba(0, 212, 255, 0.1));
        color: #00d4ff;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .stat-content {
        .stat-value {
          font-size: 28px;
          font-weight: 700;
          color: #f1f5f9;
          line-height: 1.2;
        }

        .stat-label {
          font-size: 14px;
          color: #94a3b8;
          margin-top: 4px;
        }
      }
    }
  }
}
</style>
