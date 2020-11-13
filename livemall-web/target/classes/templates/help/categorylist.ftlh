<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="3ox1IhT7EWnsFtwHIeBg8Wwd0c2Xzaw1lR84pyxe">
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
                    form.render();
                });
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
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
                                    <form class="layui-form" action="${request.contextPath}/help/categoryquery" method="post" id="submitform"></form>
                                    <div class="toolbar">
                                        <span class="left">
                                        <a class="btn btn-primary load_iframe" data-url="${request.contextPath}/help/categoryaddpage" data-width="1000px" data-height="700px" href="javascript:;" ><i class="fa fa-plus"></i>&nbsp;添加帮助分类信息</a>
                                        </span>
                                        <div class="pager-page-num"></div>
                                        <span class="pager-total-num">共有数据：
                                            <strong id="total-num">${totalsize}</strong> 条
                                        </span>
                                    </div>
                                    <table class="layui-table">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>名称</th>
                                            <th>状态</th>
                                            <th>是否显示</th>
                                            <th>排序</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if (returninfo)??>
                                            <#if (returninfo?size > 0)>
                                                <#list returninfo as list>
                                                    <tr>
                                                        <td>${list.id!}</td>
                                                        <td>${list.name!}</td>
                                                        <td>
                                                            <#if list.state=0>可用</#if>
                                                            <#if list.state=1>不可用</#if>
                                                        </td>
                                                        <td>
                                                            <#if list.isshow=0>显示</#if>
                                                            <#if list.isshow=1>不显示</#if>
                                                        </td>
                                                        <td>${list.sort}</td>
                                                        <td>
                                                                <a href="javascript:;" class="btn btn-outline btn-success load_iframe"  data-url="${request.contextPath}/help/categoryeditpage?id=${list.id}" data-width="1000px" data-height="700px">
                                                                    编辑
                                                                </a>
                                                                <a href="javascript:;"  class="btn btn-outline btn-danger "  onclick="deletethis(${list.id})">
                                                                    删除
                                                                </a>
                                                        </td>
                                                    </tr>
                                                </#list>
                                            </#if>
                                        </#if>
                                        </tbody>
                                    </table>
                                    <div class="datatable-footer" style="overflow: hidden">
                                        <div class="batch-btns" style="float: left">
                                        </div>
                                        <div class="dataTables_paginate paging_simple_numbers"></div>
                                    </div>
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
        layui.use(['laydate', 'element','form'], function () {
            var laydate = layui.laydate, element = layui.element;
            var form = layui.form;
            var layer = layui.layer;

        });
        function deletethis(id) {
            layer.open({
                content: '确定要删除吗？',
                yes: function (index, layero) {
                    $.ajax({
                        url: "${request.contextPath}/help/categorydelete",
                        data: {"id": id},
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 200) {
                                $("#submitform").submit();
                            } else {
                                layer.open({
                                    title: '警告'
                                    , content: result.msg
                                });
                            }
                        }
                    });
                }
            });
        }
    </script>

</html>
