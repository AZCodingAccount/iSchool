/*
 * @Author: AlbertZhang han892577@163.com
 * @Date: 2024-05-28 11:43:50
 * @LastEditors: AlbertZhang han892577@163.com
 * @LastEditTime: 2024-06-05 05:18:36
 * @FilePath: \iSchool-Web\src\utils\request.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import axios from 'axios'
import router from '@/router'
import { useUserInfoerStore } from '@/stores/userInfoer';
import { ElMessage } from 'element-plus'
import 'element-plus/theme-chalk/el-loading.css'
import 'element-plus/theme-chalk/el-message.css'
let baseURL = ''
// 根据不同环境配置不同的url
if (import.meta.env.MODE === 'development') {
  baseURL = 'http://localhost:8901'
  // baseURL = 'http://192.168.41.191:8901'
} else {
  baseURL = 'http://117.72.64.163:9001'
}

const instance = axios.create({
  baseURL,
  timeout: 50000
})

instance.interceptors.request.use(
  // 给配置对象加上token
  (config) => {
    const userInfoerStore = useUserInfoerStore()
    if (userInfoerStore.userInfo.token)
      config.headers.token = userInfoerStore.userInfo.token
    return config
  },
  (err) => Promise.reject(err)
)

instance.interceptors.response.use(
  (res) => {
    if (res.data.code === 1)
      return res.data

    ElMessage({ message: res.data.msg || '未知异常', type: 'error' })
    if (res.data.code === 40100)// 如果是40100，跳转到登录页面
      router.push('/login')
    return Promise.reject(res.data)
  },
  (err) => {
    if (!err.response) {
      ElMessage({ message: '服务器无法访问，请稍后再试。', type: 'error' })
    } else {
      ElMessage({ message: err.response.data.msg || '未知异常', type: 'error' })
      // if (err.response?.status === 40100) {// 如果是40100，跳转到登录页面
      //   router.push('/login')
      // }
    }
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
