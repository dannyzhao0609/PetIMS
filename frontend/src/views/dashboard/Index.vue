<template>
  <div class="dashboard">
    <div class="welcome-section">
      <h1 class="welcome-title">欢迎回来，{{ userInfo.username || '用户' }}！</h1>
      <p class="welcome-subtitle">今天也要好好照顾您的毛孩子呀 🐾</p>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card glass-card">
          <div class="stat-icon pet-icon">
            <el-icon :size="32"><Box /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">3</div>
            <div class="stat-label">我的宠物</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card glass-card">
          <div class="stat-icon health-icon">
            <el-icon :size="32"><FirstAidKit /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">12</div>
            <div class="stat-label">健康记录</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card glass-card">
          <div class="stat-icon feeding-icon">
            <el-icon :size="32"><Food /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">45</div>
            <div class="stat-label">喂食记录</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card glass-card">
          <div class="stat-icon reminder-icon">
            <el-icon :size="32"><Bell /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">2</div>
            <div class="stat-label">待办提醒</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :lg="16">
        <div class="chart-card glass-card">
          <div class="card-header">
            <h3 class="card-title">本周喂食统计</h3>
          </div>
          <div ref="feedingChartRef" class="chart-container"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="8">
        <div class="chart-card glass-card">
          <div class="card-header">
            <h3 class="card-title">宠物分布</h3>
          </div>
          <div ref="petTypeChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="recent-row">
      <el-col :xs="24" :lg="12">
        <div class="recent-card glass-card">
          <div class="card-header">
            <h3 class="card-title">最近活动</h3>
            <el-button type="primary" text>查看全部</el-button>
          </div>
          <div class="activity-list">
            <div class="activity-item" v-for="(item, index) in activities" :key="index">
              <div class="activity-icon" :class="item.type">
                <el-icon><component :is="item.icon" /></el-icon>
              </div>
              <div class="activity-content">
                <div class="activity-title">{{ item.title }}</div>
                <div class="activity-time">{{ item.time }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="recent-card glass-card">
          <div class="card-header">
            <h3 class="card-title">待办事项</h3>
            <el-button type="primary" text>添加</el-button>
          </div>
          <div class="todo-list">
            <div class="todo-item" v-for="(item, index) in todos" :key="index">
              <el-checkbox v-model="item.completed">{{ item.title }}</el-checkbox>
              <div class="todo-time">{{ item.time }}</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { Box, FirstAidKit, Food, Bell, Document, ChatDotRound, Calendar } from '@element-plus/icons-vue'

const userInfo = ref({})
const feedingChartRef = ref(null)
const petTypeChartRef = ref(null)
let feedingChart = null
let petTypeChart = null

const activities = ref([
  { title: '给小白喂食了猫粮', time: '10分钟前', type: 'feeding', icon: Food },
  { title: '小黑的体检记录已更新', time: '1小时前', type: 'health', icon: FirstAidKit },
  { title: '给大黄洗澡了', time: '3小时前', type: 'hygiene', icon: Document },
  { title: '社区收到新评论', time: '5小时前', type: 'community', icon: ChatDotRound }
])

const todos = ref([
  { title: '带小白去打疫苗', time: '今天 14:00', completed: false },
  { title: '给小黑买新狗粮', time: '明天 10:00', completed: false },
  { title: '记录大黄的体重', time: '本周内', completed: true }
])

onMounted(() => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
  }
  initFeedingChart()
  initPetTypeChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  if (feedingChart) {
    feedingChart.dispose()
  }
  if (petTypeChart) {
    petTypeChart.dispose()
  }
  window.removeEventListener('resize', handleResize)
})

const handleResize = () => {
  if (feedingChart) {
    feedingChart.resize()
  }
  if (petTypeChart) {
    petTypeChart.resize()
  }
}

