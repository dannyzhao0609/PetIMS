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

export const postApi = {
  // 获取帖子列表
  getPosts(userId, petId) {
    let params = {}
    if (userId) params.userId = userId
    if (petId) params.petId = petId
    return apiClient.get('/posts', { params })
  },
  // 根据ID获取帖子
  getPostById(id) {
    return apiClient.get(`/posts/${id}`)
  },
  // 添加帖子
  addPost(postData) {
    return apiClient.post('/posts', postData)
  },
  // 更新帖子
  updatePost(id, postData) {
    return apiClient.put(`/posts/${id}`, postData)
  },
  // 删除帖子
  deletePost(id) {
    return apiClient.delete(`/posts/${id}`)
  },
  // 增加点赞数
  incrementLikes(id) {
    return apiClient.post(`/posts/${id}/like`)
  },
  // 增加评论数
  incrementComments(id) {
    return apiClient.post(`/posts/${id}/comment`)
  }
}