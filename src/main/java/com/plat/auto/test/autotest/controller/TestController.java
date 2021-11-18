package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.config.RequestConfig;
import com.plat.auto.test.autotest.entity.Document;
import com.plat.auto.test.autotest.entity.Project;
import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.entity.TestHistory;
import com.plat.auto.test.autotest.mapper.DocumentMapper;
import com.plat.auto.test.autotest.mapper.ProjectMapper;
import com.plat.auto.test.autotest.mapper.TestHistoryMapper;
import com.plat.auto.test.autotest.util.JacksonUtil;
import com.plat.auto.test.autotest.util.StringUtil;
import com.plat.auto.test.autotest.util.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/17
 * @description
 */
@Controller
@RequestMapping("/test")
@Api(tags = "1.0.0-SNAPSHOT", value = "测试记录相关")
@Slf4j
public class TestController {
    @Resource
    private DocumentMapper documentMapper;
    @Resource
    private TestHistoryMapper testHistoryMapper;
    @Resource
    private ProjectMapper projectMapper;

    @RequestMapping
    @ApiOperation(value = "初始化", notes = "初始化")
    public String index(Model model, int documentId, @RequestParam(required = false, defaultValue = "0") int testId) {


        // params
        Document document = documentMapper.load(documentId);
        if (document == null) {
            throw new RuntimeException("项目不存在");
        }
        Project project = projectMapper.load(document.getProjectId());

        List<Map<String, String>> requestHeaders;
        List<Map<String, String>> queryParams;

        if (testId > 0) {
            TestHistory testHistory = testHistoryMapper.load(testId);
            if (testHistory == null) {
                throw new RuntimeException("测试用例不存在");
            }
            model.addAttribute("testHistory", testHistory);

            requestHeaders = (StringUtil.isNotBlank(testHistory.getRequestHeaders())) ?
                    JacksonUtil.readValue(testHistory.getRequestHeaders(), List.class) : null;
            queryParams = (StringUtil.isNotBlank(testHistory.getQueryParams())) ?
                    JacksonUtil.readValue(testHistory.getQueryParams(), List.class) : null;
        } else {
            requestHeaders = (StringUtil.isNotBlank(document.getRequestHeaders())) ?
                    JacksonUtil.readValue(document.getRequestHeaders(), List.class) : null;
            queryParams = (StringUtil.isNotBlank(document.getQueryParams())) ?
                    JacksonUtil.readValue(document.getQueryParams(), List.class) : null;
        }

        model.addAttribute("document", document);
        model.addAttribute("project", project);
        model.addAttribute("requestHeaders", requestHeaders);
        model.addAttribute("queryParams", queryParams);
        model.addAttribute("documentId", documentId);
        model.addAttribute("testId", testId);

        // enum
        model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
        model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
        model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
        model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

        return "test/test.index";
    }

