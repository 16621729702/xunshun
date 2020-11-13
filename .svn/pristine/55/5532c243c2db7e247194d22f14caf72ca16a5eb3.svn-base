
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
            | 多商户基础设置            </title>
        <meta name="csrf-token" content="qy7aokAJzaWKSWBkJuSchDiG4PMlsBTZa4UD5KgO">
        <meta name="request-type" content="page">
        <link href="${request.contextPath}/web/css/bootstrap.css" rel="stylesheet">
        <link href="${request.contextPath}/web/font-awesome/css/font-awesome.css" rel="stylesheet">
        <link href="${request.contextPath}/lib/Hui/css/iconfont.css" rel="stylesheet">
        <link href="${request.contextPath}/web/layui/css/layui.css?v=2212" rel="stylesheet">
        <link href="${request.contextPath}/web/css/animate.css" rel="stylesheet">
        <link href="${request.contextPath}/web/css/style.css?v=12223" rel="stylesheet">
        <link href="${request.contextPath}/web/css/main.css?v=666" rel="stylesheet">
        <link href="${request.contextPath}/lib/toastr/toastr.min.css" rel="stylesheet">
        <link href="${request.contextPath}/web/we7/resource/css/common.css" rel="stylesheet">
        <link href="${request.contextPath}/web/css/fix.css?v=121" rel="stylesheet">
        <script type="text/javascript">
            if(navigator.appName == 'Microsoft Internet Explorer'){
                if(navigator.userAgent.indexOf("MSIE 5.0")>0 || navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
                    alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                }
            }
        </script>
    </head>
    <body >

        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
            <div id="page-wrapper" class="gray-bg">
            <div class="wrapper wrapper-content animated fadeInRight">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="ibox ">
                            <div class="ibox-content">
                                <form class="layui-form form-horizontal" id="ajaxform"
                                      action="" method="post"
                                      enctype="multipart/form-data">
                                    <input type="hidden"  name="id" value="${id!}" >
                                    <div class="tabs-container">
                                        <div class="tab-content">
                                            <div class="panel-body">
                                                <div class="row">
                                                    <div class="col-lg-12">
                                                        <div class="ibox ">
                                                            <div class="tabs-container">
                                                                <ul class="nav nav-tabs" role="tablist">
                                                                    <li><a class="nav-link active show" data-toggle="tab" href="#tab-1">基本</a></li>
                                                                    <li><a class="nav-link" data-toggle="tab" href="#tab-2">入驻协议</a></li>
                                                                </ul>
                                                                <div class="tab-content">
                                                                    <#if config??>
                                                                        <div role="tabpanel" id="tab-1" class="tab-pane active show">
                                                                            <div class="panel-body">
                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">是否开启申请</label>
                                                                                    <div class="layui-input-block">
                                                                                        <#if config.open_apply??>
                                                                                            <input type="radio" lay-filter="open_apply" ${(config.open_apply=='1')?string('checked','')} name="open_apply"  value="1" title="开启">
                                                                                            <input type="radio" lay-filter="open_apply" ${(config.open_apply=='0')?string('checked','')} name="open_apply" value="0" title="关闭">
                                                                                        <#else >
                                                                                            <input type="radio" lay-filter="open_apply" name="open_apply"  value="1" title="开启">
                                                                                            <input type="radio" lay-filter="open_apply"  name="open_apply" value="0" title="关闭">
                                                                                        </#if>
                                                                                        <div class="form-text">是否开启商户申请入驻</div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">申请条件</label>
                                                                                    <div class="layui-input-block">

                                                                                        <#if config.apply_limit??>
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="1" ${(config.apply_limit=='1')?string('checked','')} title="无条件">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="2" ${(config.apply_limit=='2')?string('checked','')} title="付费">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="3" ${(config.apply_limit=='3')?string('checked','')} title="消费限制">
                                                                                        <#else >
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="1" title="无条件">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="2" title="付费">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="3" title="消费限制">
                                                                                        </#if>
                                                                                        <div class="form-text"></div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="layui-form-item apply_limit_box2 apply_limit_box"
                                                                                     style="display: none" >
                                                                                    <label class="layui-form-label"></label>
                                                                                    <div class="layui-input-block">
                                                                                        <div class="input-group m-b" style="width: 300px">
                                                                                            <div class="input-group-prepend">
                                                                                                <span class="input-group-addon">支付</span>
                                                                                            </div>
                                                                                            <input type="text" class="form-control" name="apply_limit_pay_money"
                                                                                                   value="${config.apply_limit_pay_money!}">
                                                                                            <div class="input-group-append">
                                                                                                <span class="input-group-addon">元</span>
                                                                                            </div>
                                                                                            <div class="form-text m-b-none">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="layui-form-item apply_limit_box3 apply_limit_box"
                                                                                     style="display: none" >
                                                                                    <label class="layui-form-label"></label>
                                                                                    <div class="layui-input-block">
                                                                                        <div class="input-group m-b" style="width: 300px">
                                                                                            <div class="input-group-prepend">
                                                                                                <span class="input-group-addon">消费满</span>
                                                                                            </div>
                                                                                            <input type="text" class="form-control" name="apply_limit_buy_monty"
                                                                                                   value="${config.apply_limit_buy_monty!}">
                                                                                            <div class="input-group-append">
                                                                                                <span class="input-group-addon">元</span>
                                                                                            </div>
                                                                                            <div class="form-text m-b-none">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">是否需要审核</label>
                                                                                    <div class="layui-input-block">

                                                                                        <#if config.need_check??>
                                                                                            <input type="radio" lay-filter="need_check" name="need_check" value="1" ${(config.need_check=='1')?string('checked','')} title="需要">
                                                                                            <input type="radio" lay-filter="need_check" name="need_check" value="0" ${(config.need_check=='0')?string('checked','')} title="不需要">
                                                                                        <#else >
                                                                                            <input type="radio" lay-filter="need_check" name="need_check" value="1" title="需要">
                                                                                            <input type="radio" lay-filter="need_check" name="need_check" value="0" title="不需要">
                                                                                        </#if>
                                                                                        <div class="form-text">前台开店申请是否需要后台审核</div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">店铺有效期</label>
                                                                                    <div class="layui-input-block">
                                                                                        <div class="input-group m-b" style="width: 300px">
                                                                                            <#if config.valid_type??>
                                                                                                <input lay-filter="valid_type" type="radio" name="valid_type" value="1" ${(config.valid_type=='1')?string('checked','')} title="永久">
                                                                                                <input lay-filter="valid_type" type="radio" name="valid_type" value="2" ${(config.valid_type=='2')?string('checked','')} title="月">
                                                                                            <#else >
                                                                                                <input type="radio" name="valid_type" value="1" title="永久"  lay-filter="valid_type">
                                                                                                <input type="radio" name="valid_type" value="2" title="月"  lay-filter="valid_type">
                                                                                            </#if>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>


                                                                                <div class="layui-form-item valid_type_box2 valid_type_box"
                                                                                     style="display: none" >
                                                                                    <label class="layui-form-label"></label>
                                                                                    <div class="layui-input-block">
                                                                                        <div class="input-group m-b" style="width: 300px">
                                                                                            <input type="text" class="form-control" name="valid_month"
                                                                                                   value="${config.valid_month!}">
                                                                                            <div class="input-group-append">
                                                                                                <span class="input-group-addon">月</span>
                                                                                            </div>
                                                                                            <div class="form-text m-b-none">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label">宣传图文</label>
                                                                                    <div class="layui-input-block">
                                                                                        <textarea class="layui-textarea" name="intro" id="intro" lay-verify="intro" style="display:none">${config.intro!}</textarea>
                                                                                    </div>
                                                                                </div>
