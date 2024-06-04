package com.search.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-17 21:24
 * @description: 框架封装好的简单的增删改查, 本项目没用上
 **/
public interface AnnouncementEsDao extends ElasticsearchRepository<AnnouncementESDTO, Long> {
}
