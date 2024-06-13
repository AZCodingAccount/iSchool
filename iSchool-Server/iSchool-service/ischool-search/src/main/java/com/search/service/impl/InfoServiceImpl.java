package com.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischool.exception.BusinessException;
import com.ischool.model.ErrorCode;
import com.ischool.model.PageResult;
import com.search.es.AnnouncementESDTO;
import com.search.mapper.InfoMapper;
import com.search.model.dto.SearchAnnouncementRequest;
import com.search.model.entity.Info;
import com.search.model.vo.AnnouncementVO;
import com.search.model.vo.searchAnnouncementVO;
import com.search.service.InfoService;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AlbertZhang
 * @description 针对表【info】的数据库操作Service实现
 * @createDate 2024-05-04 21:31:19
 */
@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info>
        implements InfoService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * @param searchAnnouncementRequest
     * @param school
     * @return com.ischool.model.PageResult<com.search.model.vo.SearchAnnouncementVO>
     * @description MySQL普通查询
     **/
    @Override
    public PageResult<searchAnnouncementVO> search(SearchAnnouncementRequest searchAnnouncementRequest, String school) {
        String keyword = searchAnnouncementRequest.getKeyword();
        Integer pageNum = searchAnnouncementRequest.getPageNum();
        Integer pageSize = searchAnnouncementRequest.getPageSize();
        LocalDate startDate = searchAnnouncementRequest.getStartDate();
        LocalDate endDate = searchAnnouncementRequest.getEndDate();

        // 1：校验参数
        if (pageNum <= 0 || pageSize <= 0 || pageSize >= 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数不合法");
        }
        // 2：分页查询(匹配title和content)
        Page<Info> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<Info>()
                .like(StringUtils.isNotBlank(keyword), Info::getTitle, keyword)
                .or()
                .like(StringUtils.isNotBlank(keyword), Info::getContent, keyword);

        // 添加日期过滤条件
        if (startDate != null) {
            queryWrapper.ge(Info::getPubTime, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(Info::getPubTime, endDate);
        }

        // 添加学校过滤条件，默认学校
        if (school == null) {
            school = "HRBUST";
        }
        queryWrapper.eq(Info::getSchool, school);

        this.page(page, queryWrapper);


        // 3：拼接参数
        long total = page.getTotal();
        long current = page.getCurrent();
        long size = page.getSize();
        List<Info> records = page.getRecords();
        // 3.1：转换数据并根据发布时间降序
        List<searchAnnouncementVO> searchAnnouncementVOList = records.stream().map(item -> {
            searchAnnouncementVO searchAnnouncementVO = new searchAnnouncementVO();
            BeanUtils.copyProperties(item, searchAnnouncementVO);
            return searchAnnouncementVO;
        }).sorted(Comparator.comparing(searchAnnouncementVO::getPubTime)).collect(Collectors.toList());
        return new PageResult<>(searchAnnouncementVOList, total, current, size);
    }


    public PageResult<searchAnnouncementVO> searchFromES(SearchAnnouncementRequest request, String school) {
        // 1：校验参数
        if (request.getPageNum() <= 0 || request.getPageSize() <= 0 || request.getPageSize() >= 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数不合法");
        }

        // 2：构建查询条件
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            boolQueryBuilder.must(new Query.Builder()
                    .bool(b -> b
                            .should(new Query.Builder().match(m -> m.field("title").boost(3F).query(request.getKeyword())).build())
                            .should(new Query.Builder().match(m -> m.field("pureText").query(request.getKeyword())).build())
                    ).build());
        }

        // 严格筛选日期范围
        if (request.getStartDate() != null && request.getEndDate() != null) {
            boolQueryBuilder.filter(new Query.Builder()
                    .range(r -> r
                            .field("pubTime")
                            .gte(JsonData.of(request.getStartDate().toString()))
                            .lte(JsonData.of(request.getEndDate().toString()))
                    ).build());
        } else {
            if (request.getStartDate() != null) {
                boolQueryBuilder.filter(new Query.Builder()
                        .range(r -> r
                                .field("pubTime")
                                .gte(JsonData.of(request.getStartDate().toString()))
                        ).build());
            }
            if (request.getEndDate() != null) {
                boolQueryBuilder.filter(new Query.Builder()
                        .range(r -> r
                                .field("pubTime")
                                .lte(JsonData.of(request.getEndDate().toString()))
                        ).build());
            }
        }

        if (school != null && !school.isEmpty()) {
            boolQueryBuilder.filter(new Query.Builder()
                    .term(t -> t
                            .field("school")
                            .value(school)
                    ).build());
        }

        Query boolQuery = new Query.Builder().bool(boolQueryBuilder.build()).build();

        // 3：构建高亮显示
        Highlight.Builder highlightBuilder = new Highlight.Builder()
                .preTags("<span style=\"color: red;\">")
                .postTags("</span>")
                .fields("title", new HighlightField.Builder().build())
                .fields("pureText", new HighlightField.Builder().build());

        // 4：执行查询
        try {
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("announcement")
                    .query(boolQuery)
                    .from((request.getPageNum() - 1) * request.getPageSize())
                    .size(request.getPageSize())
                    .highlight(highlightBuilder.build())  // 添加高亮显示
                    .build();

            SearchResponse<AnnouncementESDTO> searchResponse = elasticsearchClient.search(searchRequest, AnnouncementESDTO.class);

            // 5：处理查询结果
            long total = 0;
            if (searchResponse.hits().total() != null) {
                total = searchResponse.hits().total().value();
            }
            List<searchAnnouncementVO> searchAnnouncementVOList = searchResponse.hits().hits().stream()
                    .map(hit -> {
                        searchAnnouncementVO vo = new searchAnnouncementVO();
                        AnnouncementESDTO announcement = hit.source();
                        if (announcement != null) {
                            BeanUtils.copyProperties(announcement, vo);
                            // 处理高亮字段
                            if (hit.highlight() != null) {
                                if (hit.highlight().containsKey("title")) {
                                    vo.setTitle(String.join("", hit.highlight().get("title")));
                                }
                                if (hit.highlight().containsKey("pureText")) {
                                    vo.setContent(String.join("", hit.highlight().get("pureText")));
                                }
                            }
                        }
                        return vo;
                    })
                    .collect(Collectors.toList());

            return new PageResult<>(searchAnnouncementVOList, total, request.getPageNum(), request.getPageSize());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ElasticSearch 查询失败");
        }
    }

    /**
     * @param school
     * @param batchSize
     * @return java.util.List<com.search.es.AnnouncementESDTO>
     * @description 根据学校和单次查询值查询数据
     **/
    @Override
    public List<AnnouncementESDTO> findBySchoolLimitBatchSize(String school, int batchSize, int batchNum) {
        int offset = (batchNum - 1) * batchSize;

        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<Info>()
                .eq(Info::getSchool, school)
                .last("LIMIT " + offset + ", " + batchSize);
        List<Info> infoList = this.baseMapper.selectList(queryWrapper);
        return getAnnouncementESDTOS(infoList);
    }

    /**
     * @param infoList
     * @return java.util.List<com.search.es.AnnouncementESDTO>
     * @description 过滤数据
     **/
    private static List<AnnouncementESDTO> getAnnouncementESDTOS(List<Info> infoList) {
        return infoList.stream().map(item -> {
            AnnouncementESDTO announcementESDTO = new AnnouncementESDTO();
            BeanUtils.copyProperties(item, announcementESDTO);
            return announcementESDTO;
        }).toList();
    }

    /**
     * @param school
     * @param lastEndArticleId
     * @return java.util.List<com.search.es.AnnouncementESDTO>
     * @description 根据id和学校查询数据
     **/
    @Override
    public List<AnnouncementESDTO> findBySchoolAndIdGreaterThan(String school, Long lastEndArticleId) {
        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<Info>()
                .eq(Info::getSchool, school)
                .gt(Info::getArticleId, lastEndArticleId);
        List<Info> infoList = this.baseMapper.selectList(queryWrapper);
        return getAnnouncementESDTOS(infoList);
    }

    @Override
    public AnnouncementVO searchByIdFromES(Long id) {
        // 1：校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID参数不合法");
        }

        // 2：构建查询条件
        Query idQuery = new Query.Builder()
                .term(t -> t
                        .field("_id")
                        .value(id)
                ).build();

        // 3：执行查询
        try {
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("announcement")
                    .query(idQuery)
                    .build();

            SearchResponse<AnnouncementESDTO> searchResponse = elasticsearchClient.search(searchRequest, AnnouncementESDTO.class);

            // 4：处理查询结果
            if (!searchResponse.hits().hits().isEmpty()) {
                Hit<AnnouncementESDTO> hit = searchResponse.hits().hits().get(0);
                AnnouncementVO vo = new AnnouncementVO();
                AnnouncementESDTO announcement = hit.source();
                if (announcement != null) {
                    BeanUtils.copyProperties(announcement, vo);
                    return vo;
                }
            }
            return null;  // 如果没有找到对应的文档，返回null
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ElasticSearch 查询失败");
        }
    }

}




