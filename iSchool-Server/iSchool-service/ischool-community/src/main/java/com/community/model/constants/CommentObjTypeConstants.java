package com.community.model.constants;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 21:39
 * @description: 评论对象常量类
 **/
public class CommentObjTypeConstants {
    public static final String TEACHER = "老师";
    public static final String COMPETITION = "竞赛";
    public static final String COURSE = "课程";
    public static final String DEPARTMENT = "部门";

    public static boolean isLegal(String type) {
        if (type == null) {
            return false;
        }
        return type.equals(TEACHER) || type.equals(COMPETITION) || type.equals(COURSE) || type.equals(DEPARTMENT);
    }
}
