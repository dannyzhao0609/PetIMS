<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>宠物位置跟踪</h2>
    </div>
    <div class="page-content">
      <div class="pet-selector">
        <el-select v-model="selectedPetId" placeholder="请选择要跟踪的宠物" style="width: 300px" @change="handlePetChange">
          <el-option 
            v-for="pet in petsWithSensors" 
            :key="pet.id" 
            :label="pet.name" 
            :value="pet.id" 
          />
        </el-select>
        <el-button v-if="selectedPetId" type="primary" @click="refreshLocation">
          <el-icon><Refresh /></el-icon>
          刷新位置
        </el-button>
      </div>
      
      <div class="tracking-content">
        <div class="map-container glass-card">
          <div id="container" class="amap-container" :style="{ opacity: mapLoaded && !mapLoadError ? 1 : 0 }"></div>
          <div v-if="mapLoadError" class="map-overlay map-error">
            <el-icon :size="48"><Warning /></el-icon>
            <p>地图服务暂时不可用</p>
            <p class="error-detail">请检查网络连接或稍后重试</p>
          </div>
          <div v-else-if="!mapLoaded" class="map-overlay map-placeholder">
            <el-icon :size="48" class="loading-icon"><Loading /></el-icon>
            <p>地图加载中...</p>
          </div>
        </div>
        
        <div class="location-info glass-card" v-if="latestLocation">
          <h3>当前位置信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="宠物名称">{{ selectedPet?.name }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ formatTime(latestLocation.trackTime) }}</el-descriptions-item>
            <el-descriptions-item label="经度">{{ latestLocation.longitude }}</el-descriptions-item>
            <el-descriptions-item label="纬度">{{ latestLocation.latitude }}</el-descriptions-item>
            <el-descriptions-item label="精度" :span="2">{{ latestLocation.accuracy }}米</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="track-history glass-card" v-if="locationTracks.length > 0">
          <h3>移动轨迹记录 (最近{{ locationTracks.length }}条)</h3>
          <el-table :data="locationTracks" style="width: 100%" size="small" max-height="300" stripe>
            <el-table-column prop="trackTime" label="时间" width="180">
              <template #default="{ row }">
                {{ formatTime(row.trackTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="longitude" label="经度" width="120" />
            <el-table-column prop="latitude" label="纬度" width="120" />
            <el-table-column prop="accuracy" label="精度" width="80">
              <template #default="{ row }">
                {{ row.accuracy }}m
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Warning, Loading } from '@element-plus/icons-vue'
import { getPetsByUserId } from '@/api/pet'
import { getBindings } from '@/api/sensor'
import { getLocationTracksByPetId, getLatestLocationByPetId } from '@/api/location'

const selectedPetId = ref(null)
const pets = ref([])
const bindings = ref([])
const locationTracks = ref([])
const latestLocation = ref(null)
const mapLoaded = ref(false)
const mapLoadError = ref(false)
let map = null
let marker = null
let polyline = null

const petsWithSensors = computed(() => {
  console.log('=== 计算petsWithSensors ===')
  console.log('pets:', pets.value)
  console.log('bindings:', bindings.value)
  
  if (!pets.value || pets.value.length === 0) {
    console.log('没有宠物数据')
    return []
  }
  
  console.log('返回所有宠物，不进行过滤')
  return pets.value
})

const selectedPet = computed(() => {
  return pets.value.find(p => p.id === selectedPetId.value)
})

const formatTime = (timeStr) => {
  if (!timeStr) return '-'
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN')
}

const loadPets = async () => {
  try {
    console.log('开始加载宠物列表...')
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    console.log('userInfo:', userInfo)
    const res = await getPetsByUserId(userInfo.id || 1)
    console.log('宠物列表响应:', res)
    pets.value = res.data || []
    console.log('pets.value:', pets.value)
  } catch (error) {
    console.error('加载宠物列表失败:', error)
  }
}

const loadBindings = async () => {
  try {
    console.log('开始加载绑定列表...')
    const res = await getBindings()
    console.log('绑定列表响应:', res)
    bindings.value = res.data || []
    console.log('bindings.value:', bindings.value)
  } catch (error) {
    console.error('加载绑定列表失败:', error)
  }
}

const loadLocationTracks = async () => {
  if (!selectedPetId.value) {
    locationTracks.value = []
    latestLocation.value = null
    if (mapLoaded.value) {
      showChinaMap()
    }
    return
  }
  
  try {
    const [tracksRes, latestRes] = await Promise.all([
      getLocationTracksByPetId(selectedPetId.value),
      getLatestLocationByPetId(selectedPetId.value)
    ])
    
    locationTracks.value = tracksRes.data || []
    latestLocation.value = latestRes.data || null
    
    nextTick(() => {
      if (mapLoaded.value) {
        updateMap()
      }
    })
  } catch (error) {
    console.error('加载位置信息失败:', error)
  }
}

const showChinaMap = () => {
  if (!map || !mapLoaded.value) return
  try {
    console.log('显示中国地图...')
    if (marker) {
      map.remove(marker)
      marker = null
    }
    if (polyline) {
      map.remove(polyline)
      polyline = null
    }
    map.setZoomAndCenter(4, [105.0, 35.0])
    console.log('中国地图显示完成')
  } catch (error) {
    console.error('显示中国地图失败:', error)
  }
}

const initMap = () => {
  console.log('开始初始化地图...')
  
  window._AMapSecurityConfig = {
    securityJsCode: '42a134d9233036bb99a00d5e6aa1bd38'
  }
  
  if (window.AMap) {
    console.log('AMap已加载，直接初始化')
    createMap()
    return
  }
  
  console.log('开始加载AMap脚本...')
  const script = document.createElement('script')
  script.type = 'text/javascript'
  script.src = 'https://webapi.amap.com/maps?v=2.0&key=8b6498c94c59588ec45280887d868b29&callback=initAMapCallback'
  
  window.initAMapCallback = () => {
    console.log('AMap脚本加载完成回调')
    createMap()
  }
  
  script.onerror = () => {
    console.error('AMap脚本加载失败')
    mapLoadError.value = true
    mapLoaded.value = false
  }
  
  document.head.appendChild(script)
}

const createMap = () => {
  try {
    console.log('创建地图实例...')
    const AMap = window.AMap
    
    map = new AMap.Map('container', {
      resizeEnable: true,
      zoom: 4,
      center: [105.0, 35.0]
    })
    
    AMap.plugin(['AMap.ToolBar', 'AMap.Scale'], () => {
      console.log('插件加载完成')
      map.addControl(new AMap.ToolBar())
      map.addControl(new AMap.Scale())
      
      map.on('complete', () => {
        console.log('地图加载完成')
        mapLoaded.value = true
        mapLoadError.value = false
        
        nextTick(() => {
          if (latestLocation.value) {
            updateMap()
          } else {
            showChinaMap()
          }
        })
      })
    })
    
  } catch (error) {
    console.error('创建地图失败:', error)
    mapLoadError.value = true
    mapLoaded.value = false
  }
}

const updateMap = () => {
  if (!map || !mapLoaded.value) return
  
  try {
    console.log('更新地图...')
    if (marker) {
      map.remove(marker)
    }
    
    if (polyline) {
      map.remove(polyline)
    }
    
    if (latestLocation.value) {
      const AMap = window.AMap
      marker = new AMap.Marker({
        position: [latestLocation.value.longitude, latestLocation.value.latitude],
        title: selectedPet.value?.name || '宠物'
      })
      map.add(marker)
      map.setZoomAndCenter(13, [latestLocation.value.longitude, latestLocation.value.latitude])
    }
    
    if (locationTracks.value.length > 1) {
      const AMap = window.AMap
      const path = locationTracks.value.map(track => [track.longitude, track.latitude])
      polyline = new AMap.Polyline({
        path: path,
        borderWeight: 2,
        strokeColor: '#00d4ff',
        lineJoin: 'round'
      })
      map.add(polyline)
    }
    console.log('地图更新完成')
  } catch (error) {
    console.error('更新地图失败:', error)
  }
}

const handlePetChange = () => {
  console.log('宠物选择变化:', selectedPetId.value)
  loadLocationTracks()
}

const refreshLocation = () => {
  loadLocationTracks()
  ElMessage.success('位置已刷新')
}

watch(selectedPetId, () => {
  console.log('watch监测到宠物ID变化:', selectedPetId.value)
  loadLocationTracks()
})

onMounted(async () => {
  console.log('组件挂载，开始初始化...')
  await Promise.all([
    loadPets(),
    loadBindings()
  ])
  
  console.log('宠物和绑定数据加载完成，开始初始化地图')
  nextTick(() => {
    initMap()
  })
})

onUnmounted(() => {
  if (map) {
    map.destroy()
    map = null
  }
  if (window.initAMapCallback) {
    delete window.initAMapCallback
  }
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
    .pet-selector {
      margin-bottom: 20px;
      display: flex;
      gap: 16px;
      align-items: center;
    }
    
    .tracking-content {
      display: flex;
      flex-direction: column;
      gap: 20px;
      
      .map-container {
        padding: 20px;
        position: relative;
        
        .amap-container {
          width: 100%;
          height: 400px;
          border-radius: 8px;
          background: #1e293b;
          transition: opacity 0.3s ease;
        }
        
        .map-overlay {
          position: absolute;
          top: 20px;
          left: 20px;
          right: 20px;
          bottom: 20px;
          width: calc(100% - 40px);
          height: 400px;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          color: #94a3b8;
          gap: 16px;
          background: #1e293b;
          border-radius: 8px;
          z-index: 10;
          
          .loading-icon {
            animation: spin 1s linear infinite;
          }
          
          .error-detail {
            font-size: 12px;
            opacity: 0.8;
          }
        }
      }
      
      .location-info {
        padding: 20px;
        
        h3 {
          margin: 0 0 16px 0;
          color: #f1f5f9;
          font-size: 16px;
        }
      }
      
      .track-history {
        padding: 20px;
        
        h3 {
          margin: 0 0 16px 0;
          color: #f1f5f9;
          font-size: 16px;
        }
      }
    }
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
