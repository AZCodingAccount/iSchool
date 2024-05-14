package com.search.service;

import com.ischool.model.PageResult;
import com.search.model.entity.Info;
import com.search.model.vo.SearchAnnouncementVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Albert han
 * @description 针对表【info】的数据库操作Service
 * @createDate 2024-05-04 21:31:19
 */
public interface InfoService extends IService<Info> {

    /**
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.search.model.vo.SearchAnnouncementVO>
     * @description MySQL普通查询公告信息
     **/
    PageResult<SearchAnnouncementVO> search(String keyword, Integer pageNum, Integer pageSize);
}
