package com.search.model.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-19 14:20
 * @description: 查询请求类
 **/
@Data
public class SearchAnnouncementRequest {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private LocalDate startDate;
    private LocalDate endDate;

}
