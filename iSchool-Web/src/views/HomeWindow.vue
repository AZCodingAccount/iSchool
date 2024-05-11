<script setup>
import { ElMessage } from 'element-plus'
import { ref, nextTick } from 'vue'
import { MdPreview } from 'md-editor-v3'

import { homeResultData, homeResultData_AI } from '@/assets/testData.json' // 测试数据引用
import { sleep } from '@/utils/time'
import { aiSearch, aiSearchRes, chat, searchAnnouncement } from '@/api/home'

const numShowingDataOnOnePage = 10 // 单页展示的查询结果数量

const searchMessage = ref('') // 搜索内容
const searchingKeyword = ref('') // 当前正在搜索的关键字
const resultData = ref({ // 搜索结果
    items: [],
    counts: 0,
    pageNum: 1,
    pageSize: numShowingDataOnOnePage
}) // 所有查询结果
// const showingResultData = ref([]) // 正在展示的查询结果
const resultScrollbarRef = ref(null) // 滚动条绑定变量
const currentPage = ref(1) // 分页框显示的当前页码
const isLoading = ref(false) // 是否在加载中
const onChangePage = async () => { // 切换搜索页面
    isLoading.value = true
    try {
        // var res = await searchAnnouncement(searchingKeyword.value, currentPage.value, numShowingDataOnOnePage)
        await sleep(1000) // test
        var res = { // 访问请求
            data: homeResultData, // 搜索结果
        }
    }
    catch { isLoading.value = false; return; }
    isLoading.value = false
    resultData.value = res.data
    resultScrollbarRef.value.setScrollTop(0)
}
const onSearch = async () => { // 搜索信息
    if (searchMessage.value == '') {
        ElMessage.error({
            message: '搜索内容不能为空',
            grouping: true,
        })
        return
    }
    // console.log('on search', searchMessage.value)
    if (isChatting.value)
        chatPlaceholder.value = searchMessage.value
    searchingKeyword.value = searchMessage.value
    isLoading.value = true
    try {
        // var res = await searchAnnouncement(searchMessage.value, 1, numShowingDataOnOnePage)
        // await aiSearch(searchMessage.value)
        await sleep(1000) // test
        var res = { data: homeResultData }
    }
    catch { isLoading.value = false; return; }
    isLoading.value = false
    // console.log('res', res)
    resultData.value = res.data
    currentPage.value = 1

    if (isFirstTimeClick.value) // 若还没有点击过机器人按钮
        setRobatButtonStatus(1) // 让机器人探头
}

const chatBoxRef = ref(null) // 聊天窗口绑定变量
const chatSmallWindowRef = ref(null) // 聊天小窗口绑定变量
const isChatting = ref(false) // 是否在聊天状态
const chatMessage = ref('') // 聊天信息
const chatPlaceholder = ref('') // 聊天框为空时显示的信息（悬浮信息）
const chatData = ref([]) // 聊天信息
const chatScrollbarRef = ref(null) // 聊天窗口滚动条绑定变量
const isResulting = ref(false) // ai是否正在回复消息
const getAiResult = async (keyword) => { // 获取AI搜索建议
    chatData.value.push({
        sender: 'ai',
        message: ''
    })

    isResulting.value = true

    // var res = { code: 0 }
    var res = { code: 1 } // test
    await sleep(2000)
    chatData.value[chatData.value.length - 1].message = '你好啊'

    try {
        while (res.code != 1) {
            res = await aiSearchRes(keyword)
            chatData.value[chatData.value.length - 1].message = res.data
            await sleep(500) // 每隔半秒刷新一次ai返回结果
        }
    } catch { ElMessage.error('回答生成中断') }
    isResulting.value = false
    chatSmallWindowRef.value.style.top = '-100%' // 弹出聊天小窗口
}
const isFirstTimeClick = ref(true) // 是否第一次点击机器人按钮
const onClickChatButton = () => { // 点击机器人按钮
    setRobatButtonStatus(2) // 机器人按钮完整出现
    if (isFirstTimeClick.value) { // 第一次点击机器人按钮，获取AI搜索建议
        isFirstTimeClick.value = false
        chatData.value.push({
            sender: 'user',
            message: searchingKeyword.value
        })
        getAiResult(searchingKeyword.value)
    }
    chatBoxRef.value.style.right = '1%'
    chatScrollbarRef.value.setScrollTop(chatScrollbarRef.value.wrapRef.scrollHeight)
}

