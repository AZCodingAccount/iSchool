package com.ischool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ischool.mapper.SchoolMapper;
import com.ischool.model.entity.School;
import com.ischool.service.SchoolService;
import org.springframework.stereotype.Service;

/**
* @author Ljx
* @description 针对表【school(支持的学校表)】的数据库操作Service实现
* @createDate 2024-05-19 13:41:39
*/
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
    implements SchoolService {

}




