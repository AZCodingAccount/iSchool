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
    // {
    //   path: '/main',
    //   component: () => import('@/views/MainWindow.vue'),
    //   children: [
    //     {
    //       path: '',
    //       redirect: '/main/course_schedule'
    //     },
    //     {
    //       path: 'course_schedule',
    //       component: () => import('@/views/CourseScheduleWindow.vue'),
    //     },
    //     {
    //       path: 'friend',
    //       component: () => import('@/views/FriendWindow.vue'),
    //     },
    //     {
    //       path: 'user',
    //       component: () => import('@/views/UserWindow.vue'),
    //     }
    //   ]
    // },
    {
      path: '/:pathMatch(.*)',
      component: () => import('@/views/NotFound.vue')
    }
  ]
})

export default router
