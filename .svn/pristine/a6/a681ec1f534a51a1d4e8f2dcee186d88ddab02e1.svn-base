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
                                    <form class="layui-form" action="${request.contextPath}/tag/query" id="submitform">
                                        <div class="layui-inline">
                                            <label class="layui-form-label">专题类型:</label>
                                            <div class="layui-input-inline">
                                                <select name="type" lay-verify="">
                                                    <option value="">全部</option>
                                                    <option value="1" ${(type=='1')?string('selected','')}>商品专题</option>
                                                    <option value="2" ${(type=='2')?string('selected','')}>直播间专题</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">显示样式:</label>
                                            <div class="layui-input-inline">
                                                <select name="style" lay-verify="">
                                                    <option value="">全部</option>
                                                    <option value="1" ${(style=='1')?string('selected','')}>风格1</option>
                                                    <option value="2" ${(style=='2')?string('selected','')}>风格2</option>
                                                    <option value="3" ${(style=='3')?string('selected','')}>风格3</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">专题名称:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="name" lay-verify="" placeholder="专题名称搜索"
                                                       value="${name!}" autocomplete="off" class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline" style="padding-bottom: 5px">
                                            <div class="layui-input-inline">
                                                <input type="hidden" name="pagesize" value="${pagesize}" id="formpagesize">
                                                <input type="hidden" name="page" value="${page}" id="formpage">
                                                <button name="" class="btn btn-success" type="submit">搜索</button>
                                            </div>
                                        </div>
                                    </form>

                                    <div class="toolbar">
                                <span class="left">
                            <a class="btn btn-primary load_iframe" data-url="${request.contextPath}/tag/addpage" data-width="1000px" data-height="700px" href="javascript:;" ><i
                                        class="fa fa-plus"></i>&nbsp;添加专题会场</a>
                                </span>
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
                                        <span class="pager-total-num">共有数据：<strong id="total-num">${totalsize}</strong> 条</span>
                                    </div>
                                    <form class="layui-form form-horizontal" id="ajaxform">
                                        <table class="layui-table">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>类型</th>
                                                <th>名称</th>
                                                <th>标语</th>
                                                <th>样式</th>
                                                <th>排序</th>
                                                <th>状态</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <#if (returninfo)??>
                                            <#if (returninfo?size > 0)>
                                            <#list returninfo as list>
                                                <tr>
                                                    <td>${list.id!}</td>
                                                    <td>${list.type!}</td>
                                                    <td>${list.name!}</td>
                                                    <td>${list.title!}</td>
                                                    <td>${list.style!}</td>
                                                    <td>${list.sort!}</td>
                                                    <td>
                                                        <input type="checkbox" ${(list.isshow==1) ? string('checked','')} lay-skin="switch" lay-text="显示|隐藏"  data-id="${list.id}"  value="${list.isshow}" lay-filter = "change" >
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <a href="javascript:;" class="btn btn-outline btn-success load_iframe"  data-url="${request.contextPath}/tag/editpage?id=${list.id!}" data-width="1000px" data-height="700px">
                                                                编辑
                                                            </a>
                                                            &nbsp;&nbsp;
                                                            <a href="javascript:;"  class="btn btn-outline btn-danger"  onclick="deletethis(${list.id})">
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
                                            <ul class="pagination">
                                                <li onclick="submitform('1');"><span>&laquo;</span></li>
                                                <#list 1..totalpage as i>
                                                    <li onclick="submitform('${i_index+1}');" name="otherpage" value="${i_index+1}"><span>${i_index+1}</span></li>
                                                </#list>
                                                <li onclick="submitform('${totalpage}');"><span>&raquo;</span></li>
                                            </ul>
                                            <div class="gotopage">
                                                前往
                                                <input type="number" class="gopagenum"/>
                                                页
                                                <input type="button" class="btn btn-default btn-sm btn-gotopage" value="确定" onclick="querypage()"/>
                                            </div>

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
                        url:"${request.contextPath}/tag/changestatus",
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
                    url: "${request.contextPath}/tag/delete",
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
