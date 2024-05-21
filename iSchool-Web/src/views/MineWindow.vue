<script setup>
import { useUserInfoerStore } from '@/stores/userInfoer'
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// import { myMessage1 } from '@/assets/testData.json' // 测试数据引用
// import { sleep } from '@/utils/time'
import { deleteUser_1, getMessageList, readMessage, updateUserInfo_1, upload } from '@/api/user'
import router from '@/router'
// import { sleep } from '@/utils/time'

const userInfoerStore = useUserInfoerStore()
const formInfo = ref({}) // 个人信息的表单信息

const isModifying = ref(false) // 是否正在修改

const onEdit = () => { // 点击编辑
    // console.log('on edit')
    for (let key in userInfoerStore.userInfo) {
        if (key != 'password') {
            formInfo.value[key] = userInfoerStore.userInfo[key]
        }
    }
    isModifying.value = true
}

// 修改
const onModify = async () => { // 确认修改
    // for (let key in formInfo.value) {
    //     if (formInfo.value[key] == '') {
    //         ElMessage.error({
    //             message: '修改信息不能为空',
    //             grouping: true
    //         })
    //         return
    //     }
    // }
    // 发送修改请求
    // delete formInfo.value.password // 删除密码字段
    await updateUserInfo_1(formInfo.value)
    userInfoerStore.updateUserInfo(formInfo.value)
    isModifying.value = false
    ElMessage.success('个人信息修改成功')
}

const onAvatar = async (rawFile) => { // 上传头像
    const formData = new FormData()
    formData.append('file', rawFile)
    if (rawFile.size / 1024 / 1024 > 10) {
        ElMessage.error('文件过大')
        return;
    }
    const res = await upload(formData)
    userInfoerStore.updateUserInfo({ userAvatar: res.data })
}

const showingExitDialog = ref(false) // 是否展示退出对话框
const onExit = () => { // 确认退出
    userInfoerStore.updateUserInfo({ token: '' })
    showingExitDialog.value = false
    router.push('/login')
}

const showingLogoutDialog = ref(false) // 是否展示注销对话框
const onLogout = async () => { // 确认注销
    await deleteUser_1()
    userInfoerStore.cleanUserInfo()
    showingLogoutDialog.value = false
    router.push('/login')
    ElMessage.error('账号注销成功')
}

const myMessage = ref([]) // 我的信息
const isLoading_myMessage = ref(false) // 是否在加载我的信息
const initMyMessage = async () => { // 初始化我的信息
    isLoading_myMessage.value = true
    try {
        var res = await getMessageList()
        // await sleep(1000) // test
        // var res = { data: myMessage1 }
    } catch { isLoading_myMessage.value = false; return; }
    myMessage.value = res.data
    // console.log('myMessage', myMessage.value)
    isLoading_myMessage.value = false
}
onMounted(() => {
    initMyMessage()
})

const onSelectMessage = async (messageObj) => { // 选择了某个信息
    await readMessage({ messageId: messageObj.id }) // 标为已读
    myMessage.value = myMessage.value.filter((item) => { return item.id != messageObj.id })
    // router.push('/main/comment?objId=' + messageObj.objId)
}

</script>