const initFeedingChart = () => {
  if (!feedingChartRef.value) return
  
  feedingChart = echarts.init(feedingChartRef.value)
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
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } },
      axisLabel: { color: '#94a3b8' }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } },
      axisLabel: { color: '#94a3b8' },
      splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.05)' } }
    },
    series: [
      {
        name: '喂食次数',
        type: 'line',
        stack: 'Total',
        smooth: true,
        data: [3, 4, 3, 5, 4, 6, 3],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(0, 212, 255, 0.3)' },
            { offset: 1, color: 'rgba(0, 212, 255, 0)' }
          ])
        },
        lineStyle: { color: '#00d4ff', width: 3 },
        itemStyle: { color: '#00d4ff' }
      }
    ]
  }
  feedingChart.setOption(option)
}

const initPetTypeChart = () => {
  if (!petTypeChartRef.value) return
  
  petTypeChart = echarts.init(petTypeChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(15, 23, 42, 0.9)',
      borderColor: 'rgba(255, 255, 255, 0.1)',
      textStyle: { color: '#f1f5f9' }
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      textStyle: { color: '#94a3b8' }
    },
    series: [
      {
        name: '宠物类型',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['35%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#0f172a',
          borderWidth: 2
        },
        label: { show: false },
        emphasis: {
          label: {
            show: true,
            fontSize: 14,
            fontWeight: 'bold'
          }
        },
        data: [
          { value: 2, name: '狗', itemStyle: { color: '#3b82f6' } },
          { value: 1, name: '猫', itemStyle: { color: '#8b5cf6' } },
          { value: 0, name: '其他', itemStyle: { color: '#06b6d4' } }
        ]
      }
    ]
  }
  petTypeChart.setOption(option)
}
</script>

<style scoped lang="scss">
.dashboard {
  .welcome-section {
    margin-bottom: 32px;

    .welcome-title {
      font-size: 28px;
      font-weight: 700;
      margin: 0 0 8px 0;
      background: linear-gradient(135deg, #00d4ff, #a855f7);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    .welcome-subtitle {
      font-size: 16px;
      color: #94a3b8;
      margin: 0;
    }
  }

  .stats-row {
    margin-bottom: 24px;
  }

  .stat-card {
    padding: 24px;
    display: flex;
    align-items: center;
    gap: 16px;

    .stat-icon {
      width: 64px;
      height: 64px;
      border-radius: 16px;
      display: flex;
      align-items: center;
      justify-content: center;

      &.pet-icon {
        background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(59, 130, 246, 0.1));
        color: #3b82f6;
      }

      &.health-icon {
        background: linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(16, 185, 129, 0.1));
        color: #10b981;
      }

      &.feeding-icon {
        background: linear-gradient(135deg, rgba(245, 158, 11, 0.2), rgba(245, 158, 11, 0.1));
        color: #f59e0b;
      }

      &.reminder-icon {
        background: linear-gradient(135deg, rgba(168, 85, 247, 0.2), rgba(168, 85, 247, 0.1));
        color: #a855f7;
      }
    }

    .stat-content {
      .stat-value {
        font-size: 32px;
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

  .charts-row {
    margin-bottom: 24px;
  }

  .chart-card,
  .recent-card {
    padding: 24px;
    height: 100%;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
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

  .activity-list {
    .activity-item {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px 0;
      border-bottom: 1px solid rgba(255, 255, 255, 0.05);

      &:last-child {
        border-bottom: none;
      }

      .activity-icon {
        width: 40px;
        height: 40px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;

        &.feeding {
          background: rgba(245, 158, 11, 0.1);
          color: #f59e0b;
        }

        &.health {
          background: rgba(16, 185, 129, 0.1);
          color: #10b981;
        }

        &.hygiene {
          background: rgba(59, 130, 246, 0.1);
          color: #3b82f6;
        }

        &.community {
          background: rgba(168, 85, 247, 0.1);
          color: #a855f7;
        }
      }

      .activity-content {
        flex: 1;

        .activity-title {
          font-size: 14px;
          color: #f1f5f9;
          margin-bottom: 4px;
        }

        .activity-time {
          font-size: 12px;
          color: #64748b;
        }
      }
    }
  }

  .todo-list {
    .todo-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid rgba(255, 255, 255, 0.05);

      &:last-child {
        border-bottom: none;
      }

      .todo-time {
        font-size: 12px;
        color: #64748b;
      }
    }
  }
}
</style>
