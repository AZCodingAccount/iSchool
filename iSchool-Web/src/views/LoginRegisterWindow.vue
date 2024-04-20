<script setup>
import { useUserInfoerStore } from '@/stores/userInfoer';
import { ref } from 'vue';
const userInfoerStore = useUserInfoerStore()
const isLogin = ref(true) // true登录，false注册
const rememberMe = ref(userInfoerStore.userInfo.rememberMe)

// 登录
const loginForm = ref({
    account: '',
    password: ''
})

if (rememberMe.value) {
    loginForm.value.account = userInfoerStore.userInfo.account,
        loginForm.value.password = userInfoerStore.userInfo.password
}

const onLogin = async () => {
    console.log('onLogin')
    // ...
}

// 注册
const registerForm = ref({
    account: '',
    setPassword: '',
    surePassword: ''
})

const onRegister = async () => {
    console.log('onRegister')
    // ...
}

// 忘记密码
const onForgetPassword = () => {
    console.log('onForgetPassword')
}

</script>

<template>
    <el-image class="background" src="/img/background/loginRegister.jpg" />
    <div class="box">
        <!-- 登录界面 -->
        <div v-show="isLogin">
            <h1 style="text-align: center;">iSchool登录</h1>
            <el-form :model="loginForm" label-width="80px">
                <el-form-item label="账号">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <User />
                        </el-icon>
                        <el-input v-model="loginForm.account" placeholder="请输入用户名" />
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
                        <el-input v-model="registerForm.account" placeholder="请输入用户名" />
                    </div>
                </el-form-item>

                <el-form-item label="密码">
                    <div style="display: flex; width: 100%;">
                        <el-icon :size="30" style="margin-right: 20px;">
                            <Unlock />
                        </el-icon><el-input v-model="registerForm.setPassword" type="password" placeholder="请输入密码"
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

.box {
    display: inline-block;
    position: fixed;
    width: 30%;
    left: 34%;
    top: 20%;
    background-color: white;
    border-radius: 10px;
    padding: 20px;
    /* border: 1px red solid; */
}
</style>
