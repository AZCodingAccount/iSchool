import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      component: () => import('@/views/LoginRegisterWindow.vue')
    },
    {
      path: '/main',
      component: () => import('@/views/MainWindow.vue'),
      children: [
        {
          path: '',
          redirect: '/main/home'
        },
        {
          path: 'home',
          component: () => import('@/views/HomeWindow.vue'),
        },
        {
          path: 'comment',
          component: () => import('@/views/CommentWindow.vue'),
        },
        {
          path: 'mine',
          component: () => import('@/views/MineWindow.vue'),
        },
      ]
    },
    {
      path: '/tempView',
      component: () => import('@/views/tempView.vue')
    },
    {
      path: '/:pathMatch(.*)',
      component: () => import('@/views/NotFound.vue')
    }
  ]
})

export default router
