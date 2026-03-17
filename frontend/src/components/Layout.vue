<template>
  <div class="layout-container">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
        <div class="logo-section">
          <div class="logo">
            <img src="@/assets/images/设计宠物管理网站 logo.png" alt="PetIMS Logo" style="width: 48px; height: auto;" />
            <span v-show="!isCollapse" class="logo-text">PetIMS</span>
          </div>
        </div>

        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          background-color="transparent"
          text-color="#94a3b8"
          active-text-color="#00d4ff"
          class="sidebar-menu"
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <template #title>仪表板</template>
          </el-menu-item>
          <el-menu-item index="/pets">
            <el-icon><Box /></el-icon>
            <template #title>宠物管理</template>
          </el-menu-item>
          <el-menu-item index="/health">
            <el-icon><FirstAidKit /></el-icon>
            <template #title>健康管理</template>
          </el-menu-item>
          <el-menu-item index="/feeding">
            <el-icon><Food /></el-icon>
            <template #title>饮食管理</template>
          </el-menu-item>
          <el-menu-item index="/hygiene">
            <el-icon><CircleCheck /></el-icon>
            <template #title>卫生管理</template>
          </el-menu-item>
          <el-menu-item index="/monitoring">
            <el-icon><Monitor /></el-icon>
            <template #title>日常监控</template>
          </el-menu-item>
          <el-menu-item index="/growth">
            <el-icon><TrendCharts /></el-icon>
            <template #title>成长分析</template>
          </el-menu-item>
          <el-menu-item index="/community">
            <el-icon><ChatDotRound /></el-icon>
            <template #title>宠友互动</template>
          </el-menu-item>
          <el-menu-item index="/share">
            <el-icon><Share /></el-icon>
            <template #title>日常分享</template>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container class="main-container">
        <el-header class="header">
          <div class="header-left">
            <el-icon class="collapse-icon" @click="toggleCollapse">
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentPage }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="36" style="background: linear-gradient(135deg, #3b82f6, #8b5cf6)">
                  {{ userInfo.username?.charAt(0)?.toUpperCase() || 'U' }}
                </el-avatar>
                <span class="username">{{ userInfo.username || '用户' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="main-content">
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
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  DataAnalysis,
  Box,
  FirstAidKit,
  Food,
  CircleCheck,
  Monitor,
  TrendCharts,
  ChatDotRound,
  Share,
  Fold,
  Expand,
  User,
  Setting,
  SwitchButton,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)
const userInfo = ref({})

const activeMenu = computed(() => route.path)

const currentPage = computed(() => {
  const pageMap = {
    '/dashboard': '仪表板',
    '/pets': '宠物管理',
    '/health': '健康管理',
    '/feeding': '饮食管理',
    '/hygiene': '卫生管理',
    '/monitoring': '日常监控',
    '/growth': '成长分析',
    '/community': '宠友互动',
    '/share': '日常分享'
  }
  return pageMap[route.path] || '页面'
})

onMounted(() => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
  }
})

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.success('退出登录成功')
      router.push('/login')
    } catch {
    }
  } else if (command === 'profile') {
    ElMessage.info('个人中心功能开发中...')
  } else if (command === 'settings') {
    ElMessage.info('系统设置功能开发中...')
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  width: 100%;
  height: 100%;
}

.el-container {
  height: 100%;
}

.sidebar {
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(255, 255, 255, 0.05);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
}

.logo-section {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);

  .logo {
    display: flex;
    align-items: center;
    gap: 12px;

    .logo-text {
      font-size: 20px;
      font-weight: 700;
      background: linear-gradient(135deg, #00d4ff, #a855f7);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      transition: opacity 0.3s ease;
    }
  }
}

.sidebar-menu {
  flex: 1;
  border: none;
  padding: 12px;

  .el-menu-item {
    border-radius: 8px;
    margin-bottom: 4px;
    transition: all 0.2s ease;

    &:hover {
      background: rgba(255, 255, 255, 0.05);
    }

    &.is-active {
      background: rgba(0, 212, 255, 0.1);
      box-shadow: 0 0 15px rgba(0, 212, 255, 0.2);
    }
  }
}

.main-container {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

.header {
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .collapse-icon {
      font-size: 20px;
      cursor: pointer;
      color: #94a3b8;
      transition: color 0.2s ease;

      &:hover {
        color: #00d4ff;
      }
    }

    .el-breadcrumb {
      :deep(.el-breadcrumb__item) {
        .el-breadcrumb__inner {
          color: #94a3b8;

          &.is-link {
            &:hover {
              color: #00d4ff;
            }
          }
        }
      }
    }
  }

  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      padding: 8px 16px;
      border-radius: 8px;
      transition: background 0.2s ease;

      &:hover {
        background: rgba(255, 255, 255, 0.05);
      }

      .username {
        color: #f1f5f9;
        font-size: 14px;
        font-weight: 500;
      }
    }
  }
}

.main-content {
  padding: 24px;
  overflow-y: auto;
}
</style>
