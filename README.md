# 大数据量下的教务公告聚合搜索平台

## 需求分析❓

​		大学🎓系统**性能差**、**可扩展性低**、**易用性差**已是共识。网站应该回归到为用户服务的本质，在此背景下，教务公告检索系统应运而生。本系统解决师生对于教务公告检索的需求，实现**高效检索🔍**、**AI问答🤖**、**用户点评💭**等一站式功能。下面给出功能模块图如下

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240522153811319.png" alt="image-20240522153811319" style="zoom:50%;" />

项目后端所用技术架构概览👀

![image-20240613104609407](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240613104609407.png)

## 技术选型🛠️

**前端：**

Vue3框架、Element-Plus组件库

**后端：**

开发框架：Spring Cloud2023、Spring Cloud Alibaba2023、SpringBoot3.2.4

数据库：Redis、ElasticSearch8.11、MySQL

中间件：RabbitMQ消息队列、OpenFeign远程调用、分布式任务调度框架 xxl-job

其他：SpringAI框架调用OpenAI生成式AI接口、Python爬虫技术🕷️

## 项目亮点✨

1. SpringCloud Gateway网关实现`用户鉴权`、`跨域解决`、`接口文档聚合`

2. Elastic Search+Redis实现高效检索，10w级数据量检索，从传统数据库查询**大于30s**优化到—>命中缓存情况下**小于100ms**，不命中缓存**小于1s**。

3. xxl-job定时任务结合python爬虫实现`教务网站数据库数据`、`MySQL`、`Redis`、`Elastic Search`四者的增量和全量同步

4. RabbitMQ进行AI生成建议的消息调用，实现**异步解耦**。使用SpringAI框架封装的开箱即用的OpenAI接口，实现**进一步聊聊**功能。

## 文档📚

- 查看[开发文档](https://github.com/AZCodingAccount/iSchool/blob/main/开发文档.md)了解项目开发与优化过程
- 查看[接口文档](https://github.com/AZCodingAccount/iSchool/tree/main/接口文档)了解项目接口设计

## 贡献🤝

项目欢迎任何形式的贡献

- 提出[issue](https://github.com/AZCodingAccount/iSchool/issues)报告 bug 或要求接入新系统
- 提交[PR](https://github.com/AZCodingAccount/iSchool/pulls)帮助完善项目

## 快速启动与项目概览 🚀

由于后端涉及到中间件较多，请观看B站视频启动项目与预览项目：[iSchool项目介绍](https://space.bilibili.com/501122856)

项目预览如下：

搜索页

![image-20240719203443727](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240719203443727.png)

AI问答页

![image-20240719203531956](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240719203531956.png)

社区模块页

![image-20240719203907039](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240719203907039.png)

评论页

![image-20240719203941258](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240719203941258.png)

个人中心页

![image-20240719203811664](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20240719203811664.png)

欢迎页

![img](https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/clip_image002.jpg)

## 查询接口性能测试

工具：jmeter、apipost

​		使用redis缓存在用户同时搜索一个关键词时，测试报告如下：

硬件配置：4h8g服务器，es堆内存配置1G

​		本地测试结果表明，在3000并发时，前99.99%的用户响应时间可以控制在1.2s以内。但在线上环境时，只在500并发时，时间在可以容忍的范围内(200并发时时间小于1s)，分析原因是因为**服务器硬件配置不足**、**服务器网络上行不足**、**网络传输延迟**等原因。

解决方案可以**加机器**、**加配置**。目前项目瓶颈在服务器而非数据库这边

1. 本地测试，时间稳定在1s以下，在3000并发时，错误率明显上升。

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020221204804.png" alt="image-20241020221204804" style="width:67%;" />

不使用缓存，3000并发下，本地QPS和响应时间下降4倍左右，由此可见，虽然es也有缓存，但是redis缓存也是有一定必要的

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020222542994.png" alt="image-20241020222542994" style="width:67%;" />

1. 在无可用线程时，连接会直接拒绝，解决方案：
   1. 增加最大线程数（默认是100）
   2. 加机器
   3. 改变数据库连接池的大小（默认几十），优化逻辑，每个请求执行快一点（这里已经到顶了）
2. 4h8g服务器，给的es堆内存只有1G。

100并发下，QPS达到了80，平均响应时间在100ms左右

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020214822618.png" alt="image-20241020214822618" width=66% />

​    500并发下，QPS在40左右徘徊，等待最后几个请求时出现明显卡顿，平均响应时间增加到了4s

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020215109256.png" alt="image-20241020215109256" style="width:66%;" />

​    1000并发下，与500个用户类似，起初请求QPS在40左右，处理后续请求有明显卡顿，平均响应时间增加到了11s

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020215539764.png" alt="image-20241020215539764" style="width:66%;" />

2000并发下，QPS15，平均响应时间20s，**第一次出现error**

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020220259655.png" alt="image-20241020220259655" style="width:67%;" />

增加到4000以后，错误率明显上升达到30%，错误信息为连接超时

<img src="https://my-picture-bed1-1321100201.cos.ap-beijing.myqcloud.com/mypictures/image-20241020220606635.png" alt="image-20241020220606635" style="width:67%;" />


