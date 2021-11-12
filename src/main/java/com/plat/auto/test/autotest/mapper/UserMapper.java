package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int add(User user);

    int update(User user);

    int delete(@Param("id") int id);

    User findByUserName(@Param("userName") String userName);

    User findById(@Param("id") int id);

    List<User> loadAll();

    List<User> pageList(@Param("offset") int offset,
                        @Param("pages") int pages,
                        @Param("userName") String userName,
                        @Param("type") int type);

    int pageListCount(@Param("offset") int offset,
                      @Param("pages") int pages,
                      @Param("userName") String userName,
                      @Param("type") int type);

}
