package com.community.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 15:34
 * @description: 添加回复评论
 **/
@Data
@Schema(description = "回复评论请求实体", requiredProperties = {"objId", "parentCommentId", "commentId", "replyContent", "replyUserId"})
public class AddReplyCommentRequest {
    /*
     * 点评对象id
     */
    @Schema(description = "点评对象id", example = "1789548655582642177")
    private Long objId;
    /*
     * 父级评论id
     */
    @Schema(description = "父级评论id", example = "1789548655582642177")
    private Long parentCommentId;
    /*
     * 回复的评论id
     */
    @Schema(description = "回复的评论id（一级二级均可）", example = "1789548655582642177")
    private Long commentId;
    /*
     * 回复的内容
     */
    @Schema(description = "回复的内容", example = "你说的对，但是......")
    private String replyContent;
    /*
     * 回复给的用户
     */
    @Schema(description = "回复给的用户id", example = "1789548655582642177")
    private Long replyUserId;
}
