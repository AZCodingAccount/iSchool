package com.ischool.model;

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
public class PageResult<T> implements Serializable {

    // 数据列表
    private List<T> items;

    // 总记录数
    private long counts;

    // 当前页码
    private long pageNum;

    // 每页记录数
    private long pageSize;

}