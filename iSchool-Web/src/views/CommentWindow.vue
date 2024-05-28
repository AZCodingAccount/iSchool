<script setup>
import { addComment, addCommentLikes_1, addCommentObj, addComment_1, decreaseCommentLikes, decreaseCommentLikes_1, getCommentsList, getCommentsList_1, score, searchCommentObj } from '@/api/comment';
import { ElMessage } from 'element-plus'
import { ref, computed, onMounted } from 'vue'

// import { commentObj, comment1, comment2 } from '@/assets/testData.json' // 测试数据引用
// import { sleep } from '@/utils/time'
import router from '@/router'
import { readMessage } from '@/api/user';

const commentObjData = ref([]) // 点评对象数据
const keyword = ref('') // 搜索内容
const type = ref('') // 搜索类型
const typeColor = (typeName) => { // 根据标签名决定标签颜色
    if (typeName == '课程') return 'danger'
    if (typeName == '老师') return 'warning'
    if (typeName == '竞赛') return 'primary'
    if (typeName == '部门') return 'success'
}
const isLoading = ref(false) // 是否在加载中
const resultScrollbarRef = ref(null) // 滚动条绑定变量
const onSearch = async () => { // 搜索
    toStatus(1)
    selectedCommentObj.value = { id: -1 }
    isLoading.value = true
    try {
        var res = await searchCommentObj(keyword.value, type.value)
        // await sleep(1000) // test
        // var res = { data: commentObj }
    }
    catch { // 请求错误时关闭加载画面
        isLoading.value = false
        return
    }
    commentObjData.value = res.data
    for (let ind in commentObjData.value)
        commentObjData.value[ind].userScore = commentObjData.value[ind].userScore / 2
    isLoading.value = false
    resultScrollbarRef.value.setScrollTop(0)
}
onMounted(() => {
    onSearch().then(async () => { // 最开始展示全部的搜索结果
        if (router.currentRoute.value.query.objId != undefined) {
            let objId = router.currentRoute.value.query.objId
            let lit_commentObj = commentObjData.value.filter((item) => { return item.id == objId })
            if (lit_commentObj.length !== 1) {
                ElMessage.error('点评对象定位失败')
                return
            }
            onSelectCommentObj(lit_commentObj[0])
            resultScrollbarRef.value.setScrollTop(commentObjData.value.indexOf(lit_commentObj[0]) * 175)
            await readMessage({ messageId: objId }) // 标为已读
        }
    })
})

const showingAddCommentDialog = ref(false) // 是否显示添加点评对象对话框
const formAddCommentObj = ref({ // 添加点评对象信息表单
    name: '',
    type: ''
})
const onAddCommentObj = async () => { // 添加点评对象
    if (formAddCommentObj.value.name == '' || formAddCommentObj.value.type == '') {
        ElMessage.error({
            message: '添加点评对象的名称或类别不能为空',
            grouping: true
        })
        return
    }
    await addCommentObj(formAddCommentObj.value)
    ElMessage.success('点评对象添加成功')
    showingAddCommentDialog.value = false
    formAddCommentObj.value.name = ''
    formAddCommentObj.value.type = ''
}
const commentObj_like = async (commentObj) => { // 给点评对象点赞
    await score({
        commentObjId: commentObj.id,
        score: commentObj.userScore * 2
    })
}

const selectedCommentObj = ref({ id: -1 }) // 当前选择的点评对象
const isLoading_comment1 = ref(false) // 是否在加载一级评论数据
const onSelectCommentObj = async (commentObj) => { // 选择点评对象
    selectedCommentObj.value = commentObj
    isLoading_comment1.value = true
    input1Ref.value.focus()
    toStatus(2)
    try {
        var res = await getCommentsList_1(selectedCommentObj.value.id)
        // await sleep(1000) // test
        // var res = { data: comment1 }
    }
    catch { isLoading_comment1.value = false; return; }
    commentData1.value = res.data
    isLoading_comment1.value = false
    comment1ScrollbarRef.value.setScrollTop(0)
}


