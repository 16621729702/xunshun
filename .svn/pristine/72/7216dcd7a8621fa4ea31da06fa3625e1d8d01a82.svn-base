<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
            | 多商户基础设置 </title>
        <meta name="csrf-token" content="qy7aokAJzaWKSWBkJuSchDiG4PMlsBTZa4UD5KgO">
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
                    layui.use('layedit', function () {
                        var layedit = layui.layedit;
                        var index = layedit.build('modelcontent1');
                        var index2 = layedit.build('modelcontent2');
                        form.verify({
                            modelcontent1: function () {
                                layedit.sync(index);
                            },
                            modelcontent2: function () {
                                layedit.sync(index2);
                            }
                        });
                    });
                    form.render();
                    form.on('submit(*)',function (data) {
                        $.ajax({
                            url:"${request.contextPath}/merch/pageSettingsave",
                            data:$("#ajaxform").serialize(),
                            dataType:"json",
                            success:function(result){
                                if(result.code == 200){
                                    location.href="${request.contextPath}/merch/pageSetting";//回到父级页面
                                }else{
                                    layer.open({
                                        title: '警告'
                                        ,content: result.msg
                                    });
                                }
                            }});
                    });
                });
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });
        </script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
            <div id="page-wrapper" class="gray-bg">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="ibox ">
                                <div class="ibox-content">
                                    <form class="layui-form form-horizontal" id="ajaxform"
                                          action="" method="post"
                                          enctype="multipart/form-data">
                                        <input type="hidden" name="id" value="${id!}">
                                        <div class="tabs-container">
                                            <div class="tab-content">
                                                <div class="panel-body">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <div class="ibox ">
                                                                <div class="tabs-container">
                                                                    <ul class="nav nav-tabs" role="tablist">
                                                                        <li><a class="nav-link active show" data-toggle="tab" href="#tab-1">基本</a></li>
                                                                    </ul>
                                                                    <div class="tab-content">
                                                                        <#if config??>
                                                                            <div role="tabpanel" id="tab-1" class="tab-pane active show">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                            <input type="text" class="form-control" name="title" value="${config.title!}">
                                                                                            <div class="form-text"></div>
                                                                                        </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                            <input type="text" class="form-control" name="subtitle" value="${config.subtitle!}">
                                                                                            <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">简介</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                            <input type="text" class="form-control" name="content" value="${config.content!}">
                                                                                            <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块1标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modeltitle1" value="${config.modeltitle1!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>

                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块1内容</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea class="layui-textarea" name="modelcontent1" id="modelcontent1" lay-verify="modelcontent1" style="display:none">${config.modelcontent1!}</textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modeltitle2" value="${config.modeltitle2!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置标题1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modellabel1" value="${config.modellabel1!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置副标题1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modelsublabel1" value="${config.modelsublabel1!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置图片1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="modellabelpic1" lay-verify="required" value="${config.modellabelpic1!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('modellabelpic1');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="modellabelpic1imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.modellabelpic1?contains("http")>
                                                                                                    <img src="${config.modellabelpic1!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.modellabelpic1!}"  width="90px" height="90px">
                                                                                                </#if>

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置标题2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modellabel2" value="${config.modellabel2!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置副标题2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modelsublabel2" value="${config.modelsublabel2!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置图片2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="modellabelpic2" lay-verify="required" value="${config.modellabelpic2!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('modellabelpic2');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="modellabelpic2imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.modellabelpic2?contains("http")>
                                                                                                    <img src="${config.modellabelpic2!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.modellabelpic2!}"  width="90px" height="90px">
                                                                                                </#if>

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置标题3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modellabel3" value="${config.modellabel3!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置副标题3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modelsublabel3" value="${config.modelsublabel3!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置图片3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="modellabelpic3" lay-verify="required" value="${config.modellabelpic3!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('modellabelpic3');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="modellabelpic3imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.modellabelpic3?contains("http")>
                                                                                                    <img src="${config.modellabelpic3!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.modellabelpic3!}"  width="90px" height="90px">
                                                                                                </#if>

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.label1!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签1副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.sublabel1!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签1图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic1" lay-verify="required" value="${config.labelpic1!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic1');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic1imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.labelpic1?contains("http")>
                                                                                                    <img src="${config.labelpic1!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.labelpic1!}"  width="90px" height="90px">
                                                                                                </#if>

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group" >
                                                                                                <input type="text" class="form-control" name="label2" value="${config.label2!}">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签2副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.sublabel2!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签2图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic2" lay-verify="required" value="${config.labelpic2!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic2');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic2imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.labelpic2?contains("http")>
                                                                                                    <img src="${config.labelpic2!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.labelpic2!}"  width="90px" height="90px">
                                                                                                </#if>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" class="form-control" name="label3" value="${config.label3!}" >
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签3副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.sublabel3!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签3图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic3" lay-verify="required" value="${config.labelpic3!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic3');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic3imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.labelpic3?contains("http")>
                                                                                                    <img src="${config.labelpic3!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.labelpic3!}"  width="90px" height="90px">
                                                                                                </#if>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签4</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group" >
                                                                                                <input type="text" class="form-control" name="label4" value="${config.label4!}">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签4副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.sublabel4!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签4图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic4" lay-verify="required" value="${config.labelpic4!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic4');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic4imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.labelpic4?contains("http")>
                                                                                                    <img src="${config.labelpic4!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.labelpic4!}"  width="90px" height="90px">
                                                                                                </#if>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签5</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" class="form-control" name="label5" value="${config.label5!}">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签5副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.sublabel5!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签5图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic5" lay-verify="required" value="${config.labelpic5!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic5');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic5imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.labelpic5?contains("http")>
                                                                                                    <img src="${config.labelpic5!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.labelpic5!}"  width="90px" height="90px">
                                                                                                </#if>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签6</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" class="form-control" name="label6" value="${config.label6!}">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签6副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="label1" value="${config.sublabel6!}">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签6图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic6" lay-verify="required" value="${config.labelpic6!}"
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic6');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic6imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                                <#if config.labelpic6?contains("http")>
                                                                                                    <img src="${config.labelpic6!}"  width="90px" height="90px">
                                                                                                    　　<#else >
                                                                                                    <img src="${request.contextPath}/image/IoReadImage/${config.labelpic6!}"  width="90px" height="90px">
                                                                                                </#if>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        <#else >

                                                                            <div role="tabpanel" id="tab-1" class="tab-pane active show">
                                                                                <div class="panel-body">
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="text" class="form-control" name="title" value="">
                                                                                            <div class="form-text"></div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="text" class="form-control" name="subtitle" value="">
                                                                                            <div class="form-text"></div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">简介</label>
                                                                                        <div class="layui-input-block">
                                                                                            <input type="text" class="form-control" name="content" value="">
                                                                                            <div class="form-text"></div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块1标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="modeltitle1" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>

                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块1内容</label>
                                                                                        <div class="layui-input-block">
                                                                                            <textarea class="layui-textarea" name="modelcontent1" id="modelcontent1" lay-verify="modelcontent1" style="display:none"></textarea>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="modeltitle2" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置标题1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modellabel1" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置副标题1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modelsublabel1" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置图片1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="modellabelpic1" lay-verify="required" value=""
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('modellabelpic1');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置标题2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modellabel2" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置副标题2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modelsublabel2" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置图片2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="modellabelpic2" lay-verify="required" value=""
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('modellabelpic2');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置标题3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modellabel3" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置副标题3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group">
                                                                                                <input type="text" class="form-control" name="modelsublabel3" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">子模块2内置图片3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="modellabelpic3" lay-verify="required" value=""
                                                                                                       class="form-control" autocomplete="off" readonly>
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('modellabelpic3');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>


                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签1</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="label1" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签1副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="sublabel1" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签1图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="labelpic1" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签2</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="label2" value="">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签2副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="sublabel2" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签2图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="labelpic2" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签3</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="label3" value="">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签3副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="sublabel3" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签3图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="labelpic3" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签4</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="label4" value="">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签4副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="sublabel4" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签4图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="labelpic4" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签5</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="label5" value="">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签5副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="sublabel5" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签5图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="labelpic5" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签6</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="label6" value="">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签6副标题</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group m-b" style="width: 300px">
                                                                                                <input type="text" class="form-control" name="sublabel6" value="">
                                                                                                <div class="form-text"></div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="layui-form-item">
                                                                                        <label class="layui-form-label ">标签6图片</label>
                                                                                        <div class="layui-input-block">
                                                                                            <div class="input-group ">
                                                                                                <input type="text" name="labelpic6" lay-verify="required" value=""
                                                                                                       class="form-control" autocomplete="off">
                                                                                                <span class="input-group-btn">
                                                                                                    <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('labelpic6');">选择图片</button>
                                                                                            </span>
                                                                                            </div>
                                                                                            <div class="input-group " id="labelpic6imgshow"
                                                                                                 style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>


                                                                        </#if>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <div class="offset-sm-2 col-sm-4">
                                                                        <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>保存更改
                                                                        </button>
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
                    </div>
                </div>
                <div class="footer">
                </div>
            </div>
        </div>
    </body>
    <script>
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
</html>
