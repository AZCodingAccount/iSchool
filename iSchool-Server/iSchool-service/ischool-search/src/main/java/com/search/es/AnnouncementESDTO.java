package com.search.es;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-17 21:10
 * @description: 公告DTO
 **/
@Document(indexName = "announcement")
@Data
public class AnnouncementESDTO {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    @Id
    private Long id;

    private String title;

    private String content;

    @Field(index = true, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private LocalDateTime pubTime;

    private String url;

    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private LocalDateTime createTime;

    private Long articleId;

    private String school;
}
