import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useUserInfoerStore = defineStore('UserInfoer', () => {
  const userInfo = ref({
    account: '',
    password: '',
    rememberMe: false
  })
  const setUserInfo = (data) => {
    userInfo.value.account = data.account
    userInfo.value.password = data.password
    userInfo.value.rememberMe = data.rememberMe
  }

  return {
    userInfo,
    setUserInfo
  }
}, {
  persist: true
})