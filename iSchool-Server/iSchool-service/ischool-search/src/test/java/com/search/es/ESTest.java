package com.search.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-17 20:01
 * @description: ES测试类
 **/
@SpringBootTest
public class ESTest {
    // @Autowired
    // private ElasticsearchTemplate elasticsearchTemplate;

    /*
     * 演示单个数据的增删改查操作，跟mp一模一样，就是更新时候这个需要先查然后全量更新，删除时候只能根据id删除,
     * 查询时候也有限制，根据业务找实现的方法
     * */

    @Autowired
    private AnnouncementEsDao announcementEsDao;

    /*
     * 测试插入
     * */
    @Test
    public void testInsert() {
        //
        AnnouncementESDTO announcementESDTO = new AnnouncementESDTO();
        announcementESDTO.setId(1L);
        announcementESDTO.setTitle("保研政策");
        announcementESDTO.setContent("保研政策大改");
        announcementESDTO.setPubTime(LocalDateTime.now());
        announcementESDTO.setUrl("http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do?articleId=4720&columnId=354");
        announcementESDTO.setCreateTime(LocalDateTime.now());
        announcementESDTO.setArticleId(1L);
        announcementESDTO.setSchool("HRBUST");
        announcementEsDao.save(announcementESDTO);
    }

    /*
     * 测试查询
     * */
    @Test
    public void testSelect() {
        Optional<AnnouncementESDTO> announcementEsDaoById = announcementEsDao.findById(1L);
        if (announcementEsDaoById.isPresent()) {
            // 如果查询到结果，获取 AnnouncementESDTO 对象
            AnnouncementESDTO announcement = announcementEsDaoById.get();
            System.out.println(announcement);
        }
    }

    /*
     * 测试删除
     * */
    @Test
    public void testDelete() {
        AnnouncementESDTO announcementESDTO = new AnnouncementESDTO();
        announcementESDTO.setTitle("保研政策");
        announcementEsDao.deleteById(1L);    // 根据id删除
        // announcementEsDao.delete(announcementESDTO); // 根据实体对象的id删除
    }


    /*
     * 测试更新
     * */
    @Test
    public void testUpdate() {
        AnnouncementESDTO announcementToUpdate = announcementEsDao.findById(1L).orElse(null);

        if (announcementToUpdate != null) {
            announcementToUpdate.setTitle("新保研政策");
            // 保存更新后的文档
            announcementEsDao.save(announcementToUpdate);
        }

    }


}
