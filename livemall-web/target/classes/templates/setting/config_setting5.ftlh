<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 | 系统配置            </title>
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
            if(navigator.appName == 'Microsoft Internet Explorer'){
                if(navigator.userAgent.indexOf("MSIE 5.0")>0 || navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
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
                            <form class="layui-form form-horizontal" id="ajaxform" action="" method="post" enctype="multipart/form-data">
                                <#if configs??>
                                    <input type="hidden" name="id" value="${configs.id!}">
                                </#if>                                <div class="tabs-container">
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
                                            <a class="nav-link active" href="${request.contextPath}/tosetting/pay">支付配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/logistics">物流配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/trading">交易设置</a>
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
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">商户号(mid)</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="mid" class="form-control" value="${info.mid!}" placeholder="">
                                                                    <span class="form-text m-b-none"></span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">终端号(tid)</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="tid" class="form-control" value="${info.tid!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">终端号</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">机构商户号(instMid)</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="instMid" class="form-control" value="${info.instMid!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">机构商户号(instMid)</span>
                                                                </div>
                                                            </div>

                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">消息来源(msgSrc)</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="msgSrc" class="form-control" value="${info.msgSrc!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">消息来源(msgSrc)</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">来源编号（msgSrcId）</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="msgSrcId" class="form-control" value="${info.msgSrcId!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">来源编号（msgSrcId）</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">通讯密钥</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="MD5Key" class="form-control" value="${info.MD5Key!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">通讯密钥</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">APIurl</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="APIurl" class="form-control" value="${info.APIurl!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">APIurl</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">微信appid</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="appid" class="form-control" value="${info.appid!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">微信appid</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">支付回调地址</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="notifyUrl" class="form-control" value="${info.notifyUrl!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">支付回调地址</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <div class="offset-sm-2 col-sm-4">
                                                                    <button class="btn btn-primary" type="button" id="ajaxsubmit" onclick="submitform();">保存更改
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
                    url:"${request.contextPath}/tosetting/payupdate",
                    data:$("#ajaxform").serialize(),
                    dataType:"json",
                    success:function(result){
                        if(result.code == 200){
                            location.href="${request.contextPath}/setting/index";//回到父级页面
                        }else{
                            layer.open({
                                title: '警告'
                                ,content: result.msg
                            });
                        }
                    }});
            }
        </script>

    </body>
</html>
