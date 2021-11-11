package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.DataType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/6
 * @description
 */
@Mapper
public interface DataTypeMapper {
    int add(DataType dataType);

    int update(DataType dataType);

    int delete(@Param("id") int id);

    DataType load(@Param("id") int id);

    List<DataType> pageList(@Param("offset") int offset,
                            @Param("pages") int pages,
                            @Param("bizId") int bizId,
                            @Param("name") String name);

    int pageListCount(@Param("offset") int offset,
                      @Param("pages") int pages,
                      @Param("bizId") int bizId,
                      @Param("name") String name);

    DataType loadByName(@Param("name") String name);
}
