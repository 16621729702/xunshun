
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理 |     商品分类管理
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
    </head>
    <body class="iframe-body">
        <div id="wrapper">
            <div style="width: 100%;">
                <div class="col-lg-12">
                    <div style="width: 100%;">
                        <div class="ibox-content">
                            <form class="layui-form form-horizontal m-t" id="ajaxform" method="post">
                                <input type="hidden" name="id" value="${lmLive.id!}">
                                <input type="hidden" name="merch_id" value="${lmLive.merch_id!}" >
                                <div class="layui-form-item">
                                    <label class="layui-form-label">直播分类</label>
                                    <div class="layui-input-block">
                                        <select name="categoryid" lay-verify="required" >
                                            <#if (categorylist)??>
                                                <#if (categorylist?size > 0)>
                                                    <#list categorylist as list>
                                                        <option value="${list.id}" ${(list.selected=='true')?string('selected','')}>${list.name}</option>
                                                    </#list>
                                                </#if>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">直播间名称</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="name" lay-verify="required" placeholder="请输入直播间名称"
                                               value="${lmLive.name!}" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">聊天室id</label>
                                    <div class="layui-input-block">
                                        <input type="text" name="livegroupid" lay-verify="required" placeholder="请输入聊天室id" value="${lmLive.livegroupid!}" class="layui-input">
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label">推流信息</label>
                                    <div class="layui-input-block">
                                        <input type="text" lay-verify="required"  value="${lmLive.pushurl!}" class="layui-input" readonly>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">播流信息</label>
                                    <div class="layui-input-block">
                                        <input type="text" lay-verify="required"  value="${lmLive.readurl!}" class="layui-input" readonly>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">短视频图片</label>
                                    <div class="layui-input-block">
                                        <div class="input-group ">
                                            <input type="text" name="img" lay-verify="required" value="${lmLive.img!}"
                                                   class="form-control" autocomplete="off">
                                            <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('img');">选择图片</button>
                                            </span>
                                        </div>
                                        <div id="imgimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                            <img src="${lmLive.img!}" height="90px" width="90px">
                                        </div>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">状态</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="status"  value="0" title="开始" ${(lmLive.status==0)?string('checked','')}>
                                        <input type="radio" name="status" value="1" title="关闭" ${(lmLive.status==1)?string('checked','')}>
                                        <input type="radio" name="status" value="2" title="过期" ${(lmLive.status==1)?string('checked','')}>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label class="layui-form-label">是否被推荐</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="isrecommend"  value="0" title="否" ${(lmLive.isrecommend==0)?string('checked','')}>
                                        <input type="radio" name="isrecommend" value="1" title="是" ${(lmLive.isrecommend==1)?string('checked','')}>
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <label class="layui-form-label">是否开启直播</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="isstart"  value="0" title="否" ${(lmLive.isrecommend==0)?string('checked','')}>
                                        <input type="radio" name="isstart" value="1" title="是" ${(lmLive.isrecommend==1)?string('checked','')}>
                                        <input type="radio" name="isstart"  value="2" title="预展" ${(lmLive.isrecommend==0)?string('checked','')}>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <div class="layui-input-block">
                                        <button class="btn btn-primary" type="button" id="ajaxsubmit" onclick="commit()">提交保存</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });
            layui.use(['form','element'], function(){
                var form = layui.form;
            });
            function commit() {
                $.ajax({
                    url:"${request.contextPath}/live/liveedit",
                    data:$("#ajaxform").serialize(),
                    dataType:"json",
                    success:function(result){
                        if(result.code == 200){
                            console.log("1000")
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index); //执行关闭 
                            parent.location.href="${request.contextPath}/live/query";//回到父级页面
                        }else{
                            layer.open({
                                title: '警告'
                                ,content: result.msg
                            });
                        }
                    }});
            }
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
        </script>
    </body>
</html>
