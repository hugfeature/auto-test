package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.config.RequestConfig;
import com.plat.auto.test.autotest.entity.*;
import com.plat.auto.test.autotest.mapper.*;
import com.plat.auto.test.autotest.service.DataTypeService;
import com.plat.auto.test.autotest.service.LoginService;
import com.plat.auto.test.autotest.util.ArrayUtil;
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
import java.util.List;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/17
 * @description
 */
@Controller
@RequestMapping("/document")
@Api(tags = "1.0.0-SNAPSHOT", value = "文档接口")
@Slf4j
public class DocumenController {
    @Resource
    private DocumentMapper documentMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private MockMapper mockMapper;
    @Resource
    private TestHistoryMapper testHistoryMapper;
    @Resource
    private DataTypeService dataTypeService;

    private boolean hasBizPermission(HttpServletRequest request, int bizId) {
        User user = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (user.getType() == 1 || ArrayUtil.contains(StringUtil.split(user.getPermissionBiz(), ","),
                String.valueOf(bizId))) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/markStar")
    @ResponseBody
    @ApiOperation(value = "打标", notes = "打标")
    public ReturnT<String> markStar(HttpServletRequest request, int id, int starLevel) {
        Document document = documentMapper.load(id);
        if (document == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "ID 不存在");
        }
        Project project = projectMapper.load(id);
        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有操作权限");
        }
        document.setStarLevel(starLevel);
        int setRet = documentMapper.update(document);
        return setRet > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除", notes = "删除")
    public ReturnT<String> delete(HttpServletRequest request, int id) {
        Document document = documentMapper.load(id);
        if (document == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "ID 不存在");
        }

        Project project = projectMapper.load(id);
        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有操作权限");
        }
        List<TestHistory> historyList = testHistoryMapper.loadByDocumentId(id);
        if (historyList != null && historyList.size() > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "存在Test记录，不允许删除");
        }
        List<Mock> mockList = mockMapper.loadAll(id);
        if (mockList != null && mockList.size() > 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "存在Mock记录，不允许删除");
        }
        int deleteRet = documentMapper.deleteById(id);
        return deleteRet > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/addPage")
    @ApiOperation(value = "新增页面", notes = "新增页面")
    public String addPage(HttpServletRequest request, Model model, int projectId, @RequestParam(required = false,
            defaultValue = "0") int groupId) {
        Project project = projectMapper.load(projectId);
        if (project == null) {
            throw new RuntimeException("ID 不存在");
        }
        model.addAttribute("projectId", projectId);
        model.addAttribute("groupId", groupId);
        if (!hasBizPermission(request, project.getBizId())) {
            throw new RuntimeException("没有操作权限");
        }
        List<Group> groupList = (List<Group>) groupMapper.loadAll(projectId);
        model.addAttribute("groupList", groupList);

        model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
        model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
        model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
        model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

        return "document/document.add";
    }

    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation(value = "增加", notes = "增加")
    public ReturnT<Integer> add(HttpServletRequest request, Document document) {
        Project project = projectMapper.load(document.getProjectId());
        if (project == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "操作失败，项目不存在");
        }
        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "没有操作权限");
        }
        int addRet = documentMapper.add(document);
        return addRet > 0 ? new ReturnT<>(document.getId()) : new ReturnT<>(ReturnT.FAIL_CODE, null);
    }

    @RequestMapping("/updatePage")
    @ApiOperation(value = "更新页面", notes = "更新页面")
    public String updatePage(HttpServletRequest request, Model model, int id) {
        Document document = documentMapper.load(id);
        if (document == null) {
            throw new RuntimeException("ID 不存在");
        }
        model.addAttribute("document", document);
        model.addAttribute("requestHeadersList", (StringUtil.isNotBlank(document.getRequestHeaders())) ?
                JacksonUtil.readValue(document.getRequestHeaders(), List.class) : null);
        model.addAttribute("queryParamList", (StringUtil.isNotBlank(document.getQueryParams())) ?
                JacksonUtil.readValue(document.getQueryParams(), List.class) : null);
        model.addAttribute("responseParamList", (StringUtil.isNotBlank(document.getResponseParams())) ?
                JacksonUtil.readValue(document.getResponseParams(), List.class) : null);

        int projectId = document.getProjectId();
        model.addAttribute("projectId", projectId);
        Project project = projectMapper.load(document.getProjectId());
        if (!hasBizPermission(request, project.getBizId())) {
            throw new RuntimeException("没有操作权限");
        }
        List<Group> groupList = (List<Group>) groupMapper.loadAll(projectId);
        model.addAttribute("groupList", groupList);
        // enum
        model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
        model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
        model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
        model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

        // 响应数据类型
        DataType responseDatatypeRet = dataTypeService.loadDataType(document.getResponseDataTypeId());
        model.addAttribute("responseDatatype", responseDatatypeRet);

        return "document/document.update";
    }

    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新项目", notes = "更新项目")
    public ReturnT<String> update(HttpServletRequest request, Document document) {

        Document oldVo = documentMapper.load(document.getId());
        if (oldVo == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
        }

        // 权限
        Project project = projectMapper.load(oldVo.getProjectId());
        if (!hasBizPermission(request, project.getBizId())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
        }

        // fill not-change val
        document.setProjectId(oldVo.getProjectId());
        document.setStarLevel(oldVo.getStarLevel());
        document.setAddTime(oldVo.getAddTime());

        int ret = documentMapper.update(document);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }


    @RequestMapping("/detailPage")
    @ApiOperation(value = "项目详情页", notes = "项目详情页")
    public String detailPage(HttpServletRequest request, Model model, int id) {

        // document
        Document document = documentMapper.load(id);
        if (document == null) {
            throw new RuntimeException("操作失败，接口ID非法");
        }
        model.addAttribute("document", document);
        model.addAttribute("requestHeadersList", (StringUtil.isNotBlank(document.getRequestHeaders())) ?
                JacksonUtil.readValue(document.getRequestHeaders(), List.class) : null);
        model.addAttribute("queryParamList", (StringUtil.isNotBlank(document.getQueryParams())) ?
                JacksonUtil.readValue(document.getQueryParams(), List.class) : null);
        model.addAttribute("responseParamList", (StringUtil.isNotBlank(document.getResponseParams())) ?
                JacksonUtil.readValue(document.getResponseParams(), List.class) : null);

        // project
        int projectId = document.getProjectId();
        Project project = projectMapper.load(projectId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", project);

        // groupList
        List<Group> groupList = (List<Group>) groupMapper.loadAll(projectId);
        model.addAttribute("groupList", groupList);

        // mock list
        List<Mock> mockList = mockMapper.loadAll(id);
        model.addAttribute("mockList", mockList);

        // test list
        List<TestHistory> testHistoryList = testHistoryMapper.loadByDocumentId(id);
        model.addAttribute("testHistoryList", testHistoryList);

        // enum
        model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
        model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
        model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
        model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

        // 响应数据类型
        DataType responseDatatypeRet = dataTypeService.loadDataType(document.getResponseDataTypeId());
        model.addAttribute("responseDatatype", responseDatatypeRet);

        // 权限
        model.addAttribute("hasBizPermission", hasBizPermission(request, project.getBizId()));

        return "document/document.detail";
    }
}
