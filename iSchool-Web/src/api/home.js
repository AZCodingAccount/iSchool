import request from '@/utils/request'

// ai-controller

export const chat = (message) => {
    return request.get('/search/ai/chat?message=' + message)
    // {
    //     "code": 0,
    //     "data": "",
    //     "msg": ""
    // }
}

export const aiSearchRes = (keyword) => {
    return request.get('/search/ai/genRes?keyword=' + keyword)
}

export const aiSearch = (keyword) => {
    return request.get('/search/ai/toGenRes?keyword=' + keyword)
}

// search-controller

export const searchAnnouncement = (keyword, pageNum, pageSize, startDate, endDate) => {
    let url = `/search/page?keyword=${keyword}&pageNum=${pageNum}&pageSize=${pageSize}`
    if (startDate != null)
        url += `&startDate=${startDate}`
    if (endDate != null)
        url += `&endDate=${endDate}`
    return request.get(url)
    // {
    //     "code": 50010,
    //     "data": {
    //         "items": [
    //             {
    //                 "id": 1789548655582642177,
    //                 "title": "关于蓝桥杯2024年报名工作的通知",
    //                 "content": "<span color='red'>蓝桥杯</span>2024年报名工作的通知",
    //                 "pubTime": "2024-05-22",
    //                 "url": "http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do?articleId=4712&columnId=354"
    //             }
    //         ],
    //         "counts": 0,
    //         "pageNum": 0,
    //         "pageSize": 0
    //     },
    //     "msg": "接口调用失败"
    // }
}

// 搜索单一公告
export const getOneAnnouncement = (announcementId) => {
    return request.get('/search/' + announcementId)
    // {
    //     "code": 50010,
    //     "data": {
    //         "id": 1789548655582642177,
    //         "content": "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n    <title>关于举办第七届全国大学生化工实验大赛的通知</title>",
    //         "pubTime": "2024-05-22",
    //         "url": "http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do?articleId=4712&columnId=354"
    //     },
    //     "msg": "接口调用失败"
    // }
}











