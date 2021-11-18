package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.entity.*;
import com.plat.auto.test.autotest.mapper.DocumentMapper;
import com.plat.auto.test.autotest.mapper.GroupMapper;
import com.plat.auto.test.autotest.mapper.ProjectMapper;
import com.plat.auto.test.autotest.service.LoginService;
import com.plat.auto.test.autotest.util.ArrayUtil;
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
import java.util.List;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/17
 * @description
 */
@Controller
@Api(tags = "1.0.0-SNAPSHOT", value = "分组类型接口")
@RequestMapping("/group")
@Slf4j
public class GroupController {
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private DocumentMapper documentMapper;
    @RequestMapping
    @ApiOperation(value = "初始页", notes = "初始页")
    public String index(HttpServletRequest request, Model model, int projectId, @RequestParam(required = false,
            defaultValue = "-1")  int groupId) {
        // 项目
        Project project = projectMapper.load(projectId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", project);

        // 分组列表
        List<Group> groupList = (List<Group>) groupMapper.loadAll(projectId);
        model.addAttribute("groupList", groupList);

        // 选中分组
        Group groupInfo = null;
        if (groupList!=null && groupList.size()>0) {
            for (Group groupItem: groupList) {
                if (groupId == groupItem.getId()) {
                    groupInfo = groupItem;
                }
            }
        }
        if (groupId!=0 && groupInfo==null) {
            groupId = -1;
        }
        model.addAttribute("groupId", groupId);
        model.addAttribute("groupInfo", groupInfo);

        // 分组下的，接口列表
        List<Document> documentList = documentMapper.loadAll(projectId, groupId);
        model.addAttribute("documentList", documentList);

        // 权限
        model.addAttribute("hasBizPermission", hasBizPermission(request, project.getBizId()));

        return "group/group.list";
    }

    private boolean hasBizPermission(HttpServletRequest request, int bizId){
        User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if ( loginUser.getType()==1 ||
                ArrayUtil.contains(StringUtil.split(loginUser.getPermissionBiz(), ","), String.valueOf(bizId))
        ) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation(value = "新增分组", notes = "新增分组")
    public ReturnT<String> add(HttpServletRequest request, Group group) {
        // valid
        if (StringUtil.isBlank(group.getName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入“分组名称”");
        }

        // 权限校验
        Project xxlApiProject = projectMapper.load(group.getProjectId());
        if (!hasBizPermission(request, xxlApiProject.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "无操作权限");
        }

        int ret = groupMapper.add(group);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新分组", notes = "更新分组")
    public ReturnT<String> update(HttpServletRequest request, Group group) {
        // exist
        Group existGroup = groupMapper.load(group.getId());
        if (existGroup == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "分组不存在");
        }

        // 权限校验
        Project project = projectMapper.load(existGroup.getProjectId());
        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "无操作权限");
        }

        // valid
        if (StringUtil.isBlank(group.getName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入“分组名称”");
        }

        int ret = groupMapper.update(group);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除分组", notes = "删除分组")
    public ReturnT<String> delete(HttpServletRequest request, int id) {

        // exist
        Group existGroup = groupMapper.load(id);
        if (existGroup == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "分组不存在");
        }

        // 权限校验
        Project xxlApiProject = projectMapper.load(existGroup.getProjectId());
        if (!hasBizPermission(request, xxlApiProject.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "无操作权限");
        }

        // 分组下是否存在接口
        List<Document> documentList = documentMapper.loadByGroupId(id);
        if (documentList!=null && documentList.size()>0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "分组下存在接口，不允许强制删除");
        }

        int ret = groupMapper.delete(id);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
