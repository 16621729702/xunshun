
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
        <link href="${request.contextPath}/web/layui/formSelects-v4.css" rel="stylesheet">

    </head>
    <body class="iframe-body">
        <div id="wrapper">
            <div style="width: 100%;">
                <div class="col-lg-12">
                    <div style="width: 100%;">
                        <div class="ibox-content">
                            <form class="layui-form form-horizontal m-t" id="ajaxform" action="" method="post">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">会员</label>
                                    <div class="layui-input-block">
                                        <select name="member_id" lay-verify="required" xm-select="selecttag" xm-select-search="" xm-select-show-count="1">
                                            <#if (lmMemberList)??>
                                                <#if (lmMemberList?size > 0)>
                                                    <#list lmMemberList as list>
                                                        <option value="${list.id!}"  >${list.nickname!}</option>
                                                    </#list>
                                                </#if>
                                             </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>头像</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                            <input type="text" name="avatar" placeholder="请输入名称" value="" class="form-control">
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('avatar');">选择图片</button>
                                        </span>
                                        </div>
                                        <div id="avatarimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                        </div>
                                    </div>

                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>商户名称</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="store_name" placeholder="" value="" class="layui-input">
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>微信</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="weixin" placeholder="" value="" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>手机号码</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="mobile" placeholder="" value="" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>背景图</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                            <input type="text" name="bg_image" placeholder="" value="" class="form-control">
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('bg_image');">选择图片</button>
                                        </span>
                                        </div>
                                        <div id="bg_imageimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                        </div>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="state" value="0" title="待认证" lay-filter="is_show">
                                        <input type="radio" name="state" value="1" title="正常使用"  lay-filter="is_show">
                                        <input type="radio" name="state" value="2" title="封号"  lay-filter="is_show">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>描述</label>
                                    <div class="layui-input-block">
                                        <textarea placeholder="" class="layui-textarea" name="description" id="pattern" lay-verify="pattern" style="display:none"></textarea>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <div class="layui-input-block">
                                        <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>提交保存</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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

        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <script src="${request.contextPath}/web/layui/formSelects-v4.js" type="text/javascript" charset="utf-8"></script>
        <script>
            $(document).ready(function () {
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });
        </script>
            <script>
                var formSelects = layui.formSelects;
                layui.formSelects.filter('selecttag', function(id, inputVal, val, isDisabled){
                    if(val.name.indexOf(inputVal) != -1){
                        return false;
                    }
                    return true;
                });
            layui.use(['layer', 'form'], function () {
                $ = layui.jquery;
                var form = layui.form
                layui.use('layedit', function(){
                    var layedit = layui.layedit;
                    var index = layedit.build('pattern');
                    form.verify({
                        pattern:function () {
                            layedit.sync(index);
                        }
                    });
                });
                form.render();

                form.on('submit(*)',function (data) {
                    $.ajax({
                        url: "${request.contextPath}/merch/insert",
                        data: $("#ajaxform").serialize(),
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href="${request.contextPath}/merch/query";//回到父级页面
                            }else{
                                layer.open({
                                    title: '警告'
                                    ,content: result.msg
                                });
                            }
                        }
                    });
                });
            });
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
