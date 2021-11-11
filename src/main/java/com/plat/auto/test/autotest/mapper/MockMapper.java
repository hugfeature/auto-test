package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.Mock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface MockMapper {
    int add(Mock mock);

    int update(Mock mock);

    int delete(@Param("id") int id);

    List<Mock> loadAll(@Param("documentId") int documentId);

    Mock load(@Param("id") int id);

    Mock loadByUuid(@Param("uuid") String uuid);
}
