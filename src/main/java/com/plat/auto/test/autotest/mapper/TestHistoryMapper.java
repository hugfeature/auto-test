package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.TestHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TestHistoryMapper {
    int add(TestHistory testHistory);

    int update(TestHistory testHistory);

    int delete(@Param("id") int id);

    TestHistory load(@Param("id") int id);

    List<TestHistory> loadByDocumentId(@Param("documentId") int documentId);
}
