// import { ElMessage } from 'element-plus'
import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useUserInfoerStore = defineStore('UserInfoer', () => {
  const userInfo = ref({
    userId: 0,
    username: 'Xing Zai',
    nickname: '小张昵称',
    // password: '123456',
    gender: '男',
    age: 20,
    userAvatar: '/public/img/myAvatar.jpg',
    email: '1476652531@qq.com',

    rememberMe: false,
    token: '',
  })

  const updateUserInfo = (data) => {
    // data: obj
    for (let key in data)
      userInfo.value[key] = data[key]
  }

  return {
    userInfo,
    updateUserInfo
  }
}, {
  persist: true
})