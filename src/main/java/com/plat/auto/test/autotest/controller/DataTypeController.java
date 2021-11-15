package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.controller.annotation.PermessionLimit;
import com.plat.auto.test.autotest.entity.Biz;
import com.plat.auto.test.autotest.entity.DataType;
import com.plat.auto.test.autotest.mapper.BizMapper;
import com.plat.auto.test.autotest.mapper.DataTypeFieldMapper;
import com.plat.auto.test.autotest.mapper.DataTypeMapper;
import com.plat.auto.test.autotest.mapper.DocumentMapper;
import com.plat.auto.test.autotest.service.IDataTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
    private IDataTypeService dataTypeService;
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
        int listNum = dataTypeMapper.pageListCount(start, length, bizId,name);

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

}
