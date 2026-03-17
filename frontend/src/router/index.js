import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue')
      },
      {
        path: 'pets',
        name: 'Pets',
        component: () => import('@/views/pets/Index.vue')
      },
      {
        path: 'health',
        name: 'Health',
        component: () => import('@/views/health/Index.vue')
      },
      {
        path: 'feeding',
        name: 'Feeding',
        component: () => import('@/views/feeding/Index.vue')
      },
      {
        path: 'hygiene',
        name: 'Hygiene',
        component: () => import('@/views/hygiene/Index.vue')
      },
      {
        path: 'monitoring',
        name: 'Monitoring',
        component: () => import('@/views/monitoring/Index.vue')
      },
      {
        path: 'growth',
        name: 'Growth',
        component: () => import('@/views/growth/Index.vue')
      },
      {
        path: 'community',
        name: 'Community',
        component: () => import('@/views/community/Index.vue')
      },
      {
        path: 'share',
        name: 'Share',
        component: () => import('@/views/share/Index.vue')
      },
      {
        path: 'sensor',
        name: 'Sensor',
        component: () => import('@/views/sensor/Index.vue')
      },
      {
        path: 'tracking',
        name: 'Tracking',
        component: () => import('@/views/tracking/Index.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Index.vue')
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/settings/Index.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
