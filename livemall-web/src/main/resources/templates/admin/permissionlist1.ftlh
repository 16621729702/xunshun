
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>后台管理
    </title>
    <meta name="csrf-token" content="SgGoq0h3KRT1pGLrBK9ryvteA1GmHyJIrwNytvhQ">
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
                        <div class="ibox-title">
                            <a  href="javascript:void(0);" class="btn btn-primary load_iframe" data-url="${request.contextPath}/permission/insertpage" data-width="1000px" data-height="80%">添加权限项</a>
                        </div>
                        <div class="ibox-content">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>父级</th>
                                        <th>名称</th>
                                        <th>代号</th>
                                        <th>排序</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <#if (permissionlist)??>
                                        <#if (permissionlist?size > 0)>
                                            <#list permissionlist as list>
                                                <tr>
                                                    <td>
                                                        ${list.id}
                                                    </td>
                                                    <td>
                                                        ${list.pid}
                                                    </td>
                                                    <td>
                                                        ${list.name}
                                                    </td>
                                                    <td>${list.code}</td>
                                                    <td>${list.orderno}</td>
                                                    <td>${(list.status=='ACTIVE') ? string('启用','禁用')}</td>
                                                    <td>
                                                        <a  href="javascript:;" class="btn  btn-info load_iframe" data-url="${request.contextPath}/permission/editpage?id=${list.id}" data-width="1000px" data-height="80%">编辑</a>
                                                        <a href="javascript:;"
                                                           class="btn btn-outline btn-danger"
                                                           onclick="deletethis(${list.id})" >
                                                            删除
                                                        </a>
                                                        <div class="btn-group">
                                                            <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle">${(list.status=='ACTIVE') ? string('启用','禁用')}
                                                            </button>
                                                            <ul class="dropdown-menu">
                                                                <li><a href="javascript:;" class="ajax-switch" onclick="changestatus('${list.status}','${list.id}')">${(list.status=='ACTIVE') ? string('禁用','启用')}</a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </#list>
                                        </#if>
                                    </#if>
                                    </tbody>
                                </table>
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
    function deletethis(id) {
        layer.open({
            content: '确定删除权限项？',
            yes: function(index, layero){
                $.ajax({
                    url:"${request.contextPath}/permission/delete",
                    data:{"id":id},
                    dataType:"json",
                    success:function(result){
                        if(result.code == 200){
                            location.href="${request.contextPath}/permission/query";
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

    function changestatus(status,id){
        if(status=="ACTIVE"){status="INACTIVE"}else{status="ACTIVE"}
        $.ajax({
            url:"${request.contextPath}/permission/edit",
            data:{"id":id,"status":status},
            dataType:"json",
            success:function(result){
                if(result.code == 200){
                    location.href="${request.contextPath}/permission/query";
                }else{
                    layer.open({
                        title: '警告'
                        ,content: result.msg
                    });
                }
            }});
    }
</script>
</body>
</html>
