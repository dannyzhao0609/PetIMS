<template>
  <div class="app-container">
    <el-container>
      <el-header v-if="$route.meta.requiresAuth">
        <div class="header-content">
          <h1>PetIMS - 宠物管理系统</h1>
          <div class="user-info">
            <el-dropdown>
              <span class="user-name">{{ user.name }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px" v-if="$route.meta.requiresAuth">
          <el-menu
            :default-active="activeMenu"
            class="side-menu"
            router
          >
            <el-sub-menu index="1">
              <template #title>
                <el-icon><House /></el-icon>
                <span>宠物管理</span>
              </template>
              <el-menu-item index="/pets">宠物档案</el-menu-item>
              <el-menu-item index="/health">健康管理</el-menu-item>
              <el-menu-item index="/feeding">饮食管理</el-menu-item>
              <el-menu-item index="/hygiene">卫生管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><Monitor /></el-icon>
                <span>监控分析</span>
              </template>
              <el-menu-item index="/monitoring">日常监控</el-menu-item>
              <el-menu-item index="/growth">成长分析</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title>
                <el-icon><ChatLineSquare /></el-icon>
                <span>社交互动</span>
              </template>
              <el-menu-item index="/community">宠友社区</el-menu-item>
              <el-menu-item index="/share">日常分享</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { House, Monitor, ChatLineSquare } from '@element-plus/icons-vue'
import { useUserStore } from './store/user'

const router = useRouter()
const userStore = useUserStore()
const user = ref({ name: '用户' })

const activeMenu = computed(() => {
  const path = router.currentRoute.value.path
  if (path.startsWith('/pets')) return '1'
  if (path.startsWith('/health') || path.startsWith('/feeding') || path.startsWith('/hygiene')) return '1'
  if (path.startsWith('/monitoring') || path.startsWith('/growth')) return '2'
  if (path.startsWith('/community') || path.startsWith('/share')) return '3'
  return '1'
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  // 从localStorage获取用户信息
  const userInfo = localStorage.getItem('user')
  if (userInfo) {
    user.value = JSON.parse(userInfo)
  }
})
</script>

<style scoped>
.app-container {
  height: 100vh;
  overflow: hidden;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #409EFF;
  color: white;
}

.header-content h1 {
  margin: 0;
  font-size: 20px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-name {
  cursor: pointer;
  margin-right: 10px;
}

.side-menu {
  height: 100%;
  border-right: none;
}

.el-main {
  padding: 20px;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>