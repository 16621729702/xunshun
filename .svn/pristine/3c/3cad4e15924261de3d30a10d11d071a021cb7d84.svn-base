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
    <nav class="navbar-default navbar-static-side"  id="left"></nav>
    <div id="page-wrapper" class="gray-bg">
        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox">
                        <div class="ibox-content">
                            <form class="layui-form" action="${request.contextPath}/goodcategory/query" method="post">
                                <div class="layui-inline">
                                    <label class="layui-form-label">分类名称:</label>
                                    <div class="layui-input-inline">
                                        <input type="text" name="name" lay-verify="" placeholder="分类名称"
                                               value="${name!''}" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline" style="padding-bottom: 5px">
                                    <div class="layui-input-inline">
                                        <button name="submit" class="btn btn-success" type="submit">搜索</button>
                                    </div>
                                </div>

                            </form>

                            <div class="toolbar">
                                <span class="left">
                            <a href="javascript:;" class="btn btn-primary load_iframe" data-url="${request.contextPath}/goodcategory/addpage"
                               data-width="1000px" data-height="700px" ><i
                                    class="fa fa-plus"></i>&nbsp;添加类别</a>
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
                                        <th>父级</th>
                                        <th>名称
                                            (点击查看二级分类)
                                        </th>
                                        <#if isshow>
                                            <#else >
                                           <th>分类图标</th>
                                        </#if>
                                        <th>排序</th>
                                        <#if isshow>
                                        <#else >
                                            <th>是否推荐</th>
                                        </#if>
                                        <th>状态</th>
                                        <#if isshow>
                                            <th>是否展示在商户入驻页</th>
                                        </#if>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#if (returninfo)??>
                                        <#if (returninfo?size > 0)>
                                            <#list returninfo as list>
                                                <tr>
                                                    <td>${list.id}</td>
                                                    <td>
                                                       ${list.pname}
                                                    </td>
                                                    <td>
                                                        <#if isshow>
                                                            <a title="点击查看二级分类" class="link-color" href="${request.contextPath}/goodcategory/query?pid=${list.id}">${list.name}</a>
                                                            <#else >
                                                                ${list.name}
                                                        </#if>
                                                    </td>
                                                    <#if isshow>
                                                        <#else>
                                                        <td><img src="${list.pic!}" width="50px" height="50px" class="rounded img-md img_pop_show"></td>
                                                    </#if>
                                                    <td>${list.sort}</td>

                                                        <#if isshow>
                                                        <#else >
                                                            <td>
                                                                <input type="checkbox"   name="isrecommend" ${(list.isrecommend=='1') ? string('checked','')} lay-skin="switch" lay-text="显示|隐藏" data-id="${list.id}"  value="${list.isrecommend!}" lay-filter = "recommendchange" >
                                                            </td>
                                                        </#if>

                                                    <td data-url="">
                                                        <input type="checkbox"   name="isshow" ${(list.isshow=='0') ? string('checked','')} lay-skin="switch" lay-text="显示|隐藏" data-id="${list.id}"  value="${list.isshow}" lay-filter = "change" >
                                                    </td>
                                                    <#if isshow>
                                                        <td data-url="">
                                                            <input type="checkbox"   name="merchshow" ${(list.merchshow=='0') ? string('checked','')} lay-skin="switch" lay-text="显示|隐藏" data-id="${list.id}"  value="${list.merchshow}" lay-filter = "merchchange" >
                                                        </td>
                                                    </#if>
                                                    <td>
                                                            <a href="javascript:;" class="btn btn-outline btn-success load_iframe" data-url="${request.contextPath}/goodcategory/editpage?id=${list.id}" data-width="1000px" data-height="700px">
                                                                编辑
                                                            </a>
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
        <form id="submitform" action="${request.contextPath}/goodcategory/query" method="post">
            <input type="hidden" name="name" value="${name!''}"/>
            <input type="hidden" name="page" value="${page}" id="formpage"/>
            <input type="hidden" name="pagesize" value="${pagesize}" id="formpagesize"/>
            <input type="hidden" name="pid" value="${pid!''}"/>
        </form>
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
                    url: "${request.contextPath}/goodcategory/delete",
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
                url:"${request.contextPath}/goodcategory/edit",
                data:{"id":id,"status":status},
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
        form.on('switch(merchchange)',function(obj){
            var data = $(obj.elem);
            var merchshow = data.val();
            if(merchshow=="0"){merchshow="1"}else{merchshow="0"}
            var id =data.attr("data-id");
            $.ajax({
                url:"${request.contextPath}/goodcategory/edit",
                data:{"id":id,"merchshow":merchshow},
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
        form.on('switch(recommendchange)',function(obj){
            var data = $(obj.elem);
            var isrecommend = data.val();
            if(isrecommend=="0"){isrecommend="1"}else{isrecommend="0"}
            var id =data.attr("data-id");
            $.ajax({
                url:"${request.contextPath}/goodcategory/edit",
                data:{"id":id,"isrecommend":isrecommend},
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
</body>
</html>
