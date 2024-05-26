<script setup>
import { getOneAnnouncement } from '@/api/home';
import router from '@/router';
// import { sleep } from '@/utils/time';
import { ElLoading } from 'element-plus';
import { ref } from 'vue'

const resultDataObj = ref({}) // 搜索结果对象
const init = async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading'
    })
    try {
        let res = await getOneAnnouncement(router.currentRoute.value.query.id)
        // await sleep(3000)
        // let res = { // test
        //     data: {
        //         id: -1,
        //         content: '<h1>temp view</h1>',
        //         pubTime: '2024-5-25',
        //         url: 'https://www.baidu.com/'
        //     }
        // }
        resultDataObj.value = res.data
    } catch {
        loading.close()
        resultDataObj.value.content = '<h1 style="color: red;">Error</h1>'
    }
    loading.close()
}
init()

</script>

<template>
    <!-- 按钮 -->
    <div class="button">
        <a style="height: 100%;" :href="resultDataObj.url">
            <el-tooltip style="height: 100%;" effect="dark" content="跳转到教务在线查看" placement="top">
                <img style="height: 100%;" src="/public/img/building.png">
            </el-tooltip>
        </a>
    </div>
    <div v-html="resultDataObj.content"></div>
</template>

<style scoped>
.button {
    position: fixed;
    bottom: 160px;
    right: 60px;
    display: inline-block;
    border: 1px gray solid;
    border-radius: 100px;
    overflow: hidden;
    width: 100px;
    height: 100px;
    z-index: 2001;
}
</style>
