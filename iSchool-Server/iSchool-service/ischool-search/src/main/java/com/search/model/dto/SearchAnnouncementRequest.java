package com.search.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-19 14:20
 * @description: 查询请求类
 **/
@Data
@Schema(name = "公告搜索请求实体", requiredProperties = {"pageNum", "pageSize"})
public class SearchAnnouncementRequest {
    @Schema(description = "搜索关键字", example = "蓝桥杯")
    private String keyword;

    @Schema(description = "第几页", example = "1")
    private Integer pageNum;
    @Schema(description = "每页大小", example = "20")
    private Integer pageSize;
    @Schema(description = "开始日期", example = "2023-05-20")
    private LocalDate startDate;

    @Schema(description = "结束日期", example = "2024-05-22")
    private LocalDate endDate;

}
