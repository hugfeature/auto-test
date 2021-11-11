package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.Biz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author: wangzhaoxian
 * @date: 2021/11/6
 * @description
 */
@Mapper
public interface BizMapper {
    /**
     * @Description: add
     * @Param: [biz]
     * @return: int
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    int add(Biz biz);

    /**
     * @Description: update
     * @Param: [biz]
     * @return: int
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    int update(Biz biz);

    /**
     * @Description: delete
     * @Param: [id]
     * @return: int
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    int delete(@Param("id") int id);

    /**
     * @Description: loadAll
     * @Param: []
     * @return: java.util.List<com.plat.auto.test.autotest.entity.Biz>
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    List<Biz> loadAll();

    /**
     * @Description: pageList
     * @Param: [offset, pageSize, bizName]
     * @return: java.util.List<com.plat.auto.test.autotest.entity.Biz>
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    List<Biz> pageList(@Param("offset") int offset,
                       @Param("pages") int pages,
                       @Param("bizName") String bizName);

    /**
     * @Description: pageListCount
     * @Param: [offset, pageSize, bizName]
     * @return: int
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pages") int pages,
                      @Param("bizName") String bizName);

    /**
     * @Description: loadById
     * @Param: [id]
     * @return: com.plat.auto.test.autotest.entity.Biz
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    Biz loadById(@Param("id") int id);
}
