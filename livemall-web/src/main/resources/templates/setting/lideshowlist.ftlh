<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="P0mRlgIzX3MmpC3Y5wwCVJdq8TNNjjCnDFWCrsKE">
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
                                    <form class="layui-form" action="${request.contextPath}/lideshow/query" id="submitform" method="post">
                                        <input type="hidden" name="pagesize" value="${pagesize!}" id="formpagesize">
                                        <input type="hidden" name="page" value="${page!}" id="formpage">
                                        <div class="layui-inline">
                                            <label class="layui-form-label">名称:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="name" lay-verify="" placeholder="轮播图名称" value="${name!}" autocomplete="off" class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">显示位置</label>
                                            <div class="layui-input-block">
                                                <select name="type" lay-verify="required" lay-search="" lay-filter="type">
                                                    <option value="" selected >请选择</option>
                                                    <option value="1" ${(type=='1')?string('selected','')} >首页</option>
                                                    <option value="2" ${(type=='2')?string('selected','')}>店铺</option>
                                                    <option value="3" ${(type=='3')?string('selected','')}>商城</option>
                                                    <option value="4" ${(type=='4')?string('selected','')}>分类</option>

                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline" style="padding-bottom: 5px">
                                            <div class="layui-input-inline">
                                                <input type="hidden" name="page_num" value="20">
                                                <button name="" class="btn btn-success" type="submit">搜索</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="toolbar">
                            <span class="left">
                            <a class="btn btn-primary" href="${request.contextPath}/lideshow/addpage" ><i class="fa fa-plus"></i>&nbsp;添加类别</a>
                        </span>
                                        <div class="pager-page-num">
                                            <label>
                                                每页显示
                                                <select onchange="changepagesize(this.value)">
                                                    <option  value="20" ${(pagesize=='20')?string('selected','')}>20</option>
                                                    <option value="50" ${(pagesize=='50')?string('selected','')}>50</option>
                                                    <option value="100" ${(pagesize=='100')?string('selected','')}>100</option>
                                                    <option value="200" ${(pagesize=='200')?string('selected','')}>200</option>
                                                </select> 条
                                            </label>
                                        </div>
                                        <span class="pager-total-num">共有数据：<strong id="total-num">${totalsize}</strong> 条</span>
                                    </div>
                                    <div class="">
                                        <table class="layui-table">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>位置</th>
                                                <th>名称</th>
                                                <th>排序</th>
                                                <th>轮播图</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <#if (returninfo)??>
                                            <#if (returninfo?size > 0)>
                                            <#list returninfo as list>
                                            <tr>
                                                <td>${list.id!}</td>
                                                <td>
                                                    <#if list.type==1>首页</#if>
                                                    <#if list.type==2>店铺</#if>
                                                    <#if list.type==3>商城</#if>
                                                    <#if list.type==4>分类</#if>
                                                </td>
                                                <td>${list.name!}</td>
                                                <td>${list.sort!}</td>
                                                <td><img class="rounded img-md img_pop_show" src="${list.pic!}"></td>
                                                <td>
                                                    <a class="btn btn-outline btn-success load_iframe" href="javascript:;" data-url="${request.contextPath}/lideshow/editpage?id=${list.id!}" data-width="1000px" data-height="700px">编辑</a>
                                                    <a class="btn btn-outline btn-success " onclick="deletethis(${list.id})" href="javascript:;">删除</a>
                                                </td>
                                            </tr>
                                            </#list>
                                            </#if>
                                            </#if>
                                        </table>
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
                </div>
                <div class="footer"></div>
            </div>
        </div>
<script>
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
                    url: "${request.contextPath}/lideshow/delete",
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
