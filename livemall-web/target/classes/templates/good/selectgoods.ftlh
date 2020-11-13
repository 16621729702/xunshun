
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 |  </title>
        <meta name="csrf-token" content="1sIIBHW9nHtSl5uESS9R2rNkaompDHfOnowtePRN">
        <meta name="request-type" content="alert">
        <link href="${request.contextPath}/web/css/bootstrap.css" rel="stylesheet">
        <link href="${request.contextPath}/web/font-awesome/css/font-awesome.css" rel="stylesheet">
        <link href="${request.contextPath}/lib/Hui/css/iconfont.css" rel="stylesheet">
        <link href="${request.contextPath}/web/layui/css/layui.css?v=2212" rel="stylesheet">
        <link href="${request.contextPath}/web/css/animate.css" rel="stylesheet">
        <link href="${request.contextPath}/web/css/style.css?v=12223" rel="stylesheet">
        <link href="${request.contextPath}/web/css/main.css?v=666" rel="stylesheet">
        <link href="${request.contextPath}/lib/toastr/toastr.min.css" rel="stylesheet">
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
            <div class="" style="width: 100%;background: #fff;">
                <div class="">
                    <div class="ibox ">
                        <div class="ibox-content">
                            <form class="layui-form" action="${request.contextPath}/good/selectgood" id="submitform" method="post" >
                                <input type="hidden" name="page" value="${condient.page}" id="formpage">
                                <input type="hidden" name="pagesize" value="${condient.pagesize}" id="formpagesize">
                                <div class="layui-inline">
                                    <label class="layui-form-label">商品名称:</label>
                                    <div class="layui-input-inline">
                                        <input type="text" name="title" value="" placeholder="请输入商品名称" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline" style="padding-bottom: 5px">
                                    <div class="layui-input-inline">
                                        <button class="btn btn-success" type="submit" onclick="search()">搜索</button>
                                    </div>
                                </div>
                            </form>
                            <div class="toolbar">
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
                                        <input type="checkbox" class="check_all">
                                    </th>
                                    <th>商品ID</th>
                                    <th style="width: 100px;">名称</th>
                                    <th>商品图片</th>
                                    <th>价格</th>
                                    <th>库存</th>
                                    <th>销量</th>
                                    <th>状态</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <#if (returninfo)??>
                                    <#if (returninfo?size > 0)>
                                    <#list returninfo as list>
                                <tr >
                                    <td><input type="checkbox" name="goodid" class="check_item " data-title="${list.title!}"
                                               data-id="${list.id!}"></td>
                                    <td>${list.id}</td>
                                    <td>${list.title}</td>
                                    <td>
                                        <#if list.thumb?contains("http")>
                                            <img src="${list.thumb}" class="rounded img-md img_pop_show"/>
                                    <#else >
                                        <img src="${request.contextPath}/image/IoReadImage/${list.thumb}" class="rounded img-md img_pop_show"/>
                                    </#if>
                                    </td>
                                    <td>${list.marketprice}</td>
                                    <td>${list.stock}</td>
                                    <td>${list.sale_num}</td>
                                    <td>
                                        <span class="label label-success">${(list.state==0)?string('上架','下架')}</span>
                                    </td>
                                </tr>
                                </#list>
                                </#if>
                                </#if>
                                </tbody>
                            </table>
                            <div style="text-align: center">
                                <input style="width: 200px" type="button" class="btn btn-success commit" value="确定">
                            </div>
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

            layui.use(['laydate', 'element', 'form'], function () {
                var laydate = layui.laydate, element = layui.element;
                var form = layui.form;
                var layer = layui.layer;

                $(".line").click(function () {
                    if ($(this).find(".check_item").is(":checked")) {
                        $(this).find(".check_item").attr("checked", false);
                    } else {
                        $(this).find(".check_item").attr("checked", true);
                    }
                });

                $(".commit").click(function () {
                    layer.closeAll();
                    var index = parent.layer.getFrameIndex(window.name);
                    if (index) {
                        parent.layer.close(index);
                    }
                    //获取选择的用户id
                    var ids = [];
                    var titles = [];
                    $(".check_item:checked").each(function () {
                        var value = $(this).data('id');
                        var title = $(this).data('title');
                        ids.push(value);
                        titles.push(title);
                    });
                    //调用父类方法
                    window.parent.addGoods(ids, titles);
                });
            });

        </script>
    </body>
</html>
