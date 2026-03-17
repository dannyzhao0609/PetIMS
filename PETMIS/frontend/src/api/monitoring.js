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

export const monitoringApi = {
  // 获取监控记录列表
  getMonitoringRecords(petId, recordType, startDate, endDate) {
    let params = {}
    if (petId) params.petId = petId
    if (recordType) params.recordType = recordType
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate
    return apiClient.get('/monitoring-records', { params })
  },
  // 根据ID获取监控记录
  getMonitoringRecordById(id) {
    return apiClient.get(`/monitoring-records/${id}`)
  },
  // 添加监控记录
  addMonitoringRecord(monitoringData) {
    return apiClient.post('/monitoring-records', monitoringData)
  },
  // 删除监控记录
  deleteMonitoringRecord(id) {
    return apiClient.delete(`/monitoring-records/${id}`)
  }
}