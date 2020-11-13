<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 | 系统配置 </title>
        <meta name="csrf-token" content="8o3fjX43jo46GsxyJyP9QAyXFduFCzcn7BRi9aZB">
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
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
            <div id="page-wrapper" class="gray-bg">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="layui-form form-horizontal" id="ajaxform"
                                  action="" method="post"
                                  enctype="multipart/form-data">
                                <#if configs??>
                                    <input type="hidden" name="id" value="${configs.id!}">
                                </#if>
                                <div class="tabs-container">
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/basic">基础配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/flow">推流配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/chat">聊天配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/sms">短信配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/pay">支付配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/logistics">物流配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link active" href="${request.contextPath}/tosetting/trading">交易设置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/live">直播间设置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/uploadconfig">附件设置</a>
                                        </li>
                                    </ul>
                                    <div class="tab-content">
                                        <div class="panel-body">
                                            <div class="row">
                                                <div class="col-lg-12">
                                                    <div class="ibox ">
                                                        <div class="ibox-content">
                                                            <style>
                                                                .input-group-addon {
                                                                    line-height: initial;
                                                                }
                                                            </style>
                                                            <div class="layui-card">
                                                                <div class="layui-card-header">订单设置</div>
                                                                <div class="layui-card-body">
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">未付款关闭</label>
                                                                        <div class="layui-input-block">
                                                                            <input type="text" name="order_cancel_time"
                                                                                   placeholder="请输入小时数"
                                                                                   value="${info.order_cancel_time!}"
                                                                                   class="layui-input">
                                                                            <div class="form-text m-b-none">未付款订单关闭时间
                                                                                单位/小时
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">自动确认收货</label>
                                                                        <div class="layui-input-block">
                                                                            <input type="text"
                                                                                   name="automatic_receiving_goods"
                                                                                   placeholder="请输入天数"
                                                                                   value="${info.automatic_receiving_goods!}"
                                                                                   class="layui-input">
                                                                            <div class="form-text m-b-none">订单自动确认收货时间
                                                                                单位/天
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">自动关闭时间</label>
                                                                        <div class="layui-input-block">
                                                                            <input type="text"
                                                                                   name="automatic_close_goods"
                                                                                   placeholder="请输入天数"
                                                                                   value="${info.automatic_close_goods!}"
                                                                                   class="layui-input">
                                                                            <div class="form-text m-b-none">自动关闭时间（关闭后无法退货退款）
                                                                                单位/天
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <div class="layui-card">
                                                                <div class="layui-card-header">余额设置</div>
                                                                <div class="layui-card-body">
