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
                            <form class="layui-form form-horizontal" id="ajaxform"
                                  action="" method="post"
                                  enctype="multipart/form-data">
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
                                            <a class="nav-link " href="${request.contextPath}/tosetting/pay">支付配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/logistics">物流配置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link " href="${request.contextPath}/tosetting/trading">交易设置</a>
                                        </li>
                                        <li>
                                            <a class="nav-link active" href="${request.contextPath}/tosetting/live">直播间设置</a>
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
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">默认聊天信息</label>
                                                                <div class="layui-input-block">
                                                                    <input type="number" name="chat_nums" placeholder="请输入默认聊天信息条数" value="${info.chat_nums!}" class="layui-input">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">礼物平台抽拥是否开启</label>
                                                                <div class="layui-input-block">
                                                                    <#if info.open_deduct??>
                                                                        <input type="radio"  name="open_deduct" value="1" ${(info.open_location=='1')?string('checked','')} title="开启"  lay-filter="open_deduct"/>
                                                                        <input type="radio"  name="open_deduct" value="0"  ${(info.open_location=='0')?string('checked','')}  title="关闭"  lay-filter="open_deduct"/>
                                                                    <#else >
                                                                        <input type="radio"  name="open_deduct" value="1" title="开启"  lay-filter="open_deduct"/>
                                                                        <input type="radio"  name="open_deduct" value="0" title="关闭"  lay-filter="open_deduct"/>
                                                                    </#if>

                                                                </div>
                                                            </div>
                                                            <div class="open_deduct"  ${(info.open_location=='1')?string('','style="display: block;" ')} >
                                                                <div class="layui-form-item">
                                                                    <label class="layui-form-label">抽拥比例</label>
                                                                    <div class="layui-input-block">
                                                                        <input type="number" name="deduct_ratio" placeholder="请输入提成比例" value="${info.deduct_ratio!}" class="layui-input">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">是否开启定位</label>
                                                                <div class="layui-input-block">
                                                                    <#if info.open_location??>
                                                                        <input type="radio"  name="open_location" value="1"  ${(info.open_location=='1')?string('checked','')}  title="开启"    lay-filter="open_location"/>
                                                                        <input type="radio"  name="open_location" value="0"   ${(info.open_location=='0')?string('checked','')}  title="关闭"  lay-filter="open_location"/>
                                                                    <#else >
                                                                        <input type="radio"  name="open_location" value="1" title="开启"    lay-filter="open_location"/>
                                                                        <input type="radio"  name="open_location" value="0" title="关闭"  lay-filter="open_location"/>
                                                                    </#if>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">聊天提示语</label>
                                                                <div class="layui-input-block">
                                                                    <textarea name="chat_notice" placeholder="请输入聊天提示语" class="layui-textarea">${info.chat_notice!}</textarea>
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
                <div class="footer"></div>    </div>
        </div>
        <script>
            layui.use(["layer", "form", "upload"], function () {
                var form = layui.form;
                form.on('radio(open_deduct)', function (data) {
                    var type = data.value;//data.elem
                    if(type==1){
                        $('.open_deduct').show();
                    }else{
                        $('.open_deduct').hide();
                    }
                });
            });
        </script>
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
                    url:"${request.contextPath}/tosetting/liveupdate",
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
