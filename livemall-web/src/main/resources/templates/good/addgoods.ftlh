<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>后台管理
        </title>
        <meta name="csrf-token" content="YmiVnOmNgVzdICfo7eRlRKlU1eIBMp2OAIeubwZq">
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
        <link href="${request.contextPath}/web/layui/formSelects-v4.css" rel="stylesheet">
        <style>
            .alert-info {
                color: #00c1de;
                background-color: #dff6fa;
                border-color: #c1e7ee;
            }

            .alert {
                border-radius：2排序；padding: 15px;
                margin-bottom: 20px;
                border: none;
            }

            .tabs-container .form-group {
                overflow: hidden;
            }
        </style>
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
        <script src="${request.contextPath}/web/layui/formSelects-v4.js" type="text/javascript" charset="utf-8"></script>

        <script>
            $(document).ready(function () {
                layui.use(["layer", "form"], function () {
                    var form = layui.form;
                    var layer = layui.layer;
                    var form = layui.form, $ = layui.jquery;
                    form.render();
                });
                $("#left").load("${request.contextPath}/left");
                $(".footer").load("${request.contextPath}/footer");
                $('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
            });
        </script>

        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
            <div id="page-wrapper" class="gray-bg">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div style="width: 100%;">
                        <div class="col-lg-12">
                            <div class="tabs-container">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li><a class="nav-link active" data-toggle="tab" href="#tab-1">基本信息</a></li>
                                </ul>
                                <form class="layui-form form-horizontal m-t" id="ajaxform"
                                      action="" method="post" style="margin-top: 0px">
                                    <div class="tab-content">
                                        <div role="tabpanel" id="tab-1" class="tab-pane active">
                                            <div class="panel-body">
                                                <style>
                                                    .layui-form-select dl {
                                                        z-index: 9990;
                                                    }

                                                    .lay-ext-mulitsel .layui-input.multiple a {
                                                        height: auto;
                                                    }
                                                </style>
                                                <input type="hidden" name="buyway" value="1"/>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品名称</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="title"  lay-verify="required" placeholder="请输入商品名称,最多40个字"
                                                               value="" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label">商品类型</label>
                                                    <div class="layui-input-block">
                                                        <input type="radio" name="type" value="0" title="一口价" lay-filter = "type">
                                                        <input type="radio" name="type" value="1" title="拍卖" lay-filter = "type">
                                                    </div>
<#--                                                    <div class="form-text m-b-none">-->
<#--                                                            拍卖商品不做成多规格-->
<#--                                                    </div>-->
                                                </div>
                                                <div class="layui-form-item auction" style='display:none'>
                                                    <label class="layui-form-label">拍卖起始价格</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="startprice" placeholder="" value="" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item auction" style='display:none'>
                                                    <label class="layui-form-label">每次加价价格</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="stepprice" placeholder="" value="" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item  auction" style='display:none'>
                                                    <label class="layui-form-label">拍卖开始时间</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" class="layui-input  start_time" value="" name="auction_start_time">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item auction" style='display:none'>
                                                    <label class="layui-form-label">拍卖结束时间</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="auction_end_time" placeholder="" value="" class="layui-input end_time">
                                                    </div>
                                                </div>

                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label">商品副标题</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="subtitle"  lay-verify="required" placeholder="请输入商品副标题,最多20个字"
                                                               value=""
                                                               class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label"><span style="color: red;"></span>商品关键词</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="keyword"  lay-verify="required" placeholder="" value=""
                                                               class="layui-input">
                                                        <div class="form-text m-b-none">
                                                            （用户在搜索关键词时将会显示该商品，每个关键词中间请用英文状态下的逗号,隔开）
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品类目</label>
                                                    <div class="layui-input-block" id="category_id">
                                                        <select name="category_id" lay-verify="required" xm-select="id"
                                                                xm-select-show-count="10">
                                                            <option value="" disabled="disabled" selected>请选择，最多选10个</option>
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
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品主图</label>
                                                    <div class="layui-input-block">
                                                        <div class="input-group ">
                                                            <input type="text" name="thumb" value=""  lay-verify="required"
                                                                   class="form-control">
                                                            <span class="input-group-btn">
                                                            <button class="btn btn-default" type="button" onclick="showimage('thumb');">选择图片</button>
                                                        </span>
                                                        </div>
                                                        <div class="input-group multi-img-details" id="thumbimgshow"></div>
                                                        <div class="form-text m-b-none">
                                                            （建议尺寸800*800或1：1，仅支持JPG、PNG2种格式大小不超过2.0MB）
                                                        </div>
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label">商品轮播图</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <div class="input-group">-->
<#--                                                            <input type="text" class="form-control"  lay-verify="required" value="" name="thumbs" >-->
<#--                                                            <span class="input-group-btn">-->
<#--                                                            <button class="btn btn-default" type="button" onclick="showimage('thumbs');">选择图片</button>-->
<#--                                                        </span>-->
<#--                                                        </div>-->
<#--                                                        <div class="input-group multi-img-details" id="thumbsimgshow"></div>-->
<#--                                                        <div class="form-text m-b-none">-->
<#--                                                            （建议尺寸800*800或1：1，仅支持JPG、PNG2种格式大小不超过2.0MB，最多上传5张）-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label">商品视频</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <div class="input-group">-->
<#--                                                            <input type="text" value="" name="video"  lay-verify="required"-->
<#--                                                                   class="form-control">-->
<#--                                                            <span class="input-group-btn">-->
<#--                                                                <button class="btn btn-default"-->
<#--                                                                        type="button">选择媒体文件</button>-->
<#--                                                            </span>-->
<#--                                                        </div>-->
<#--                                                        <div class="form-text m-b-none">-->
<#--                                                            商品详情首图视频-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
<#--                                                <div class="layui-form-item buyway1_show">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品单位</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="text" name="unit" lay-verify="required"  placeholder="如件，个等单位名称" value=""-->
<#--                                                               class="layui-input">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>市场价</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="marketprice"  lay-verify="required" placeholder="0.00"
                                                               value="" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品价格</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="productprice"   lay-verify="required" placeholder="0.00"
                                                               value=""
                                                               class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品库存</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="stock"  lay-verify="required" placeholder="0" value=""
                                                               class="layui-input">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item buyway1_show">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>虚拟销量</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="number" name="ficti_num"  lay-verify="required" placeholder="0" value=""-->
<#--                                                               class="layui-input">-->
<#--                                                        <div class="form-text m-b-none"> （前台展示销量=虚拟销量加真实销量）-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>快递运费</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="expressprice"  lay-verify="required" placeholder="0.00"
                                                               value=""
                                                               class="layui-input">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品描述</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="text" name="description"  lay-verify="required" value=""-->
<#--                                                               class="layui-input">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>商品描述</label>
                                                    <div class="layui-input-block">
                                                        <textarea placeholder="" class="layui-textarea" name="description" id="pattern" lay-verify="pattern" style="display:none"></textarea>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品状态</label>
                                                    <div class="layui-input-block">
                                                        <input type="radio" name="state" value="1" title="上架" checked
                                                               lay-filter="state">
                                                        <input type="radio" name="state" value="0" title="下架"
                                                               lay-filter="state">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label">包邮</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="radio" name="is_free_freight" value="1" title="是"-->
<#--                                                               lay-filter="is_free_freight">-->
<#--                                                        <input type="radio" name="is_free_freight" value="0" title="否"-->
<#--                                                               checked-->
<#--                                                               lay-filter="is_free_freight">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品排序</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="sort" placeholder="" value=""
                                                               class="layui-input">
                                                        <div class="form-text m-b-none"> （排序数字越大，商品在购买页展示顺序越前）
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item"
                                                     style="position: relative;border: 0;left: 0">
                                                    <div class="layui-input-block">
                                                        <button class="btn btn-primary" type="button" lay-filter="*" lay-submit>提交保存
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer"></div>
            </div>
        </div>
        <script type="text/javascript">
            layui.use(['layer', 'form', 'jquery','laydate','element','layedit'], function () {
                $ = layui.jquery;
                var form = layui.form
                var laydate = layui.laydate, element = layui.element;
                //日期时间选择器
                laydate.render({
                    elem: '.start_time'
                    , type: 'datetime'
                });
                laydate.render({
                    elem: '.end_time'
                    , type: 'datetime'
                });

                form.on('radio(type)',function(obj){
                    var data = $(obj.elem);
                   if(data.val()==1){
                       $(".auction").show();
                   }else{
                       $(".auction").hide();
                   }
                })
                var layedit = layui.layedit;
                var index = layedit.build('pattern');
                form.verify({
                    pattern:function () {
                        layedit.sync(index);
                    }
                });
                form.on('submit(*)',function (data) {
                    $.ajax({
                        url:"${request.contextPath}/good/insert",
                        data:$("#ajaxform").serialize(),
                        dataType:"json",
                        success:function(result){
                            if(result.code == 200){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //执行关闭 
                                parent.location.href="${request.contextPath}/good/query";//回到父级页面
                            }else{
                                layer.open({
                                    title: '警告'
                                    ,content: result.msg
                                });
                            }
                        }});
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
                    if(classname=="thumbs"){
                        $("input[name='"+classname+"']").val(v);
                        var imgurl = v.split(",");
                        var html = "";
                        for(var i=0;i<imgurl.length;i++){
                            if(imgurl[i].indexOf("http") !== -1){
                                html +="<img src='${request.contextPath}/image/IoReadImage/"+imgurl[i]+"' width='90px' height='90px'>";
                            }else{
                                html +="<img src='"+imgurl[i]+"' width='90px' height='90px'>";
                            }
                        }
                        $("#"+classname+"imgshow").html(html);
                    }else{
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
            }
        </script>
    </body>
</html>
