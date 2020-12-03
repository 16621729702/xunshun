<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
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
            if (navigator.appName == 'Microsoft Internet Explorer') {
                if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                    alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                }
            }

        </script>
    </head>

    <body class="iframe-body">

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
        <div id="wrapper">
            <div style="width: 100%;">
                <div class="col-lg-12">
                    <div style="width: 100%;">
                        <div class="ibox-content">
                            <form class="layui-form form-horizontal m-t" id="ajaxform" action=""
                                  method="post">
                                <input type="hidden" name="id" class="layui-input" value="${lideshow.id!}"/>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">显示位置</label>
                                    <div class="layui-input-block">
                                        <select name="type" lay-verify="required" lay-search="" lay-filter="type">
                                            <option value="0">请选择</option>
                                            <option value="1" ${(lideshow.type==1)?string('selected','')} >首页</option>
                                            <option value="2" ${(lideshow.type==2)?string('selected','')}>店铺</option>
                                            <option value="3" ${(lideshow.type==3)?string('selected','')}>商城</option>
                                            <option value="4" ${(lideshow.type==4)?string('selected','')}>分类</option>
                                            <option value="9" ${(lideshow.type==9)?string('selected','')}>直播</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">关联商户</label>
                                    <div class="layui-input-block">
                                        <select name="merchid" lay-verify="required" lay-filter="parent_id">
                                            <#if (merchlist)??>
                                                <#if (merchlist?size > 0)>
                                                    <#list merchlist as list>
                                                        <option value="${list.id}" ${(list.id == lideshow.merchid)?string('selected','')}  >${list.store_name}</option>
                                                    </#list>
                                                </#if>
                                            </#if>
                                        </select>
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label">名称</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="name" class="layui-input" value="${lideshow.name!}"/>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">轮播图</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                            <input type="text" name="pic" lay-verify="required" value="${lideshow.pic!}"
                                                   class="form-control" autocomplete="off" readonly>
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('pic');">选择图片</button>
                                            </span>
                                        </div>
                                        <div id="picimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                            <#if lideshow.pic?contains("http")>
                                                <img src="${lideshow.pic!}" width="90px" height="90px">
                                                　　<#else >

                                                <img src="${request.contextPath}/image/IoReadImage/${lideshow.pic!}" width="90px" height="90px">
                                            </#if>

                                        </div>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">跳转地址</label>
                                    <div class="layui-input-block">
                                        <div class="input-group">
                                            <input type="text" name="wxappurl" class="form-control valid"
                                                   value="${lideshow.wxappurl!}" aria-invalid="false"
                                                   placeholder="请填写小程序跳转链接"
                                                   id="wxappurl">
                                        </div>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">排序</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="sort" class="layui-input" value="${lideshow.sort!}"/>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <div class="layui-input-block">
                                        <button class="btn btn-primary" type="button" id="ajaxsubmit"
                                                onclick="commit()">提交保存
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            <script>
                function commit() {
                    $.ajax({
                        url: "${request.contextPath}/lideshow/edit",
                        data: $("#ajaxform").serialize(),
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 200) {
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href = "${request.contextPath}/lideshow/query";//回到父级页面
                            } else {
                                layer.open({
                                    title: '警告'
                                    , content: result.msg
                                });
                            }
                        }
                    });
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
