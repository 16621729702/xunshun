
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 |     修改分类
        </title>
        <meta name="csrf-token" content="7DEzduUKkgvzUC9pe8eZ4XtGREXpcw2N64T6jrqy">
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
        <script src="${request.contextPath}/web/layui/formSelects-v4.js" type="text/javascript" charset="utf-8"></script>

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
            <div style="width: 100%;">
                <div class="col-lg-12">
                    <div style="width: 100%;height: 500px">
                        <div class="ibox-content">
                            <form class="layui-form form-horizontal m-t" id="ajaxform" action="" method="post">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">商品类目</label>
                                    <div class="layui-input-block" id="categoryid">
                                        <select name="category_id" lay-verify="required" xm-select="id"
                                                xm-select-show-count="10">
                                            <option value="" disabled="disabled" selected>请选择，最多选10个
                                            </option>
                                            <#if (categoryList)??>
                                            <#if (categoryList?size > 0)>
                                            <#list categoryList as list>
                                            <option value="${list.id}" ${list.isuse}>${list.name}</option>
                                            </#list>
                                            </#if>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" value="${ids!}" name="ids">
                                <input type="hidden" value="edit" name="operate">
                                <div class="layui-form-item">
                                    <div class="layui-input-block">
                                        <button class="btn btn-primary" type="button" id="ajaxsubmit" onclick="submitform();">提交保存</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script>
        function submitform() {
            $.ajax({
                url: "${request.contextPath}/good/batchoption",
                data: $("#ajaxform").serialize(),
                dataType: "json",
                success: function (result) {
                    if (result.code == 200) {
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index); //执行关闭 
                        parent.location.href = "${request.contextPath}/good/query";//回到父级页面
                    } else {
                        layer.open({
                            title: '警告'
                            , content: result.msg
                        });
                    }
                }
            });
        }

    </script>
</html>
