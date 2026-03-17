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

export const hygieneApi = {
  // 获取卫生记录列表
  getHygieneRecords(petId, recordType) {
    let params = {}
    if (petId) params.petId = petId
    if (recordType) params.recordType = recordType
    return apiClient.get('/hygiene-records', { params })
  },
  // 根据ID获取卫生记录
  getHygieneRecordById(id) {
    return apiClient.get(`/hygiene-records/${id}`)
  },
  // 添加卫生记录
  addHygieneRecord(hygieneData) {
    return apiClient.post('/hygiene-records', hygieneData)
  },
  // 更新卫生记录
  updateHygieneRecord(id, hygieneData) {
    return apiClient.put(`/hygiene-records/${id}`, hygieneData)
  },
  // 删除卫生记录
  deleteHygieneRecord(id) {
    return apiClient.delete(`/hygiene-records/${id}`)
  }
}