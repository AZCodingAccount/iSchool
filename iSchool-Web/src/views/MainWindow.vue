<script setup>
import { useUserInfoerStore } from '@/stores/userInfoer'
// import { ref } from 'vue'

const userInfoerStore = useUserInfoerStore()
</script>

<template>
  <div style="position: fixed; z-index: 5; width: 100%; margin-top: -0.4%; left: 0">
    <el-menu :default-active="$route.path" mode="horizontal" :ellipsis="false" :router="true">
        <!-- logo -->
      <div style="display: flex; margin-left: 2%">
        <img style="height: 100%" src="/public/img/code.png" />
        <div style="font-size: 30px; padding: 5px 10px; font-style: italic; line-height: 160%">
          智搜通
        </div>
      </div>
      <div style="flex-grow: 100"></div>
      <el-tooltip content="<strong>首页</strong>" placement="bottom" effect="light" raw-content>
        <el-menu-item index="/main/home"
          ><el-icon size="160%"> <Search /> </el-icon
        ></el-menu-item>
      </el-tooltip>
      <div style="flex-grow: 2"></div>
      <el-tooltip content="<strong>大众点评</strong>" placement="bottom" effect="light" raw-content>
        <el-menu-item index="/main/comment"
          ><el-icon size="160%"> <Star /> </el-icon
        ></el-menu-item>
      </el-tooltip>
      <div style="flex-grow: 2"></div>
      <el-tooltip content="<strong>个人中心</strong>" placement="bottom" effect="light" raw-content>
        <el-menu-item index="/main/mine">
          <el-badge
            style="margin-top: -10px"
            :is-dot="userInfoerStore.userInfo.unReadCommentsCount != 0"
            :offset="[-5, 19]"
          >
            <div class="avatar">
              <img
                style="height: 100%; vertical-align: top"
                :src="userInfoerStore.userInfo.userAvatar"
              />
            </div>
          </el-badge>
        </el-menu-item>
      </el-tooltip>
      <div style="flex-grow: 1"></div>
    </el-menu>
  </div>

  <router-view />
  <div class="footer">
    <a href="https://github.com/AZCodingAccount/iSchool">
      <img style="width: 50px" src="/public/img/github.png"
    /></a>
    <div>
      我们<span style="font-weight: 700">不</span
      >开展任何盈利活动，如果您发现本系统不支持您的学校，请点击上方去Github提出issue
    </div>
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
  margin-top: 3px;
  background-color: white;
  /* padding: 3px; */
  /* width: 100%; */
}

.avatar {
  display: inline-block;
  border: 1px rgb(202, 202, 202) solid;
  width: 40px;
  height: 40px;
  border-radius: 40px;
  overflow: hidden;
}
</style>