<#--                                                                                <div class="layui-form-item">-->
<#--                                                                                    <label class="layui-form-label">入驻表单</label>-->
<#--                                                                                    <div class="layui-input-block">-->
<#--                                                                                        <#if config.is_apply_diyform??>-->
<#--                                                                                        <input type="radio" name="is_apply_diyform" value="1" title="是" ${(config.is_apply_diyform=='1')?string('checked','')} lay-filter="is_apply_diyform">-->
<#--                                                                                        <input type="radio" name="is_apply_diyform" value="0" title="否" ${(config.is_apply_diyform=='0')?string('checked','')} lay-filter="is_apply_diyform">-->
<#--                                                                                            <#else >-->
<#--                                                                                                <input type="radio" name="is_apply_diyform" value="1" title="是"  lay-filter="is_apply_diyform">-->
<#--                                                                                                <input type="radio" name="is_apply_diyform" value="0" title="否"  lay-filter="is_apply_diyform">-->
<#--                                                                                        </#if>-->

<#--                                                                                        <div class="form-text">申请时需要填写的资料</div>-->
<#--                                                                                    </div>-->
<#--                                                                                </div>-->
<#--                                                                                <div class="layui-form-item apply_diyform_box"  style="display: none" >-->
<#--                                                                                    <label class="layui-form-label">选择表单</label>-->
<#--                                                                                    <div class="layui-input-block">-->
<#--                                                                                        <select name="apply_diyform" lay-verify="required" lay-filter="code_type">-->
<#--                                                                                            <option value="0">请选择自定义表单</option>-->
<#--                                                                                        </select>-->
<#--                                                                                    </div>-->
<#--                                                                                </div>-->
                                                                            </div>
                                                                        </div>
                                                                        <div role="tabpanel" id="tab-2" class="tab-pane">
                                                                            <div class="panel-body">
                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">入驻申请协议</label>
                                                                                    <div class="layui-input-block">
                                                                                        <#if config.open_protocol??>
                                                                                            <input type="radio" name="open_protocol" value="1" title="显示" ${(config.open_protocol=='1')?string('checked','')}>
                                                                                        <input type="radio" name="open_protocol" value="0" title="隐藏" ${(config.open_protocol=='0')?string('checked','')}>
                                                                                        <#else >
                                                                                            <input type="radio" name="open_protocol" value="1" title="显示">
                                                                                            <input type="radio" name="open_protocol" value="0" title="隐藏">
                                                                                        </#if>
                                                                                        <div class="form-text">是否显示入驻申请协议</div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">协议名称</label>
                                                                                    <div class="layui-input-block">
                                                                                        <input type="text" name="apply_protocol_title" class="layui-input" value="${config.apply_protocol_title!}">
                                                                                    </div>
                                                                                </div>
                                                                                <div class="layui-form-item">
                                                                                    <label class="layui-form-label ">协议内容</label>
                                                                                    <div class="layui-input-block">
                                                                                        <textarea class="layui-textarea" name="apply_protocol_content" id="apply_protocol_content" lay-verify="apply_protocol_content" style="display:none">${config.apply_protocol_content!}</textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <#else>
                                                                            <div role="tabpanel" id="tab-1" class="tab-pane active show">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">是否开启申请</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="radio" lay-filter="open_apply" name="open_apply"  value="1" title="开启">
                                                                                            <input type="radio" lay-filter="open_apply" name="open_apply" value="0" title="关闭">
                                                                                            <div class="form-text">是否开启商户申请入驻</div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">申请条件</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="1"
                                                                                                   title="无条件">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="2"
                                                                                                   title="付费">
                                                                                            <input type="radio" lay-filter="apply_limit" name="apply_limit" value="3"
                                                                                                   title="消费限制">
                                                                                            <div class="form-text"></div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item apply_limit_box2 apply_limit_box"
                                                                                         style="display: none" >
                                                                                        <label class="layui-form-label"></label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <div class="input-group-prepend">
                                                                                                    <span class="input-group-addon">支付</span>
                                                                                                </div>
                                                                                                <input type="text" class="form-control" name="apply_limit_pay_money"
                                                                                                       value="">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">元</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item apply_limit_box3 apply_limit_box"
                                                                                         style="display: none" >
                                                                                        <label class="layui-form-label"></label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <div class="input-group-prepend">
                                                                                                    <span class="input-group-addon">消费满</span>
                                                                                                </div>
                                                                                                <input type="text" class="form-control" name="apply_limit_buy_monty" value="">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">元</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">是否需要审核</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="radio" lay-filter="need_check" name="need_check" value="1"
                                                                                                   title="需要">
                                                                                            <input type="radio" lay-filter="need_check" name="need_check" value="0"
                                                                                                   title="不需要">
                                                                                            <div class="form-text">前台开店申请是否需要后台审核</div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">店铺有效期</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input lay-filter="valid_type" type="radio" name="valid_type" value="1"
                                                                                                       title="永久">
                                                                                                <input lay-filter="valid_type" type="radio" name="valid_type" value="2"
                                                                                                       title="月">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>


                                                                                    <div class="layui-form-item valid_type_box2 valid_type_box"
                                                                                         style="display: none" >
                                                                                        <label class="layui-form-label"></label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="valid_month"
                                                                                                       value="">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">月</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>


                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label">宣传图文</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea class="layui-textarea" name="intro" id="intro" lay-verify="intro" style="display:none"></textarea>
                                                                                        </div>
                                                                                    </div>
