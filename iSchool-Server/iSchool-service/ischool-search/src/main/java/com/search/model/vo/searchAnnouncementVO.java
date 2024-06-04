package com.search.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-04 21:45
 * @description: 搜索结果
 **/
@Data
@Schema(description = "搜索结果VO实体")
public class searchAnnouncementVO {

    @Schema(description = "公告id", example = "1789548655582642177")
    private Long id;

    /**
     * 公告标题
     */
    @Schema(description = "公告标题", example = "关于蓝桥杯2024年报名工作的通知")
    private String title;

    /**
     * 公告部分内容|全量内容
     */
    @Schema(description = "匹配到关键字的公告部分内容(而非全量)，使用v-html渲染", example = "<span color='red'>蓝桥杯</span>2024年报名工作的通知")
    private String content;

    /**
     * 公告发布时间
     */
    @Schema(description = "公告发布时间", example = "2024-05-22")
    private LocalDate pubTime;

    /**
     * 公告url
     */
    @Schema(description = "公告原始url", example = "http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do?articleId=4712&columnId=354")
    private String url;
}
