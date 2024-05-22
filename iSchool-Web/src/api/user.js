import request from '@/utils/request'

export const getLoginUser_1 = () => { // 登录成功后，获取用户个人信息
    return request.get('/user/')
    // {
    //     "code": 0,
    //     "data": {
    //         "userId": 0,
    //         "username": "",
    //         "nickname": "",
    //         "gender": "",
    //         "age": 0,
    //         "userAvatar": "",
    //         "email": "",
    //         "totalLikes": 0,
    //         "totalComments": 0,
    //         "unReadCommentsCount": 0
    //     },
    //     "msg": ""
    // }
}

export const updateUserInfo_1 = (data) => { // 更新个人信息
    // {
    //     "nickname": "",
    //     "password": "",
    //     "gender": "",
    //     "age": 0,
    //     "userAvatar": "",
    //     "email": ""
    //   }
    return request.put('/user/', data)
}

export const deleteUser_1 = () => { // 注销
    return request.delete('/user/')
}

export const login = (data) => { // 登录，成功后获得token
    // {
    //     "username": "",
    //     "password": ""
    //   }
    return request.post('/user/login', data)
    // {
    //     "code": 0,
    //     "data": "", // token
    //     "msg": ""
    // }
}

export const getMessageList = () => { // 获取信息
    return request.get('/user/messages')
    // {
    //     "code": 0,
    //     "data": [
    //         {
    //             "id": 0,
    //             "userId": 0,
    //             "userNickname": "",
    //             "objId": 0,
    //             "objName": '',
    //             "replyUserId": 0,
    //             "replyCommentId": 0,
    //             "content": "",
    //             "likes": 0,
    //             "pubTime": ""
    //         }
    //     ],
    //     "msg": ""
    // }
}

export const readMessage = (data) => { // 已读信息
    // {
    //     "messageId": 0
    // }
    return request.put('/user/read/messages?messageId=' + data.messageId)
}

export const register = (data) => { // 注册
    // {
    //     "username": "",
    //     "password": ""
    //   }
    return request.post('/user/register', data)
}

export const upload = (file) => { // 上传头像
    return request.post('/user/upload', file)
    // {
    //     "data": "" // avatar url
    // }
}



