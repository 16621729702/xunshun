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
                                </#if>
                                <div class="tabs-container">
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/basic">基础配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link active" href="${request.contextPath}/tosetting/flow">推流配置</a>
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
                                                                <label class="col-sm-2 col-form-label">七牛云直播AccessKeyId</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="access_key_id" class="form-control" value="${info.access_key_id!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">七牛云直播AccessKeyId</span>
                                                                </div>
                                                            </div>
                                                            <div class="hr-line-dashed"></div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">七牛云直播AccessSecret</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="access_secret" class="form-control" value="${info.access_secret!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">七牛云直播AccessSecret</span>
                                                                </div>
                                                            </div>
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">七牛云直播ApiUrl</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="api_url" class="form-control" value="${info.api_url!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">七牛云直播ApiUrl</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
                                                            <div class="hr-line-dashed"></div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">七牛云直播推流域名</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="pushurl" class="form-control" value="${info.pushurl!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">七牛云直播推流域名</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">rtmp七牛云直播播放域名</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="readurl" class="form-control" value="${info.readurl!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">rtmp七牛云直播播放域名</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">hsl七牛云直播播放域名</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="readurl2" class="form-control" value="${info.readurl2!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">hsl七牛云直播播放域名</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">hdl七牛云直播播放域名</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="readurl3" class="form-control" value="${info.readurl3!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">hdl七牛云直播播放域名</span>
                                                                </div>
                                                            </div>
                                                            <div class="hr-line-dashed"></div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">七牛云直播空间名称</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="hubname" class="form-control" value="${info.hubname!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">七牛云直播空间名称</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">直播播放主密匙</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="key" class="form-control" value="${info.key!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">直播播放主密匙</span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group  row">
                                                                <label class="col-sm-2 col-form-label">点播域名</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" name="vod" class="form-control" value="${info.vod!}"
                                                                           placeholder="">
                                                                    <span class="form-text m-b-none">点播域名</span>
                                                                </div>
                                                            </div>
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云推流地址</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="push_flow" class="form-control" value="${info.push_flow!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云推流地址</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云推流地址参数</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="push_flow_ext" class="form-control" value="${info.push_flow_ext!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云推流地址参数</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云鉴权主KEY</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="auth_main_key" class="form-control" value="${info.auth_main_key!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云鉴权主KEY</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云OssEndpoint</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="oss_endpoint" class="form-control" value="${info.oss_endpoint!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云OssEndpoint</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云OssBucket</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="oss_bucket" class="form-control" value="${info.oss_bucket!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云OssBucket</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云TimeInterval</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="time_interval" class="form-control" value="${info.time_interval!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云TimeInterval</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="hr-line-dashed"></div>-->
<#--                                                            <div class="form-group  row">-->
<#--                                                                <label class="col-sm-2 col-form-label">阿里云CDN</label>-->
<#--                                                                <div class="col-sm-10">-->
<#--                                                                    <input type="text" name="cdn" class="form-control" value="${info.cdn!}"-->
<#--                                                                           placeholder="">-->
<#--                                                                    <span class="form-text m-b-none">阿里云CDN</span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
                                                            <div class="form-group row">
                                                                <div class="offset-sm-2 col-sm-4">
                                                                    <button class="btn btn-primary" type="button" id="ajaxsubmit" onclick="submitform()">保存更改</button>
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
                    url:"${request.contextPath}/tosetting/flowupdate",
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
            }</script>
    </body>
</html>
