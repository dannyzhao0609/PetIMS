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

export const feedingApi = {
  // 获取饮食记录列表
  getFeedingRecords(petId, startDate, endDate) {
    let params = {}
    if (petId) params.petId = petId
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate
    return apiClient.get('/feeding-records', { params })
  },
  // 根据ID获取饮食记录
  getFeedingRecordById(id) {
    return apiClient.get(`/feeding-records/${id}`)
  },
  // 添加饮食记录
  addFeedingRecord(feedingData) {
    return apiClient.post('/feeding-records', feedingData)
  },
  // 删除饮食记录
  deleteFeedingRecord(id) {
    return apiClient.delete(`/feeding-records/${id}`)
  }
}