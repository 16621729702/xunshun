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
                                    <li><a class="nav-link active"  href="${request.contextPath}/member/editpage?id=${id!}">基本信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage2?id=${id!}">地址信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage4?id=${id!}">收藏信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage5?id=${id!}">关注信息</a></li>
                                    <li><a class="nav-link "  href="${request.contextPath}/member/editpage7?id=${id!}">足迹信息</a></li>
                                </ul>
                                <form class="layui-form form-horizontal m-t" id="ajaxform"
                                      action="" method="post" style="margin-top: 0px">
                                    <div class="tab-content">
                                        <div role="tabpanel"  class="tab-pane active">
                                            <input type="hidden" name="id" placeholder="" value="${lmMember.id!}" class="layui-input">
                                            <div class="panel-body">
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>昵称</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="nickname" placeholder="" value="${lmMember.nickname!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>头像</label>
                                                    <div class="layui-input-block">
                                                        <div class="input-group ">
                                                            <input type="text" name="avatar" placeholder="" value="${lmMember.avatar!}" class="form-control">
                                                            <span class="input-group-btn">
                                                        <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('avatar');">选择图片</button>
                                                    </span>
                                                        </div>
                                                        <div id="avatarimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                                            <img src="${lmMember.avatar!}" width="90px" height="90px">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>真实姓名</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="realname" placeholder="" value="${lmMember.realname!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>手机号码</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="mobile" placeholder="" value="${lmMember.mobile!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>证件号</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="idcard" placeholder="" value="${lmMember.idcard!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>性别</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="gender" placeholder="" value="${lmMember.gender!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>ip地址</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="ip" placeholder="" value="${lmMember.ip!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>国籍</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="country" placeholder="" value="${lmMember.country!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>省份</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="province" placeholder="" value="${lmMember.province!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>市</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="city" placeholder="" value="${lmMember.city!}" class="layui-input">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>信誉</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="text" name="credit" placeholder="" value="${lmMember.credit!}" class="layui-input">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>用户类型</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="usertype" placeholder="" value="${lmMember.usertype!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>用户账户</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="username" placeholder="" value="${lmMember.username!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>会员成长值</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="growth_value" placeholder="" value="${lmMember.growth_value!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>会员等级</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="level_id" placeholder="" value="${lmMember.level_id!}" class="layui-input">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>等级有效期</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="text" name="level_valid_time" placeholder="" value="${lmMember.level_valid_time?string('yyyy-MM-dd HH:mm:ss')!}" class="layui-input">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>状态</label>
                                                    <div class="layui-input-block">
                                                        <input type="radio" name="state" ${(lmMember.state == 0 )?string('checked','')} value="0" title="启用" lay-filter = "type">
                                                        <input type="radio" name="state" ${(lmMember.state == 1 )?string('checked','')} value="1" title="禁用" lay-filter = "type">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item"
                                                     style="position: relative;border: 0;left: 0">
                                                    <div class="layui-input-block">
                                                        <button class="btn btn-primary" type="button"
                                                                onclick="submitform()">提交保存
                                                        </button>
                                                    </div>
                                                </div>
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

            function submitform(){
                $.ajax({
                    url:"${request.contextPath}/member/memberedit",
                    data:$("#ajaxform").serialize(),
                    dataType:"json",
                    success:function(result){
                        if(result.code == 200){
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index); //执行关闭 
                            parent.location.href="${request.contextPath}/member/query";//回到父级页面
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
