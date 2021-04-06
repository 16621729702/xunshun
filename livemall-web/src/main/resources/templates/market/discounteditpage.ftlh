
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="jOL58rIiCWdrDTjHhLT43gIpM9tcJIFmutLjZ69P">
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
        <script type="text/javascript">
            if(navigator.appName == 'Microsoft Internet Explorer'){
                if(navigator.userAgent.indexOf("MSIE 5.0")>0 || navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
                    alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                }
            }

        </script>
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
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
    </head>
    <body class="iframe-body">
        <div id="wrapper">
            <div style="width: 100%;">
                <div class="col-lg-12">
                    <div style="width: 100%;">
                        <div class="ibox-content">
                            <form class="layui-form form-horizontal m-t" id="ajaxform" action="" method="post">
                                <input type="hidden" name="id"  value="${Discount.id!}" >
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>优惠手机号码</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="mobile" placeholder="" value="${Discount.mobile!}" class="layui-input">
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="margin"  ${(Discount.margin="1")?string('checked','')} value="1" title="无需缴纳" lay-filter="is_show">
                                        <input type="radio" name="margin"   ${(Discount.margin="0")?string('checked','')} value="0" title="需要缴纳"  lay-filter="is_show">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>年费优惠价格</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="couponPrice" placeholder="" value="${Discount.coupon_price!}" class="layui-input"/>
                                        -1 不免年费 0 为年费全免  其他数值都是优惠面值
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">店铺类型</label>
                                    <div class="layui-input-block">
                                            <input type="radio" name="type"  ${(Discount.type==1)?string('checked','')} value="1" title="普通店铺" lay-filter="is_show">
                                            <input type="radio" name="type"   ${(Discount.type==2)?string('checked','')} value="2" title="优选店铺"  lay-filter="is_show">
                                        <input type="radio" name="type"   ${(Discount.type==3)?string('checked','')} value="3" title="直播店铺"  lay-filter="is_show">
                                        <input type="radio" name="type"   ${(Discount.type==4)?string('checked','')} value="4" title="合买店铺"  lay-filter="is_show">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">经营主体类型</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="businessid"  ${(Discount.businessid==1)?string('checked','')} value="1" title="个人" lay-filter="is_show">
                                        <input type="radio" name="businessid"   ${(Discount.businessid==2)?string('checked','')} value="2" title="企业"  lay-filter="is_show">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="isstart"  ${(Discount.isstart==1)?string('checked','')} value="1" title="可用" lay-filter="is_show">
                                        <input type="radio" name="isstart"   ${(Discount.isstart==0)?string('checked','')} value="0" title="不可用"  lay-filter="is_show">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <div class="layui-input-block">
                                        <button class="btn btn-primary" type="button"  lay-filter="*" lay-submit>提交保存</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
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
                layui.use('layedit', function(){
                    var layedit = layui.layedit;
                    var index = layedit.build('pattern');
                    form.verify({
                        pattern:function () {
                            layedit.sync(index);
                        }
                    });
                });
                form.on('radio(type)',function(obj){
                    var data = $(obj.elem);
                    if(data.val()==1){
                        $(".auction").show();
                    }else{
                        $(".auction").hide();
                    }
                })
                form.render();
                form.on('submit(*)',function (data) {
                    $.ajax({
                        url:"${request.contextPath}/discount/discountedit",
                        data:$("#ajaxform").serialize(),
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href="${request.contextPath}/discount/discountquery";//回到父级页面
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