    @RequestMapping("/add")
    @ResponseBody
    @ApiOperation(value = "测试记录增加", notes = "测试记录增加")
    public ReturnT<Integer> add(TestHistory testHistory) {
        int ret = testHistoryMapper.add(testHistory);
        return ret > 0 ? new ReturnT<>(testHistory.getId()) : new ReturnT<>(ReturnT.FAIL_CODE, null);
    }

    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "测试记录更新", notes = "测试记录更新")
    public ReturnT<String> update(TestHistory testHistory) {
        int ret = testHistoryMapper.update(testHistory);
        return ret > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "测试记录删除", notes = "测试记录删除")
    public ReturnT<String> delete(int id) {
        int ret = testHistoryMapper.delete(id);
        return ret > 0 ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/run")
    @ResponseBody
    @ApiOperation(value = "执行测试", notes = "执行测试")
    public ReturnT<String> run(TestHistory testHistory, HttpServletRequest request, HttpServletResponse response) {

        // valid
        RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(testHistory.getRespType());
        if (contentType == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "响应数据类型(MIME)非法");
        }

        if (StringUtil.isBlank(testHistory.getRequestUrl())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请输入接口URL");
        }

        // request headers
        Map<String, String> requestHeaderMap = null;
        List<Map<String, String>> requestHeaders = (StringUtil.isNotBlank(testHistory.getRequestHeaders())) ?
                JacksonUtil.readValue(testHistory.getRequestHeaders(), List.class) : null;
        if (requestHeaders != null && requestHeaders.size() > 0) {
            requestHeaderMap = new HashMap<>();
            for (Map<String, String> item : requestHeaders) {
                requestHeaderMap.put(item.get("key"), item.get("value"));
            }
        }

        // query param
        Map<String, String> queryParamMap = null;
        List<Map<String, String>> queryParams = (StringUtil.isNotBlank(testHistory.getQueryParams())) ?
                JacksonUtil.readValue(testHistory.getQueryParams(), List.class) : null;
        if (queryParams != null && queryParams.size() > 0) {
            queryParamMap = new HashMap<>();
            for (Map<String, String> item : queryParams) {
                queryParamMap.put(item.get("key"), item.get("value"));
            }
        }

        // invoke 1/3
        HttpRequestBase remoteRequest;
        if (RequestConfig.RequestMethodEnum.POST.name().equals(testHistory.getRequestMethod())) {
            HttpPost httpPost = new HttpPost(testHistory.getRequestUrl());
            // query params
            if (queryParamMap != null) {
                List<NameValuePair> formParams = new ArrayList<>();
                for (Map.Entry<String, String> entry : queryParamMap.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                }
            }
            remoteRequest = httpPost;
        } else if (RequestConfig.RequestMethodEnum.GET.name().equals(testHistory.getRequestMethod())) {
            remoteRequest = new HttpGet(markGetUrl(testHistory.getRequestUrl(), queryParamMap));
        } else if (RequestConfig.RequestMethodEnum.PUT.name().equals(testHistory.getRequestMethod())) {
            remoteRequest = new HttpPut(markGetUrl(testHistory.getRequestUrl(), queryParamMap));
        } else if (RequestConfig.RequestMethodEnum.DELETE.name().equals(testHistory.getRequestMethod())) {
            remoteRequest = new HttpDelete(markGetUrl(testHistory.getRequestUrl(), queryParamMap));
        } else if (RequestConfig.RequestMethodEnum.HEAD.name().equals(testHistory.getRequestMethod())) {
            remoteRequest = new HttpHead(markGetUrl(testHistory.getRequestUrl(), queryParamMap));
        } else if (RequestConfig.RequestMethodEnum.OPTIONS.name().equals(testHistory.getRequestMethod())) {
            remoteRequest = new HttpOptions(markGetUrl(testHistory.getRequestUrl(), queryParamMap));
        } else if (RequestConfig.RequestMethodEnum.PATCH.name().equals(testHistory.getRequestMethod())) {
            remoteRequest = new HttpPatch(markGetUrl(testHistory.getRequestUrl(), queryParamMap));
        } else {
            return new ReturnT<>(ReturnT.FAIL_CODE, "请求方法非法");
        }

        // invoke 2/3
        if (requestHeaderMap != null && !requestHeaderMap.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                remoteRequest.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // write response
        String responseContent = remoteCall(remoteRequest);
        return new ReturnT<>(responseContent);
    }

    private String markGetUrl(String url, Map<String, String> queryParamMap) {
        StringBuilder finalUrl = new StringBuilder(url);
        if (queryParamMap != null && queryParamMap.size() > 0) {
            finalUrl = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : queryParamMap.entrySet()) {
                finalUrl.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            // 后缀处理，去除 ？ 或 & 符号
            finalUrl = new StringBuilder(finalUrl.substring(0, finalUrl.length() - 1));
        }
        return finalUrl.toString();
    }

    private String remoteCall(HttpRequestBase remoteRequest) {
        // remote test
        String responseContent = null;

        CloseableHttpClient httpClient = null;
        try {
            org.apache.http.client.config.RequestConfig requestConfig =
                    org.apache.http.client.config.RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            remoteRequest.setConfig(requestConfig);

            httpClient = HttpClients.custom().disableAutomaticRetries().build();

            // parse response
            HttpResponse response = httpClient.execute(remoteRequest);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseContent = EntityUtils.toString(entity, "UTF-8");
                } else {
                    responseContent = "请求状态异常：" + response.getStatusLine().getStatusCode();
                    if (statusCode == 302 && response.getFirstHeader("Location") != null) {
                        responseContent += "；Redirect地址：" + response.getFirstHeader("Location").getValue();
                    }

                }
                EntityUtils.consume(entity);
            }
            log.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            responseContent = "请求异常：" + ThrowableUtil.toString(e);
        } finally {
            if (remoteRequest != null) {
                remoteRequest.releaseConnection();
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return responseContent;
    }
}
