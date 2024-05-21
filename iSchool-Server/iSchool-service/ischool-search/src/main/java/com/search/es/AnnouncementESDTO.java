package com.search.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ischool.config.CustomLocalDateDeserializer;
import com.ischool.config.CustomLocalDateTimeDeserializer;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementESDTO {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    @Id
    private Long id;

    private String title;

    private String content;

    @Field(index = true, store = true, type = FieldType.Date, format = {}, pattern = DATE_PATTERN)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate pubTime;

    private String url;

    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    private Long articleId;

    private String school;
}
