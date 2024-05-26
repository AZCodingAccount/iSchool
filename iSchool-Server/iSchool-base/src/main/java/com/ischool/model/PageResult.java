package com.ischool.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/*
 * 分页查询工具类
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页查询通用返回类")
public class PageResult<T> implements Serializable {

    @Schema(description = "数据列表")
    // 数据列表
    private List<T> items;

    @Schema(description = "后端所有可用总记录")
    // 总记录数
    private long counts;

    @Schema(description = "当前页码")
    // 当前页码
    private long pageNum;

    @Schema(description = "每页记录数")
    // 每页记录数
    private long pageSize;

}