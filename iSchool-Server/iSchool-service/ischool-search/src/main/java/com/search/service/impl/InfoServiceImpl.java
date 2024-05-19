package com.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ischool.exception.BusinessException;
import com.ischool.model.ErrorCode;
import com.ischool.model.PageResult;
import com.search.mapper.InfoMapper;
import com.search.model.dto.SearchAnnouncementRequest;
import com.search.model.entity.Info;
import com.search.model.vo.SearchAnnouncementVO;
import com.search.service.InfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Albert han
 * @description 针对表【info】的数据库操作Service实现
 * @createDate 2024-05-04 21:31:19
 */
@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info>
        implements InfoService {

    /**
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return com.ischool.model.PageResult<com.search.model.vo.SearchAnnouncementVO>
     * @description MySQL普通查询
     **/
    @Override
    public PageResult<SearchAnnouncementVO> search(SearchAnnouncementRequest searchAnnouncementRequest, String school) {
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
        List<SearchAnnouncementVO> searchAnnouncementVOList = records.stream().map(item -> {
            SearchAnnouncementVO searchAnnouncementVO = new SearchAnnouncementVO();
            BeanUtils.copyProperties(item, searchAnnouncementVO);
            return searchAnnouncementVO;
        }).sorted(Comparator.comparing(SearchAnnouncementVO::getPubTime)).collect(Collectors.toList());
        return new PageResult<>(searchAnnouncementVOList, total, current, size);
    }
}