<#--                                                                    <div class="layui-form-item">-->
<#--                                                                        <label class="layui-form-label">余额功能</label>-->
<#--                                                                        <div class="layui-input-block">-->
<#--                                                                            <#if info.switch??>-->
<#--                                                                                <input type="radio" name="switchinfo" value="1" title="开启" ${(info.switchinfo=='1')?string('checked','')}>-->
<#--                                                                                <input type="radio" name="switchinfo" value="0" title="关闭" ${(info.switchinfo=='0')?string('checked','')}>-->
<#--                                                                            <#else >-->
<#--                                                                                <input type="radio" name="switchinfo" value="1" checked title="开启">-->
<#--                                                                                <input type="radio" name="switchinfo" value="0" title="关闭">-->
<#--                                                                            </#if>-->
<#--                                                                        </div>-->
<#--                                                                    </div>-->
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">账户充值</label>
                                                                        <div class="layui-input-block">
                                                                            <#if info.recharge??>
                                                                                <input type="radio" name="recharge" value="1" title="开启" ${(info.recharge=='1')?string('checked','')}>
                                                                                <input type="radio" name="recharge" value="0" title="关闭" ${(info.recharge=='0')?string('checked','')}>
                                                                            <#else >
                                                                                <input type="radio" name="recharge" checked value="1" title="开启">
                                                                                <input type="radio" name="recharge" value="0" title="关闭">
                                                                            </#if>
                                                                        </div>
                                                                    </div>
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">最低充值金额</label>
                                                                        <div class="layui-input-block">
                                                                            <div class="input-group m-b">
                                                                                <input type="text" class="form-control"
                                                                                       name="min_recharge"
                                                                                       value="${info.min_recharge!}">
                                                                                <div class="input-group-append">
                                                                                    <span class="input-group-addon">元</span>
                                                                                </div>
                                                                                <div class="form-text m-b-none">
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
<#--                                                                    <div class="layui-form-item">-->
<#--                                                                        <label class="layui-form-label">余额提现</label>-->
<#--                                                                        <div class="layui-input-block">-->
<#--                                                                            <#if info.withdraw??>-->
<#--                                                                                <input type="radio" name="withdraw" value="1" title="开启" ${(info.withdraw=='1')?string('checked','')}>-->
<#--                                                                                <input type="radio" name="withdraw" value="0" title="关闭" ${(info.withdraw=='0')?string('checked','')}>-->
<#--                                                                            <#else >-->
<#--                                                                                <input type="radio" name="withdraw" checked value="1" title="开启">-->
<#--                                                                                <input type="radio" name="withdraw" value="0" title="关闭">-->
<#--                                                                            </#if>-->
<#--                                                                        </div>-->
<#--                                                                    </div>-->
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">余额提现限制</label>
                                                                        <div class="layui-input-block">
                                                                            <input type="number" name="withdraw_limit"
                                                                                   value="${info.withdraw_limit!}"
                                                                                   class="layui-input">
                                                                        </div>
                                                                        <div class="form-text m-b-none">
                                                                            余额满多少才能提现,空或0不限制
                                                                        </div>
                                                                    </div>
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">提现手续费</label>
                                                                        <div class="layui-input-block">
                                                                            <div class="input-group m-b">
                                                                                <input type="text" class="form-control"
                                                                                       name="withdraw_commission"
                                                                                       value="${info.withdraw_commission!}">
                                                                                <div class="input-group-append">
                                                                                    <span class="input-group-addon">%</span>
                                                                                </div>
                                                                            </div>
                                                                            <div class="form-text m-b-none">
                                                                                余额提现时,扣除的手续费.为空不扣除手续费
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="layui-form-item">
                                                                        <label class="layui-form-label">免手续费区间</label>
                                                                        <div class="layui-input-block row">
                                                                            <div class="input-group m-b col-md-6"
                                                                                 style="padding-left: 0px;padding-right: 0">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-addon">最小余额</span>
                                                                                </div>
                                                                                <input type="text"
                                                                                       name="withdraw_commission_free_min"
                                                                                       class="form-control"
                                                                                       value="${info.withdraw_commission_free_min!}">
                                                                            </div>
                                                                            <div class="input-group m-b col-md-6"
                                                                                 style="padding-left: 0px">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-addon">最大余额</span>
                                                                                </div>
                                                                                <input type="text" name="withdraw_commission_free_max"
                                                                                       class="form-control"
                                                                                       value="${info.withdraw_commission_free_max!}">
                                                                            </div>
                                                                            <div class="help-block">
                                                                                在最小金额和最大金额内，无提现手续费
                                                                            </div>
                                                                        </div>

                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <div class="offset-sm-2 col-sm-4">
                                                                    <button class="btn btn-primary" type="button"
                                                                            id="ajaxsubmit" onclick="submitform();">保存更改
                                                                    </button>
                                                                    <button class="btn btn-white" type="reset">取消
                                                                    </button>
                                                                </div>
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
                <div class="footer"></div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                layui.use(["layer", "form"], function () {
                    var form = layui.form;
                    var layer = layui.layer;
                    var form = layui.form, $ = layui.jquery;
                    form.render();
                });
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });

            function submitform() {
                $.ajax({
                    url: "${request.contextPath}/tosetting/tradingupdate",
                    data: $("#ajaxform").serialize(),
                    dataType: "json",
                    success: function (result) {
                        if (result.code == 200) {
                            location.href = "${request.contextPath}/setting/index";//回到父级页面
                        } else {
                            layer.open({
                                title: '警告'
                                , content: result.msg
                            });
                        }
                    }
                });
            }
        </script>

    </body>
</html>
