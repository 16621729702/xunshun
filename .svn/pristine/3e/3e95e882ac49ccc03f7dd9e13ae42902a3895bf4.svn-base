
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理</title>
        <meta name="csrf-token" content="DSA5K8tM2uXXm5tMGBk9OYRVJLdl4a4rAqF1uHmn">
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
        <link href="${request.contextPath}/modules/live/css/panel.css" rel="stylesheet">
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
        <script>
            $(document).ready(function () {
                layui.use(["layer", "form"], function () {
                    var form = layui.form;
                    var layer = layui.layer;
                    var form = layui.form, $ = layui.jquery;
                    form.render();
                });
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
                $(document).ready(function(){
                    $("#left").load("${request.contextPath}/left");
                    $(".footer").load("${request.contextPath}/footer");
                })
            });
        </script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>


            <div id="page-wrapper" class="gray-bg">

                <div class="wrapper wrapper-content animated fadeInRight">
                    <div class="row">
                        <div class="col-sm-12" style="min-height:600px">
                            <div class="list">
                                <div class="topic">
                                    <ul>
                                        <li>
                                            <a href=""
                                               title="评论管理">
                                                <div class="img">
                                                    <img src="${request.contextPath}/web/img/评论.png">
                                                    <p>评论管理</p>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${request.contextPath}/live/goodlist?liveid=${liveid!}"
                                               title="商品管理">
                                                <div class="img">
                                                    <img src="${request.contextPath}/web/img/购物车.png">
                                                    <p>商品管理</p>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/"
                                               title="用户管理">
                                                <div class="img">
                                                    <img src="${request.contextPath}/web/img/个人中心1.png">
                                                    <p>用户管理</p>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${request.contextPath}/live/playback?liveid=${liveid!}" title="回放记录">
                                                <div class="img">
                                                    <img src="${request.contextPath}/web/img/组织.png">
                                                    <p>回放记录</p>
                                                </div>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer"></div>
            </div>
        </div>

    </body>
</html>
