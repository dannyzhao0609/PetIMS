import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
    token: localStorage.getItem('token') || '',
    isLoggedIn: !!localStorage.getItem('token')
  }),
  getters: {
    getUserInfo: (state) => state.userInfo,
    getToken: (state) => state.token,
    getIsLoggedIn: (state) => state.isLoggedIn
  },
  actions: {
    login(userInfo, token) {
      this.userInfo = userInfo
      this.token = token
      this.isLoggedIn = true
      localStorage.setItem('user', JSON.stringify(userInfo))
      localStorage.setItem('token', token)
    },
    logout() {
      this.userInfo = null
      this.token = ''
      this.isLoggedIn = false
      localStorage.removeItem('user')
      localStorage.removeItem('token')
    },
    updateUserInfo(userInfo) {
      this.userInfo = userInfo
      localStorage.setItem('user', JSON.stringify(userInfo))
    }
  }
})