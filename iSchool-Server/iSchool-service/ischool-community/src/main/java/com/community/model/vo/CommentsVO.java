package com.community.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-03 22:42
 * @description: 评论VO
 **/
@Data
@Schema(description = "一级评论VO实体")
public class CommentsVO implements Serializable {

    /**
     * 评论id
     */
    @Schema(description = "一级评论id", example = "1789548655582642177")
    private Long id;

    /**
     * 点评对象id
     */
    @Schema(description = "点评对象id", example = "1789548655582642177")
    private Long objId;

    /**
     * 用户id
     */
    @Schema(description = "当前用户id", example = "1789548655582642177")
    private Long userId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容", example = "你说的对，但是......")
    private String content;


    /**
     * 点赞数
     */
    @Schema(description = "当前评论点赞数", example = "100")
    private Integer likes;


    /**
     * 当前用户是否点赞
     */
    @Schema(description = "当前用户是否点赞（True已点赞）", example = "True")
    private Boolean liked;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像url", example = "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg")
    private String userAvatar;

    /**
     * 评论用户名
     */
    @Schema(description = "评论的用户名", example = "张狗蛋")
    private String username;


    /**
     * 回复数
     */
    @Schema(description = "当前评论回复数", example = "100")
    private Long replyCount;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间", example = "2024-05-22 10:00:00")
    private LocalDateTime pubTime;

    private static final long serialVersionUID = 1L;
}
