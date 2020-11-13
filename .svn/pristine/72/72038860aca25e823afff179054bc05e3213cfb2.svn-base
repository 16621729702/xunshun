
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 |     设置订单信息
        </title>
        <meta name="csrf-token" content="lMY8vAepkU6MEKxKzJmV5tdU6RcR7dkvumVs61Li">
        <meta name="request-type" content="alert">
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
            });
        </script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>        </head>
    <body class="iframe-body">

        <div id="wrapper">
            <div class="col-lg-12">
                <div style="width: 100%;">
                    <div class="ibox-content" style="height: 500px;">
                        <form class="layui-form form-horizontal m-t" id="ajaxform" action="${request.contextPath}/orders/saveedit" method="post">
                            <input type="hidden" name="id" value="${order.id}">
                            <div class="layui-form-item">
                                <label class="layui-form-label">订单号</label>
                                <div class="layui-input-block">
                                    <input type="text" value="${order.orderid!}"  class="layui-input" disabled>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label"><span style="color: red;">*</span>商品总价</label>
                                <div class="layui-input-block">
                                    <input type="number" name="totalprice" placeholder="请输入商品总价" value="${order.totalprice!}" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label"><span style="color: red;">*</span>原始邮费</label>
                                <div class="layui-input-block">
                                    <input type="number" name="payexpressprice" placeholder="请输入原始邮费" value="${order.payexpressprice!}" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label"><span style="color: red;">*</span>实际支付金额</label>
                                <div class="layui-input-block">
                                    <input type="number" name="realpayprice" placeholder="请输入实际支付金额" value="${order.realpayprice!}" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label"><span style="color: red;">*</span>实际支付邮费</label>
                                <div class="layui-input-block">
                                    <input type="number" name="realexpressprice" placeholder="请输入实际支付邮费" value="${order.realexpressprice!}" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-input-block">
                                    <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>确认修改</button>
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
                form.render();
                form.on('submit(*)',function (data) {
                    $.ajax({
                        url:"${request.contextPath}/orders/saveedit",
                        data:$("#ajaxform").serialize(),
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.reload();//回到父级页面
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
