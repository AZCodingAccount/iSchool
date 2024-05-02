<script setup>
// import { ref } from 'vue'

</script>

<template>
    <el-menu style="position: fixed; z-index: 5; width: 100%; margin-top: -0.4%;" :default-active="home"
        mode="horizontal" :ellipsis="false" :router="true">
        <!-- logo -->
        <div style="display: flex; margin-left: 2%;">
            <img style="height: 80%;" src="/public/img/logo.png" alt="logo">
            <div style="font-size: 30px; margin: 3%">iSchool</div>
        </div>
        <div style="flex-grow: 1;"></div>
        <el-menu-item style="font-size: 130%;" index="/main/home"><el-icon size="160%">
                <Search />
            </el-icon>首页</el-menu-item>
        <el-menu-item style="font-size: 130%;" index="/main/comment"><el-icon size="160%">
                <ChatDotRound />
            </el-icon>大众点评</el-menu-item>
        <el-menu-item style="font-size: 130%;" index="/main/mine"><el-icon size="160%">
                <User />
            </el-icon>个人中心</el-menu-item>
    </el-menu>
    <router-view />
    <div style="text-align: center; color: #cccccc; margin-top: 2%;">
        <a href="https://github.com/AZCodingAccount/iSchool">
            <img src="/public/img/github.png" alt="github" style="width: 50px;"></a>
        <div>我们<span>不</span>开展任何盈利活动，如果您发现本系统不支持您的学校，请点击上方去Github提出issue</div>
        <div>CopyRight © 2024 Search plus所有. ALL Right Reseved</div>
    </div>
</template>
<style scoped>
.box {
    border: 1px rgb(250, 250, 5) solid;
    max-height: 1300px;
}
</style>