<script setup>
import { useUserInfoerStore } from '@/stores/userInfoer'
import { ref } from 'vue'
import { getLoginUser_1, login, register } from '@/api/user'
import router from '@/router';
import { ElMessage } from 'element-plus';
const userInfoerStore = useUserInfoerStore()
const isLogin = ref(true) // true登录，false注册
const rememberMe = ref(false) // 记住我
const titleRef = ref(null) // 标题绑定变量
const boxRef = ref(null) // 盒子绑定变量

const loginForm = ref({ // 登录表单
    username: '',
    password: ''
})

rememberMe.value = userInfoerStore.userInfo.rememberMe
if (rememberMe.value) { // 若选中了“记住我”，则自动填上用户名和密码
    loginForm.value.username = userInfoerStore.userInfo.username
    loginForm.value.password = userInfoerStore.userInfo.password
}

// 登录
const onLogin = async () => {
    if (loginForm.value.username == '' || loginForm.value.password == '') {
        ElMessage.error({
            message: '用户名或密码不能为空',
            grouping: true
        })
        return
    }
    let res = await login(loginForm.value)
    userInfoerStore.updateUserInfo({
        token: res.data,
        rememberMe: rememberMe.value
    })
    res = await getLoginUser_1()
    userInfoerStore.updateUserInfo(res.data)
    router.push('/main')
}

const registerForm = ref({ // 注册表单
    username: '',
    password: '',
    surePassword: ''
})

// 注册
const onRegister = async () => {
    if (registerForm.value.username == '' || registerForm.value.password == '') {
        ElMessage.error({
            message: '用户名或密码不能为空',
            grouping: true
        })
        return
    }
    if (registerForm.value.password != registerForm.value.surePassword) {
        ElMessage.error({
            message: '密码确认失败',
            grouping: true
        })
        return
    }
    await register(registerForm.value)
    ElMessage.success('注册成功')
    isLogin.value = true
}

// 忘记密码
const onForgetPassword = () => {
    console.log('onForgetPassword')
}

</script>

<template>
    <el-image class="background" src="/img/background/loginRegisterBackground.jpg" />
    <div ref="titleRef" class="title" @click="titleRef.style.top = '-100%'; boxRef.style.top = '28%'">
        iSchool
    </div>
    <div ref="boxRef" class="box">
        <!-- 登录界面 -->
        <div v-show="isLogin">
            <h1 style="text-align: center;">iSchool登录</h1>
            <el-form :model="loginForm" label-width="80px">
                <el-form-item label="账号">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <User />
                        </el-icon>
                        <el-input v-model="loginForm.username" placeholder="请输入用户名" />
                    </div>
                </el-form-item>

                <el-form-item label="密码">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <Unlock />
                        </el-icon><el-input v-model="loginForm.password" type="password" placeholder="请输入密码"
                            :show-password="true" />
                    </div>
                </el-form-item>
                <div>
                    <el-switch size="large" style="margin-left: 130px;" v-model="rememberMe" inline-prompt
                        active-text="记住我" inactive-text="记住我" />
                    <el-link style="float: right;" @click="onForgetPassword">忘记密码</el-link>
                </div>

                <div style="display: flex;">
                    <div style="flex-grow: 1;"></div>
                    <el-button type="primary" style="width: 180px; height: 45px; font-size: 20px"
                        @click="onLogin">登录</el-button>
                    <div style="flex-grow: 0.45;"></div>
                    <el-link style="float: right; color: #409eff;" :underline="false"
                        @click="isLogin = false">没有账号？去注册</el-link>
                </div>
            </el-form>
        </div>
        <!-- 注册界面 -->
        <div v-show="!isLogin">
            <h1 style="text-align: center;">iSchool注册</h1>
            <el-form :model="registerForm" label-width="80px">
                <el-form-item label="账号">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <User />
                        </el-icon>
                        <el-input v-model="registerForm.username" placeholder="请输入用户名" />
                    </div>
                </el-form-item>

                <el-form-item label="密码">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <Unlock />
                        </el-icon><el-input v-model="registerForm.password" type="password" placeholder="请输入密码"
                            :show-password="true" />
                    </div>
                </el-form-item>

                <el-form-item label="确认密码">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <Unlock />
                        </el-icon><el-input v-model="registerForm.surePassword" type="password" placeholder="请再次输入密码"
                            :show-password="true" />
                    </div>
                </el-form-item>

                <div>
                    <el-switch size="large" style="margin-left: 130px;" v-model="rememberMe" inline-prompt
                        active-text="记住我" inactive-text="记住我" />
                    <el-link style="float: right;" @click="onForgetPassword">忘记密码</el-link>
                </div>

                <div style="display: flex;">
                    <div style="flex-grow: 1;"></div>
                    <el-button type="primary" style="width: 180px; height: 45px; font-size: 20px"
                        @click="onRegister">注册</el-button>
                    <div style="flex-grow: 0.8;"></div>
                    <el-link style="float: right; color: #409eff;" :underline="false"
                        @click="isLogin = true">去登录</el-link>
                </div>
            </el-form>
        </div>
    </div>
</template>

<style scoped>
.background {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: -1;
}

.title {
    display: inline-block;
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    text-align: center;
    line-height: 400%;
    font-size: 200px;
    /* border: 1px red solid; */
    font-style: italic;
    /* background-color: rgba(193, 193, 193, 0.842); */
    background: linear-gradient(45deg, #b2e68d, #bce689);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-text-stroke-width: 1px;
    -webkit-text-stroke-color: white;
    transition: all 0.6s;
}

.box {
    display: inline-block;
    position: fixed;
    width: 30%;
    left: 34%;
    top: 105%;
    background-color: white;
    border-radius: 10px;
    padding: 20px;
    /* border: 1px red solid; */
    transition: all 0.6s;
}
</style>
