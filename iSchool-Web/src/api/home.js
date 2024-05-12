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

export const searchAnnouncement = (keyword, pageNum, pageSize) => {
    return request.get('/search/page?keyword=' + keyword + '&pageNum=' + pageNum + '&pageSize=' + pageSize)
    // {
    //     "code": 0,
    //     "data": {
    //         "items": [
    //             {
    //                 "id": 0,
    //                 "title": "",
    //                 "content": "",
    //                 "pubTime": "",
    //                 "url": ""
    //             }
    //         ],
    //         "counts": 0,
    //         "pageNum": 0,
    //         "pageSize": 0
    //     },
    //     "msg": ""
    // }
}











