package com.plat.auto.test.autotest.service;

import com.plat.auto.test.autotest.entity.DataType;
import com.plat.auto.test.autotest.entity.DataTypeField;
import com.plat.auto.test.autotest.mapper.DataTypeFieldMapper;
import com.plat.auto.test.autotest.mapper.DataTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/12
 * @description
 */
@Service
@Slf4j
public class DataTypeServiceImpl implements IDataTypeService {
    @Resource
    private DataTypeMapper dataTypeMapper;
    @Resource
    private DataTypeFieldMapper dataTypeFieldMapper;

    private DataType fillFileDataType(DataType dataType, int maxRelateLevel) {
        List<DataTypeField> fieldList = dataTypeFieldMapper.findByParentDataTypeId(dataType.getId());
        dataType.setFieldList(fieldList);
        if (dataType.getFieldList() != null && dataType.getFieldList().size() > 0 && maxRelateLevel > 0) {
            for (DataTypeField field : dataType.getFieldList()) {
                DataType fieldDataType = dataTypeMapper.load(field.getFieldDataTypeId());
                fieldDataType = fillFileDataType(fieldDataType, --maxRelateLevel);
                field.setFieldDataType(fieldDataType);
            }
        }
        return dataType;
    }

    @Override
    public DataType loadDataType(int dataTypeId) {
        DataType dataType = dataTypeMapper.load(dataTypeId);
        if (dataType == null) {
            return dataType;
        }
        int maxRelateLevel = 5;
        dataType = fillFileDataType(dataType, maxRelateLevel);
        return dataType;
    }


}
