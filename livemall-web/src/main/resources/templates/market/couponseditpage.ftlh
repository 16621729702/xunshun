
<!DOCTYPE html>
<html>

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
                                <input type="hidden" name="id"  value="${lmCoupons.id!}" >
                                <div class="layui-form-item">
                                    <label class="layui-form-label">商户</label>
                                    <div class="layui-input-block">
                                        <select name="merch_id" lay-verify="required" lay-filter="parent_id">
                                            <#if (lmMerchInfoList)??>
                                                <#if (lmMerchInfoList?size > 0)>
                                                    <#list lmMerchInfoList as list>
                                                        <option value="${list.id}"  >${list.store_name}</option>
                                                    </#list>
                                                </#if>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>名称</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="name" placeholder="请输入名称" value="${lmCoupons.name!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>数量</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="num" placeholder="" value="${lmCoupons.num!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>剩余数量</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="left_num" placeholder="" value="${lmCoupons.left_num!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>使用数量</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="use_num" placeholder="" value="${lmCoupons.use_num!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>折扣比例</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="rate" placeholder="" value="${lmCoupons.rate!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>条件金额</label>
                                    <div class="layui-input-block">
                                        <input type="number" name="useprice" placeholder="" value="${lmCoupons.useprice!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>开始时间</label>
                                    <div class="layui-input-block">
                                        <input type="text" class="layui-input  start_time" value="${lmCoupons.start_date?string('yyyy-MM-dd HH:mm:ss')!}" name="start_date">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>结束时间</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="end_date" placeholder="" value="${lmCoupons.end_date?string('yyyy-MM-dd HH:mm:ss')!}" class="layui-input end_time">
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="state"  ${(lmCoupons.state==1)?string('checked','')} value="1" title="不可用" lay-filter="is_show">
                                        <input type="radio" name="state"   ${(lmCoupons.state==0)?string('checked','')} value="0" title="可用"  lay-filter="is_show">
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>描述</label>
                                    <div class="layui-input-block">
                                        <textarea placeholder="" class="layui-textarea" name="description"   id="pattern" lay-verify="pattern" style="display:none">${lmCoupons.description!}</textarea>
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
                        url:"${request.contextPath}/marker/couponsedit",
                        data:$("#ajaxform").serialize(),
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href="${request.contextPath}/marker/couponsquery";//回到父级页面
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
