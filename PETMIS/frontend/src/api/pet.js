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

export const petApi = {
  // 获取宠物列表
  getPets() {
    return apiClient.get('/pets')
  },
  // 根据ID获取宠物
  getPetById(id) {
    return apiClient.get(`/pets/${id}`)
  },
  // 添加宠物
  addPet(petData) {
    return apiClient.post('/pets', petData)
  },
  // 更新宠物
  updatePet(id, petData) {
    return apiClient.put(`/pets/${id}`, petData)
  },
  // 删除宠物
  deletePet(id) {
    return apiClient.delete(`/pets/${id}`)
  }
}