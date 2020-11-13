
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>后台管理
    </title>
    <meta name="csrf-token" content="uWEUXven75m9GdSxbwIOVtUhbDj7Wy8dtzBLemba">
    <meta name="request-type" content="alert">
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
    <link href="${request.contextPath}/web/layui/formSelects-v4.css" rel="stylesheet">
</head>
<style>
</style>
<body class="iframe-body">
<div id="wrapper">
    <div style="width: 100%;">
        <div class="col-lg-12">
            <div style="width: 100%;">
                <div class="ibox-content">
                    <form class="layui-form form-horizontal m-t" id="ajaxform" action="" method="post">
                        <input type="hidden" name="id"  value="${admin.id}" class="layui-input">
                        <div class="layui-form-item">
                            <label class="layui-form-label">角色</label>
                            <div class="layui-input-block">
                                <select name="roles" lay-verify="required"  xm-select="id" xm-select-show-count="5">
                                    <#if (rolelist)??>
                                        <#if (rolelist?size > 0)>
                                            <#list rolelist as list>
                                                <option value="${list.id}" ${list.ischecked!}  >${list.name}</option>
                                            </#list>
                                        </#if>
                                    </#if>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="name" placeholder="请输入名称"   lay-verify="required" value="${admin.name!}" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">账号</label>
                            <div class="layui-input-block">
                                <input type="text" name="username" placeholder="请输入账号"  lay-verify="required" value="${admin.username!}" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">密码</label>
                            <div class="layui-input-block">
                                <input type="password" name="password" placeholder="请输入账号"  lay-verify="required" value="${admin.password!}" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">状态</label>
                            <div class="layui-input-block">
                                <input type="radio" name="status" value="ACTIVE" title="启用"  lay-filter="status" ${(admin.status=='ACTIVE') ? string('checked','')}>
                                <input type="radio" name="status" value="INACTIVE" title="禁用"  lay-filter="status" ${(admin.status=='INACTIVE') ? string('checked','')}>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>提交保存</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
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

<script>
    $(document).ready(function () {
        $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
    });
</script>
<script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
<script src="${request.contextPath}/web/js/myconfig.js"></script>
<script src="${request.contextPath}/web/layui/formSelects-v4.js" type="text/javascript" charset="utf-8"></script>
<script>
    var formSelects = layui.formSelects;
    layui.use(['form','element'], function(){
        var form = layui.form;
        form.render();
        form.on('submit(*)',function (data) {
            $.ajax({
                url:"${request.contextPath}/admin/edit",
                data:$("#ajaxform").serialize(),
                dataType:"json",
                success:function(result){
                    if(result.code == 200){
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index); //执行关闭 
                        parent.location.href="${request.contextPath}/admin/query";//回到父级页面
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        });
    });
</script>
</body>
</html>
