<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="YmiVnOmNgVzdICfo7eRlRKlU1eIBMp2OAIeubwZq">
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
        <link href="${request.contextPath}/web/layui/formSelects-v4.css" rel="stylesheet">
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
        <script src="${request.contextPath}/web/layui/formSelects-v4.js" type="text/javascript" charset="utf-8"></script>
        <script src="${request.contextPath}/web${request.contextPath}/goods.js"></script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <script src="${request.contextPath}/web${request.contextPath}/goods.js"></script>
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
        </script>

        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
            <div id="page-wrapper" class="gray-bg">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div style="width: 100%;">
                        <div class="col-lg-12">
                            <div class="tabs-container">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage?id=${id!}">基本信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage2?id=${id!}">地址信息</a></li>
                                    <li><a class="nav-link active"  href="${request.contextPath}/member/editpage4?id=${id!}">收藏信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage5?id=${id!}">关注信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage7?id=${id!}">足迹信息</a></li>
                                </ul>
                                <form class="layui-form form-horizontal m-t" id="ajaxform"
                                      action="" method="post" style="margin-top: 0px">
                                    <div class="tab-content">
                                        <div role="tabpanel" id="tab-4" class="tab-pane active">
                                            <div class="panel-body">
                                                <table class="layui-table">
                                                    <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>商品id/视频id</th>
                                                        <th>收藏时间</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <#if (lmMemberFavList)??>
                                                        <#if (lmMemberFavList?size > 0)>
                                                            <#list lmMemberFavList as list>
                                                                <tr>
                                                                    <td>${list.id}</td>
                                                                    <td>  <#if (list.video_id != 0)>
                                                                            ${list.video_id!}
                                                                            </#if>
                                                                        <#if (list.goods_id != 0)>
                                                                            ${list.goods_id!}
                                                                        </#if>
                                                                    </td>
                                                                    <td>${list.create_time?string('yyyy-MM-dd HH:mm:ss')!}</td>
                                                                </tr>
                                                            </#list>
                                                        </#if>
                                                    </#if>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer"></div>
            </div>
        </div>
        <script type="text/javascript">
            layui.use(['layer', 'form', 'jquery','laydate','element'], function () {
                $ = layui.jquery;
                var form = layui.form
                var laydate = layui.laydate, element = layui.element;
                //日期时间选择器
                laydate.render({
                    elem: '.start_time'
                    , type: 'datetime'
                });
                laydate.render({
                    elem: '.end_time'
                    , type: 'datetime'
                });
                form.on('radio(type)',function(obj){
                    var data = $(obj.elem);
                    if(data.val()==1){
                        $(".auction").show();
                    }else{
                        $(".auction").hide();
                    }
                })
            });
        </script>
    </body>
</html>
