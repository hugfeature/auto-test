package com.plat.auto.test.autotest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plat.auto.test.autotest.entity.*;
import com.plat.auto.test.autotest.mapper.BizMapper;
import com.plat.auto.test.autotest.mapper.DataTypeFieldMapper;
import com.plat.auto.test.autotest.mapper.DataTypeMapper;
import com.plat.auto.test.autotest.mapper.DocumentMapper;
import com.plat.auto.test.autotest.service.DataTypeService;
import com.plat.auto.test.autotest.service.LoginService;
import com.plat.auto.test.autotest.util.ArrayUtil;
import com.plat.auto.test.autotest.util.DataTypeToCode;
import com.plat.auto.test.autotest.util.JacksonUtil;
import com.plat.auto.test.autotest.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/15
 * @description
 */
@Controller
@Api(tags = "1.0.0-SNAPSHOT", value = "数据类型接口")
@RequestMapping("/dataType")
@Slf4j
public class DataTypeController {
    @Resource
    private DataTypeMapper dataTypeMapper;
    @Resource
    private DataTypeFieldMapper dataTypeFieldMapper;
    @Resource
    private BizMapper bizMapper;
    @Resource
    private DocumentMapper documentMapper;
    @Resource
    private DataTypeService dataTypeService;

    @RequestMapping
    @ApiOperation(value = "初始化", notes = "初始化")
    public String index(Model model) {
        List<Biz> bizList = bizMapper.loadAll();
        model.addAttribute("bizList", bizList);
        return "dataType/dataType.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @ApiOperation(value = "分页展示", notes = "分页展示")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        int bizId, String name) {
        // page list
        List<DataType> list = dataTypeMapper.pageList(start, length, bizId, name);
        int listNum = dataTypeMapper.pageListCount(start, length, bizId, name);

        // package result
        Map<String, Object> maps = new HashMap<>();
        // 总记录数
        maps.put("reacordTotal", listNum);
        // 过滤后的记录数
        maps.put("recoredFiltered", listNum);
        // 分页列表
        maps.put("data", list);
        return maps;
    }

    @RequestMapping("/addDataTypePage")
    @ApiOperation(value = "初始化", notes = "初始化")
    public String addDataTypePage(Model model) {
        // 业务线列表
        List<Biz> bizList = bizMapper.loadAll();
        model.addAttribute("bizList", bizList);
        return "dataType/dataType.add";
    }

