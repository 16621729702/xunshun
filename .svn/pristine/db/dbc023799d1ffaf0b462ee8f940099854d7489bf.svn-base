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
                    var pagenum = $("#formpage").val();
                    var pageindex = $("li[name$='otherpage']");//匹配所有name结尾为returna_date的数组
                    for(var x=0; x<pageindex.length;x++){
                        var page = $(pageindex[x]).val();
                        if(pagenum==page){
                            $(pageindex[x]).addClass("active");
                        }
                    }
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
                                    <form class="layui-form" action="${request.contextPath}/good/query" id="submitform" method="post">
                                        <input type="hidden" name="page" value="${condient.page}" id="formpage">
                                        <input type="hidden" name="pagesize" value="${condient.pagesize}" id="formpagesize">
                                        <div class="layui-inline">
                                            <label class="layui-form-label">商品名称:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="name"  placeholder="商品名称"
                                                       value="${condient.title!''}"  class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">商户id:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="merchid"  placeholder="商户id"
                                                       value="${condient.merchid!''}"  class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">类别</label>
                                            <div class="layui-input-inline">
                                                <select name="categoryid" >
                                                    <option value="">全部</option>
                                                    <#if (categoryList)??>
                                                        <#if (categoryList?size > 0)>
                                                            <#list categoryList as list>
                                                                <option value="${list.id}" ${list.isselect} >${list.name}</option>
                                                            </#list>
                                                        </#if>
                                                    </#if>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">上架时间:</label>
                                            <div class="layui-input-inline" style="width: 150px;">
                                                <input type="text" class="layui-input  start_time" value="${condient.startdate!''}"
                                                       name="startdate"
                                                       readonly>
                                            </div>
                                            -
                                            <div class="layui-input-inline" style="width: 150px;">
                                                <input type="text" class="layui-input end_time" value="${condient.enddate!''}"
                                                       name="enddate"
                                                       readonly>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">状态</label>
                                            <div class="layui-input-inline">
                                                <select name="state" lay-verify="">
                                                    <option value="" ${(condient.state == '')?string('selected','')}>全部</option>
                                                    <option value="1" ${(condient.state == '1')?string('selected','')}>上架</option>
                                                    <option value="0" ${(condient.state == '0')?string('selected','')}>下架</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline" style="padding-bottom: 5px">
                                            <div class="layui-input-inline">
                                                <button class="btn btn-success" type="submit">搜索</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="toolbar">
                                        <span class="left">
                                             <a class="btn btn-primary" href="${request.contextPath}/good/addpage"><i class="fa fa-plus"></i>&nbsp;添加商品</a>
                                        </span>
                                        <div class="pager-page-num">
                                            <label>
                                                每页显示
                                                <select onchange="changepagesize(this.value)" >
                                                    <option value="20" ${(condient.pagesize == '20')?string('selected','')}>20</option>
                                                    <option value="50" ${(condient.pagesize == '50')?string('selected','')}>50</option>
                                                    <option value="100" ${(condient.pagesize == '100')?string('selected','')}>100</option>
                                                    <option value="200" ${(condient.pagesize == '200')?string('selected','')}>200</option>
                                                </select> 条
                                            </label>
                                        </div>
                                        <span class="pager-total-num">共有数据：
                                            <strong id="total-num">${totalsize}</strong> 条
                                        </span>
                                    </div>
                                    <table class="layui-table">
                                        <thead>
                                        <tr>
                                            <th>
                                                <input type="checkbox" class="check_all" onclick="checkall();" id="check_all">
                                            </th>
                                            <th>商品ID</th>
                                            <th>商品名称</th>
                                            <th>商品主图</th>
                                            <th>购买价</th>
                                            <th>库存</th>
                                            <th>运费</th>
                                            <th>总销量</th>
                                            <th>排序</th>
                                            <th>时间</th>
                                            <th>状态</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if (returninfo)??>
                                            <#if (returninfo?size > 0)>
                                                <#list returninfo as list>
                                                    <tr>
                                                        <td><input type="checkbox" name="goodid" class="check_item" value="${list.id}"></td>
                                                        <td>${list.id}</td>
                                                        <td>${list.title}</td>
                                                        <td>
                                                            <img class="rounded img-md img_pop_show" src="${list.thumb!}" height="50px" width="50px">
                                                        </td>
                                                        <td>${list.marketprice}</td>
                                                        <td>${list.stock}</td>
                                                        <td>${list.expressprice}</td>
                                                        <td>${list.sale_num}</td>
                                                        <td>${list.sort}</td>
                                                        <td>
                                                            创建: ${list.create_at!}<br/>
                                                            最后修改: ${list.update_at!}
                                                        </td>
                                                        <td>
                                                            <span class="label label-success">${(list.state==1)?string('上架','下架')}</span>
                                                        </td>
                                                        <td>
                                                            <a class="btn btn-outline btn-success" href="${request.contextPath}/good/editpage?id=${list.id}">编辑</a>
                                                            <button type="button" class="btn btn-outline btn-warning" data-id="${list.id}" data-value="${(list.state==0)?string('1','0')}" onclick="changestatus(this)" >
                                                                ${(list.state==1)?string('下架','上架')}
                                                            </button>
                                                            <button type="button" class="btn btn-outline btn-danger " onclick="deletethis(${list.id})">
                                                                删除
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </#list>
                                            </#if>
                                        </#if>
                                        </tbody>
                                    </table>
                                    <div class="datatable-footer" style="overflow: hidden">
                                        <div class="batch-btns" style="float: left">
                                            <button class="btn btn-default" type="button" onclick="batchoption('delete');"><i
                                                        class="fa fa-trash-o"></i> 批量删除
                                            </button>
                                            <button class="btn btn-default" type="button" onclick="batchoption('putaway');"><i
                                                        class="fa fa-exchange"></i> 批量上架
                                            </button>
                                            <button class="btn btn-default" type="button" onclick="batchoption('sold');"><i
                                                        class="fa fa-exchange"></i> 批量下架
                                            </button>
                                            <button class="btn btn-default " type="button" onclick="showcategory();"><i class="fa fa-exchange"></i> 批量分类
                                            </button>

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
                <div class="footer">
                </div>
            </div>
        </div>
    </body>
    <script>
        layui.use(['laydate', 'element'], function () {
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
        function changestatus(el){
            var state = $(el).attr("data-value");
            var id = $(el).attr("data-id");
            if(state){state="0"}else{state="1"}
            $.ajax({
                url:"${request.contextPath}/good/changestatus",
                data:{"id":id,"state":state},
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
        }
        function deletethis(id) {
            layer.open({
                content: '删除商品后不可恢复，确定吗？？',
                yes: function(index, layero){
                    $.ajax({
                        url:"${request.contextPath}/good/delete",
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
                }
            });
        }
        function  showcategory() {
            var ids = "";
            $('input[name="goodid"]:checked').each(function () {
                var idVal = $(this).attr("value");
                ids += idVal + ",";
            });
            ids = ids.substring(0, ids.lastIndexOf(','));
            if(ids==""){
                layer.open({
                    title: '警告'
                    , content: '请选择要操作的数据'
                });
            }else{
                layer.open({
                    type: 2,
                    title: '批量分配',
                    move: false,
                    area: ['1000px', '700px'],
                    content: '${request.contextPath}/good/editcategorypage?ids='+ids
                });
            }
        }
        function batchoption(operate) {
            var ids = "";
            $('input[name="goodid"]:checked').each(function () {
                var idVal = $(this).attr("value");
                ids += idVal + ",";
            });
            ids = ids.substring(0, ids.lastIndexOf(','));
            if(ids==""){
                layer.open({
                    title: '警告'
                    , content: '请选择要操作的数据'
                });
            }else{
                $.ajax({
                    url:"${request.contextPath}/good/batchoption",
                    data:{"operate":operate,"ids":ids},
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
            }
        }
        function checkall(){
            var box = document.getElementById("check_all");
            if(box.checked == false){//如果全选按钮不为选中
                $('input[name="goodid"]').each(function () {
                    $(this).prop("checked", false);
                });
            }else{
                $('input[name="goodid"]').each(function () {
                    $(this).prop("checked", true);
                });
            }
        }

    </script>

</html>