const toChat = () => { // 点击 进一步聊聊
    chatSmallWindowRef.value.style.top = '-5%' // 收起小窗口
    isChatting.value = true
}

// 聊天信息为空时点击发送，会发送悬浮信息，若悬浮信息也为空，会提示“不能发送空白信息”
const onSendMessage = async () => { // 发送聊天信息
    var message = ''
    if (chatMessage.value != '') {
        message = chatMessage.value
    } else if (chatPlaceholder.value != '') {
        message = chatPlaceholder.value
    } else {
        ElMessage.error({
            message: '发送信息不能为空',
            grouping: true
        })
        return
    }
    chatData.value.push({
        sender: 'user',
        message: message
    })
    nextTick(() => { // 回到底部
        chatScrollbarRef.value.setScrollTop(chatScrollbarRef.value.wrapRef.scrollHeight)
    })
    // 清空聊天框信息
    chatMessage.value = ''
    chatPlaceholder.value = ''

    isResulting.value = true
    try {
        let res = await chat(message)
        chatData.value.push({
            sender: 'ai',
            message: res.data
        })
    } catch {
        isResulting.value = false
        chatData.value.pop()
        chatPlaceholder.value = message // 当信息发送失败时，发送的信息会作为悬浮信息显示在输入框中
        return
    }
    isResulting.value = false
}

const robotButtonRef = ref(null) // 机器人按钮的绑定变量
const setRobatButtonStatus = (status) => {
    if (status == 0)
        robotButtonRef.value.style.right = '-120px'
    if (status == 1)
        robotButtonRef.value.style.right = '-25px'
    if (status == 2)
        robotButtonRef.value.style.right = '55px'
}

// onMounted(() => { // test
//     chatData.value = homeResultData_AI
// })
</script>

