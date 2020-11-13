<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 | 系统配置            </title>
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

        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
            <div id="page-wrapper" class="gray-bg">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="ibox">
                                <div class="ibox-content">
                                    <form class="layui-form" action="${request.contextPath}/template/query" id="submitform" >
                                        <div class="layui-inline">
                                            <label class="layui-form-label">状态:</label>
                                            <div class="layui-input-inline">
                                                <select name="status" lay-verify="">
                                                    <option value="">全部</option>
                                                    <option  value="1" ${(status=='1')?string('selected','')}>启用</option>
                                                    <option  value="0" ${(status=='0')?string('selected','')}>禁用</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">模版名称:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="name" lay-verify="" placeholder="模版名称"
                                                       value="${name!}" autocomplete="off" class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline" style="padding-bottom: 5px">
                                            <div class="layui-input-inline">
                                                <input type="hidden" name="pagesize" value="${pagesize!}" id="formpagesize">
                                                <input type="hidden" name="page" value="${page!}" id="formpage">
                                                <button name="" class="btn btn-success" type="submit">搜索</button>
                                            </div>
                                        </div>
                                    </form>

                                    <div class="toolbar">
                            <span class="left">
                            <a class="btn btn-primary load_iframe" data-url="${request.contextPath}/template/addpage" data-width="1000px" data-height="700px" href="javascript:;" ><i class="fa fa-plus"></i>&nbsp;添加模版</a></span>
                                        <div class="pager-page-num">
                                            <label>
                                                每页显示
                                                <select onchange="changepagesize(this.value)">
                                                    <option ${(pagesize=='20')?string('selected','')} value="20">20</option>
                                                    <option ${(pagesize=='50')?string('selected','')} value="50">50</option>
                                                    <option ${(pagesize=='100')?string('selected','')} value="100">100</option>
                                                    <option ${(pagesize=='200')?string('selected','')} value="200">200</option>
                                                </select> 条
                                            </label>
                                        </div>
                                        <span class="pager-total-num">共有数据：
                                            <strong id="total-num">${totalsize}</strong> 条
                                        </span>
                                    </div>
                                    <form class="layui-form form-horizontal" id="ajaxform">
                                        <table class="layui-table">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>模版名称</th>
                                                <th>模版编号</th>
                                                <th>模版ID</th>
                                                <th>模版格式</th>
                                                <th>状态</th>
                                                <th>添加时间</th>
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
                                                    <td>${list.tempkey!}</td>
                                                    <td>${list.tempid!}</td>
                                                    <td>
                                                        <pre>${list.content!}</pre>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" ${(list.status==1) ? string('checked','')} lay-filter="change" name="status" data-id="${list.id}" lay-skin="switch" lay-text="启用|禁用" value="${list.status!}">
                                                    </td>
                                                    <td>
                                                        ${(list.created_at?string("yyyy-MM-dd HH:mm:ss"))!}
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <a href="javascript:;" class="btn btn-outline btn-success load_iframe"  data-url="${request.contextPath}/template/editpage?id=${list.id!}" data-width="1000px" data-height="700px">
                                                                编辑
                                                            </a>&nbsp;
                                                            <a href="javascript:;" class="btn btn-outline btn-danger" onclick="deletethis(${list.id})">
                                                                删除
                                                            </a>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </#list>
                                            </#if>
                                            </#if>
                                            </tbody>
                                        </table>
                                    </form>
                                    <div class="datatable-footer" style="overflow: hidden">
                                        <div class="batch-btns" style="float: left">
                                        </div>
                                        <div class="dataTables_paginate paging_simple_numbers">

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer"></div>
            </div>
        </div>

        <script>
            layui.use(['laydate', 'element','form'], function () {
                var laydate = layui.laydate, element = layui.element;
                var form = layui.form;
                var layer = layui.layer;

                form.on('switch(change)',function(obj){
                    var data = $(obj.elem);
                    var id =data.attr("data-id");
                    $.ajax({
                        url:"${request.contextPath}/template/changestatus",
                        data:{"id":id},
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                $("#submitform").submit();
                            }else{
                                layer.open({
                                    title: '警告'
                                    ,content: result.msg
                                });
                            }
                        }});
                })


            });

        </script>
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
    function changepagesize(pagesize){
        $("#formpagesize").val(pagesize);
        $("#submitform").submit();
    }
    function querypage(){
        var page = $(".gopagenum").val();
        $("#formpage").val(page);
        $("#submitform").submit();
    }
    function submitform(page){
        $("#formpage").val(page);
        $("#submitform").submit();
    }
    function deletethis(id) {
        layer.open({
            content: '确定要删除吗？',
            yes: function (index, layero) {
                $.ajax({
                    url: "${request.contextPath}/template/delete",
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
    </body>
</html>
