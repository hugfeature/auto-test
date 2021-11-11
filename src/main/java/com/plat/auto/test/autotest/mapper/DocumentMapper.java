package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocumentMapper {
    int add(Document document);

    int update(Document document);

    int deleteById(@Param("id") int id);

    Document load(@Param("id") int id);

    List<Document> loadAll(@Param("projectId") int projectId,
                           @Param("groupId") int groupId);

    List<Document> loadByGroupId(@Param("groupId") int groupId);

    List<Document> findByResponseDataTypeId(@Param("responseDataTypeId") int responseDataTypeId);

}
