<template>
  <div class="growth-analysis-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>成长分析</h3>
        </div>
      </template>
      <el-select v-model="petId" placeholder="选择宠物" style="margin-bottom: 20px; width: 200px;">
        <el-option v-for="pet in pets" :key="pet.id" :label="pet.name" :value="pet.id"></el-option>
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="margin-left: 10px; margin-bottom: 20px; width: 300px;"
      />
      <el-button type="primary" @click="fetchGrowthData" style="margin-left: 10px;">查询</el-button>
      
      <div class="chart-container">
        <el-card class="chart-card">
          <template #header>
            <h4>体重变化</h4>
          </template>
          <div ref="weightChart" class="chart"></div>
        </el-card>
        
        <el-card class="chart-card">
          <template #header>
            <h4>身高变化</h4>
          </template>
          <div ref="heightChart" class="chart"></div>
        </el-card>
        
        <el-card class="chart-card">
          <template #header>
            <h4>成长评估</h4>
          </template>
          <div class="evaluation">
            <el-descriptions :column="2">
              <el-descriptions-item label="当前体重">{{ currentWeight }} kg</el-descriptions-item>
              <el-descriptions-item label="当前身高">{{ currentHeight }} cm</el-descriptions-item>
              <el-descriptions-item label="体重趋势">{{ weightTrend }}</el-descriptions-item>
              <el-descriptions-item label="身高趋势">{{ heightTrend }}</el-descriptions-item>
              <el-descriptions-item label="健康状况" :span="2">{{ healthStatus }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { usePetStore } from '../../store/pet'
import * as echarts from 'echarts'

const petStore = usePetStore()

const pets = ref([])
const petId = ref('')
const dateRange = ref([])
const weightChart = ref(null)
const heightChart = ref(null)
const currentWeight = ref('0')
const currentHeight = ref('0')
const weightTrend = ref('稳定')
const heightTrend = ref('稳定')
const healthStatus = ref('健康')

const fetchGrowthData = async () => {
  // 这里可以根据实际情况从后端获取成长数据
  // 暂时使用模拟数据
  await nextTick(() => {
    renderWeightChart()
    renderHeightChart()
  })
}

const renderWeightChart = () => {
  if (!weightChart.value) return
  
  const chart = echarts.init(weightChart.value)
  const option = {
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value',
      name: '体重 (kg)'
    },
    series: [{
      data: [2.5, 3.2, 4.1, 4.8, 5.5, 6.2],
      type: 'line',
      smooth: true
    }]
  }
  chart.setOption(option)
}

const renderHeightChart = () => {
  if (!heightChart.value) return
  
  const chart = echarts.init(heightChart.value)
  const option = {
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value',
      name: '身高 (cm)'
    },
    series: [{
      data: [20, 25, 30, 35, 40, 45],
      type: 'line',
      smooth: true
    }]
  }
  chart.setOption(option)
}

const fetchPets = async () => {
  await petStore.fetchPets()
  pets.value = petStore.getPets
}

onMounted(() => {
  fetchPets()
  nextTick(() => {
    renderWeightChart()
    renderHeightChart()
  })
})
</script>

<style scoped>
.growth-analysis-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 20px;
}

.chart-card {
  margin-bottom: 0;
}

.chart {
  width: 100%;
  height: 300px;
}

.evaluation {
  padding: 20px;
}
</style>