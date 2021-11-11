package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ProjectMapper {
    int add(Project project);

    int update(Project project);

    int delete(@Param("id") int id);

    Project load(@Param("id") int id);

    List<Project> pageList(@Param("offset") int offset,
                           @Param("pageSize") int pageSize,
                           @Param("name") String name,
                           @Param("bizId") int bizId);

    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("name") String name,
                      @Param("bizId") int bizId);

}
