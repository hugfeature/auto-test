package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.entity.*;
import com.plat.auto.test.autotest.mapper.BizMapper;
import com.plat.auto.test.autotest.mapper.DocumentMapper;
import com.plat.auto.test.autotest.mapper.GroupMapper;
import com.plat.auto.test.autotest.mapper.ProjectMapper;
import com.plat.auto.test.autotest.service.LoginService;
import com.plat.auto.test.autotest.util.ArrayUtil;
import com.plat.auto.test.autotest.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @date: 2021/11/17
 * @description
 */
@Controller
@RequestMapping("/project")
@Api(tags = "1.0.0-SNAPSHOT", value = "项目接口")
public class ProjectController {
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private BizMapper bizMapper;
    @Resource
    private DocumentMapper documentMapper;

    @RequestMapping
    @ApiOperation(value = "初始化", notes = "初始化")
    public String index(Model model, @RequestParam(required = false, defaultValue = "0") int bizId) {

        // 业务线ID
        model.addAttribute("bizId", bizId);

        // 业务线列表
        List<Biz> bizList = bizMapper.loadAll();
        model.addAttribute("bizList", bizList);

        return "project/project.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @ApiOperation(value = "页面列表", notes = "页面列表")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String name, int bizId) {
        // page list
        List<Project> list = projectMapper.pageList(start, length, name, bizId);
        int listCount = projectMapper.pageListCount(start, length, name, bizId);

        // package result
        Map<String, Object> maps = new HashMap<>();
        // 总记录数
        maps.put("recordsTotal", listCount);
        // 过滤后的总记录数
        maps.put("recordsFiltered", listCount);
        // 分页列表
        maps.put("data", list);
        return maps;
    }

    private boolean hasBizPermission(HttpServletRequest request, int bizId) {
        User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (loginUser.getType() == 1 ||
                ArrayUtil.contains(StringUtil.split(loginUser.getPermissionBiz(), ","), String.valueOf(bizId))
        ) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation(value = "增加接口", notes = "增加接口")
    public ReturnT<String> add(HttpServletRequest request, Project project) {
        // valid
        if (StringUtil.isBlank(project.getName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入项目名称");
        }
        if (StringUtil.isBlank(project.getBaseProjectUrl())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入根地址(线上)");
        }

        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
        }

        int ret = projectMapper.add(project);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新接口", notes = "更新接口")
    public ReturnT<String> update(HttpServletRequest request, Project project) {
        // exist
        Project existProject = projectMapper.load(project.getId());
        if (existProject == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "项目ID不存在");
        }

        // valid
        if (StringUtil.isBlank(project.getName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入项目名称");
        }
        if (StringUtil.isBlank(project.getBaseProjectUrl())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入根地址(线上)");
        }

        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有相关操作权限");
        }

        int ret = projectMapper.update(project);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除接口", notes = "删除接口")
    public ReturnT<String> delete(HttpServletRequest request, int id) {

        // exist
        Project existProject = projectMapper.load(id);
        if (existProject == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "项目不存在");
        }

        if (!hasBizPermission(request, existProject.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "无操作权限");
        }

        // 项目下是否存在分组
        List<Group> groupList = (List<Group>) groupMapper.loadAll(id);
        if (groupList != null && groupList.size() > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "该项目下存在分组信息，不允许删除");
        }

        // 项目下是否存在接口
        List<Document> documents = documentMapper.loadAll(id, -1);
        if (documents != null && documents.size() > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "该项目下存在接口信息，不允许删除");
        }

        int ret = projectMapper.delete(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }
}