    private boolean hasBizPermission(HttpServletRequest request, int bizId) {
        User user = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (user.getType() == 1 || ArrayUtil.contains(StringUtil.split(user.getPermissionBiz(), ","),
                String.valueOf(bizId))) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/addDataType")
    @ResponseBody
    @ApiOperation(value = "增加数据类型", notes = "增加数据类型")
    public ReturnT<Integer> addDataType(HttpServletRequest request, DataType dataType, String fieldTypeJson) {
        if (StringUtil.isNotBlank(fieldTypeJson)) {
            List<DataTypeField> fieldList = JacksonUtil.readValueRefer(fieldTypeJson,
                    new TypeReference<List<DataTypeField>>() {
                    });
            if (fieldList != null && fieldList.size() > 0) {
                dataType.setFieldList(fieldList);
            }
        }
        if (StringUtil.isBlank(dataType.getName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型名称不可为空");
        }
        if (StringUtil.isBlank(dataType.getAbout())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型描述不可为空");
        }
        if (!hasBizPermission(request, dataType.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有相关业务线的权限,请联系管理员开通");
        }
        Biz biz = bizMapper.loadById(dataType.getBizId());
        if (biz == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "业务线非法");
        }
        DataType existName = dataTypeMapper.loadByName(dataType.getName());
        if (existName != null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型名称不可重复");
        }
        // 有效的筛选
        if (dataType.getFieldList() != null && dataType.getFieldList().size() > 0) {
            for (DataTypeField field : dataType.getFieldList()
            ) {
                if (StringUtil.isBlank(field.getFieldName())) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "名称不可为空");
                }
                DataType fieldDataType = dataTypeService.loadDataType(field.getFieldDataTypeId());
                if (fieldDataType == null) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型ID非法");
                }
                if (FiledTypeEnum.match(field.getFieldType()) == null) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "字段非法");
                }
            }
        }
        int addRet = dataTypeMapper.add(dataType);
        if (addRet < 1) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型新增失败");
        }
        if (dataType.getFieldList() != null && dataType.getFieldList().size() > 0) {
            for (DataTypeField field : dataType.getFieldList()
            ) {
                field.setParentDataTypeId(dataType.getId());
            }
            addRet = dataTypeMapper.add((DataType) dataType.getFieldList());
            if (addRet < 1) {
                return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型新增失败");
            }
        }
        return dataType.getId() > 0 ? new ReturnT<>(dataType.getId()) : new ReturnT<>(ReturnT.FAIL_CODE, "数据类型新增失败");
    }

    @RequestMapping("/updateDataTypePage")
    @ApiOperation(value = "数据类型编辑页", notes = "数据类型编辑页")
    public String updateDataTypePage(HttpServletRequest request, Model model, int dataTypeId) {
        DataType dataType = dataTypeService.loadDataType(dataTypeId);
        if (dataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("dataType", dataType);
        if (!hasBizPermission(request, dataType.getBizId())) {
            throw new RuntimeException("无权限操作");
        }
        List<Biz> bizs = bizMapper.loadAll();
        model.addAttribute("bizList", bizs);
        return "dataType/dataType.update";
    }

    @RequestMapping("/updateDataType")
    @ResponseBody
    @ApiOperation(value = "数据类型更新", notes = "数据类型更新")
    public ReturnT<String> updateDataType(HttpServletRequest request, DataType dataType, String fieldTypeJson) {
        if (StringUtil.isNotBlank(fieldTypeJson)) {
            List<DataTypeField> fieldList = JacksonUtil.readValueRefer(fieldTypeJson,
                    new TypeReference<List<DataTypeField>>() {
                    });
            if (fieldList != null && fieldList.size() > 0) {
                dataType.setFieldList(fieldList);
            }
        }
        if (StringUtil.isBlank(dataType.getName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型名称不可为空");
        }
        if (StringUtil.isBlank(dataType.getAbout())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型描述不可为空");
        }
        if (!hasBizPermission(request, dataType.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有相关业务线的权限,请联系管理员开通");
        }
        Biz biz = bizMapper.loadById(dataType.getBizId());
        if (biz == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "业务线非法");
        }
        DataType existName = dataTypeMapper.loadByName(dataType.getName());
        if (existName != null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型名称不可重复");
        }
        // 有效的筛选
        if (dataType.getFieldList() != null && dataType.getFieldList().size() > 0) {
            for (DataTypeField field : dataType.getFieldList()
            ) {
                if (StringUtil.isBlank(field.getFieldName())) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "名称不可为空");
                }
                DataType fieldDataType = dataTypeService.loadDataType(field.getFieldDataTypeId());
                if (fieldDataType == null) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型ID非法");
                }
                if (FiledTypeEnum.match(field.getFieldType()) == null) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "字段非法");
                }
            }
        }
        int updateRet = dataTypeMapper.update(dataType);
        if (updateRet > 0) {
            // 移除旧的，添加新的
            dataTypeFieldMapper.deleteByParentDataTypeId(dataType.getId());
            if (dataType.getFieldList() != null && dataType.getFieldList().size() > 0) {
                for (DataTypeField field : dataType.getFieldList()
                ) {
                    field.setParentDataTypeId(dataType.getId());
                }
                updateRet = dataTypeFieldMapper.add(dataType.getFieldList());
                if (updateRet < 1) {
                    return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型更新失败");
                }
            }
        }
        return updateRet > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/dataTypeDetail")
    @ApiOperation(value = "数据类型详情页面", notes = "数据类型详情页面")
    public String dataTypeDetail(HttpServletRequest request, Model model, int dataTypeId) {
        DataType dataType = dataTypeService.loadDataType(dataTypeId);
        if (dataType == null) {
            throw new RuntimeException("数据类型不存在");
        }
        model.addAttribute("dataType", dataType);
        List<Biz> bizs = bizMapper.loadAll();
        model.addAttribute("bizList", bizs);
        String codeContent = DataTypeToCode.parseDataTypeToCode(dataType);
        model.addAttribute("codeContent", codeContent);
        model.addAttribute("hasBizPermission", hasBizPermission(request, dataType.getBizId()));
        return "dataType/dataType.detail";
    }

    @RequestMapping("/deleteDataType")
    @ResponseBody
    @ApiOperation(value = "删除数据类型", notes = "删除数据类型")
    public ReturnT<String> deleteDataType(HttpServletRequest request, int id) {
        DataType dataType = dataTypeMapper.load(id);
        if (dataType == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型不存在");
        }
        if (!hasBizPermission(request, dataType.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有相关数据权限");
        }
        List<DataTypeField> fieldList = dataTypeFieldMapper.findByParentDataTypeId(id);
        if (fieldList != null && fieldList.size() > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型使用中,拒绝删除");
        }
        List<Document> documentList = documentMapper.findByResponseDataTypeId(id);
        if (documentList != null && documentList.size() > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "数据类型API使用中,拒绝删除");
        }
        int deleteRet = dataTypeMapper.delete(id);
        dataTypeFieldMapper.deleteByParentDataTypeId(id);
        return deleteRet > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }


}
