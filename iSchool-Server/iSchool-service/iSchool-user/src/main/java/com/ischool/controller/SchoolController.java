package com.ischool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ischool.model.BaseResponse;
import com.ischool.model.Result;
import com.ischool.model.entity.School;
import com.common.vo.SchoolVO;
import com.ischool.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-19 13:42
 * @description: 学校控制器
 **/
@RestController
@RequestMapping
@Slf4j
public class SchoolController {
    @Autowired
    SchoolService schoolService;

    @GetMapping("/schools")
    public BaseResponse<List<SchoolVO>> getSchoolList() {
        log.info("获取学校列表");
        // 获取学校列表就直接在controller里面写了，后面直接改库
        List<School> schools = schoolService.getBaseMapper().selectList(new LambdaQueryWrapper<>());
        List<SchoolVO> schoolVOList = schools.stream().map(item -> {
            SchoolVO schoolVO = new SchoolVO();
            BeanUtils.copyProperties(item, schoolVO);
            return schoolVO;
        }).toList();
        return Result.success(schoolVOList);
    }
}
