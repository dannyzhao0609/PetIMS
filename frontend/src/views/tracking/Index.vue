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
      
      <div v-if="!selectedPetId" class="empty-state">
        <el-empty description="请选择要跟踪的宠物" />
      </div>
      
      <div v-else class="tracking-content">
        <div class="map-container glass-card" v-if="mapLoaded">
          <div id="amap-container" class="amap-container"></div>
        </div>
        
        <div class="map-container glass-card" v-else>
          <div class="map-placeholder">
            <el-icon :size="48"><Position /></el-icon>
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
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Position } from '@element-plus/icons-vue'
import { getPetsByUserId } from '@/api/pet'
import { getBindings } from '@/api/sensor'
import { getLocationTracksByPetId, getLatestLocationByPetId } from '@/api/location'

const selectedPetId = ref(null)
const pets = ref([])
const bindings = ref([])
const locationTracks = ref([])
const latestLocation = ref(null)
const mapLoaded = ref(false)
let map = null
let marker = null
let polyline = null
let mapInitAttempted = ref(false)

const petsWithSensors = computed(() => {
  const boundPetIds = bindings.value.map(b => b.petId)
  return pets.value.filter(p => boundPetIds.includes(p.id))
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
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const res = await getPetsByUserId(userInfo.id)
    pets.value = res.data || []
  } catch (error) {
    console.error('加载宠物列表失败:', error)
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

const loadLocationTracks = async () => {
  if (!selectedPetId.value) return
  
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

const initMap = async () => {
  if (mapInitAttempted.value) return
  mapInitAttempted.value = true
  
  try {
    const AMap = await window.AMapLoader.load({
      key: 'a2e9a8bcb1958f2a0e9f6e6d7a8b9c0d',
      version: '2.0',
      plugins: ['AMap.Marker', 'AMap.Polyline', 'AMap.ToolBar', 'AMap.Scale']
    })
    
    map = new AMap.Map('amap-container', {
      resizeEnable: true,
      zoom: 13,
      center: [116.397428, 39.90923]
    })
    
    map.addControl(new AMap.ToolBar())
    map.addControl(new AMap.Scale())
    
    mapLoaded.value = true
    
    nextTick(() => {
      if (latestLocation.value) {
        updateMap()
      }
    })
  } catch (error) {
    console.error('地图初始化失败:', error)
  }
}

const updateMap = () => {
  if (!map || !mapLoaded.value) return
  
  try {
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
      map.setCenter([latestLocation.value.longitude, latestLocation.value.latitude])
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
  } catch (error) {
    console.error('更新地图失败:', error)
  }
}

const handlePetChange = () => {
  loadLocationTracks()
}

const refreshLocation = () => {
  loadLocationTracks()
  ElMessage.success('位置已刷新')
}

const loadAMapSDK = () => {
  if (window.AMapLoader) {
    return Promise.resolve()
  }
  
  return new Promise((resolve, reject) => {
    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = 'https://webapi.amap.com/loader.js'
    script.onload = () => {
      resolve()
    }
    script.onerror = () => {
      reject(new Error('加载高德地图SDK失败'))
    }
    document.head.appendChild(script)
  })
}

onMounted(async () => {
  loadPets()
  loadBindings()
  
  try {
    await loadAMapSDK()
    nextTick(() => {
      initMap()
    })
  } catch (error) {
    console.error('加载地图SDK失败:', error)
  }
})

onUnmounted(() => {
  if (map) {
    map.destroy()
    map = null
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
    
    .empty-state {
      padding: 60px 0;
    }
    
    .tracking-content {
      display: flex;
      flex-direction: column;
      gap: 20px;
      
      .map-container {
        padding: 20px;
        
        .amap-container {
          width: 100%;
          height: 400px;
          border-radius: 8px;
        }
        
        .map-placeholder {
          width: 100%;
          height: 400px;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          color: #94a3b8;
          gap: 16px;
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
</style>
