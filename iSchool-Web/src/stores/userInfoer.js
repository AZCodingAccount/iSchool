// import { ElMessage } from 'element-plus'
import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useUserInfoerStore = defineStore('UserInfoer', () => {
  const userInfo = ref({
    userId: 0,
    username: '2104060415',
    nickname: '小张昵称',
    password: '123456',
    gender: '男',
    age: 20,
    userAvatar: '/public/img/myAvatar.jpg',
    email: '1476652531@qq.com',

    rememberMe: true,
    token: '',
  })

  const updateUserInfo = (data) => {
    // data: obj
    for (let key in data)
      userInfo.value[key] = data[key]
  }

  const cleanUserInfo = () => {
    let obj = {
      userId: -1,
      username: '',
      nickname: '',
      password: '',
      gender: '',
      age: -1,
      userAvatar: '',
      email: '',
      rememberMe: false,
      token: ''
    }
    for (let key in obj)
      userInfo.value[key] = obj[key]
  }

  return {
    userInfo,
    updateUserInfo,
    cleanUserInfo
  }
}, {
  persist: true
})