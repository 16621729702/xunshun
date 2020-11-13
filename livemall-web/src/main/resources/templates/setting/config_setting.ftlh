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
                                            <a class="nav-link active" href="${request.contextPath}/tosetting/basic">基础配置</a>
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
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">站点名称</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="site_name" placeholder="请输入站点名称" value="${info.site_name!}" class="layui-input">
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">Logo</label>
                                                                <div class="layui-input-block">
                                                                    <div class="input-group ">
                                                                        <input type="text" name="logo" value="${info.logo!}" class="form-control" autocomplete="off" readonly>
                                                                        <span class="input-group-btn">
                                                                        <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('logo');">选择图片</button>
                                                                    </span>
                                                                    </div>
                                                                    <div id="logoimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                                                        <#if info.logo?contains("http")>
                                                                            <img src="${info.logo!}" width="90px" height="90px">
                                                                        <#else >
                                                                            <img src="${request.contextPath}/image/IoReadImage/${info.logo!}" width="90px" height="90px">
                                                                        </#if>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">客服电话</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="consumer_hotline" placeholder="请输入客服电话" value="${info.consumer_hotline!}"
                                                                           class="layui-input">
                                                                    <div class="form-text m-b-none">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">授权手机号</label>
                                                                <div class="layui-input-block">
                                                                    <#if info.auth_mobile??>
                                                                      <input type="checkbox" name="auth_mobile"  ${(info.auth_mobile=='1')?string('checked','')}  title="授权手机号" value="1">
                                                                        <#else >
                                                                            <input type="checkbox" name="auth_mobile"   title="授权手机号" value="1">
                                                                    </#if>
                                                                    <div class="layui-unselect layui-form-checkbox">
                                                                        <span>授权手机号</span>
                                                                        <i class="layui-icon layui-icon-ok"></i>
                                                                    </div>
                                                                    <div class="form-text m-b-none">
                                                                        开启后，授权用户信息后，会提示授权手机号
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">版权文字</label>
                                                                <div class="layui-input-block">
                                                                    <input type="text" name="copyright_text" placeholder="请输入版权文字" value="${info.copyright_text!}" class="layui-input">
                                                                    <div class="form-text m-b-none">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">版权Logo</label>
                                                                <div class="layui-input-block">
                                                                    <div class="input-group ">
                                                                        <input type="text" name="copyright_logo" value="${info.copyright_logo!}" class="form-control" autocomplete="off" readonly>
                                                                        <span class="input-group-btn">
                                                                        <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('copyright_logo');">选择图片</button>
                                                                    </span>
                                                                    </div>
                                                                    <div id="copyright_logoimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                                                        <#if info.copyright_logo?contains("http")>
                                                                            <img src="${info.copyright_logo!}" width="90px" height="90px">
                                                                        <#else >
                                                                            <img src="${request.contextPath}/image/IoReadImage/${info.copyright_logo!}" width="90px" height="90px">
                                                                        </#if>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="layui-form-item">
                                                                <label class="layui-form-label">版权跳转地址</label>
                                                                <div class="layui-input-block">
                                                                    <div class="input-group">
                                                                        <input type="text" name="copyright_link" value="${info.copyright_link!}" class="form-control valid" aria-invalid="false" placeholder="请填写小程序跳转链接" id="copyright_link">
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
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
            url:"${request.contextPath}/tosetting/basicupdate",
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
    function showimage(classname) {
        layui.use('layer', function(){
            var layer = layui.layer;
            layer.open({
                type: 2,
                title:'选择图片',
                maxmin: true, //开启最大化最小化按钮
                shadeClose: true,
                shade: false,
                area: ['1000px', '700px'],
                content: '${request.contextPath}/image/query?classname='+classname, //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
            });
        });
    }
    function ChooseAdidValues(v,classname) {
        if (v != "") {
            var imgurl = v.split(",");
            $("input[name='"+classname+"']").val(imgurl[0]);
            if(imgurl[0].indexOf("http") == -1){
                var html ="<img src='${request.contextPath}/image/IoReadImage/"+imgurl[0]+"' width='90px' height='90px'>";
            }else{
                var html ="<img src='"+imgurl[0]+"' width='90px' height='90px'>";
            }
            $("#"+classname+"imgshow").html(html);
        }
    }

</script>
    </body>
</html>
