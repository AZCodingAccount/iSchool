create database if not exists ischool;
use ischool;
create table comments
(
    id       bigint                   not null
        primary key,
    obj_id   bigint                   not null comment '点评对象id',
    user_id  bigint                   not null comment '用户id',
    content  text                     not null comment '评论内容',
    pub_time datetime default (now()) not null comment '发布时间',
    likes    int      default 0       null comment '评论点赞数'
);

create table info
(
    id          bigint                             not null
        primary key,
    title       varchar(255)                       not null comment '公告标题',
    content     text                               not null comment '公告内容',
    pub_time    datetime                           not null comment '公告发布时间',
    url         varchar(255)                       not null comment '公告url',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
);

create table obj
(
    id            bigint                             not null
        primary key,
    type          varchar(255)                       not null comment '点评对象类型（课程|竞赛|老师）',
    comment_count int      default 0                 not null comment '评论数',
    score         double   default 10                not null comment '平均评分',
    count         int      default 0                 not null,
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    name          varchar(255)                       null comment '点评对象名称'
);

create table reply_comments
(
    id               bigint        not null
        primary key,
    user_id          bigint        not null comment '当前评论用户id',
    reply_user_id    bigint        not null comment '回复用户id',
    content          text          not null comment '评论内容',
    likes            int default 0 null comment '评论点赞数',
    pub_time         datetime      not null comment '发布时间',
    reply_comment_id bigint        null comment '回复的评论id'
);

create table task
(
    id          bigint                             not null
        primary key,
    keyword     varchar(255)                       not null comment '关键字',
    user_id     bigint                             not null comment '关联的用户',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
);

create table user
(
    user_id     bigint                                      not null
        primary key,
    username    varchar(255)                                not null comment '用户名',
    password    varchar(255)                                not null comment '密码，加密存储',
    gender      char             default '男'               not null comment '用户性别 男  女',
    age         tinyint unsigned default '18'               not null comment '用户年龄',
    user_avatar varchar(1024)                               null comment '用户头像url',
    email       varchar(255)     default 'han892577@qq.com' not null comment '用户邮箱',
    role        tinyint          default 1                  not null comment '用户角色，1学生、2管理员',
    create_time datetime         default CURRENT_TIMESTAMP  not null comment '创建时间',
    nickname    varchar(255)                                null comment '昵称'
);



