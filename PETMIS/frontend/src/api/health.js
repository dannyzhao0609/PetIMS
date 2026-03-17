import axios from 'axios'

const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器，添加token
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

export const healthApi = {
  // 获取健康记录列表
  getHealthRecords(petId, recordType) {
    let params = {}
    if (petId) params.petId = petId
    if (recordType) params.recordType = recordType
    return apiClient.get('/health-records', { params })
  },
  // 根据ID获取健康记录
  getHealthRecordById(id) {
    return apiClient.get(`/health-records/${id}`)
  },
  // 添加健康记录
  addHealthRecord(healthData) {
    return apiClient.post('/health-records', healthData)
  },
  // 更新健康记录
  updateHealthRecord(id, healthData) {
    return apiClient.put(`/health-records/${id}`, healthData)
  },
  // 删除健康记录
  deleteHealthRecord(id) {
    return apiClient.delete(`/health-records/${id}`)
  }
}