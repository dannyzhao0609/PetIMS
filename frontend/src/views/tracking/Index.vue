<template>
  <div class="tracking-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>宠物位置跟踪</span>
        </div>
      </template>
      
      <div class="tracking-content">
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
        
        <div v-else class="map-container">
          <div id="amap-container" class="amap-container"></div>
          
          <div class="location-info" v-if="latestLocation">
            <el-descriptions title="当前位置信息" :column="2" border>
              <el-descriptions-item label="宠物名称">{{ selectedPet?.name }}</el-descriptions-item>
              <el-descriptions-item label="更新时间">{{ formatTime(latestLocation.trackTime) }}</el-descriptions-item>
              <el-descriptions-item label="经度">{{ latestLocation.longitude }}</el-descriptions-item>
              <el-descriptions-item label="纬度">{{ latestLocation.latitude }}</el-descriptions-item>
              <el-descriptions-item label="精度" :span="2">{{ latestLocation.accuracy }}米</el-descriptions-item>
            </el-descriptions>
          </div>
          
          <div class="track-history" v-if="locationTracks.length > 0">
            <h4>移动轨迹记录 (最近{{ locationTracks.length }}条)</h4>
            <el-table :data="locationTracks" style="width: 100%" size="small" max-height="200">
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
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import AMapLoader from '@amap/amap-jsapi-loader'
import { getPetsByUserId } from '@/api/pet'
import { getBindings } from '@/api/sensor'
import { getLocationTracksByPetId, getLatestLocationByPetId } from '@/api/location'

const selectedPetId = ref(null)
const pets = ref([])
const bindings = ref([])
const locationTracks = ref([])
const latestLocation = ref(null)
let map = null
let marker = null
let polyline = null

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
    ElMessage.error('加载宠物列表失败')
  }
}

const loadBindings = async () => {
  try {
    const res = await getBindings()
    bindings.value = res.data || []
  } catch (error) {
    ElMessage.error('加载绑定列表失败')
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
      updateMap()
    })
  } catch (error) {
    ElMessage.error('加载位置信息失败')
  }
}

const initMap = async () => {
  try {
    const AMap = await AMapLoader.load({
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
  } catch (error) {
    console.error('地图初始化失败:', error)
    ElMessage.error('地图初始化失败')
  }
}

const updateMap = () => {
  if (!map) return
  
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
}

const handlePetChange = () => {
  loadLocationTracks()
}

const refreshLocation = () => {
  loadLocationTracks()
  ElMessage.success('位置已刷新')
}

onMounted(() => {
  loadPets()
  loadBindings()
  nextTick(() => {
    initMap()
  })
})

onUnmounted(() => {
  if (map) {
    map.destroy()
    map = null
  }
})
</script>

<style scoped lang="scss">
.tracking-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .tracking-content {
    .pet-selector {
      margin-bottom: 20px;
      display: flex;
      gap: 16px;
      align-items: center;
    }
    
    .empty-state {
      padding: 60px 0;
    }
    
    .map-container {
      .amap-container {
        width: 100%;
        height: 400px;
        border-radius: 8px;
        margin-bottom: 20px;
      }
      
      .location-info {
        margin-bottom: 20px;
      }
      
      .track-history {
        h4 {
          margin-bottom: 12px;
          color: #94a3b8;
        }
      }
    }
  }
}
</style>
