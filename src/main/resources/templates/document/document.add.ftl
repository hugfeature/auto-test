<!DOCTYPE html>
<html>
<head>
  	<title>自动化测试平台</title>
    <link rel="shortcut icon" href="${request.contextPath}/static/favicon.ico" type="image/x-icon" />
  	<#import "../common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/_all.css">
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.css">

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["adminlte_settings"]?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "projectList" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>新增接口</h1>
		</section>

        <section class="content">
            <form class="form-horizontal" id="ducomentForm" >
                <input type="hidden" name="projectId" value="${projectId}" >

                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:window.location.href='${request.contextPath}/group?projectId=${projectId}'" >返回接口列表</button>
                            <button class="btn btn-info btn-xs" type="submit" >保存接口</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">URL</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="requestUrl" placeholder="请输入接口URL（相对地址）" maxlength="100" >
                            </div>
                            <label class="col-sm-1 control-label">分组</label>
                            <div class="col-sm-4">
                                <select class="form-control select2" style="width: 100%;" name="groupId">
                                    <option value="0">默认分组</option>
                                <#if groupList?exists && groupList?size gt 0>
                                    <#list groupList as group>
                                        <option value="${group.id}" <#if groupId==group.id>selected</#if> >${group.name}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">Method</label>
                            <div class="col-sm-6">
                                <select class="form-control select2" style="width: 100%;" name="requestMethod">
                                <#list RequestMethodEnum as item>
                                    <option value="${item}">${item}</option>
                                </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">状态</label>
                            <div class="col-sm-4">
                                <input type="radio" class="iCheck" name="status" value="0" checked >启用  &nbsp;&nbsp;
                                <input type="radio" class="iCheck" name="status" value="1" >维护  &nbsp;&nbsp;
                                <input type="radio" class="iCheck" name="status" value="2" >废弃
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-1 control-label">名称</label>
                            <div class="col-sm-11">
                                <input type="text" class="form-control" name="name" placeholder="请输入接口名称" maxlength="50" >
                            </div>
                        </div>
                    </div>
                </div>

                <#--请求头部-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">请求头部</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" id="requestHeaders_add" ><i class="fa fa-plus"></i></button>
                        </div>
                    </div>

                    <div id="requestHeaders_example" style="display: none;" >
                        <div class="form-group requestHeaders_item" >
                            <label class="col-sm-1 control-label">Key</label>
                            <div class="col-sm-4 item">
                                <select class="form-control select2_tag_new key" >
                                    <option value=""></option>
                                    <#list requestHeadersEnum as item>
                                        <option value="${item}">${item}</option>
                                    </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">Value</label>
                            <div class="col-sm-5 item">
                                <input type="text" class="form-control value">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="requestHeaders_parent" >
                    </div>
                </div>

                <#--请求参数-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">请求参数</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" id="queryParams_add" ><i class="fa fa-plus"></i></button>
                        </div>
                    </div>

                    <div id="queryParams_example" style="display: none;" >
                        <div class="form-group queryParams_item" >
                            <label class="col-sm-1 control-label">参数</label>
                            <div class="col-sm-2 item">
                                <input type="text" class="form-control name">
                            </div>
                            <label class="col-sm-1 control-label">说明</label>
                            <div class="col-sm-2 item">
                                <input type="text" class="form-control desc">
                            </div>
                            <label class="col-sm-1 control-label">类型</label>
                            <div class="col-sm-2 item">
                                <select class="form-control select2_tag_new type" style="width: 100%;">
                                    <#list QueryParamTypeEnum as item>
                                        <option value="${item}">${item}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="col-sm-2 item">
                                <select class="form-control select2_tag_new notNull" style="width: 100%;">
                                    <option value="true">必填</option>
                                    <option value="false">非必填</option>
                                </select>
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="queryParams_parent" >
                    </div>
                </div>

                <#--响应结果-->
                <div class="nav-tabs-custom">
                    <!-- Tabs within a box -->
                    <ul class="nav nav-tabs pull-right">
                        <li><a href="#fail_resp" data-toggle="tab">失败响应结果</a></li>
                        <li class="active"><a href="#success_resp" data-toggle="tab">成功响应结果</a></li>
                        <li class="pull-left header">响应结果</li>
                    </ul>
                    <div class="tab-content no-padding">
                        <!-- Morris chart - Sales -->
                        <div class="chart tab-pane active" id="success_resp" style="position: relative; height: 365px;">
                            <div class="box-body">
                                响应数据类型(MIME)：
                                <#list ResponseContentType as item>
                                    <input type="radio" class="iCheck" name="successRespType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                                </#list>
                                <button type="button" class="btn btn-box-tool pull-right" id="successRespExample_2json" >JSON格式化</button>
                                <br>
                                <textarea name="successRespExample" id="successRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" ></textarea>
                            </div>
                        </div>
                        <div class="chart tab-pane" id="fail_resp" style="position: relative; height: 365px;">
                            <div class="box-body">
                                响应数据类型(MIME)：
                                <#list ResponseContentType as item>
                                    <input type="radio" class="iCheck" name="failRespType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                                </#list>
                                <button type="button" class="btn btn-box-tool pull-right" id="failRespExample_2json" >JSON格式化</button>
                                <br>
                                <textarea name="failRespExample" id="failRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" ></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                

                <#--响应数据类型-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">响应数据类型</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open('${request.contextPath}/datatype/addDataTypePage');" >+ 新增数据类型</button>
                        </div>
                    </div>
                    <div class="box-body" >

                        <label class="col-sm-2 control-label">数据类型</label>
                        <div class="col-sm-4 item">
                            <select class="form-control" style="width: 100%;" id="responseDatatypeId" name="responseDatatypeId"  >
                            </select>
                        </div>

                    </div>
                </div>


                <#-- 接口备注 -->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">接口备注</h3>
                    </div>
                    <div class="box-body" >
                        <div class="box-body pad" id="remark" >
                        </div>
                    </div>
                </div>

            </form>

        </section>

	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />

<script src="${request.contextPath}/static/adminlte/plugins/select2/select2.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.js"></script>
<script src="${request.contextPath}/static/js/document.add.1.js"></script>
</body>
</html>