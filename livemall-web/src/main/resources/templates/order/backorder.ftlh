
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 |     设置退款信息
        </title>
        <meta name="csrf-token" content="ARh4q4A8oeB7z6cvHgVxhNUFiHDxNlRacLhkpCs2">
        <meta name="request-type" content="alert">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/css/bootstrap.css" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/font-awesome/css/font-awesome.css" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/lib/Hui/css/iconfont.css" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/layui/css/layui.css?v=2212" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/css/animate.css" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/css/style.css?v=12223" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/css/main.css?v=666" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/lib/toastr/toastr.min.css" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/we7/resource/css/common.css" rel="stylesheet">
        <link href="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/css/fix.css?v=121" rel="stylesheet">
        <script type="text/javascript">
            if(navigator.appName == 'Microsoft Internet Explorer'){
                if(navigator.userAgent.indexOf("MSIE 5.0")>0 || navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
                    alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                }
            }
            window.sysinfo = {
                'uniacid': '2',                    'acid': '2',                                                                        'isfounder':  0 ,
                'family': 'x',
                'siteroot': 'https://xiangbo.win-k.com/',
                'siteurl': 'https://xiangbo.win-k.com/addons/shimmer_liveshop/index.php?s=live%2F2%2Fshop%2Forder%2Frefund%2Fform%2F233',
                'attachurl': '',
                'attachurl_local': '',
                'attachurl_remote': '',
                'module' : {'url' : '', 'name' : ''},
                'cookie' : {'pre': 'c5a5_'},
                'account' : null,
                'server' : {'php' : '7.1.29'},
            };
        </script>
        <!-- Mainly scripts -->
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/jquery-1.11.1.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/popper.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/bootstrap.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/plugins/metisMenu/jquery.metisMenu.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/plugins/iCheck/icheck.min.js"></script>
        <!-- Custom and plugin javascript -->
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/inspinia.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/plugins/pace/pace.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/jquery.form.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/layui/layui.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/lib/toastr/toastr.min.js"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/we7/util.js?v=2"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/we7/common.min.js?v=2"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/we7/require.js"></script>
        <script>
            $(document).ready(function () {
                layui.use(["layer", "form"], function () {
                    var form = layui.form;
                    var layer = layui.layer;
                    var form = layui.form, $ = layui.jquery;
                    form.render();
                });
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });
        </script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/common.js?v=1.0.2"></script>
        <script src="https://xiangbo.win-k.com/addons/shimmer_liveshop/static/web/js/myconfig.js"></script>        </head>
    <body class="iframe-body">

        <div id="wrapper">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-content">
                        <form class="layui-form form-horizontal m-t" id="ajaxform" action="https://xiangbo.win-k.com/addons/shimmer_liveshop/live/2/shop/order/refund/submit/233" method="post">
                            <input type="hidden" name="_token" value="ARh4q4A8oeB7z6cvHgVxhNUFiHDxNlRacLhkpCs2">
                            <div class="layui-form-item">
                                <label class="layui-form-label">退款类型</label>
                                <div class="layui-input-block">
                                    <input type="radio" checked name="type" value="1" title="直接退款" lay-filter="type">
                                    <input type="radio"  name="type" value="2" title="退款后，保持退款状态" lay-filter="type">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">退款单号</label>
                                <div class="layui-input-block">
                                    <input type="text" value="wx158494759507159591" class="layui-input" disabled>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label"><span style="color: red;">*</span>退款金额</label>
                                <div class="layui-input-block">
                                    <input type="number" name="refund_price" placeholder="请输入退款金额" value="74.00" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-input-block">
                                    <button class="btn btn-primary" type="submit" id="ajaxsubmit">确认退款</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script>
            layui.use(['form','element'], function(){
                var form = layui.form;
                form.on('radio(deliver_type)', function(data){
                    var type = data.value;
                    $('.deliver_type_box').hide();
                    $('.deliver_type'+type).show();
                });
            });
        </script>
    </body>
</html>
