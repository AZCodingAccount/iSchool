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
  // baseURL = 'http://localhost:8080'
} else {
  baseURL = 'http://logistics:8080'
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
    ElMessage({ message: res.data.msg || '未知异常（一般是输入数据不合规）', type: 'error' })
    return Promise.reject(res.data)
  },
  (err) => {
    if (!err.response) {
      ElMessage({ message: '服务器无法访问，请稍后再试。', type: 'error' })
    } else {
      ElMessage({ message: err.response.data.msg || '未知异常（一般是输入数据不合规）', type: 'error' })
      if (err.response?.status === 401) {// 如果是401，跳转到登录页面
        router.push('/login')
      }
    }
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
