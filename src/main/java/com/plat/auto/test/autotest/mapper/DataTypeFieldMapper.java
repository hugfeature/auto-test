package com.plat.auto.test.autotest.mapper;

import com.plat.auto.test.autotest.entity.DataTypeField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataTypeFieldMapper {
    int add(List<DataTypeField> dataTypeFieldList);

    int deleteByParentDataTypeId(@Param("parentDataTypeId") int parentDataTypeId);

    List<DataTypeField> findByParentDataTypeId(@Param("parentDataTypeId") int parentDataTypeId);

    List<DataTypeField> findByFieldDataTypeId(@Param("fieldDataTypeId") int fieldDataTypeId);
}
