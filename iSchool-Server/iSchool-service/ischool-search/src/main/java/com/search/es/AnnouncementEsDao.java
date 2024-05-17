package com.search.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-17 21:24
 * @description: 框架封装好的简单的增删改查
 **/
public interface AnnouncementEsDao extends ElasticsearchRepository<AnnouncementESDTO, Long> {
}
