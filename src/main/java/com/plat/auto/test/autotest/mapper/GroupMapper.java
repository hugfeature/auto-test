package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMapper {
    int add(Group group);
    int update(Group group);
    int delete(@Param("id") int id);
    Group load(@Param("id") int id);
    Group loadAll(@Param("projectId") int projectId);
}
