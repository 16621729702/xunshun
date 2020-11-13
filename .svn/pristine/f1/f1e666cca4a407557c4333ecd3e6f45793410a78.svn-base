<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 | 商品分类管理
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
        <link href="${request.contextPath}/web/css/fix.css?v=121" rel="stylesheet">
        <script type="text/javascript">
            if (navigator.appName == 'Microsoft Internet Explorer') {
                if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
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
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
                $(".child_form_item").hide();
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
                                    <label class="layui-form-label">短视频分类</label>
                                    <div class="layui-input-block">
                                        <select name="category" lay-verify="required" >
                                            <#if (category)??>
                                                <#if (category?size > 0)>
                                                    <#list category as list>
                                                        <option value="${list.id}">${list.name}</option>
                                                    </#list>
                                                </#if>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">短视频名称</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="name" lay-verify="required" placeholder=""
                                               value="" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">短视频图片</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                            <input type="text" name="img" lay-verify="required" value=""
                                                   class="form-control" autocomplete="off">
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('img');">选择图片</button>
                                            </span>
                                        </div>
                                        <div id="imgimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                        </div>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">短视频视频</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                        <input type="text" name="video" id="videourl" lay-verify="required" placeholder="" value="" class="form-control">
                                        <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" id="uploadfile">选择视频</button>
                                            </span>
                                        </div>
                                    </div>

                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">短视频标签</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="tag" lay-verify="required" placeholder=""
                                               value="" class="layui-input">
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label"><span style="color: red;"></span>短视频内容</label>
                                    <div class="layui-input-block">
                                        <textarea placeholder="" class="layui-textarea" name="content" id="pattern" lay-verify="pattern" style="display:none"></textarea>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">关联商品</label>
                                    <div class="layui-input-block">
                                        <a class="btn btn-primary load_iframe"
                                           data-url="${request.contextPath}/good/selectgood" data-width="1000px"
                                           data-height="700px" href="javascript:;"><i
                                                    class="fa fa-plus"></i>&nbsp;添加商品</a>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label"></label>
                                    <div class="layui-input-block">
                                        <ul class="goods-list">
                                        </ul>
                                        <input type="hidden" name="good_id" class="goodsIds" value=""/>
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
                        url: '${request.contextPath}/video/uploadfile', //上传接口
                        method: 'POST',
                        accept: 'video', //允许上传的文件类型
                        size: 204800, //设置文件最大可允许上传的大小，单位 KB
                        done: function (res) { //上传完毕回调
                            layer.msg("上传成功", {icon: 1});
                            $("#videourl").val(res.videourl);
                        },
                        error: function () {
                            //请求异常回调
                            layer.msg("上传失败", {icon: 0});
                        }
                    });
                });
                form.on('select(parent_id)', function (data) {
                    var parent_id = data.value;
                    if (parent_id != 0) {
                        $(".child_form_item").show();
                    } else {
                        $(".child_form_item").hide();
                    }
                })
                form.on('submit(*)', function (data) {
                    $.ajax({
                        url: "${request.contextPath}/video/insert",
                        data: $("#ajaxform").serialize(),
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 200) {
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href = "${request.contextPath}/video/query";//回到父级页面
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
            function showimage(classname) {
                layui.use('layer', function(){
                    var layer = layui.layer;
                    layer.open({
                        type: 2,
                        title:'选择图片',
                        maxmin: true, //开启最大化最小化按钮
                        shadeClose: true,
                        shade: false,
                        area: ['1000px', '700px'],
                        content: '${request.contextPath}/image/query?classname='+classname, //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
                    });
                });
            }



            function ChooseAdidValues(v,classname) {

                if (v != "") {
                    var imgurl = v.split(",");
                    $("input[name='"+classname+"']").val(imgurl[0]);
                    if(imgurl[0].indexOf("http") == -1){
                        var html ="<img src='${request.contextPath}/image/IoReadImage/"+imgurl[0]+"' width='90px' height='90px'>";
                    }else{
                        var html ="<img src='"+imgurl[0]+"' width='90px' height='90px'>";
                    }
                    $("#"+classname+"imgshow").html(html);
                }
            }

            function addGoods(ids, titles) {
                if (ids) {
                    var html = "";
                    var goodsIdsArr = [];
                    var goodsIds = $(".goodsIds").val();
                    if (goodsIds) {
                        goodsIdsArr = goodsIds.split(',');
                    }

                    var goodsIdsArrRet = goodsIdsArr.concat(ids);
                    $(".goodsIds").val(goodsIdsArrRet);

                    for (var i=0;i<titles.length;i++ ) {
                        var key = parseInt(goodsIdsArr.length) + parseInt(i);
                        html += '<li>' + titles[i] + ' <input type="button" class="btn btn-danger delGoods" data-key="' + key + '" value="删除"></li>';
                    }
                    $(".goods-list").append(html);
                }
            }

            $(".goods-list").on('click', ".delGoods", function () {
                var key = $(this).data('key');
                $(this).parent().remove();
                var goodsIds = $(".goodsIds").val();
                var goodsIdsArr = goodsIds.split(',');
                delete goodsIdsArr[key];

                $(".goodsIds").val(goodsIdsArr.join(','));
            });

        </script>
    </body>
</html>
