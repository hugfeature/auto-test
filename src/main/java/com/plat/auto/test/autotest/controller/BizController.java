package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.controller.annotation.PermessionLimit;
import com.plat.auto.test.autotest.entity.Biz;
import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.mapper.BizMapper;
import com.plat.auto.test.autotest.mapper.DataTypeMapper;
import com.plat.auto.test.autotest.mapper.ProjectMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/15
 * @description
 */
@Controller
@Api(tags = "1.0.0-SNAPSHOT", value = "业务接口")
@RequestMapping("/biz")
@Slf4j
public class BizController {
    @Resource
    private BizMapper bizMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private DataTypeMapper dataTypeMapper;

    @RequestMapping
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "初始化", notes = "初始化")
    public String index(Model model) {
        return "biz/biz.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "分页展示", notes = "分页展示")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String bizName) {
        // page list
        List<Biz> list = bizMapper.pageList(start, length, bizName);
        int listNum = bizMapper.pageListCount(start, length, bizName);
        Map<String, Object> maps = new HashMap<>();
        // 总记录数
        maps.put("reacordTotal", listNum);
        // 过滤后的记录数
        maps.put("recoredFiltered", listNum);
        // 分页列表
        maps.put("data", list);
        return maps;
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "业务添加", notes = "业务添加")
    public ReturnT<String> add(Biz biz) {
        if (StringUtil.isBlank(biz.getBizName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "业务名称不可为空");
        }
        int ret = bizMapper.add(biz);
        return ret > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "业务编辑", notes = "业务编辑")
    public ReturnT<String> update(Biz biz) {
        if (StringUtil.isBlank(biz.getBizName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "业务名称不可为空");
        }
        int ret = bizMapper.update(biz);
        return ret > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "删除业务", notes = "删除业务")
    public ReturnT<String> delete(int id) {

        int projectCount = projectMapper.pageListCount(0, 10, null, id);
        if (projectCount > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "业务下存在项目不允许删除");
        }
        int dtCount = dataTypeMapper.pageListCount(0, 10, id, null);
        if (dtCount > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "业务下存在数据类型不允许删除");
        }
        List<Biz> bizs = bizMapper.loadAll();
        if (bizs.size() <= 1) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "至少保留一个业务线");
        }
        int ret = bizMapper.delete(id);
        return ret > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }
}
