package com.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-07 10:27
 * @description: 信息传输Dto
 **/
@Data
@NoArgsConstructor
@Schema(description = "用户信息传输实体类")
public class MessageDto {

    @Schema(description = "消息id", example = "1789548655582642177")
    private Long id;

    /**
     * 当前评论用户id
     */
    @Schema(description = "当前评论用户id（回复你的评论）", example = "1789548655582642177")
    private Long userId;

    /**
     * 当前评论用户昵称
     */
    @Schema(description = "回复评论的用户昵称", example = "张三")
    private String userNickname;

    /**
     * 当前评论对象
     */
    @Schema(description = "当前评论所属点评对象", example = "服务端架构设计")
    private String objName;
    /**
     * 当前评论对象id
     */
    @Schema(description = "当前评论所属点评对象id", example = "1789548655582642177")
    private Long objId;

    /**
     * 回复用户id
     */
    @Schema(description = "被回复的用户id", example = "1789548655582642177")
    private Long replyUserId;

    /**
     * 回复评论id
     */
    @Schema(description = "被回复的评论id", example = "1789548655582642177")
    private Long replyCommentId;

    /**
     * 评论内容
     */
    @Schema(description = "回复的评论内容", example = "你说的对，但是......")
    private String content;

    /**
     * 评论点赞数
     */
    @Schema(description = "评论所获点赞数", example = "100")
    private Integer likes;

    /**
     * 发布时间
     */
    @Schema(description = "评论发布时间", example = "2023-11-22 8:00:00")
    private LocalDateTime pubTime;
}