const commentSendTo = ref(0) // 一级评论区输入框内容的发送对象，0则评论点评对象，1则是回复某个一级评论，2则是回复某个二级评论
const commentData1 = ref([]) // 一级评论数据
const messageComment = ref('') // 一级评论信息
const input1Ref = ref(null) // 一级评论输入框绑定变量
const inputPlaceholder = computed(() => { // 一级评论输入框悬浮文字
    if (commentSendTo.value == 0) return '点评 ' + selectedCommentObj.value.name + '：'
    if (commentSendTo.value == 1) return '回复 ' + selectedComment.value.username + '：'
    return '回复 ' + selectedComment.value.replyUsername + '：'
})
const likeImgUrl = (isLike) => { // 点赞的图标
    if (isLike) return '/public/img/like_active.png'
    else return '/public/img/like.png'
}
const onLikeComment1 = async (commentObj) => { // 给一级评论点赞
    if (commentObj.liked) { // 取消点赞
        await decreaseCommentLikes_1(commentObj.id)
        // await sleep(1000) // test
        commentObj.liked = false
        commentObj.likes--
        return
    }
    await addCommentLikes_1(commentObj.id)
    // await sleep(1000) // test
    commentObj.liked = true
    commentObj.likes++
}
const onLikeComment2 = async (commentObj) => { // 给二级评论点赞
    if (commentObj.liked) { // 取消点赞
        await decreaseCommentLikes(commentObj.id)
        // await sleep(1000) // test
        commentObj.liked = false
        commentObj.likes -= 1
        return
    }
    await addCommentLikes_1(commentObj.id)
    // await sleep(1000) // test
    commentObj.liked = true
    commentObj.likes += 1
}
const selectedComment1 = ref({ id: -1 }) // 当前选择的一级评论
const selectedComment = ref({ id: -1 }) // 当前选择的评论
const comment1ScrollbarRef = ref(null) // 一级评论滚动窗口绑定变量
const comment2ScrollbarRef = ref(null) // 一级评论滚动窗口绑定变量
const isLoading_comment2 = ref(false) // 是否在加载二级评论数据
const commentData2 = ref([]) // 所有二级评论数据
const InputDisabled = ref(false) // 是否禁用输入框
const onSelectComment = async (commentObj, commentLevel) => { // 选择或取消选择评论
    if (selectedComment.value.id == commentObj.id) { // 取消对评论的选择
        selectedComment.value = { id: -1 }
        if (commentLevel == 1) { // 若取消选择的是一级评论
            selectedComment1.value = { id: -1 }
            toStatus(2)
        }
        commentSendTo.value = 0
    } else {
        selectedComment.value = commentObj
        commentSendTo.value = commentLevel
        input1Ref.value.focus()
        toStatus(3)
        // 若发出当前评论的用户已注销，则禁用输入框
        InputDisabled.value = (commentSendTo.value !== 0 && selectedComment.value.userId === '0')
        if (commentLevel == 1) { // 选的是一级评论
            selectedComment1.value = commentObj
            // 刷新二级评论数据
            isLoading_comment2.value = true
            try {
                var res = await getCommentsList(commentObj.id)
                // await sleep(1000) // test
                // var res = { data: comment2 }
            }
            catch { isLoading_comment2.value = false; return; }
            commentData2.value = res.data
            isLoading_comment2.value = false
            comment2ScrollbarRef.value.setScrollTop(0)
        }
    }
}
const onSendComment = async () => { // 一级评论输入框发送信息
    if (messageComment.value == '') {
        ElMessage.error({
            message: '评论信息不能为空',
            grouping: true
        })
    }
    if (commentSendTo.value == 0) { // 评论点评对象
        await addComment_1({
            objId: selectedCommentObj.value.id,
            content: messageComment.value
        })
        // 刷新一级评论信息
        isLoading_comment1.value = true
        try {
            var res1 = await getCommentsList_1(selectedCommentObj.value.id)
            // await sleep(1000) // test
            // var res1 = { data: comment1 }
        }
        catch { isLoading_comment1.value = false; return; }
        commentData1.value = res1.data
        isLoading_comment1.value = false
        // comment1ScrollbarRef.value.setScrollTop(0)
        selectedCommentObj.value.commentCount += 1
    } else { // 回复某一级或二级评论
        await addComment({
            objId: selectedCommentObj.value.id,
            commentId: selectedComment.value.id,
            parentCommentId: selectedComment1.value.id,
            replyContent: messageComment.value,
            replyUserId: selectedComment.value.userId
        })
        selectedComment1.value.replyCount += 1
        // 刷新二级评论数据
        isLoading_comment2.value = true
        try {
            var res2 = await getCommentsList(selectedComment1.value.id)
            // await sleep(1000) // test
            // var res2 = { data: comment2 }
        }
        catch { isLoading_comment2.value = false; return; }
        commentData2.value = res2.data
        isLoading_comment2.value = false
    }
    messageComment.value = ''
}

