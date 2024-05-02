import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'
import 'element-plus/theme-chalk/el-loading.css'
import 'element-plus/theme-chalk/el-message.css'
let baseURL = ''
// 根据不同环境配置不同的url
if (import.meta.env.MODE === 'development') {
  baseURL = 'http://127.0.0.1:8000'
  // baseURL = 'http://10.51.15.145:8080'
  // baseURL = 'http://localhost:8080'
} else {
  baseURL = 'http://logistics:8080'
}

const instance = axios.create({
  baseURL,
  timeout: 50000
})

instance.interceptors.request.use(
  // 序列化参数
  (config) => {
    // console.log('config.data1', config.data)
    // config.data = JSON.stringify(config.data)
    // console.log('config.data2', config.data)
    return config
  },
  (err) => Promise.reject(err)
)

instance.interceptors.response.use(
  (res) => {
    if (res.data.code === 1) {
      return res.data
    }
    // code != 1
    ElMessage({ message: res.data.msg || '未知异常（一般是输入数据不合规）', type: 'error' })
    return Promise.reject(res.data)
  },
  (err) => { // 请求失败
    console.log('err', err)
    if (!err.response) { // 无法访问
      ElMessage({ message: '服务器无法访问，请稍后再试。', type: 'error' })
    } else {  // 可访问但报错
      ElMessage({
        message: err.response.data.message || '未知异常（一般是输入数据不合规）',
        type: 'error'
      })
      // 如果是401，跳转到登录页面
      if (err.response?.status === 401) {
        router.push('/login')
      }
    }
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
