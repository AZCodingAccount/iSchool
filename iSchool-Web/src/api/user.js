import request from '@/utils/request'

export const getLoginUser = () => { // 登录成功后，获取用户个人信息
    return request.get('/api/v1/user')
    // {
    //     "code": 0,
    //     "data": {
    //         "userId": 0,
    //         "username": "",
    //         "nickname": "",
    //         "gender": "",
    //         "age": 0,
    //         "userAvatar": "",
    //         "email": ""
    //     },
    //     "msg": ""
    // }
}

export const updateUserInfo = (data) => { // 更新个人信息
    // {
    //     "nickname": "",
    //     "password": "",
    //     "gender": "",
    //     "age": 0,
    //     "userAvatar": "",
    //     "email": ""
    // }
    return request.put('/api/v1/user', data)
}

export const deleteUser = () => { // 注销
    return request.delete('/api/v1/user')
}

export const register = (data) => { // 注册
    // {
    //     "username": "",
    //     "password": ""
    //   }
    return request.post('/api/v1/user/register', data)
}

export const login = (data) => { // 登录，成功后获得token
    // {
    //     "username": "",
    //     "password": ""
    //   }
    return request.post('/api/v1/user/login', data)
    // {
    //     "code": 0,
    //     "data": "", // token
    //     "msg": ""
    // }
}

export const upload = (file) => { // 上传头像
    return request.post('/api/v1/upload', file)
}



