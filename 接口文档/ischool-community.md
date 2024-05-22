# iSchool项目API 文档


**简介**:iSchool项目API 文档


**HOST**:http://192.168.148.191:8903/api/v1/community


**联系人**:AlbertZhang


**Version**:1.0


**接口路径**:/community/v3/api-docs


[TOC]






# 点评对象相关接口


## 添加点评对象


**接口地址**:`/community/comment_obj`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "name": "服务端架构设计",
  "type": "课程"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|addCommentObjRequest|点评对象请求实体|body|true|AddCommentObjRequest|AddCommentObjRequest|
|&emsp;&emsp;name|点评对象名称||true|string||
|&emsp;&emsp;type|点评对象类型||true|string||


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


## 用户评分


**接口地址**:`/community/comment_obj/score`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "commentObjId": 1789548655582642177,
  "score": 7.5
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|scoreRequest|评分请求实体|body|true|ScoreRequest|ScoreRequest|
|&emsp;&emsp;commentObjId|评分的对象id||true|integer(int64)||
|&emsp;&emsp;score|评分分数||true|number(double)||


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


## 搜索点评对象


**接口地址**:`/community/comment_obj/search`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|keyword|搜索关键字|query|true|string||
|type|搜索类型|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseListCommentObjVO|


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


# 二级评论相关接口


## 添加二级评论信息


**接口地址**:`/community/reply_comment`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "objId": 1789548655582642177,
  "parentCommentId": 1789548655582642177,
  "commentId": 1789548655582642177,
  "replyContent": "你说的对，但是......",
  "replyUserId": 1789548655582642177
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|addReplyCommentRequest|回复评论请求实体|body|true|AddReplyCommentRequest|AddReplyCommentRequest|
|&emsp;&emsp;objId|点评对象id||true|integer(int64)||
|&emsp;&emsp;parentCommentId|父级评论id||true|integer(int64)||
|&emsp;&emsp;commentId|回复的评论id（一级二级均可）||true|integer(int64)||
|&emsp;&emsp;replyContent|回复的内容||true|string||
|&emsp;&emsp;replyUserId|回复给的用户id||true|integer(int64)||


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


## 获取某一级评论下的所有二级评论


**接口地址**:`/community/reply_comment/{replyCommentId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|replyCommentId|一级评论id|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseListReplyCommentsVO|


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


## 给二级评论点赞


**接口地址**:`/community/reply_comment/like/{commentId}`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|当前点赞二级评论id|path|true|integer(int64)||


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


## 取消二级评论点赞


**接口地址**:`/community/reply_comment/like/{commentId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|当前取消点赞二级评论id|path|true|integer(int64)||


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


# 社区模块暴露出的RPC相关接口


## 标记消息为已读


**接口地址**:`/community/read_message`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>供后端系统远程调用，前端不需关注</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||
|messageId||query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 获取用户社交相关数据


**接口地址**:`/community/social_data`


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
|200|OK|SocialDataDto|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|totalLikes||integer(int32)|integer(int32)|
|totalComments||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"totalLikes": 0,
	"totalComments": 0
}
```


## 获取用户未读消息列表


**接口地址**:`/community/unread_messages`


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
|200|OK|MessageDto|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|id|消息id|integer(int64)|integer(int64)|
|userId|当前评论用户id（回复你的评论）|integer(int64)|integer(int64)|
|userNickname|回复评论的用户昵称|string||
|objName|当前评论所属点评对象|string||
|objId|当前评论所属点评对象id|integer(int64)|integer(int64)|
|replyUserId|被回复的用户id|integer(int64)|integer(int64)|
|replyCommentId|被回复的评论id|integer(int64)|integer(int64)|
|content|回复的评论内容|string||
|likes|评论所获点赞数|integer(int32)|integer(int32)|
|pubTime|评论发布时间|string(date-time)|string(date-time)|


**响应示例**:
```javascript
[
	{
		"id": 1789548655582642177,
		"userId": 1789548655582642177,
		"userNickname": "张三",
		"objName": "服务端架构设计",
		"objId": 1789548655582642177,
		"replyUserId": 1789548655582642177,
		"replyCommentId": 1789548655582642177,
		"content": "你说的对，但是......",
		"likes": 100,
		"pubTime": ""
	}
]
```


# 一级评论相关接口


## 添加评论信息


**接口地址**:`/community/comment`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "objId": 1789548655582642177,
  "content": "1789548655582642177"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|addCommentRequest|添加一级评论请求实体|body|true|AddCommentRequest|AddCommentRequest|
|&emsp;&emsp;objId|点评对象id||true|integer(int64)||
|&emsp;&emsp;content|评论内容||true|string||


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


## 获取某一点评对象的所有评论


**接口地址**:`/community/comment/{objId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|objId|点评对象id|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseListCommentsVO|


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


## 给一级评论点赞


**接口地址**:`/community/comment/like/{commentId}`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|一级评论id|path|true|integer(int64)||


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


## 取消一级评论点赞


**接口地址**:`/community/comment/like/{commentId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|一级评论id|path|true|integer(int64)||


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