<template>
    <div style="display: inline-block; position: fixed; z-index: 1; right: 3%; bottom: 5%; line-height: 60px;">
        <div><el-button type="info" size="large" round @click="showingExitDialog = true">退出</el-button></div>
        <div><el-button type="danger" size="large" round @click="showingLogoutDialog = true">注销</el-button></div>
    </div>
    <!-- 退出对话框 -->
    <el-dialog v-model="showingExitDialog" title="退出" width="500" center>
        <div style="text-align: center; font-size: 30px; padding: 10px;">您确定要退出登录吗？</div>
        <template #footer>
            <div>
                <el-button @click="showingExitDialog = false" size="large">取消</el-button>
                <el-button type="primary" @click="onExit" size="large">确定</el-button>
            </div>
        </template>
    </el-dialog>
    <!-- 注销对话框 -->
    <el-dialog v-model="showingLogoutDialog" title="注销" width="500" center>
        <div style="text-align: center; font-size: 30px; padding: 10px;">您确定要注销账号吗？</div>
        <template #footer>
            <div>
                <el-button @click="showingLogoutDialog = false" size="large">取消</el-button>
                <el-button type="danger" @click="onLogout" size="large">确定</el-button>
            </div>
        </template>
    </el-dialog>


    <div style="text-align: center;">
        <div style="display: inline-block; width: 80%; margin-top: 4%;">
            <div style="display: flex;">
                <div style="width: 30%;">
                    <el-tooltip content="<strong>更改头像</strong>" placement="bottom" effect="dark" raw-content>
                        <el-upload :show-file-list="false" :before-upload="onAvatar" list-type="picture"
                            accept="image/*">
                            <div class="avatar">
                                <img style="height: 100%;" :src="userInfoerStore.userInfo.userAvatar">
                            </div>
                        </el-upload>
                    </el-tooltip>
                    <div style="display: flex; padding: 20px;">
                        <div style="text-align: left; color: #8e959c;">
                            <div style="font-size: 30px;">{{ userInfoerStore.userInfo.username }}</div>
                            <div style="font-size: 25px;">{{ userInfoerStore.userInfo.nickname }}</div>
                        </div>
                        <div style="flex-grow: 1;"></div>
                        <div style="display: flex; font-size: 17px; color: gray; height: 70%;">
                            <div style="border-right: 1px gray solid; padding: 10px;">
                                <div>{{ userInfoerStore.userInfo.totalLikes }}</div>
                                <div>获赞</div>
                            </div>
                            <div style="border-left: 1px gray solid; padding: 10px;">
                                <div>{{ userInfoerStore.userInfo.totalComments }}</div>
                                <div>回复</div>
                            </div>
                        </div>
                    </div>
                    <!-- 个人信息 -->
                    <el-card>
                        <template #header>
                            <div style="display: flex;">
                                <div style="font-size: 20px; font-weight: 600;">个人信息</div>
                                <div style="flex-grow: 1;"></div>
                                <el-button v-show="!isModifying" type="info" size="large" plain
                                    @click="onEdit">编辑</el-button>
                                <el-button v-show="isModifying" type="primary" size="large"
                                    @click="onModify">修改</el-button>
                                <el-button v-show="isModifying" type="danger" size="large" plain
                                    @click="isModifying = false">取消</el-button>
                            </div>
                        </template>
                        <el-form style="max-width: 500px;" size="small" label-position="left" label-width="80px"
                            :model="formInfo">
                            <el-form-item label="用户名">
                                <div>{{ userInfoerStore.userInfo.username }}</div>
                            </el-form-item>
                            <el-form-item label="昵称">
                                <div v-show="!isModifying">{{ userInfoerStore.userInfo.nickname }}</div>
                                <el-input v-show="isModifying" v-model="formInfo.nickname" />
                            </el-form-item>
                            <el-form-item label="性别">
                                <div v-show="!isModifying">{{ userInfoerStore.userInfo.gender }}</div>
                                <el-radio-group v-show="isModifying" v-model="formInfo.gender" size="small">
                                    <el-radio-button label="男" value="男" />
                                    <el-radio-button label="女" value="女" />
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item label="年龄">
                                <div v-show="!isModifying">{{ userInfoerStore.userInfo.age }}</div>
                                <el-input-number v-show="isModifying" v-model="formInfo.age" size="small" :min="1"
                                    :max="100" />
                            </el-form-item>
                            <el-form-item label="邮箱">
                                <div v-show="!isModifying">{{ userInfoerStore.userInfo.email }}</div>
                                <el-input v-show="isModifying" v-model="formInfo.email" />
                            </el-form-item>
                        </el-form>
                    </el-card>
                </div>
                <div style="flex-grow: 1;"></div>
                <div style="width: 68%;">
                    <!-- 消息 -->
                    <el-card style="height: 100%;">
                        <template #header>
                            <div style="display: flex;">
                                <div style="font-size: 20px; font-weight: 700;">我的消息</div>
                                <div style="flex-grow: 1;"></div>
                                <el-tag v-show="userInfoerStore.userInfo.unReadCommentsCount != 0" type="danger"
                                    effect="dark">{{ userInfoerStore.userInfo.unReadCommentsCount
                                    }}</el-tag>
                            </div>
                        </template>
                        <div v-loading="isLoading_myMessage">
                            <el-scrollbar height="690px">
                                <el-empty v-show="myMessage.length == 0" image="/public/img/empty_message.png"
                                    description="没有消息" />
                                <el-card v-for="item in myMessage" :key="item"
                                    style="max-width: 100%; margin-bottom: 2%; text-align: left;" shadow="hover">
                                    <template #header>
                                        <div style="display: flex; line-height: 20px;">
                                            <div style="font-size: 25px; font-weight: 700;">{{ item.userNickname }}
                                            </div>
                                            <div style="color: gray; margin-left: 20px;">回复了我的评论</div>
                                            <div style="flex-grow: 1;"></div>
                                            <el-tag size="large">{{ item.objName }}</el-tag>
                                        </div>
                                    </template>
                                    <el-text line-clamp="2" style="width: 100%; font-size: 20px;">{{
                                        item.content
                                        }}</el-text>
                                    <div style="display: flex; margin-top: 10px;">
                                        <div style="color: gray;">{{ item.pubTime }}</div>
                                        <el-link style="color: #409eff; margin-left: 10px;"
                                            :href="'/main/comment?objId=' + item.objId"
                                            @click="onSelectMessage(item)">去查看</el-link>
                                    </div>
                                </el-card>
                            </el-scrollbar>
                        </div>
                    </el-card>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped>
.background {
    position: fixed;
    top: -14%;
    left: 0;
    width: 100%;
    z-index: -1;
}

.avatar {
    display: inline-block;
    border: 3px rgb(202, 202, 202) solid;
    width: 350px;
    height: 350px;
    border-radius: 350px;
    overflow: hidden;
}
</style>