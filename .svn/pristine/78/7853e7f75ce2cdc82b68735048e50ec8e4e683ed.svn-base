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
                                    <form class="layui-form" action="${request.contextPath}/merch/query" method="post" id="submitform">
                                        <input type="hidden" name="pagesize" value="${pagesize}" id="formpagesize">
                                        <input type="hidden" name="page" value="${page}" id="formpage">
                                        <div class="layui-inline">
                                            <label class="layui-form-label">名称:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="store_name" placeholder="请输入名称" class="layui-input" value="${store_name!}">
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
                                            <a class="btn btn-primary load_iframe" data-url="${request.contextPath}/merch/addpage" data-width="1000px" data-height="700px" href="javascript:;" ><i class="fa fa-plus"></i>&nbsp;添加商户</a>
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
                                        <span class="pager-total-num">共有数据：
                                            <strong id="total-num">${totalsize}</strong> 条
                                        </span>
                                    </div>
                                    <table class="layui-table">
                                        <thead>
                                        <tr>
                                            <th width="10%">ID</th>
                                            <th width="10%">头像</th>
                                            <th width="10%">名称</th>
                                            <th width="10%">商户号</th>

<#--
                                            <th width="20%">介绍</th>
-->
                                            <th width="10%">微信号</th>
                                            <th width="10%">手机号</th>
                                            <th width="10%">背景图</th>
                                            <th width="10%">状态</th>
                                            <th width="20%">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if (returninfo)??>
                                            <#if (returninfo?size > 0)>
                                                <#list returninfo as list>
                                                    <tr>
                                                        <td>${list.id}</td>
                                                        <td>
                                                            <img class="rounded img-md img_pop_show" src="${list.avatar!}" height="50px" width="50px">
                                                        </td>
                                                        <td>${list.store_name!}</td>
                                                        <td>${list.merchno!}</td>
<#--
                                                        <td>${list.description!}</td>
-->
                                                        <td>${list.weixin!}</td>
                                                        <td>${list.mobile!}</td>
                                                        <td><img class="rounded img-md img_pop_show" src="${list.bg_image!}" height="50px" width="50px" ></td>
                                                        <td>
                                                           <#if list.state==0>待认证</#if>
                                                            <#if list.state==1>正常</#if>
                                                            <#if list.state==2>封号</#if>
                                                        </td>
                                                        <td>
                                                            <div class="btn-group">
                                                                <a class="btn btn-success load_iframe" data-url="${request.contextPath}/good/merchquery?merchid=${list.id}" data-width="1000px" data-height="700px" href="javascript:;" >&nbsp;商品</a>
                                                            </div>
                                                            <div class="btn-group">
                                                                <a class="btn btn-warning load_iframe" data-url="${request.contextPath}/orders/merchorderquery?merchid=${list.id}" data-width="1000px" data-height="700px" href="javascript:;" >&nbsp;订单</a>
                                                            </div>
                                                            <div class="btn-group">
                                                                <a class="btn btn-info load_iframe" data-url="${request.contextPath}/live/merchquery?merchid=${list.id}" data-width="1000px" data-height="700px" href="javascript:;" >&nbsp;直播间</a>
                                                            </div>
                                                            <div class="btn-group">
                                                                <a class="btn btn-primary load_iframe" data-url="${request.contextPath}/merch/editpage?id=${list.id}" data-width="1000px" data-height="700px" href="javascript:;" >&nbsp;编辑</a>
                                                            </div>
                                                            <!--
                                                            <div class="btn-group">
                                                                <a href="javascript:;"  class="btn btn-outline btn-danger "  onclick="deletethis(${list.id})">
                                                                    删除
                                                                </a>
                                                            </div>-->
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
        layui.use(['laydate', 'element','form'], function () {
            var laydate = layui.laydate, element = layui.element;
            var form = layui.form;
            var layer = layui.layer;

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
                        url: "${request.contextPath}/merch/delete",
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
