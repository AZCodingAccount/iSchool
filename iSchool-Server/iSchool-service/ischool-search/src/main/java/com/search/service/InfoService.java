package com.search.service;

import com.ischool.model.PageResult;
import com.search.es.AnnouncementESDTO;
import com.search.model.dto.SearchAnnouncementRequest;
import com.search.model.entity.Info;
import com.search.model.vo.AnnouncementVO;
import com.search.model.vo.searchAnnouncementVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author AlbertZhang
 * @description 针对表【info】的数据库操作Service
 * @createDate 2024-05-04 21:31:19
 */
public interface InfoService extends IService<Info> {

    /**
     * @param searchAnnouncementRequest
     * @param school
     * @return com.ischool.model.PageResult<com.search.model.vo.SearchAnnouncementVO>
     * @description MySQL分页查询
     **/
    PageResult<searchAnnouncementVO> search(SearchAnnouncementRequest searchAnnouncementRequest, String school);

    /**
     * @param searchAnnouncementRequest
     * @param school
     * @return com.ischool.model.PageResult<com.search.model.vo.SearchAnnouncementVO>
     * @description 使用es查询
     **/
    PageResult<searchAnnouncementVO> searchFromES(SearchAnnouncementRequest searchAnnouncementRequest, String school);

    /**
     * @param school
     * @return java.util.List<com.search.es.AnnouncementESDTO>
     * @description 根据学校和批量值查询数据
     **/
    List<AnnouncementESDTO> findBySchoolLimitBatchSize(String school, int batchSize, int batchNum);

    /**
     * @param school
     * @param lastEndArticleId
     * @return java.util.List<com.search.es.AnnouncementESDTO>
     * @description 根据上次同步完成的id查询数据
     **/
    List<AnnouncementESDTO> findBySchoolAndIdGreaterThan(String school, Long lastEndArticleId);

    /**
     * @param id
     * @return com.search.model.vo.SearchAnnouncementVO
     * @description 从es中根据id查询
     **/
    AnnouncementVO searchByIdFromES(Long id);
}
