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
                                    <form class="layui-form" action="${request.contextPath}/merch/applyquery" method="post" id="submitform">
                                        <input type="hidden" name="pagesize" value="${pagesize}" id="formpagesize">
                                        <input type="hidden" name="page" value="${page}" id="formpage">
                                        <div class="layui-inline">
                                            <label class="layui-form-label">真实名字:</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="true_name" placeholder="请输入名称" class="layui-input" value="${true_name!}">
                                            </div>
                                        </div>
                                        <div class="layui-inline" style="padding-bottom: 5px">
                                            <div class="layui-input-inline">
                                                <button class="btn btn-success" type="submit">搜索</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="toolbar">
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
                                            <strong id="total-num">${totalsize!}</strong> 条
                                        </span>
                                    </div>
                                    <table class="layui-table">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>商户类型</th>
                                            <th>商户名称</th>
                                            <th>经营主体</th>
                                            <th>主营类目</th>
                                            <th>真实姓名</th>
                                            <th>身份证号</th>
                                            <th>营业执照</th>
                                            <th>银行开户证明</th>
                                            <th>身份证人像面</th>
                                            <th>身份证国徽面</th>
                                            <th>手持身份证照</th>
                                            <th>状态</th>
                                            <th>原因</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if (returninfo)??>
                                            <#if (returninfo?size > 0)>
                                                <#list returninfo as list>
                                                    <tr>
                                                        <td>${list.id}</td>
                                                        <td>${list.categoryname!}</td>
                                                        <td>${list.store_name!}</td>
                                                        <td>${list.businessname!}</td>
                                                        <td>${list.sellname!}</td>
                                                        <td>${list.realname!}</td>
                                                        <td>${list.idcard!}</td>
                                                         <td><img src="${list.business_license!}" height="50px" width="50px" class="rounded img-md img_pop_show"></td>
                                                        <td><img src="${list.bank_account!}" height="50px" width="50px" class="rounded img-md img_pop_show"></td>
                                                        <td><img src="${list.idcard_front!}" height="50px" width="50px" class="rounded img-md img_pop_show"></td>
                                                        <td><img src="${list.idcard_back!}" height="50px" width="50px" class="rounded img-md img_pop_show"></td>
                                                        <td><img src="${list.idcard_hold!}" height="50px" width="50px" class="rounded img-md img_pop_show"></td>
                                                        <td>
                                                           <#if list.state==0>待审核</#if>
                                                            <#if list.state==1>审核通过</#if>
                                                            <#if list.state==2>审核失败</#if>
                                                        </td>
                                                        <td>${list.reason!}</td>
                                                        <td>
                                                            <div class="btn-group">
                                                                <div class="btn-group">
                                                                    <a class="btn btn-primary load_iframe" data-url="${request.contextPath}/merch/applyeditpage?id=${list.id}" data-width="1000px" data-height="700px" href="javascript:;" >&nbsp;编辑</a>
                                                                </div>
                                                            </div>
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
        function pass(merchid) {
            $.ajax({
                url:"${request.contextPath}/merch/applyqueryedit",
                data:{"id":merchid,"status":"1"},
                dataType:"json",
                success:function(result){
                    if(result.code == 200){
                        window.location.href="${request.contextPath}/merch/applyquery";//回到父级页面
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        }
        function nopass(merchid) {
            $.ajax({
                url:"${request.contextPath}/merch/applyqueryedit",
                data:{"id":merchid,"status":"2"},
                dataType:"json",
                success:function(result){
                    if(result.code == 200){
                        window.location.href="${request.contextPath}/merch/applyquery";//回到父级页面
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        }
    </script>

</html>
