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
@Schema(description = "单个公告VO实体")
public class AnnouncementVO {

    @Schema(description = "公告id", example = "1789548655582642177")
    private Long id;


    /**
     * 公告全量内容
     */
    @Schema(description = "匹配到关键字的公告全量内容(而非部分)，使用v-html渲染", example = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <title>关于举办第七届全国大学生化工实验大赛的通知</title>")
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
