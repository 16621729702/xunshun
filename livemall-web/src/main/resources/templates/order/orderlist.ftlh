<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="80gKX72QieZfLjXeVXTnJ9irvKrapI71lM3K1bgP">
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
        <link href="${request.contextPath}/web/css/plugins/sweetalert/sweetalert.css?v=33" rel="stylesheet">
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
                            <div class="ibox">
                                <div class="ibox-content">
                                    <form class="layui-form" action="${request.contextPath}/orders/orderquery" method="post" id="submitform">
                                        <input type="hidden" name="pagesize" value="${pagesize}" id="formpagesize">
                                        <input type="hidden" name="page" value="${page}" id="formpage">
                                        <div class="layui-inline">
                                            <label class="layui-form-label">订单号:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="orderid" lay-verify=""
                                                       placeholder="订单号搜索"
                                                       value="${orderid!}" autocomplete="off" class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">商户id:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="merchid" lay-verify=""
                                                       placeholder="商户id"
                                                       value="${merchid!}" autocomplete="off" class="layui-input">
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">订单状态</label>
                                            <div class="layui-input-inline">
                                                <select name="status" lay-verify="">
                                                    <option selected value="">全部</option>
                                                    <option ${(status=='0')?string('selected','')}  value="0">未支付</option>
                                                    <option ${(status=='1')?string('selected','')}  value="1">待发货</option>
                                                    <option ${(status=='2')?string('selected','')}  value="2">待收货</option>
                                                    <option ${(status=='3')?string('selected','')}  value="3">待评价</option>
                                                    <option ${(status=='4')?string('selected','')}  value="4">已完成</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">支付方式</label>
                                            <div class="layui-input-inline">
                                                <select name="paystatus" lay-verify="">
                                                    <option selected value="">全部</option>
                                                    <option ${(paystatus=='支付宝支付')?string('selected','')}  value="0">支付宝支付</option>
                                                    <option ${(paystatus=='微信支付')?string('selected','')}  value="1">微信支付</option>
                                                    <option ${(paystatus=='云支付')?string('selected','')}  value="2">云支付</option>

                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">时间类型</label>
                                            <div class="layui-input-inline">
                                                <select name="timetype" lay-verify="">
                                                    <option selected value="">全部</option>
                                                    <option ${(timetype=='1')?string('selected','')}  value="1">按下单时间</option>
                                                    <option ${(timetype=='2')?string('selected','')}  value="2">按付款时间</option>
                                                    <option ${(timetype=='3')?string('selected','')}  value="3">按发货时间</option>
                                                    <option ${(timetype=='4')?string('selected','')}  value="4">按完成时间</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">时间:</label>
                                            <div class="layui-input-inline" style="width: 150px;">
                                                <input type="text" class="layui-input  start_time" value="" name="startdate" readonly>
                                            </div>
                                            -
                                            <div class="layui-input-inline" style="width: 150px;">
                                                <input type="text" class="layui-input end_time" value="" name="enddate" readonly>
                                            </div>
                                        </div>
                                        <div class="layui-inline" style="padding-bottom: 5px">
                                            <div class="layui-input-inline">
                                                <button class="btn btn-success" type="submit">搜索</button>
                                                <input  type="button" class="btn btn-success" value="导出" onclick="exportorderinfo()"/>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="toolbar">
                            <span class="left">
                            <span class="static-left-text">订单总金额: ${totalprice!}</span>
                            <span class="static-left-text">|</span>
                            <span class="static-left-text">售出商品: ${totalnum!}</span>
                        </span>
                                        <div class="pager-page-num">
                                            <label>
                                                每页显示
                                                <select onchange="changepagesize(this.value)">
                                                    <option value="20" ${(pagesize=='20')?string('selected','')}>20</option>
                                                    <option value="50" ${(pagesize=='50')?string('selected','')}>50</option>
                                                    <option value="100" ${(pagesize=='100')?string('selected','')}>100</option>
                                                    <option value="200" ${(pagesize=='200')?string('selected','')}>200</option>
                                                </select> 条
                                            </label>
                                        </div>
                                        <span class="pager-total-num">共有数据：<strong id="total-num">${totalsize!}</strong> 条</span>
                                    </div>
                                    <table class="layui-table">
                                        <thead>
                                        <tr>
                                            <th>
                                                <input type="checkbox" class="check_all" onclick="checkall();" id="check_all">
                                            </th>
                                            <th>订单号</th>
                                            <th>用户信息</th>
                                            <th>商品信息</th>
                                            <th>实际支付</th>
                                            <th>支付方式</th>
                                            <th>订单状态</th>
                                            <th>支付时间</th>
                                            <th>下单时间</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if returninfo?? && (returninfo?size > 0) >
                                            <#list returninfo?keys as k>
                                                <tr style="background: #f2f2f2;">
                                                    <th colspan="13" style="text-align: left;">
                                                    </th>
                                                </tr>
                                                <#if (returninfo[k])??>
                                                <#if (returninfo[k]?size > 0)>
                                                    <#list returninfo[k] as list>
                                                        <tr class="">
                                                            <td><input type="checkbox" name="pkey" class="check_item" value="${list.id!}"></td>
                                                            <td>
                                                                ${list.orderid!}
                                                            </td>
                                                            <td>
                                                                ${list.chargename!}<br/>
                                                                ${list.chargephone!}
                                                            </td>
                                                            <td style="max-width: 300px;text-align: left" >
                                                                <#if list.goodimg?contains("http")>
                                                                    <img title="${list.goodname!}" src="${list.goodimg!}" class="img_pop_show rounded img-sm"/>
                                                                <#else >
                                                                    <img title="${list.goodname!}" src="${request.contextPath}/image/IoReadImage/${list.goodimg!}" class="img_pop_show rounded img-sm"/>
                                                                </#if>
                                                                &nbsp;${list.goodname!}
                                                                | ￥${list.goodprice!} x ${list.goodnum!}<br/>
                                                            </td>
                                                            <td>
                                                                ${list.realpayprice!}
                                                            </td>
                                                            <td>
                                                                ${list.paystatus!}
                                                            </td>
                                                            <td>
                                                                <span style="color: gray">${list.status!}</span>
                                                            </td>
                                                            <td>
                                                                ${list.paytime!}
                                                            </td>
                                                            <td>
                                                                ${list.createtime!}
                                                            </td>
                                                            <td>
                                                                <#if list.status=="待收货" >
                                                                    <a href="#" class="btn btn-success load_iframe" data-url="${request.contextPath}/orders/editexpressinfo?orderid=${list.orderid}"><i class="fa fa-shopping-cart"></i> 配送信息</a>
                                                                </#if>
                                                                <div class="btn-group">
                                                                    <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle">操作 </button>
                                                                    <ul class="dropdown-menu">
