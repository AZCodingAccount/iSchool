package com.community.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName obj
 */
@Data
@Schema(description = "点评对象VO实体")
public class CommentObjVO implements Serializable {

    /**
     * 点评对象id
     */
    @Schema(description = "点评对象id", example = "1789548655582642177")
    private Long id;

    /**
     * 点评对象类型（课程|竞赛|老师）
     */
    @Schema(description = "点评对象类型", example = "课程")
    private String type;

    /**
     * 点评对象名称
     */
    @Schema(description = "点评对象名称", example = "服务端架构设计")
    private String name;

    /**
     * 评论数
     */
    @Schema(description = "总评分数（用count吧，我也忘了这个字段）", example = "12")
    private Integer commentCount;

    /**
     * 平均评分
     */
    @Schema(description = "平均评分", example = "7.9")
    private Double score;

    /**
     * 用户评分，未评分为0
     */
    @Schema(description = "用户评分", example = "5.5")
    private Double userScore;


    /**
     * 评分用户数
     */
    @Schema(description = "总评分数", example = "12")
    private Integer count;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}