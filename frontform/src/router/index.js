import { createRouter, createWebHashHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', component: () => import('../views/Index.vue') },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/category/:slug', component: () => import('../views/Category.vue'), meta: { requiresAuth: true } },
  { path: '/article/:slug', component: () => import('../views/Detail.vue'), meta: { requiresAuth: true } },
  { path: '/sources', component: () => import('../views/Sources.vue'), meta: { requiresAuth: true } },
  { path: '/no-access', component: () => import('../views/NoAccess.vue') },
  { path: '/admin', redirect: '/admin/dashboard' },
  { path: '/admin/dashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/comments', component: () => import('../views/admin/Comments.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/interactions', component: () => import('../views/admin/Interactions.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({ history: createWebHashHistory(), routes })

router.beforeEach((to) => {
  const auth = useAuthStore()
  auth.loadFromStorage()

  if (auth.isLoggedIn && ['/login', '/register'].includes(to.path)) return '/'
  if (to.meta.requiresAdmin) {
    if (!auth.isLoggedIn) return { path: '/login', query: { redirect: to.fullPath } }
    if (!auth.isAdmin) return '/no-access'
  }
  if (to.meta.requiresAuth && !auth.isLoggedIn) return { path: '/login', query: { redirect: to.fullPath } }
  return true
})

export default router
