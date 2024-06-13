/*
 * @Author: AlbertZhang han892577@163.com
 * @Date: 2024-05-28 11:43:50
 * @LastEditors: AlbertZhang han892577@163.com
 * @LastEditTime: 2024-06-05 04:49:29
 * @FilePath: \iSchool-Web\src\router\index.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
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
        // {
        //   path: '',
        //   redirect: 'main/home'
        // },
        {
          path: 'home',
          component: () => import('@/views/HomeWindow.vue')
        },
        {
          path: 'comment',
          component: () => import('@/views/CommentWindow.vue')
        },
        {
          path: 'mine',
          component: () => import('@/views/MineWindow.vue')
        }
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