<template>
    <!-- 机器人按钮 -->
    <div ref="robotButtonRef" class="robotButton" @click="onClickChatButton">
        <el-tooltip style="height: 100%;" effect="dark" content="AI智达" placement="left">
            <img style="height: 100%;" src="/public/img/chatRobot.png" alt="chatRobot">
        </el-tooltip>
    </div>
    <!-- 机器人聊天窗口 -->
    <div class="chatBox" ref="chatBoxRef">
        <!-- header -->
        <div style="display: flex; height: 5%;">
            <div style="font-size: 120%; font-weight: 700;">AI智达</div>
            <div style="flex-grow: 1;"></div>
            <el-button circle @click="chatBoxRef.style.right = '-22%'">
                <el-icon>
                    <ArrowRightBold />
                </el-icon>
            </el-button>
        </div>
        <!-- main -->
        <div style="height: 84.5%;">
            <el-scrollbar ref="chatScrollbarRef" max-height="100%">
                <div v-for="item in chatData" :key="item"
                    :style="{ 'textAlign': item.sender == 'ai' ? 'left' : 'right' }">
                    <div class="chatBubble"
                        :style="{ 'backgroundColor': item.sender == 'ai' ? 'white' : '', 'backgroundImage': item.sender == 'user' ? 'linear-gradient(90deg, #5ba0e7, #2c67c0)' : '' }">
                        <MdPreview v-if="item.sender == 'ai'" v-model="item.message" />
                        <div v-else>{{ item.message }}</div>
                    </div>
                </div>
            </el-scrollbar>
        </div>
        <div style="flex-grow: 1;"></div>
        <!-- footer -->
        <div style="position: relative; display: flex; height: 5%;">
            <!-- 聊天小窗口 -->
            <div ref="chatSmallWindowRef" class="chatSmallWindow">
                <div style="display: flex; color: gray; font-size: 14px;">
                    <div>没有满意的信息？试试</div>
                    <div style="flex-grow: 1;"></div>
                    <!-- <el-button @click="chatSmallWindowRef.style.top = '-5%'" size="small" circle><el-icon>
                            <CloseBold />
                        </el-icon></el-button> -->
                </div>
                <div style="display: flex; margin-top: -1%;">
                    <el-link @click="toChat" target="_blank" type="primary">进一步聊聊</el-link>
                    <div style="flex-grow: 1; text-align: center;">|</div>
                    <el-link href="" target="_blank" type="primary">站外搜索</el-link>
                    <div style="flex-grow: 1; text-align: center;">|</div>
                    <el-link href="/main/comment" target="_blank" type="primary">大众点评</el-link>
                    <div style="flex-grow: 9;"></div>
                </div>
            </div>

            <el-input v-model="chatMessage" size="large" style="width: 80%" :placeholder="chatPlaceholder"
                @keyup.enter="onSendMessage" :disabled="!isChatting" />

            <div style="flex-grow: 1;"></div>
            <el-button style="z-index: 1;" type="primary" size="large" @click="onSendMessage" :disabled="!isChatting"
                :loading="isResulting"><span v-show="!isResulting">发送</span></el-button>
        </div>
    </div>



    <div style="height: 390px; overflow: hidden;">
        <img src="/public/img/background/homeBackground.png" style="width: 100%; margin-top: -16%" alt="哈尔滨理工大学">
    </div>
    <div style="text-align: center; margin-top: 1%;">
        <el-input size="large" v-model="searchMessage" style="max-width: 600px" placeholder="输入您要搜索的内容"
            class="input-with-select" clearable @keyup.enter="onSearch">
            <template #append>
                <el-button @click="onSearch">
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
        <div v-loading="isLoading">
            <el-empty v-show="resultData.counts == 0" image="/public/img/empty_search.png" description="无搜索结果" />
            <div v-show="resultData.counts != 0" class="result">
                <el-scrollbar ref="resultScrollbarRef" height="95%">
                    <!-- {{ item }} -->
                    <el-card v-for="item in resultData.items" :key="item" style="width: 98%; margin-bottom: 2%;"
                        shadow="hover">
                        <template #header>
                            <div style="display: flex;">
                                <a :href="item.url" target="_blank" style="width: 90%;">
                                    <el-text style="font-size: 150%; width: 100%;" truncated><span
                                            v-html="item.title"></span></el-text>
                                </a>
                                <div style="flex-grow: 1;"></div>
                                <div style="color: gray; font-weight: 800;">{{ item.pubTime }}</div>
                            </div>
                        </template>
                        <el-text line-clamp="2"><span v-html="item.content" style="font-size: 120%;"></span></el-text>
                    </el-card>
                </el-scrollbar>

                <div style="text-align: center; position: absolute; bottom: 0; width: 100%">
                    <div style="display: inline-block;">
                        <el-pagination ref="pageRef" background layout="prev, pager, next"
                            :page-size="numShowingDataOnOnePage" :total="resultData.counts" @change="onChangePage"
                            :hide-on-single-page="resultData.counts == 0" v-model:current-page="currentPage" />
                    </div>
                    <span style="margin-left: 2%; color: gray">搜索结果：<span>{{ resultData.counts }}</span></span>
                </div>
            </div>
        </div>

    </div>
</template>
<style scoped>
.searchBox {
    text-align: center;
    padding: 0.3%;
}

.result {
    display: inline-block;
    position: relative;
    width: 55%;
    height: 590px;
    padding: 1%;
    text-align: left;
}

.robotButton {
    position: fixed;
    bottom: 160px;
    right: -120px;
    display: inline-block;
    border: 1px gray solid;
    border-radius: 100px;
    overflow: hidden;
    width: 100px;
    height: 100px;
    z-index: 2001;
    transition: all 0.5s;
}

.chatBox {
    position: fixed;
    display: flex;
    flex-direction: column;
    width: 20%;
    height: 88%;
    border: 2px rgb(208, 208, 208) solid;
    border-radius: 15px;
    background: rgba(234, 234, 234, 0.9);
    top: 8%;
    right: -22%;
    padding: 10px;
    transition: all 0.3s;
    z-index: 2002;
}

.chatSmallWindow {
    position: absolute;
    width: 100%;
    top: -5%;
    transition: all 0.5s;
}

.chatBubble {
    display: inline-block;
    max-width: 80%;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
    text-align: left;
}
</style>