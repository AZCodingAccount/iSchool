package com.search.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 21:45
 * @description: 搜索结果
 **/
@Data
public class SearchAnnouncementVO {
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告部分内容|全量内容
     */
    private String content;

    /**
     * 公告发布时间
     */
    private LocalDateTime pubTime;

    /**
     * 公告url
     */
    private String url;
}
