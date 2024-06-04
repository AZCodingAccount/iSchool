package com.community.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-04 15:55
 * @description: 回复评论数VO
 **/
@Data
@Schema(description = "二级评论VO")
public class ReplyCommentsVO implements Serializable {

    /**
     * 评论id
     */
    @Schema(description = "评论id", example = "1789548655582642177")
    private Long id;
    /**
     * 当前评论对象id
     */
    @Schema(description = "评论所属对象id", example = "1789548655582642177")
    private Long objId;

    /**
     * 发送评论的用户id
     */
    @Schema(description = "发送评论的用户id", example = "1789548655582642177")
    private Long userId;

    /**
     * 评论内容
     */
    @Schema(description = "评论的内容", example = "我知道了，这是个好老师")
    private String content;


    /**
     * 点赞数
     */
    @Schema(description = "评论点赞数", example = "15")
    private Integer likes;

    /**
     * 用户是否点赞
     */
    @Schema(description = "用户是否点赞（True代表点赞）", example = "True")
    private Boolean liked;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像url", example = "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg")
    private String userAvatar;

    /**
     * 评论用户名
     */
    @Schema(description = "评论的用户名", example = "张三")
    private String username;

    /**
     * 回复的id
     */
    @Schema(description = "回复的用户id", example = "1789548655582642177")
    private Long replyUserId;
    /**
     * 回复的用户名
     */
    @Schema(description = "回复的用户名", example = "李四")
    private String replyUsername;

    /**
     * 回复的用户头像
     */
    @Schema(description = "回复的用户头像", example = "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg")
    private String replyUserAvatar;

    /**
     * 发布时间
     */
    @Schema(description = "评论的发布时间", example = "2024-05-22 10:00:00")
    private LocalDateTime pubTime;

    private static final long serialVersionUID = 1L;
}
