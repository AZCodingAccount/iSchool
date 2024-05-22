# iSchool项目API 文档


**简介**:iSchool项目API 文档


**HOST**:http://192.168.148.191:8904/api/v1/search


**联系人**:AlbertZhang


**Version**:1.0


**接口路径**:/search/v3/api-docs


[TOC]






# 搜索相关接口


## 搜索单一公告


**接口地址**:`/search/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponseAnnouncementVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data||AnnouncementVO|AnnouncementVO|
|&emsp;&emsp;id|公告id|integer(int64)||
|&emsp;&emsp;content|匹配到关键字的公告全量内容(而非部分)，使用v-html渲染|string||
|&emsp;&emsp;pubTime|公告发布时间|string(date)||
|&emsp;&emsp;url|公告原始url|string||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": {
		"id": 1789548655582642177,
		"content": "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n    <title>关于举办第七届全国大学生化工实验大赛的通知</title>",
		"pubTime": "2024-05-22",
		"url": "http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do?articleId=4712&columnId=354"
	},
	"msg": "接口调用失败"
}
```


## 搜索公告


**接口地址**:`/search/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|arg0||query|true|公告搜索请求实体|公告搜索请求实体|
|&emsp;&emsp;keyword|搜索关键字||false|string||
|&emsp;&emsp;pageNum|第几页||true|integer(int32)||
|&emsp;&emsp;pageSize|每页大小||true|integer(int32)||
|&emsp;&emsp;startDate|开始日期||false|string(date)||
|&emsp;&emsp;endDate|结束日期||false|string(date)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|BaseResponsePageResultSearchAnnouncementVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|响应码，成功为0其他（4xxxx客户端错误，5xxxx服务端错误）|integer(int32)|integer(int32)|
|data||PageResultSearchAnnouncementVO|PageResultSearchAnnouncementVO|
|&emsp;&emsp;items|数据列表|array|searchAnnouncementVO|
|&emsp;&emsp;&emsp;&emsp;id|公告id|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;title|公告标题|string||
|&emsp;&emsp;&emsp;&emsp;content|匹配到关键字的公告部分内容(而非全量)，使用v-html渲染|string||
|&emsp;&emsp;&emsp;&emsp;pubTime|公告发布时间|string(date)||
|&emsp;&emsp;&emsp;&emsp;url|公告原始url|string||
|&emsp;&emsp;counts|后端所有可用总记录|integer(int64)||
|&emsp;&emsp;pageNum|当前页码|integer(int64)||
|&emsp;&emsp;pageSize|每页记录数|integer(int64)||
|msg|响应消息，成功为success，错误返回失败信息|string||


**响应示例**:
```javascript
{
	"code": 50010,
	"data": {
		"items": [
			{
				"id": 1789548655582642177,
				"title": "关于蓝桥杯2024年报名工作的通知",
				"content": "<span color='red'>蓝桥杯</span>2024年报名工作的通知",
				"pubTime": "2024-05-22",
				"url": "http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do?articleId=4712&columnId=354"
			}
		],
		"counts": 0,
		"pageNum": 0,
		"pageSize": 0
	},
	"msg": "接口调用失败"
}
```


# AI模块相关接口


## 用户进行聊天


**接口地址**:`/search/ai/chat`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|message|用户聊天信息|query|true|string||


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


## 获取ai生成好的评论


**接口地址**:`/search/ai/genRes`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>系统生成成功直接返回，如果系统还未生成完成，系统返回错误码50010</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|keyword|用户搜索关键词|query|true|string||


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


## 通知后端去生成ai建议


**接口地址**:`/search/ai/toGenRes`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|keyword|用户搜索关键词|query|true|string||


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