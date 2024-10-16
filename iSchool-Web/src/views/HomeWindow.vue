<script setup>
import { ElMessage } from 'element-plus'
import { ref, nextTick, onMounted, computed } from 'vue'
import { MdPreview } from 'md-editor-v3'
// import { homeResultData, homeResultData_AI } from '@/assets/testData.json' // 测试数据引用
// import { sleep } from '@/utils/time'
import { compTime } from '@/utils/time'
import { aiSearch, aiSearchRes, chat, searchAnnouncement } from '@/api/home'
import { useUserInfoerStore } from '@/stores/userInfoer'
import { orderArray } from '@/utils/order'

const userInfoerStore = useUserInfoerStore()

const numShowingDataOnOnePage = 10 // 单页展示的查询结果数量

const searchMessage = ref('') // 搜索内容
const searchingKeyword = ref('') // 当前正在搜索的关键字
const startDate = ref(null) // 开始日期
const endDate = ref(null) // 结束日期
const searchingStartDate = ref('') // 正在搜索的开始日期
const searchingEndDate = ref('') // 正在搜索的结束日期
const resultData = ref({
  // 搜索结果
  items: [],
  counts: 0,
  pageNum: 1,
  pageSize: numShowingDataOnOnePage
})
const orderBy = ref(0) // 0为默认排序，1为时间升序，2为时间倒序
const orderContext = computed(() => {
  // 排序解释文本
  if (orderBy.value === 0) return '默认排序'
  else if (orderBy.value === 1) return '时间升序'
  else if (orderBy.value === 2) return '时间倒序'
  return 'error'
})
const orderImgUrl = computed(() => {
  // 排序图标
  if (orderBy.value === 0) return '/public/img/defaultOrder.png'
  else if (orderBy.value === 1) return '/public/img/timeAsc.png'
  else if (orderBy.value === 2) return '/public/img/timeDesc.png'
  return 'error'
})
const showingResultData = computed(() => {
  // 显示的搜索结果（排序后的数组）
  if (orderBy.value === 0) return resultData.value.items
  else if (orderBy.value === 1)
    return orderArray(
      resultData.value.items,
      (item) => {
        return item.pubTime
      },
      compTime,
      true
    )
  else if (orderBy.value === 2)
    return orderArray(
      resultData.value.items,
      (item) => {
        return item.pubTime
      },
      compTime
    )
  ElMessage.error('error')
  return []
})
const currentPage = ref(1) // 分页框显示的当前页码
const isLoading = ref(false) // 是否在加载中
const onChangePage = async () => {
  // 切换页面
  isLoading.value = true
  try {
    var res = await searchAnnouncement(
      searchingKeyword.value,
      currentPage.value,
      numShowingDataOnOnePage,
      searchingStartDate.value,
      searchingEndDate.value
    )
    // await sleep(1000) // test
    // var res = { data: homeResultData }
  } catch {
    isLoading.value = false
    return
  }
  isLoading.value = false
  resultData.value = res.data
  resultData.value.counts = Number(resultData.value.counts)
  resultData.value.pageNum = Number(resultData.value.pageNum)
  resultData.value.pageSize = Number(resultData.value.pageSize)
  orderBy.value = 0
}
const tempSearchKeyWord = ref('')
const onSearch = async () => {
  // 搜索信息
  if (searchMessage.value == '') {
    ElMessage.error({ message: '搜索内容不能为空', grouping: true })
    return
  }

  if (
    startDate.value != null &&
    endDate.value != null &&
    compTime(startDate.value, endDate.value)
  ) {
    ElMessage.error({ message: '结束日期不能在开始日期之前', grouping: true })
    return
  }
  if (isChatting.value) chatPlaceholder.value = searchMessage.value
  searchingKeyword.value = searchMessage.value
  searchingStartDate.value = startDate.value
  searchingEndDate.value = endDate.value
  isLoading.value = true
  try {
    var res = await searchAnnouncement(
      searchMessage.value,
      1,
      numShowingDataOnOnePage,
      startDate.value,
      endDate.value
    )
    await aiSearch(searchMessage.value)
    // await sleep(1000) // test
    // var res = { data: homeResultData }
  } catch {
    isLoading.value = false
    return
  }
  isLoading.value = false
  resultData.value = res.data
  resultData.value.counts = Number(resultData.value.counts)
  resultData.value.pageNum = Number(resultData.value.pageNum)
  resultData.value.pageSize = Number(resultData.value.pageSize)
  orderBy.value = 0

  currentPage.value = 1
  if (isFirstTimeClick.value)
    // 若还没有点击过机器人按钮
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
const isFirstTimeClick = ref(true) // 是否第一次点击机器人按钮
const onClickChatButton = async () => {
  // 点击机器人按钮
  if (isFirstTimeClick.value) {
    // 第一次点击机器人按钮，获取AI搜索建议
    var res = await aiSearchRes(searchingKeyword.value)
    tempSearchKeyWord.value = searchingKeyword.value
    // var res = { data: '你好test' } // test
    isFirstTimeClick.value = false
    chatData.value.push({
      sender: 'user',
      message: searchingKeyword.value
    })
    chatData.value.push({
      sender: 'ai',
      message: res.data
    })
    chatSmallWindowRef.value.style.top = '-100%' // 弹出聊天小窗口
  } else if (searchingKeyword.value != tempSearchKeyWord.value) {
    // 用户重新点击了搜索按钮
    const res = await aiSearchRes(searchingKeyword.value)
    chatData.value = []
    chatData.value.push({
      sender: 'user',
      message: searchingKeyword.value
    })
    chatData.value.push({
      sender: 'ai',
      message: res.data
    })
  }
  setRobatButtonStatus(2) // 机器人按钮完整出现
  chatBoxRef.value.style.right = '1%'
  chatScrollbarRef.value.setScrollTop(chatScrollbarRef.value.wrapRef.scrollHeight)
}

const toChat = () => {
  // 点击 进一步聊聊
  chatSmallWindowRef.value.style.top = '-5%' // 收起小窗口
  isChatting.value = true
}

// 聊天信息为空时点击发送，会发送悬浮信息，若悬浮信息也为空，会提示“不能发送空白信息”
const onSendMessage = async () => {
  // 发送聊天信息
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
  nextTick(() => {
    // 回到底部
    chatScrollbarRef.value.setScrollTop(chatScrollbarRef.value.wrapRef.scrollHeight)
  })
  // 清空聊天框信息
  chatMessage.value = ''
  chatPlaceholder.value = ''

  isResulting.value = true
  try {
    let res = await chat(message)
    // let res = { data: "test：你好" } // test
    chatData.value.push({
      sender: 'ai',
      message: res.data
    })
    nextTick(() => {
      // 回到底部
      chatScrollbarRef.value.setScrollTop(chatScrollbarRef.value.wrapRef.scrollHeight)
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
  // 机器人按钮状态转换函数
  if (status == 0) robotButtonRef.value.style.right = '-120px'
  if (status == 1) robotButtonRef.value.style.right = '-25px'
  if (status == 2) robotButtonRef.value.style.right = '55px'
}

const inputRef = ref(null)
onMounted(() => {
  inputRef.value.focus()
  // 初始搜索
  searchMessage.value = '全国大学生数学建模'
  startDate.value = '2020-10-01'
  endDate.value = '2024-10-01'
  onSearch()
})
</script>

<template>
  <!-- 机器人按钮 -->
  <div ref="robotButtonRef" class="robotButton" @click="onClickChatButton">
    <el-tooltip style="height: 100%" effect="dark" content="AI智达" placement="left">
      <img style="height: 100%" src="/public/img/chatRobot.png" />
    </el-tooltip>
  </div>
  <!-- 机器人聊天窗口 -->
  <div class="chatBox" ref="chatBoxRef">
    <!-- header -->
    <div style="display: flex; height: 5%">
      <div style="font-size: 120%; font-weight: 700">AI智达</div>
      <div style="flex-grow: 1"></div>
      <el-button circle @click="chatBoxRef.style.right = '-40%'">
        <el-icon>
          <ArrowRightBold />
        </el-icon>
      </el-button>
    </div>
    <!-- main -->
    <div style="height: 84.5%; padding: 10px">
      <el-scrollbar ref="chatScrollbarRef" max-height="100%">
        <div
          v-for="(item, index) in chatData"
          :key="index"
          :style="{
            textAlign: item.sender === 'ai' ? 'left' : 'right',
            marginBottom: '15px'
          }"
        >
          <div
            class="chatBubble"
            :style="{
              backgroundColor: item.sender === 'ai' ? '#f1f1f1' : '',
              backgroundImage:
                item.sender === 'user' ? 'linear-gradient(90deg, #5ba0e7, #2c67c0)' : '',
              color: item.sender === 'ai' ? '#333' : '#fff',
              maxWidth: '70%',
              padding: '10px 15px',
              borderRadius: '20px',
              boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)',
              display: 'inline-block'
            }"
          >
            <MdPreview v-if="item.sender === 'ai'" v-model="item.message" />
            <div v-else>{{ item.message }}</div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div style="flex-grow: 1"></div>
    <!-- footer -->
    <div style="position: relative; display: flex; height: 5%">
      <!-- 聊天小窗口 -->
      <div ref="chatSmallWindowRef" class="chatSmallWindow">
        <div style="display: flex; color: gray; font-size: 14px">
          <div>没有满意的信息？试试</div>
          <div style="flex-grow: 1"></div>
        </div>
        <div style="display: flex; margin-top: -1%">
          <el-link @click="toChat" target="_blank" type="primary">进一步聊聊</el-link>
          <div style="flex-grow: 1; text-align: center">|</div>
          <el-link href="" target="_blank" type="primary">站外搜索</el-link>
          <div style="flex-grow: 1; text-align: center">|</div>
          <el-link href="/main/comment" target="_blank" type="primary">大众点评</el-link>
          <div style="flex-grow: 9"></div>
        </div>
      </div>

      <el-input
        v-model="chatMessage"
        size="large"
        style="width: 80%"
        :placeholder="chatPlaceholder"
        @keyup.enter="onSendMessage"
        :disabled="!isChatting || isResulting"
      />

      <div style="flex-grow: 1"></div>
      <el-button
        style="z-index: 1"
        type="primary"
        size="large"
        @click="onSendMessage"
        :disabled="!isChatting"
        :loading="isResulting"
        ><span v-show="!isResulting">发送</span></el-button
      >
    </div>
  </div>

  <div style="height: 50px"></div>
  <div style="margin-top: 10px; text-align: center">
    <div style="display: inline-block">
      <el-input
        style="width: 700px; border-radius: 10px"
        ref="inputRef"
        size="large"
        v-model="searchMessage"
        placeholder="输入您要搜索的内容"
        class="input-with-select"
        clearable
        @keyup.enter="onSearch"
      >
        <template #append>
          <el-tooltip effect="dark" content="搜索" placement="bottom">
            <el-button @click="onSearch">
              <el-icon> <Search /> </el-icon
            ></el-button>
          </el-tooltip>
        </template>
      </el-input>
      <span style="color: gray; margin-left: 100px; font-size: 20px">
        当前是：
        <span style="color: #2daaff; font-weight: 800">
          {{ userInfoerStore.userInfo.school }}
        </span>
      </span>
      <div style="text-align: left; margin-top: 10px">
        <el-date-picker
          v-model="startDate"
          type="date"
          placeholder="开始日期"
          size="large"
          value-format="YYYY-MM-DD"
        />
        <span style="margin: 0 10px">—</span>
        <el-date-picker
          v-model="endDate"
          type="date"
          placeholder="结束日期"
          size="large"
          value-format="YYYY-MM-DD"
        />
        <span style="margin-left: 50px">
          <el-tooltip effect="dark" :content="orderContext" placement="right">
            <el-button
              @click="orderBy = (orderBy + 1) % 3"
              circle
              :disabled="searchingKeyword === ''"
            >
              <img style="height: 20px" :src="orderImgUrl" />
            </el-button>
          </el-tooltip>
        </span>
      </div>
    </div>
  </div>
  <div v-loading="isLoading">
    <div class="searchBox">
      <!-- 搜索结果 -->
      <el-empty
        style="height: 600px"
        v-show="resultData.counts == 0 && searchingKeyword !== ''"
        image="/public/img/emptySearch.png"
        description="无搜索结果"
      />
      <el-empty
        style="height: 600px"
        v-show="resultData.counts == 0 && searchingKeyword === ''"
        image="/public/img/searchForWhat.png"
        description="搜点什么吧"
      />
      <div class="result" v-show="resultData.counts != 0">
        <div
          style="width: 98%; margin-bottom: 30px; border-bottom: 1px #dcdfe6 solid"
          v-for="item in showingResultData"
          :key="item"
        >
          <div>
            <a style="width: 80%" :href="`/tempView?id=${item.id}`" target="_blank">
              <el-text
                class="resultTitle"
                style="font-size: 25px; max-width: 70%; color: #4c07a2"
                truncated
                ><span v-html="item.title"></span
              ></el-text>
            </a>
          </div>
          <el-text line-clamp="3">
            <span style="font-weight: 800">{{ item.pubTime }}</span>
            <span style="margin: 0 10px">|</span>
            <span v-html="item.content" style="font-size: 20px; color: #71777d"></span>
          </el-text>
        </div>
      </div>
    </div>
    <div style="text-align: center">
      <!-- 分页器 -->
      <div style="display: inline-block">
        <el-pagination
          ref="pageRef"
          background
          layout="prev, pager, next"
          :page-size="numShowingDataOnOnePage"
          :total="resultData.counts"
          @change="onChangePage"
          :hide-on-single-page="resultData.counts == 0"
          v-model:current-page="currentPage"
        />
      </div>
      <span style="margin-left: 2%; color: gray" v-show="resultData.counts != 0"
        >搜索结果：<span>{{ resultData.counts }}</span></span
      >
    </div>
  </div>
</template>
<style scoped>
.searchBox {
  text-align: center;
  padding: 10px;
}

.result {
  display: inline-block;
  width: 1100px;
  text-align: left;
}

.resultTitle:hover {
  text-decoration: underline;
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
  width: 30%;
  height: 88%;
  border: 2px rgb(208, 208, 208) solid;
  border-radius: 15px;
  background: rgba(234, 234, 234, 0.9);
  top: 8%;
  right: -40%;
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
