
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
<input type="hidden" value="${goodcategory.parent_id}" id="pid">
<div id="wrapper">
    <div style="width: 100%;">
        <div class="col-lg-12">
            <div style="width: 100%;">
                <div class="ibox-content">
                    <form class="layui-form form-horizontal m-t" id="ajaxform" method="post">
                        <input type="hidden" name="id" value="${goodcategory.id}">
                        <div class="layui-form-item">
                            <label class="layui-form-label">父级</label>
                            <div class="layui-input-block">
                                <select name="parent_id" lay-verify="required" lay-filter="parent_id">
                                    <option value="0">顶级菜单</option>
                                    <#if goodcategory.parent_id!=0>
                                        <#if (topcategory)??>
                                        <#if (topcategory?size > 0)>
                                            <#list topcategory as list>
                                                <option value="${list.id}" ${list.isselected!} >|-------${list.name}</option>
                                            </#list>
                                        </#if>
                                    </#if>
                                    </#if>

                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">分类名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="name" placeholder="请输入分类名称" value="${goodcategory.name!}" class="layui-input">
                            </div>
                        </div>

                        <div class="layui-form-item child_form_item" >
                            <label class="layui-form-label">分类图片</label>
                            <div class="layui-input-block">
                                <div class="input-group ">
                                    <input type="text" name="pic" lay-verify="required" value="${goodcategory.pic!}" class="form-control" >
                                    <span class="input-group-btn">
                                                <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('pic');">选择图片</button>
                                            </span>
                                </div>
                                <div id="picimgshow" class="input-group " style="margin-top:.5em;width:auto;display:table;border-collapse: separate;">
                                    <img src="${goodcategory.pic!}" width="90px" height="90px">
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item child_form_item" >
                            <label class="layui-form-label">首页展示图标</label>
                            <div class="layui-input-block">
                                <div class="input-group ">
                                    <input type="text" name="home_pic" lay-verify="required" value="${goodcategory.home_pic!}"  class="form-control" >
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" type="button" class="btn btn-primary" onclick="showimage('home_pic');">选择图片</button>
                                    </span>
                                </div>
                                <div class="input-group " id="home_picimgshow" style="margin-top:.5em;    width:auto;display:table;border-collapse: separate;">
                                    <img src="${goodcategory.home_pic!}" width="90px" height="90px">
                                </div>
                                <div class="form-text m-b-none"> （建议尺寸90*90或1：1，仅支持JPG、PNG2种格式大小不超过2.0MB）
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">状态</label>
                            <div class="layui-input-block">
                                <input type="radio" name="status" value="0" title="显示"  ${(goodcategory.isshow== 0) ? string('checked','')}
                                       lay-filter="isshow">
                                <input type="radio" name="status" value="1" title="隐藏"  lay-filter="isshow" ${(goodcategory.isshow == 1) ? string('checked','')}>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">是否展示在商户入驻页</label>
                            <div class="layui-input-block">
                                <input type="radio" name="merchshow" value="0" title="显示"  ${(goodcategory.merchshow== 0) ? string('checked','')}
                                       lay-filter="isshow">
                                <input type="radio" name="merchshow" value="1" title="隐藏"  lay-filter="isshow" ${(goodcategory.merchshow == 1) ? string('checked','')}>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">排序</label>
                            <div class="layui-input-block">
                                <input type="number" name="order" placeholder="请输入排序" value="${goodcategory.sort}" class="layui-input">
                                <span class="help-block">数字越大,排序越靠前</span>
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
        var parent_id = $("#pid").val();
        if(parent_id!=0){
            $(".child_form_item").show();
        } else {
            $(".child_form_item").hide();
        }
    });
    layui.use(['form','element'], function(){
        var form = layui.form;
        form.on('select(parent_id)',function (data) {
            var parent_id = data.value;
            if(parent_id!=0){
                $(".child_form_item").show();
            } else {
                $(".child_form_item").hide();
            }
        })
    });
    function commit() {
        $.ajax({
            url:"${request.contextPath}/goodcategory/edit",
            data:$("#ajaxform").serialize(),
            dataType:"json",
            success:function(result){
                if(result.code == 200){
                    console.log("1000")
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index); //执行关闭 
                    parent.location.href="${request.contextPath}/goodcategory/query";//回到父级页面
                }else{
                    layer.open({
                        title: '警告'
                        ,content: result.msg
                    });
                }
            }});
    }
    function deleteImage(elm){
        $(elm).prev().attr("src", "/web/resource/images/nopic.jpg");
        $(elm).parent().prev().find("input").val("");
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