const window1 = ref(null)
const window2 = ref(null)
const window3 = ref(null)
const toStatus = (status) => {
    if (status == 1) {
        window1.value.style.left = '31%'
        window2.value.style.left = '31%'
        window3.value.style.left = '31%'
    } else if (status == 2) {
        window1.value.style.left = '9%'
        window2.value.style.left = '49.2%'
        window3.value.style.left = '49.2%'
    } else if (status == 3) {
        window1.value.style.left = '0%'
        window2.value.style.left = '40.1%'
        window3.value.style.left = '70.1%'
    }
}
</script>

<template>
    <!-- 添加点评对象对话框 -->
    <el-dialog v-model="showingAddCommentDialog" title="添加点评对象" width="500" center>
        <el-form :model="formAddCommentObj">
            <el-form-item label="点评对象名称">
                <el-input v-model="formAddCommentObj.name" placeholder="请输入点评对象名称" clearable />
            </el-form-item>
            <el-form-item label="点评对象类型">
                <el-select v-model="formAddCommentObj.type" placeholder="请选择点评对象类型">
                    <el-option label="课程" value="课程" />
                    <el-option label="老师" value="老师" />
                    <el-option label="竞赛" value="竞赛" />
                    <el-option label="部门" value="部门" />
                </el-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <div>
                <el-button type="primary" @click="onAddCommentObj" size="large">添加</el-button>
                <el-button @click="showingAddCommentDialog = false" size="large">取消</el-button>
            </div>
        </template>
    </el-dialog>

    <div style="position: relative; height: 710px;">
        <!-- 选择点评对象 -->
        <div ref="window1" class="window1">
            <div style="display: flex">
                <el-input size="large" v-model="keyword" style="max-width: 550px" placeholder="输入您要搜索的内容" clearable
                    @keyup.enter="onSearch">
                    <template #append>
                        <el-button @click="onSearch">
                            <el-icon>
                                <Search />
                            </el-icon>搜索</el-button>
                    </template>
                </el-input>
                <div style="flex-grow: 1;"></div>
                <el-button size="large" type="primary" @click="showingAddCommentDialog = true"><el-icon>
                        <Plus />
                    </el-icon>添加</el-button>
            </div>
            <el-tabs style="margin-top: 30px;" v-model="type" @tab-change="onSearch" stretch>
                <el-tab-pane label="全部" name="" />
                <el-tab-pane label="课程" name="课程" />
                <el-tab-pane label="老师" name="老师" />
                <el-tab-pane label="竞赛" name="竞赛" />
                <el-tab-pane label="部门" name="部门" />
            </el-tabs>
            <div v-loading="isLoading">
                <el-empty style="height: 509px;" v-show="commentObjData.length == 0"
                    image="/public/img/empty_commentObj.png" description="无点评" />
                <div v-show="commentObjData.length != 0" class="result">
                    <el-scrollbar ref="resultScrollbarRef" wrap-style="transition: all 0.5s;" height="100%">
                        <!-- {{ item }} -->
                        <el-card
                            :style="{ width: '99.5%', marginBottom: '10px', backgroundColor: item.id == selectedCommentObj.id ? '#eeeeee' : '' }"
                            v-for="item in commentObjData" :key="item" shadow="hover">
                            <template #header>
                                <div style="display: flex; line-height: 30px;" @click="onSelectCommentObj(item)">
                                    <el-tag size="large" :type="typeColor(item.type)" effect="dark">{{ item.type
                                        }}</el-tag>
                                    <el-text
                                        style="font-size: 25px; font-weight: 600; max-width: 40%; margin-left: 20px;"
                                        truncated>
                                        {{ item.name }}
                                    </el-text>
                                    <div style="flex-grow: 1;"></div>
                                    <div style="color: gray;">创建时间：{{ item.createTime }}</div>
                                </div>
                            </template>
                            <div style="display: flex;">
                                <div style="text-align: center;" @click="onSelectCommentObj(item)">
                                    <div style="font-size: 25px; font-weight: 600; color: #f7ba2a">{{
                                        item.score.toFixed(1)
                                        }} <img style="height: 20px;" src="/public/img/star.png">
                                    </div>
                                    <div style="color: gray;">
                                        <span style="font-weight: 700;">{{ item.count }}</span>评分/
                                        <span style="font-weight: 700;">{{ item.commentCount }}</span>评论
                                    </div>
                                </div>
                                <div style="flex-grow: 1;" @click="onSelectCommentObj(item)"></div>
                                <div style="text-align: center;">
                                    <div style="color: gray;">立即评分<span
                                            style="color: #f2cb51; font-weight: 700; margin-left: 10px;">{{
                                                item.userScore !== 0 ? item.userScore * 2 : '' }}</span></div>
                                    <el-rate size="large" v-model="item.userScore" @change="commentObj_like(item)"
                                        :colors="['#409eff', '#67c23a', '#FF9900']" allow-half />
                                </div>
                            </div>
                        </el-card>
                    </el-scrollbar>
                </div>
            </div>
        </div>
        <!-- 一级评论区 -->
        <div ref="window2" class="window2">
            <el-empty style="height: 100%;" v-show="selectedCommentObj.id == -1" image="/public/img/empty_comment1.png"
                description="无评论" />
            <el-card style="height: 100%;" v-show="selectedCommentObj.id != -1">
                <!-- 介绍 -->
                <template #header>
                    <div style="padding: 10px;">
                        <div style="display: flex;">
                            <el-tag size="large" :type="typeColor(selectedCommentObj.type)" effect="dark">
                                {{ selectedCommentObj.type }}</el-tag>
                            <div>
                                <el-text style="font-size: 25px; font-weight: 600; margin-left: 10px;" truncated>
                                    {{ selectedCommentObj.name }}
                                </el-text>
                            </div>

                            <div style="flex-grow: 1;"></div>

                            <div style="text-align: center;">
                                <div style="font-size: 25px; font-weight: 600; color: #f7ba2a">
                                    {{ Number(selectedCommentObj.score).toFixed(1) }}
                                    <img style="height: 20px;" src="/public/img/star.png">
                                </div>
                                <div style="color: gray;"><span style="font-weight: 700;">{{
                                    selectedCommentObj.count
                                        }}</span>评分/<span style="font-weight: 700;">{{
                                            selectedCommentObj.commentCount
                                        }}</span>评论
                                </div>
                            </div>
                        </div>
                        <div style="color: gray;">创建时间：{{ selectedCommentObj.createTime }}</div>
                    </div>
                </template>
                <div v-loading="isLoading_comment1" style="height: 365px;">
                    <el-scrollbar ref="comment1ScrollbarRef" height="100%">
                        <!-- 一级评论 -->
                        <el-empty v-show="commentData1.length === 0" description="快发出你的第一条评论吧~"
                            image="/public/img/noComment.png" />
                        <div :style="{ borderBottom: '1px lightgray solid', padding: '10px 2px', backgroundColor: item.id === selectedComment.id ? '#eeeeee' : '' }"
                            v-for="item in commentData1" :key="item">
                            <div @click="onSelectComment(item, 1)">
                                <div style="display: flex;">
                                    <div class="avatar">
                                        <img style="height: 100%;" :src="item.userAvatar">
                                    </div>
                                    <div style="margin-left: 10px; max-width: 84%">
                                        <el-text style="font-size: 20px; font-weight: 600;" line-clamp="1">
                                            {{ item.username }}
                                        </el-text>
                                        <div style="color: gray;">{{ item.pubTime }}</div>
                                    </div>
                                    <div style="flex-grow: 1;"></div>
                                    <div style="margin: 0 10px;">
                                        <el-icon size="25px">
                                            <ChatDotRound />
                                        </el-icon>
                                        <div style="text-align: center;">{{ item.replyCount }}</div>
                                    </div>
                                </div>
                                <div style="padding: 10px;">
                                    <el-text style="font-size: 15px;" line-clamp="5">{{ item.content }}</el-text>
                                </div>
                            </div>

                            <div style="display: flex; font-size: 13px; padding: 0 10px;">
                                <el-link @click="onLikeComment1(item)" :underline="false">
                                    <img style="height: 25px;" :src="likeImgUrl(item.liked)">
                                    <span :style="{ color: item.liked ? '#f3c668' : '', marginLeft: '5px' }">
                                        {{ item.likes }}
                                    </span>
                                </el-link>
                            </div>
                        </div>
                    </el-scrollbar>

                </div>
                <template #footer>
                    <div style="display: flex;">
                        <el-input style="height: 50px;" ref="input1Ref" v-model="messageComment"
                            :placeholder="inputPlaceholder" @keyup.enter="onSendComment" :disabled="InputDisabled" />
                        <el-button style="height: 50px; margin-left: 20px;" type="primary"
                            @click="onSendComment">发送</el-button>
                    </div>
                </template>
            </el-card>
        </div>
        <!-- 二级评论区 -->
        <div ref="window3" class="window3">
            <el-empty style="height: 100%;" v-show="selectedComment1.id == -1" image="/public/img/empty_comment2.png"
                description="无评论" />
            <el-card style="height: 100%;" v-show="selectedComment1.id != -1">
                <!-- 介绍 -->
                <template #header>
                    <div style="padding: 10px;">
                        <div style="display: flex;">
                            <div class="avatar">
                                <img style="height: 100%;" :src="selectedComment1.userAvatar">
                            </div>
                            <div style="display: inline-block; margin-left: 10px; max-width: 75%">
                                <div style="font-size: 20px; font-weight: 600;">
                                    {{ selectedComment1.username }}
                                </div>
                                <div style="color: gray;">{{ selectedComment1.pubTime }}</div>
                            </div>
                            <div style="flex-grow: 1"></div>
                            <el-link @click="onLikeComment1(selectedComment1)" :underline="false">
                                <img style="height: 40px;" :src="likeImgUrl(selectedComment1.liked)">
                                <span :style="{ color: selectedComment1.liked ? '#f3c668' : '', marginLeft: '5px' }">{{
                                    selectedComment1.likes
                                    }}</span>
                            </el-link>
                        </div>
                        <el-scrollbar style="margin-top: 10px;" height="72px">
                            {{ selectedComment1.content }}
                        </el-scrollbar>
                    </div>
                </template>
                <div v-loading="isLoading_comment2" style="height: 393px;">
                    <el-scrollbar ref="comment2ScrollbarRef" max-height="100%">
                        <!-- 二级评论 -->
                        <el-empty v-show="commentData2.length === 0" description="快发出你的第一条评论吧~"
                            image="/public/img/noComment.png" />
                        <div :style="{ borderBottom: '1px lightgray solid', padding: '10px 2px', backgroundColor: item.id === selectedComment.id ? '#eeeeee' : '' }"
                            v-for="item in commentData2" :key="item">
                            <div @click="onSelectComment(item, 2)">
                                <div style="display: flex;">
                                    <div class="avatar">
                                        <img style="height: 100%;" :src="item.userAvatar">
                                    </div>
                                    <div style="margin-left: 10px; max-width: 84%">
                                        <div>
                                            <el-text style="font-size: 20px; font-weight: 600;" line-clamp="1">
                                                {{ item.username }}
                                            </el-text>
                                        </div>
                                        <div style="color: gray;">{{ item.pubTime }}</div>
                                    </div>
                                </div>
                                <div style="padding: 10px;">
                                    <el-scrollbar style="font-size: 15px;" max-height="170px">
                                        回复<span style="color: #409eff; font-size: 20px; font-weight: 700;">{{
                                            item.replyUsername }}</span>：
                                        {{ item.content }}
                                    </el-scrollbar>
                                </div>
                            </div>

                            <div style="display: flex; font-size: 13px; padding: 0 10px;">
                                <el-link @click="onLikeComment2(item)" :underline="false">
                                    <img style="height: 25px;" :src="likeImgUrl(item.liked)">
                                    <span :style="{ color: item.liked ? '#f3c668' : '', marginLeft: '5px' }">{{
                                        item.likes
                                        }}</span>
                                </el-link>
                            </div>
                        </div>
                    </el-scrollbar>
                </div>
            </el-card>
        </div>
    </div>

</template>
<style scoped>
.result {
    /* border: 1px green solid; */
    display: inline-block;
    position: relative;
    width: 98%;
    height: 490px;
    padding: 1%;
    text-align: left;
}

.avatar {
    display: inline-block;
    border: 1px rgb(202, 202, 202) solid;
    width: 50px;
    height: 50px;
    border-radius: 50px;
    overflow: hidden;
}

.window1 {
    display: inline-block;
    position: absolute;
    z-index: 30;
    background-color: white;
    width: 40%;
    margin-top: 70px;
    left: 31%;
    transition: all 0.5s;
    /* border: 1px red solid; */
}

.window2 {
    display: inline-block;
    position: absolute;
    z-index: 20;
    background-color: white;
    width: 30%;
    margin-top: 70px;
    left: 31%;
    transition: all 0.5s;
    /* border: 1px red solid; */
}

.window3 {
    display: inline-block;
    position: absolute;
    z-index: 10;
    background-color: white;
    width: 30%;
    margin-top: 70px;
    left: 31%;
    transition: all 0.5s;
    /* border: 1px red solid; */
}
</style>