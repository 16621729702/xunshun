<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
            | 多商户基础设置 </title>
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
            if (navigator.appName == 'Microsoft Internet Explorer') {
                if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                    alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                }
            }
        </script>
    </head>
    <body>
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
        <script>
            $(document).ready(function () {
                layui.use(["layer", "form"], function () {
                    var form = layui.form;
                    var layer = layui.layer;
                    var form = layui.form, $ = layui.jquery;
                    layui.use('layedit', function () {
                        var layedit = layui.layedit;
                        var index = layedit.build('integrity_approve');
                        var index2 = layedit.build('integrity_service');
                        form.verify({
                            integrity_approve: function () {
                                layedit.sync(index);
                            },
                            integrity_service: function () {
                                layedit.sync(index2);
                            }
                        });
                    });
                    form.render();
                    form.on('submit(*)',function (data) {
                        $.ajax({
                            url:"${request.contextPath}/merch/baseSettingsave",
                            data:$("#ajaxform").serialize(),
                            dataType:"json",
                            success:function(result){
                                if(result.code == 200){
                                    location.href="${request.contextPath}/merch/baseSetting";//回到父级页面
                                }else{
                                    layer.open({
                                        title: '警告'
                                        ,content: result.msg
                                    });
                                }
                            }});
                    });
                });
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });
        </script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
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
                                        <input type="hidden" name="id" value="${id!}">
                                        <div class="tabs-container">
                                            <div class="tab-content">
                                                <div class="panel-body">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <div class="ibox ">
                                                                <div class="tabs-container">
                                                                    <ul class="nav nav-tabs" role="tablist">
                                                                        <li><a class="nav-link active show" data-toggle="tab" href="#tab-1">基本</a></li>
                                                                        <li><a class="nav-link" data-toggle="tab" href="#tab-2">诚信商家</a></li>
                                                                    </ul>
                                                                    <div class="tab-content">
                                                                        <#if config??>
                                                                            <div role="tabpanel" id="tab-1"
                                                                                 class="tab-pane active show">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">是否开启多商户</label>
                                                                                        <div class="layui-input-block">

                                                                                            <#if config.is_deduct_commission??>
                                                                                                <input type="radio" name="enable" ${(config.enable=='1')?string('checked','')} value="1" title="开启">
                                                                                                <input type="radio" name="enable" ${(config.enable=='0')?string('checked','')} value="0" title="不开启">
                                                                                            <#else >
                                                                                                <input type="radio" name="enable" value="1" title="开启">
                                                                                                <input type="radio" name="enable" value="0" title="不开启">
                                                                                            </#if>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户的可提现金额是否扣除分销佣金</label>
                                                                                        <div class="layui-input-block">
                                                                                            <#if config.is_deduct_commission??>
                                                                                                <input type="radio" name="is_deduct_commission" ${(config.is_deduct_commission=='0')?string('checked','')} value="0" title="不扣除">
                                                                                                <input type="radio" name="is_deduct_commission" ${(config.is_deduct_commission=='1')?string('checked','')} value="1" title="扣除">
                                                                                            <#else >
                                                                                                <input type="radio" name="is_deduct_commission" value="0" title="不扣除">
                                                                                                <input type="radio" name="is_deduct_commission" value="1" title="扣除">
                                                                                            </#if>
                                                                                            <div class="form-text m-b-none">
                                                                                                例如: 订单金额100元,分销佣金10元
                                                                                                不扣除: 商户的可提现金额为100元
                                                                                                扣除: 商户的可提现金额为90元
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户税率</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b"
                                                                                                 style="width: 300px">
                                                                                                <input type="text"
                                                                                                       class="form-control"
                                                                                                       name="settlement_rate"
                                                                                                       value="${config.settlement_rate!}">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">%</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                    百分比，如百分之十税率，商家商品为标价100元，实际到商家账户为90元
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户提现方式</label>
                                                                                        <div class="layui-input-block">
                                                                                            <#if config.withdraw_weixin??>
                                                                                                <input type="checkbox" name="withdraw_weixin" ${(config.withdraw_weixin=='1')?string('checked','')} title="提现到微信钱包" value="1">
                                                                                            <#else >
                                                                                                <input type="checkbox" name="withdraw_weixin" title="提现到微信钱包" value="1">
                                                                                            </#if>
                                                                                            <div class="layui-unselect layui-form-checkbox">
                                                                                                <span>提现到微信钱包</span>
                                                                                                <i class="layui-icon layui-icon-ok"></i>
                                                                                            </div>
                                                                                            <#if config.withdraw_alipay??>
                                                                                                <input type="checkbox" name="withdraw_alipay" ${(config.withdraw_alipay=='1')?string('checked','')} title="手动提现到支付宝" value="1">
                                                                                                <#else >
                                                                                                <input type="checkbox" name="withdraw_alipay" title="手动提现到支付宝" value="1">
                                                                                            </#if>
                                                                                            <div class="layui-unselect layui-form-checkbox">
                                                                                                <span>手动提现到支付宝</span>
                                                                                                <i class="layui-icon layui-icon-ok"></i>
                                                                                            </div>
                                                                                            <#if config.withdraw_bank??>
                                                                                                <input type="checkbox" name="withdraw_bank" ${(config.withdraw_bank=='1')?string('checked','')} title="手动提现到银行卡" value="1">
                                                                                            <#else >
                                                                                                <input type="checkbox" name="withdraw_bank" title="手动提现到银行卡" value="1">
                                                                                            </#if>
                                                                                            <div class="layui-unselect layui-form-checkbox">
                                                                                                <span>手动提现到银行卡</span>
                                                                                                <i class="layui-icon layui-icon-ok"></i>
                                                                                            </div>
                                                                                            <div class="form-text m-b-none">
                                                                                                支持的银行请到<a class="link-color" href="" target="_blank">【银行设置】</a>
                                                                                                进行设置,使用手动提现到银行卡必须设置支持的银行
                                                                                                提示: 提现方式支持多选
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户提现限制</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b"
                                                                                                 style="width: 300px">
                                                                                                <input type="text"
                                                                                                       class="form-control"
                                                                                                       name="withdraw_limit"
                                                                                                       value="${config.withdraw_limit!}">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">元</span>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="form-text m-b-none">
                                                                                            商户满多少才能提现,空或0不限制
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>

                                                                            <div role="tabpanel" id="tab-2"
                                                                                 class="tab-pane">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label">文案替换</label>
                                                                                        <div class="layui-input-block row">
                                                                                            <div class="input-group m-b col-md-6"
                                                                                                 style="padding-left: 0px">
                                                                                                <div class="input-group-prepend">
                                                                                                    <span class="input-group-addon"
                                                                                                          style="line-height: inherit">诚信商家</span>
                                                                                                </div>
                                                                                                <input type="text"
                                                                                                       name="integrity_text"
                                                                                                       class="form-control"
                                                                                                       value="${config.integrity_text!}">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label">认证金额</label>
                                                                                        <div class="layui-input-block col-sm-3"
                                                                                             style="padding-left: 0px">
                                                                                            <div class="input-group m-b">
                                                                                                <input type="text"
                                                                                                       class="form-control"
                                                                                                       name="integrity_money"
                                                                                                       value="${config.integrity_money!}">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">元</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">认证文案</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea
                                                                                                    class="layui-textarea"
                                                                                                    name="integrity_approve"
                                                                                                    id="integrity_approve"
                                                                                                    lay-verify="integrity_approve"
                                                                                                    style="display:none">${config.integrity_approve!}</textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">服务保障</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea
                                                                                                    class="layui-textarea"
                                                                                                    name="integrity_service"
                                                                                                    id="integrity_service"
                                                                                                    lay-verify="integrity_service"
                                                                                                    style="display:none">${config.integrity_service!}</textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>

                                                                        <#else >
                                                                            <div role="tabpanel" id="tab-1"
                                                                                 class="tab-pane active show">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">是否开启多商户</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="radio"
                                                                                                   name="enable"
                                                                                                   value="1" title="开启">
                                                                                            <input type="radio"
                                                                                                   name="enable"
                                                                                                   value="0"
                                                                                                   title="不开启">
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户的可提现金额是否扣除分销佣金</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="radio"
                                                                                                   name="is_deduct_commission"
                                                                                                   value="0"
                                                                                                   title="不扣除">
                                                                                            <input type="radio"
                                                                                                   name="is_deduct_commission"
                                                                                                   value="1" title="扣除">
                                                                                            <div class="form-text m-b-none">
                                                                                                例如: 订单金额100元,分销佣金10元
                                                                                                不扣除: 商户的可提现金额为100元
                                                                                                扣除: 商户的可提现金额为90元
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户税率</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b"
                                                                                                 style="width: 300px">
                                                                                                <input type="text"
                                                                                                       class="form-control"
                                                                                                       name="settlement_rate"
                                                                                                       value="">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">%</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                    百分比，如百分之十税率，商家商品为标价100元，实际到商家账户为90元
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户提现方式</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="checkbox"
                                                                                                   name="withdraw_weixin"
                                                                                                   title="提现到微信钱包"
                                                                                                   value="1">
                                                                                            <div class="layui-unselect layui-form-checkbox">
                                                                                                <span>提现到微信钱包</span>
                                                                                                <i class="layui-icon layui-icon-ok"></i>
                                                                                            </div>
                                                                                            <input type="checkbox"
                                                                                                   name="withdraw_alipay"
                                                                                                   title="手动提现到支付宝"
                                                                                                   value="1">
                                                                                            <div class="layui-unselect layui-form-checkbox">
                                                                                                <span>手动提现到支付宝</span>
                                                                                                <i class="layui-icon layui-icon-ok"></i>
                                                                                            </div>
                                                                                            <input type="checkbox"
                                                                                                   name="withdraw_bank"
                                                                                                   title="手动提现到银行卡"
                                                                                                   value="1">
                                                                                            <div class="layui-unselect layui-form-checkbox">
                                                                                                <span>手动提现到银行卡</span>
                                                                                                <i class="layui-icon layui-icon-ok"></i>
                                                                                            </div>
                                                                                            <div class="form-text m-b-none">
                                                                                                支持的银行请到<a
                                                                                                        class="link-color"
                                                                                                        href=""
                                                                                                        target="_blank">【银行设置】</a>
                                                                                                进行设置,使用手动提现到银行卡必须设置支持的银行
                                                                                                提示: 提现方式支持多选
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label label140width">商户提现限制</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b"
                                                                                                 style="width: 300px">
                                                                                                <input type="text"
                                                                                                       class="form-control"
                                                                                                       name="withdraw_limit"
                                                                                                       value="">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">元</span>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="form-text m-b-none">
                                                                                            商户满多少才能提现,空或0不限制
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>

                                                                            <div role="tabpanel" id="tab-2"
                                                                                 class="tab-pane">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label">文案替换</label>
                                                                                        <div class="layui-input-block row">
                                                                                            <div class="input-group m-b col-md-6"
                                                                                                 style="padding-left: 0px">
                                                                                                <div class="input-group-prepend">
                                                                                                    <span class="input-group-addon"
                                                                                                          style="line-height: inherit">诚信商家</span>
                                                                                                </div>
                                                                                                <input type="text"
                                                                                                       name="integrity_text"
                                                                                                       class="form-control"
                                                                                                       value="">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label">认证金额</label>
                                                                                        <div class="layui-input-block col-sm-3"
                                                                                             style="padding-left: 0px">
                                                                                            <div class="input-group m-b">
                                                                                                <input type="text"
                                                                                                       class="form-control"
                                                                                                       name="integrity_money"
                                                                                                       value="">
                                                                                                <div class="input-group-append">
                                                                                                    <span class="input-group-addon">元</span>
                                                                                                </div>
                                                                                                <div class="form-text m-b-none">
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">认证文案</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea
                                                                                                    class="layui-textarea"
                                                                                                    name="integrity_approve"
                                                                                                    id="integrity_approve"
                                                                                                    lay-verify="integrity_approve"
                                                                                                    style="display:none"></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">服务保障</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea
                                                                                                    class="layui-textarea"
                                                                                                    name="integrity_service"
                                                                                                    id="integrity_service"
                                                                                                    lay-verify="integrity_service"
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
    </body>
</html>
