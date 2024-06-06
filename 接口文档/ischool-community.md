# iSchool项目API 文档


**简介**:iSchool项目API 文档


**HOST**:http://192.168.41.191:8903/api/v1/community


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
	"code": 0,
	"data": {},
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
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
|data|响应数据|array|CommentObjVO|
|&emsp;&emsp;id|点评对象id|integer(int64)||
|&emsp;&emsp;type|点评对象类型|string||
|&emsp;&emsp;name|点评对象名称|string||
|&emsp;&emsp;commentCount|总评分数（用count吧，我也忘了这个字段）|integer(int32)||
|&emsp;&emsp;score|平均评分|number(double)||
|&emsp;&emsp;userScore|用户评分|number(double)||
|&emsp;&emsp;count|总评分数|integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": [
		{
			"id": 1789548655582642177,
			"type": "课程",
			"name": "服务端架构设计",
			"commentCount": 12,
			"score": 7.9,
			"userScore": 5.5,
			"count": 12,
			"createTime": ""
		}
	],
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
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
|data|响应数据|array|ReplyCommentsVO|
|&emsp;&emsp;id|评论id|integer(int64)||
|&emsp;&emsp;objId|评论所属对象id|integer(int64)||
|&emsp;&emsp;userId|发送评论的用户id|integer(int64)||
|&emsp;&emsp;content|评论的内容|string||
|&emsp;&emsp;likes|评论点赞数|integer(int32)||
|&emsp;&emsp;liked|用户是否点赞（True代表点赞）|boolean||
|&emsp;&emsp;userAvatar|用户头像url|string||
|&emsp;&emsp;username|评论的用户名|string||
|&emsp;&emsp;replyUserId|回复的用户id|integer(int64)||
|&emsp;&emsp;replyUsername|回复的用户名|string||
|&emsp;&emsp;replyUserAvatar|回复的用户头像|string||
|&emsp;&emsp;pubTime|评论的发布时间|string(date-time)||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": [
		{
			"id": 1789548655582642177,
			"objId": 1789548655582642177,
			"userId": 1789548655582642177,
			"content": "我知道了，这是个好老师",
			"likes": 15,
			"liked": true,
			"userAvatar": "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg",
			"username": "张三",
			"replyUserId": 1789548655582642177,
			"replyUsername": "李四",
			"replyUserAvatar": "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg",
			"pubTime": ""
		}
	],
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
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
|data|响应数据|array|CommentsVO|
|&emsp;&emsp;id|一级评论id|integer(int64)||
|&emsp;&emsp;objId|点评对象id|integer(int64)||
|&emsp;&emsp;userId|当前用户id|integer(int64)||
|&emsp;&emsp;content|评论内容|string||
|&emsp;&emsp;likes|当前评论点赞数|integer(int32)||
|&emsp;&emsp;liked|当前用户是否点赞（True已点赞）|boolean||
|&emsp;&emsp;userAvatar|用户头像url|string||
|&emsp;&emsp;username|评论的用户名|string||
|&emsp;&emsp;replyCount|当前评论回复数|integer(int64)||
|&emsp;&emsp;pubTime|发布时间|string(date-time)||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": [
		{
			"id": 1789548655582642177,
			"objId": 1789548655582642177,
			"userId": 1789548655582642177,
			"content": "你说的对，但是......",
			"likes": 100,
			"liked": true,
			"userAvatar": "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg",
			"username": "张狗蛋",
			"replyCount": 100,
			"pubTime": ""
		}
	],
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
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
	"code": 0,
	"data": {},
	"msg": "success"
}
```