import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useUserInfoerStore = defineStore('UserInfoer', () => {
  const userInfo = ref({
    stu_id: '',
    password: '',
    username: '',
    department: '',
    enrollment_year: '',
    last_course_id: -1,
    create_time: ''
  })
  const setUserInfo = (data) => {
    userInfo.value.stu_id = data.stu_id
    userInfo.value.password = data.password
    userInfo.value.username = data.username
    userInfo.value.department = data.department
    userInfo.value.enrollment_year = data.enrollment_year
    userInfo.value.last_course_id = data.last_course_id
    userInfo.value.create_time = data.create_time
  }

  return {
    userInfo,
    setUserInfo
  }
}, {
  persist: true
})