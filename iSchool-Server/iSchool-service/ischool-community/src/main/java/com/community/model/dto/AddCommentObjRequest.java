package com.community.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 21:30
 * @description: 添加点评对象DTO
 **/
@Data
public class AddCommentObjRequest implements Serializable {
    /**
     * 点评对象名称
     */
    private String name;
    /**
     * 点评对象类型（课程|竞赛|老师）
     */
    private String type;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
