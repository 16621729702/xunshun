<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="uWEUXven75m9GdSxbwIOVtUhbDj7Wy8dtzBLemba">
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
    </head>
    <body>
        <div id="wrapper">
            <div id="page-wrapper" class="gray-bg" style="width: 100%;">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="ibox">
                                <div class="ibox-content">
                                    <form class="layui-form" action="${request.contextPath}/live/query" method="post" id="submitform" >
                                        <input type="hidden" name="page" value="${page!}" id="formpage"/>
                                        <input type="hidden" name="pagesize" value="${pagesize!}" id="formpagesize"/>
                                        <input type="hidden" name="merchid" lay-verify="" value="${merchid!''}" >
                                    </form>

                                    <div class="toolbar">
                                <span class="left">
                                    <#if merchid??>
                                        <a href="javascript:;" class="btn btn-primary load_iframe" data-url="${request.contextPath}/live/addpage?merchid=${merchid}"
                                           data-width="1000px" data-height="700px" ><i
                                                    class="fa fa-plus"></i>&nbsp;添加直播间</a>
                                    </#if>
                        </span>
                                        <div class="pager-page-num">
                                            <label>
                                                每页显示
                                                <select onchange="changepagesize(this.value);" >
                                                    <option value="20" ${(pagesize == '20')?string('selected','')}>20</option>
                                                    <option value="50" ${(pagesize == '50')?string('selected','')}>50</option>
                                                    <option value="100" ${(pagesize == '100')?string('selected','')}>100</option>
                                                    <option value="200" ${(pagesize == '200')?string('selected','')}>200</option>
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
                                                <th>名称</th>
                                                <th>分类名称</th>
                                                <th>所属商户</th>
                                                <th>观看人数</th>
                                                <th>封面图片</th>
                                                <th>是否被推荐</th>
                                                <th>是否开始直播</th>
                                                <th>聊天室id</th>
                                                <th>状态</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <#if (returninfo)??>
                                                <#if (returninfo?size > 0)>
                                                    <#list returninfo as list>
                                                        <tr>
                                                            <td>${list.id}</td>
                                                            <td>${list.name}</td>
                                                            <td>${list.categoryname!}</td>
                                                            <td>${list.merchname!}</td>
                                                            <td>${list.watchnum!}</td>
                                                            <td><img src="${list.img}" height="50px" width="50px"></td>
                                                            <td>
                                                                ${list.isrecommend!}
                                                            </td>
                                                            <td>
                                                                ${list.isstart!}
                                                            </td>
                                                            <td>
                                                                ${list.livegroupid!}
                                                            </td>
                                                            <td data-url="">
                                                                <input type="checkbox"   name="status" ${(list.status==0) ? string('checked','')} lay-skin="switch" lay-text="正常|关闭" data-id="${list.id}"  value="${list.status}" lay-filter = "change" >
                                                            </td>
                                                            <td>
                                                                    <a href="javascript:;" class="btn btn-outline btn-success load_iframe" data-url="${request.contextPath}/live/editpage?id=${list.id}" data-width="1000px" data-height="700px">
                                                                        编辑
                                                                    </a>
                                                                    <a href="javascript:;" class="btn btn-outline btn-danger " onclick="deletethis(${list.id})">
                                                                        删除
                                                                    </a>
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
        <script>
            if (navigator.appName == 'Microsoft Internet Explorer') {
                if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                    alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                }
            }
            $(document).ready(function () {
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
                var pagenum = $("#formpage").val();
                var pageindex = $("li[name$='otherpage']");//匹配所有name结尾为returna_date的数组
                for(var x=0; x<pageindex.length;x++){
                    var page = $(pageindex[x]).val();
                    if(pagenum==page){
                        $(pageindex[x]).addClass("active");
                    }
                }
            });
            function deletethis(id) {
                layer.open({
                    content: '确定要删除吗？',
                    yes: function (index, layero) {
                        $.ajax({
                            url: "${request.contextPath}/live/livedelete",
                            data: {"id": id},
                            dataType: "json",
                            success: function (result) {
                                if (result.code == "200") {
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
            layui.use('form', function(){
                var form = layui.form;
                form.render();

                form.on('switch(change)',function(obj){
                    var data = $(obj.elem);
                    var status = data.val();
                    if(status=="0"){status="1"}else{status="0"}

                    var id =data.attr("data-id");
                    $.ajax({
                        url:"${request.contextPath}/live/liveedit",
                        data:{"id":id,"status":status},
                        dataType:"json",
                        success:function(result){
                            if(result.code == "200"){
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
    </body>
</html>
