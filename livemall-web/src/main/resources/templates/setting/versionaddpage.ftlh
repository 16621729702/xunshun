
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="jOL58rIiCWdrDTjHhLT43gIpM9tcJIFmutLjZ69P">
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
            <div style="width: 100%;">
                <div class="col-lg-12">
                    <div style="width: 100%;">
                        <div class="ibox-content">
                            <form class="layui-form form-horizontal m-t" id="ajaxform" action="" method="post">
                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;">*</span>版本号</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="version" placeholder="请输入版本号" value="" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">版本下载地址</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                            <input type="text" name="url" id="versionurl" lay-verify="required" placeholder="" value="" class="form-control">
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" id="uploadfile">上传安装包</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="status" value="1" title="启用"  checked lay-filter="is_show">
                                        <input type="radio" name="status" value="0" title="禁用"  lay-filter="is_show">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <div class="layui-input-block">
                                        <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>提交保存
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            layui.use(['form', 'element'], function () {
                var form = layui.form;
                layui.use('layedit', function(){
                    var layedit = layui.layedit;
                    var index = layedit.build('pattern');
                    form.verify({
                        pattern:function () {
                            layedit.sync(index);
                        }
                    });
                });
                layui.use('upload', function () {
                    var upload = layui.upload;
                    var uploadInst = upload.render({
                        elem: '#uploadfile', //绑定元素
                        url: '${request.contextPath}/version/uploadfile', //上传接口
                        method: 'POST',
                        accept: 'file', //允许上传的文件类型
                        size: 204800, //设置文件最大可允许上传的大小，单位 KB
                        before: function(obj){
                        layer.msg('文件上传中...', {
                            icon: 16,
                            shade: 0.01,
                            time: 0
                        })
                        //预读本地文件示例，不支持ie8
                        obj.preview(function(index, file, result){
                            $('#demo1').attr('src', result); //图片链接（base64）
                        });
                        },
                        done: function (res) { //上传完毕回调
                            layer.close(layer.msg());
                            layer.msg("上传成功", {icon: 1});
                            $("#versionurl").val(res.videourl);
                        },
                        error: function () {
                            //请求异常回调
                            layer.close(layer.msg());
                            layer.msg("上传失败", {icon: 0});
                        }
                    });
                });
                form.on('submit(*)', function (data) {
                    $.ajax({
                        url: "${request.contextPath}/version/add",
                        data: $("#ajaxform").serialize(),
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 200) {
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href = "${request.contextPath}/version/query";//回到父级页面
                            } else {
                                layer.open({
                                    title: '警告'
                                    , content: result.msg
                                });
                            }
                        }
                    });
                });
            });
        </script>
    </body>
</html>
