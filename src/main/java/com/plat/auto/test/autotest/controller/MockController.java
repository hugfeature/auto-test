package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.config.RequestConfig;
import com.plat.auto.test.autotest.controller.annotation.PermessionLimit;
import com.plat.auto.test.autotest.entity.Document;
import com.plat.auto.test.autotest.entity.Mock;
import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.mapper.DocumentMapper;
import com.plat.auto.test.autotest.mapper.MockMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/17
 * @description
 */
@Controller
@RequestMapping("/mock")
@Api(tags = "1.0.0-SNAPSHOT", value = "mock接口")
@Slf4j
public class MockController {
    @Resource
    private MockMapper mockMapper;
    @Resource
    private DocumentMapper documentMapper;

    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation(value = "增加接口", notes = "增加")
    public ReturnT<String> add(Mock mock) {

        Document document = documentMapper.load(mock.getDocumentId());
        if (document == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "保存Mock数据失败，ID为空");
        }
        String uuid = UUID.randomUUID().toString();

        mock.setUuid(uuid);
        int ret = mockMapper.add(mock);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除接口", notes = "删除")
    public ReturnT<String> delete(int id) {
        int ret = mockMapper.delete(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新接口", notes = "更新")
    public ReturnT<String> update(Mock mock) {
        int ret = mockMapper.update(mock);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/run/{uuid}")
    @ApiOperation(value = "执行接口", notes = "执行")
    @PermessionLimit(limit = false)
    public void run(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
        Mock mock = mockMapper.loadByUuid(uuid);
        if (mock == null) {
            throw new RuntimeException("ID不存在");
        }

        RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(mock.getRespType());
        if (contentType == null) {
            throw new RuntimeException("Mock数据响应数据类型(MIME)为空");
        }

        // write response
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(contentType.type);

            PrintWriter writer = response.getWriter();
            writer.write(mock.getRespExample());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