<#--                                                                                    <div class="layui-form-item">-->
<#--                                                                                        <label class="layui-form-label">入驻表单</label>-->
<#--                                                                                        <div class="layui-input-block">-->
<#--                                                                                            <input type="radio" name="is_apply_diyform" value="1" title="是" lay-filter="is_apply_diyform">-->
<#--                                                                                            <input type="radio" name="is_apply_diyform" value="0" title="否" lay-filter="is_apply_diyform">-->
<#--                                                                                            <div class="form-text">申请时需要填写的资料</div>-->
<#--                                                                                        </div>-->
<#--                                                                                    </div>-->
<#--                                                                                    <div class="layui-form-item apply_diyform_box"  style="display: none" >-->
<#--                                                                                        <label class="layui-form-label">选择表单</label>-->
<#--                                                                                        <div class="layui-input-block">-->
<#--                                                                                            <select name="apply_diyform" lay-verify="required" lay-filter="code_type">-->
<#--                                                                                                <option value="0">请选择自定义表单</option>-->
<#--                                                                                            </select>-->
<#--                                                                                        </div>-->
<#--                                                                                    </div>-->
                                                                                </div>
                                                                            </div>
                                                                            <div role="tabpanel" id="tab-2" class="tab-pane">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">入驻申请协议</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="radio" name="config[open_protocol]" value="1"
                                                                                                   title="显示">
                                                                                            <input type="radio" name="config[open_protocol]" value="0"
                                                                                                   title="隐藏">
                                                                                            <div class="form-text">是否显示入驻申请协议</div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">协议名称</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="text" name="config[apply_protocol_title]" class="layui-input"
                                                                                                   value="">
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">协议内容</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea
                                                                                                    class="layui-textarea"
                                                                                                    name="apply_protocol_content"
                                                                                                    id="apply_protocol_content"
                                                                                                    lay-verify="apply_protocol_content"
                                                                                                    style="display:none"></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>

                                                                    </#if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <div class="offset-sm-2 col-sm-4">
                                                                    <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>保存更改
                                                                    </button>
                                                                    <button class="btn btn-white" type="reset">取消</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="footer">
            </div>
            </div>
        </div>
        <!-- Mainly scripts -->
        <script src="${request.contextPath}/web/js/jquery-1.11.1.min.js"></script>
        <script src="${request.contextPath}/web/js/popper.min.js"></script>
        <script src="${request.contextPath}/web/js/bootstrap.min.js"></script>
        <script src="${request.contextPath}/web/js/plugins/metisMenu/jquery.metisMenu.js"></script>
        <script src="${request.contextPath}/web/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="${request.contextPath}/web/js/plugins/iCheck/icheck.min.js"></script>
        <!-- Custom and plugin javascript -->
        <script src="${request.contextPath}/web/js/inspinia.js"></script>
        <script src="${request.contextPath}/web/js/plugins/pace/pace.min.js"></script>
        <script src="${request.contextPath}/web/js/jquery.form.min.js"></script>
        <script src="${request.contextPath}/web/layui/layui.js"></script>
        <script src="${request.contextPath}/lib/toastr/toastr.min.js"></script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <script>
            $(document).ready(function () {
                layui.use(["layer", "form"], function () {
                    var layer = layui.layer;
                    var form = layui.form, $ = layui.jquery;
                    form.render();
                });
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
            });
        </script>
        <script>
            layui.use(["layer", "form"], function () {
                var form = layui.form;
                layui.use('layedit', function () {
                    var layedit = layui.layedit;
                    var index = layedit.build('intro');
                    var index2 = layedit.build('apply_protocol_content');
                    form.verify({
                        intro: function () {
                            layedit.sync(index);
                        },
                        apply_protocol_content: function () {
                            layedit.sync(index2);
                        }
                    });
                });
                form.render();
                form.on('radio(apply_limit)', function (data) {
                    var value = data.value;
                    $(".apply_limit_box").hide();
                    $(".apply_limit_box" + value).show();
                });
                form.on('radio(valid_type)', function (data) {
                    var value = data.value;
                    $(".valid_type_box").hide();
                    $(".valid_type_box" + value).show();
                });
                form.on('radio(is_apply_diyform)', function (data) {
                    var value = data.value;
                    if(value==1){
                        $(".apply_diyform_box").show();
                    } else {
                        $(".apply_diyform_box").hide();
                    }
                });

                form.on('submit(*)',function (data) {
                    $.ajax({
                        url:"${request.contextPath}/merch/intoSettingsave",
                        data:$("#ajaxform").serialize(),
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                location.href="${request.contextPath}/merch/intoSetting";//回到父级页面
                            }else{
                                layer.open({
                                    title: '警告'
                                    ,content: result.msg
                                });
                            }
                        }});
                });
            });
        </script>
    </body>
</html>
