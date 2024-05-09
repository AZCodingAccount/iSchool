import request from '@/utils/request'

// comment-controller

export const addComment_1 = (data) => { // 添加一级评论
    // {
    //     "objId": 0,
    //     "content": ""
    //   }
    return request.post('/community/comment', data)
}

export const getCommentsList_1 = (objId) => { // 获取点评对象下的一级评论
    return request.get('/community/comment/' + objId)
    // {
    //     "code": 0,
    //     "data": [
    //         {
    //             "id": 0,
    //             "objId": 0,
    //             "userId": 0,
    //             "content": "",
    //             "likes": 0,
    //             "userAvatar": "",
    //             "username": "",
    //             "replyCount": 0,
    //             "pubTime": ""
    //         }
    //     ],
    //     "msg": ""
    // }
}

export const addCommentLikes_1 = (commentId) => { // 给一级评论点赞
    return request.put('/community/comment/like/' + commentId)
}

export const decreaseCommentLikes_1 = (commentId) => { // 取消给一级评论点赞
    return request.delete('/community/comment/like/' + commentId)
}

// comment-obj-controller

export const addCommentObj = (data) => { // 添加点评对象
    // {
    //     "name": "",
    //     "type": ""
    //   }
    return request.post('/community/comment_obj', data)
}

export const score = (data) => { // 给点评对象打分
    // {
    //     "commentObjId": 0,
    //     "score": 0
    //   }
    return request.put('/community/comment_obj/score', data)
}

export const searchCommentObj = (keyword, type) => { // 搜索点评对象
    return request.get('/community/comment_obj/search?keyword=' + keyword + '&type=' + type)
    // {
    //     "code": 0,
    //     "data": [
    //         {
    //             "id": 0,
    //             "name": "",
    //             "type": "",
    //             "commentCount": 0,
    //             "score": 0,
    //             "count": 0,
    //             "createTime": ""
    //         }
    //     ],
    //     "msg": ""
    // }
}

// reply-comment-controller

export const addComment = (data) => { // 添加二级评论
    // {
    //     "objId": 0,
    //     "commentId": 0,
    //     "replyContent": "",
    //     "replyUserId": 0
    //   }
    return request.post('/community/reply_comment', data)
}

export const getCommentsList = (commentId) => { // 获取一级评论下的二级评论
    return request.get('/community/reply_comment/' + commentId)
    // {
    //     "code": 0,
    //     "data": [
    //         {
    //             "id": 0,
    //             "objId": 0,
    //             "userId": 0,
    //             "content": "",
    //             "likes": 0,
    //             "userAvatar": "",
    //             "username": "",
    //             "replyUsername": "",
    //             "pubTime": ""
    //         }
    //     ],
    //     "msg": ""
    // }
}

export const addCommentLikes = (replyCommentId) => { // 给二级评论点赞
    return request.put('/community/reply_comment/like/' + replyCommentId)
}

export const decreaseCommentLikes = (commentId) => {// 取消给二级评论点赞
    return request.delete('/community/reply_comment/like/' + commentId)
}


