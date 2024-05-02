<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

import { homeResultData, homeResultData_AI } from '@/assets/testData.json' // 测试数据引用

const numShowingDataOnOnePage = 10 // 单页展示的查询结果数量

const searchMessage = ref('') // 搜索内容
const resultData = ref([]) // 所有查询结果
const showingResultData = ref([]) // 正在展示的查询结果
const resultScrollbarRef = ref(null) // 滚动条绑定变量
const currentPage = ref(1)
const onChangePage = (curPage) => {
    // console.log('on change page', curPage)
    let startIndex = (curPage - 1) * numShowingDataOnOnePage
    let endIndex = startIndex + numShowingDataOnOnePage <= resultData.value.length ? startIndex + numShowingDataOnOnePage : resultData.value.length
    showingResultData.value = resultData.value.slice(startIndex, endIndex)
    resultScrollbarRef.value.setScrollTop(0)
}
const onSearch = async () => {
    if (searchMessage.value == '') {
        ElMessage.error('搜索内容不能为空')
        return
    }
    // console.log('on search', searchMessage.value)
    var res = { // 访问请求
        data: {
            homeResultData: homeResultData, // 搜索结果
            homeResultData_AI: homeResultData_AI // AI返回结果
        }
    }
    // console.log('res', res)
    resultData.value = res.data.homeResultData
    onChangePage(1)
    currentPage.value = 1

    // console.log('AI', homeResultData_AI)
}

const chatBoxRef = ref(null)
</script>

<template>
    <!-- 机器人按钮 -->
    <div class="robotButton" @click="chatBoxRef.style.right = '1%'">
        <img style="height: 100%;" src="/public/img/chatRobot.png" alt="chatRobot">
    </div>
    <!-- 机器人聊天窗口 -->
    <div class="chatBox" ref="chatBoxRef">
        <div style="display: flex; height: 5%;">
            <div style="font-size: 120%; font-weight: 700;">AI智达</div>
            <div style="flex-grow: 1;"></div>
            <el-button circle @click="chatBoxRef.style.right = '-22%'">
                <el-icon>
                    <ArrowRightBold />
                </el-icon>
            </el-button>
        </div>
        <div style="height: 80%;">main</div>
        <div style="height: 10%;">footer</div>
    </div>



    <div style="height: 44%; overflow: hidden;">
        <img src="/public/img/background/homeBackground.png" style="width: 100%; margin-top: -16%" alt="哈尔滨理工大学">
    </div>
    <div style="text-align: center; margin-top: 1%;">
        <el-input size="large" v-model="searchMessage" style="max-width: 600px" placeholder="输入您要搜索的内容"
            class="input-with-select" clearable @keyup.enter="onSearch">
            <template #append>
                <el-button type="primary" @click="onSearch">
                    <el-icon>
                        <Search />
                    </el-icon>搜索</el-button>
            </template>
        </el-input>
        <span style="color: gray; margin-left: 10%; font-size: 120%">
            当前是：
            <span style="color: #2daaff; font-weight: 800;">哈尔滨理工大学</span>
        </span>
    </div>
    <div class="searchBox">
        <!-- 搜索结果 -->
        <div v-show="resultData.length != 0" class="result">
            <el-scrollbar ref="resultScrollbarRef" height="95%">
                <div v-for="item in showingResultData" :key="item">
                    <!-- {{ item }} -->
                    <el-card style="max-width: 100%; margin-bottom: 2%;" shadow="hover">
                        <template #header>
                            <div style="display: flex;">
                                <a :href="item.href" target="_blank" style="width: 90%;">
                                    <el-text style="font-size: 150%; width: 100%;" truncated><span
                                            v-html="item.title"></span></el-text>
                                </a>
                                <div style="flex-grow: 1;"></div>
                                <div style="color: gray; font-weight: 800;">{{ item.time }}</div>
                            </div>
                        </template>
                        <el-text line-clamp="2"><span v-html="item.description"
                                style="font-size: 120%;"></span></el-text>
                    </el-card>
                </div>
            </el-scrollbar>

            <div style="text-align: center; position: absolute; bottom: 0; width: 100%">
                <div style="display: inline-block;">
                    <el-pagination ref="pageRef" background layout="prev, pager, next"
                        :page-size="numShowingDataOnOnePage" :total="resultData.length" @change="onChangePage"
                        :hide-on-single-page="resultData.length == 0" v-model:current-page="currentPage" />
                </div>
                <span style="margin-left: 2%; color: gray">搜索结果：<span>{{ resultData.length }}</span></span>
            </div>
        </div>
    </div>
</template>
<style scoped>
.searchBox {
    /* border: 2px red solid; */
    text-align: center;
    padding: 0.3%;
    /* max-height: 580px; */
}

.result {
    /* border: 2px green solid; */
    display: inline-block;
    position: relative;
    width: 55%;
    height: 600px;
    padding: 1%;
    text-align: left;
}

.resultAI {
    border: 1px blue solid;
    display: inline-block;
    width: 35%;
    margin-right: -10px;
}

.robotButton {
    position: fixed;
    bottom: 10%;
    right: 5%;
    display: inline-block;
    border: 1px gray solid;
    /* box-shadow: 1px 1px 1px; */
    border-radius: 100px;
    overflow: hidden;
    width: 100px;
    height: 100px;
}

.chatBox {
    position: fixed;
    display: flex;
    flex-direction: column;
    width: 20%;
    height: 88%;
    border: 2px rgb(208, 208, 208) solid;
    border-radius: 15px;
    background: rgba(234, 234, 234, 0.8);
    top: 8%;
    right: -22%;
    padding: 10px;
    transition: all 0.3s;
}
</style>