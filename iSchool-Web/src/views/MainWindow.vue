<script setup>
// import { ref } from 'vue'

</script>

<template>
    <div style="position: fixed; z-index: 5; width: 100%; margin-top: -0.4%; left: 0;">
        <el-menu :default-active="home" mode="horizontal" :ellipsis="false" :router="true">
            <!-- logo -->
            <div style="display: flex; margin-left: 2%;">
                <img style="height: 100%;" src="/public/img/code.png" alt="logo">
                <div style="font-size: 30px; margin: 3%; font-style: italic; line-height: 155%;">iSchool</div>
            </div>
            <div style="flex-grow: 1;"></div>
            <el-tooltip content="<strong>首页</strong>" placement="bottom" effect="light" raw-content>
                <el-menu-item style="font-size: 130%;" index="/main/home"><el-icon size="160%">
                        <Search />
                    </el-icon></el-menu-item>
            </el-tooltip>
            <div style="flex-grow: 0.02;"></div>
            <el-tooltip content="<strong>大众点评</strong>" placement="bottom" effect="light" raw-content>
                <el-menu-item style="font-size: 130%;" index="/main/comment"><el-icon size="160%">
                        <ChatDotRound />
                    </el-icon></el-menu-item>
            </el-tooltip>
            <div style="flex-grow: 0.02;"></div>
            <el-tooltip content="<strong>个人中心</strong>" placement="bottom" effect="light" raw-content>
                <el-menu-item style="font-size: 130%;" index="/main/mine"><el-icon size="160%">
                        <User />
                    </el-icon></el-menu-item>
            </el-tooltip>
            <div style="flex-grow: 0.01;"></div>
        </el-menu>
    </div>

    <router-view />
    <div class="footer">
        <a href="https://github.com/AZCodingAccount/iSchool">
            <img src="/public/img/github.png" alt="github" style="width: 50px;"></a>
        <div>我们<span style="font-weight: 700;">不</span>开展任何盈利活动，如果您发现本系统不支持您的学校，请点击上方去Github提出issue</div>
        <div>CopyRight © 2024 Search plus所有. ALL Right Reseved</div>
    </div>
</template>
<style scoped>
.box {
    border: 1px rgb(250, 250, 5) solid;
    max-height: 1300px;
}

.footer {
    /* display: inline-block; */
    text-align: center;
    color: #cccccc;
    margin-top: 2%;
    background-color: white;
    padding: 10px;
    /* width: 100%; */
}
</style>