# OpenAPI definition


**简介**:OpenAPI definition


**HOST**:http://192.168.148.191:8902/api/v1/user


**联系人**:


**Version**:v0


**接口路径**:/user/v3/api-docs


[TOC]






# 学校管理


## 获取学校列表


**接口地址**:`/user/schools`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取系统所有支持列表，供个人中心下拉框使用</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseListSchoolVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|array|array|
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": null,
	"msg": "接口调用失败"
}
```


# 用户管理


## 获取用户登录信息


**接口地址**:`/user/`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>常常用于第一次登陆成功或路由守卫检查用户登录态</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseUserDto|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data||UserDto|UserDto|
|&emsp;&emsp;userId|用户id|integer(int64)||
|&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;gender|用户性别|string||
|&emsp;&emsp;age|用户年龄|integer(int32)||
|&emsp;&emsp;userAvatar|用户头像url|string||
|&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;schoolName|用户所属学校名称|string||
|&emsp;&emsp;schoolAbbr|用户所属学校简写|string||
|&emsp;&emsp;totalLikes|用户在社区模块的获赞总数|integer(int32)||
|&emsp;&emsp;totalComments|用户在社区模块的被评论总数|integer(int32)||
|&emsp;&emsp;unReadCommentsCount|用户未读评论的总数|integer(int32)||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": {
		"userId": 1789548655582642177,
		"username": "nick123",
		"nickname": "尼克",
		"gender": "男",
		"age": 20,
		"userAvatar": "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg",
		"email": "han892577@qq.com",
		"schoolName": "哈尔滨理工大学",
		"schoolAbbr": "HRBUST",
		"totalLikes": 100,
		"totalComments": 200,
		"unReadCommentsCount": 10
	},
	"msg": "接口调用失败"
}
```


## 修改用户信息


**接口地址**:`/user/`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "nickname": "尼克",
  "password": "123456",
  "gender": "男",
  "age": 20,
  "userAvatar": "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg",
  "email": "han892577@qq.com",
  "schoolAbbr": "HRBUST"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|updateUserDto|更新用户信息dto|body|true|UpdateUserDto|UpdateUserDto|
|&emsp;&emsp;nickname|用户昵称||false|string||
|&emsp;&emsp;password|用户密码(不传为空)||false|string||
|&emsp;&emsp;gender|用户性别||false|string||
|&emsp;&emsp;age|用户年龄||false|integer(int32)||
|&emsp;&emsp;userAvatar|用户头像的url||false|string||
|&emsp;&emsp;email|用户邮箱||false|string||
|&emsp;&emsp;schoolAbbr|用户学校(英文简写而非学校名称)||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseObject|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|object||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": null,
	"msg": "接口调用失败"
}
```


## 用户注销


**接口地址**:`/user/`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseObject|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|object||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": null,
	"msg": "接口调用失败"
}
```


## 检查用户id是否合法


**接口地址**:`/user/id`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>供后端系统远程调用，前端不需关注</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 用户登录


**接口地址**:`/user/login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "username": "",
  "password": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|loginDto|用户登录DTO|body|true|LoginDto|LoginDto|
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;password|用户密码||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|string||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": "null",
	"msg": "接口调用失败"
}
```


## 获取用户所有未读信息


**接口地址**:`/user/messages`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseListMessageDto|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|array|array|
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": null,
	"msg": "接口调用失败"
}
```


## 将消息标记为已读


**接口地址**:`/user/read/messages`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>点击去查看以后发送的请求</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|messageId|要标记的消息id|query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseObject|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|object||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": null,
	"msg": "接口调用失败"
}
```


## 用户注册


**接口地址**:`/user/register`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "username": "",
  "password": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|loginDto|用户登录DTO|body|true|LoginDto|LoginDto|
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;password|用户密码||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseObject|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|object||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": null,
	"msg": "接口调用失败"
}
```


## 获取用户信息


**接口地址**:`/user/rpc`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>供后端系统远程调用，前端不需关注</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||header|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseUserDto|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data||UserDto|UserDto|
|&emsp;&emsp;userId|用户id|integer(int64)||
|&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;gender|用户性别|string||
|&emsp;&emsp;age|用户年龄|integer(int32)||
|&emsp;&emsp;userAvatar|用户头像url|string||
|&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;schoolName|用户所属学校名称|string||
|&emsp;&emsp;schoolAbbr|用户所属学校简写|string||
|&emsp;&emsp;totalLikes|用户在社区模块的获赞总数|integer(int32)||
|&emsp;&emsp;totalComments|用户在社区模块的被评论总数|integer(int32)||
|&emsp;&emsp;unReadCommentsCount|用户未读评论的总数|integer(int32)||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": {
		"userId": 1789548655582642177,
		"username": "nick123",
		"nickname": "尼克",
		"gender": "男",
		"age": 20,
		"userAvatar": "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg",
		"email": "han892577@qq.com",
		"schoolName": "哈尔滨理工大学",
		"schoolAbbr": "HRBUST",
		"totalLikes": 100,
		"totalComments": 200,
		"unReadCommentsCount": 10
	},
	"msg": "接口调用失败"
}
```


## 文件上传


**接口地址**:`/user/upload`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file|文件对象|query|false|file||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data|响应数据|string||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": "null",
	"msg": "接口调用失败"
}
```