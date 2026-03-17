import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/pets',
    name: 'Pets',
    component: () => import('../views/pets/PetList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/pets/add',
    name: 'AddPet',
    component: () => import('../views/pets/AddPet.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/pets/edit/:id',
    name: 'EditPet',
    component: () => import('../views/pets/EditPet.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/health',
    name: 'Health',
    component: () => import('../views/health/HealthList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/health/add',
    name: 'AddHealth',
    component: () => import('../views/health/AddHealth.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/health/edit/:id',
    name: 'EditHealth',
    component: () => import('../views/health/EditHealth.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/feeding',
    name: 'Feeding',
    component: () => import('../views/feeding/FeedingList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/feeding/add',
    name: 'AddFeeding',
    component: () => import('../views/feeding/AddFeeding.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/hygiene',
    name: 'Hygiene',
    component: () => import('../views/hygiene/HygieneList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/hygiene/add',
    name: 'AddHygiene',
    component: () => import('../views/hygiene/AddHygiene.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/hygiene/edit/:id',
    name: 'EditHygiene',
    component: () => import('../views/hygiene/EditHygiene.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/monitoring',
    name: 'Monitoring',
    component: () => import('../views/monitoring/MonitoringList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/growth',
    name: 'Growth',
    component: () => import('../views/growth/GrowthAnalysis.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/community',
    name: 'Community',
    component: () => import('../views/community/CommunityList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/community/add',
    name: 'AddPost',
    component: () => import('../views/community/AddPost.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/community/post/:id',
    name: 'PostDetail',
    component: () => import('../views/community/PostDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/share',
    name: 'Share',
    component: () => import('../views/share/ShareList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/share/add',
    name: 'AddShare',
    component: () => import('../views/share/AddShare.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/',
    redirect: '/pets'
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.meta.requiresAuth
  const token = localStorage.getItem('token')
  
  if (requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router