<#--                                                                         <#if list.status=="待发货" >-->
<#--                                                                             <li><a class="dropdown-item load_iframe"  data-width="1000px" data-height="700px"  href="#">立即退款</a></li>-->
<#--                                                                         </#if>-->
<#--                                                                        <#if list.status=="待收货" >-->
<#--                                                                            <li><a class="dropdown-item load_iframe" data-url="${request.contextPath}/orders/refund?id=${list.id}" data-width="1000px" data-height="700px"  href="#">立即退款</a></li>-->
<#--                                                                            <li><a class="dropdown-item "  onclick="takegood('${list.id}');" href="#">已收货</a></li>-->
<#--                                                                        </#if>-->
                                                                        <li><a class="dropdown-item order_remark load_iframe" data-url="${request.contextPath}/orders/comment?id=${list.id}&content=${list.ordercomment!}"  data-width="1000px" data-height="700px">订单备注</a></li>
                                                                        <li><a class="dropdown-item load_iframe"  data-url="${request.contextPath}/orders/orderlog?orderid=${list.orderid}"  data-width="1000px" data-height="700px">订单记录</a></li>
                                                                    </ul>
                                                                </div>
                                                                <a href="${request.contextPath}/orders/detail?id=${list.id!}" class="btn btn-success" ><i class="fa fa-file-text-o"></i> 订单详情</a>
                                                            </td>
                                                        </tr>
                                                    </#list>
                                                </#if>
                                                </#if>
                                            </#list> 
                                        </#if>
                                        </tbody>
                                    </table>
                                    <div class="datatable-footer" style="overflow: hidden">
                                        <div class="batch-btns" style="float: left">
                                            <button class="btn btn-default" type="button" onclick="batchoption('delete');"><i class="fa fa-trash-o"></i> 批量删除
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
                <div class="footer"></div>
            </div>
        </div>
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
            function takegood(id){
                $.ajax({
                    url:"${request.contextPath}/orders/takegood",
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

            function batchoption(operate) {
                var ids = "";
                $('input[name="pkey"]:checked').each(function () {
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
                        url:"${request.contextPath}/orders/batchoption",
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

            function exportorderinfo(){
                $("#submitform").attr('action','${request.contextPath}/export/orderexport');
                $("#submitform").submit();
            }

            function checkall(){
                var box = document.getElementById("check_all");
                if(box.checked == false){//如果全选按钮不为选中
                    $('input[name="pkey"]').each(function () {
                        $(this).prop("checked", false);

                    });
                }else{
                    $('input[name="pkey"]').each(function () {
                        $(this).prop("checked", true);

                    });
                }
            }
        </script>

    </body>
</html>
