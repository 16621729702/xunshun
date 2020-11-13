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
    </head>
    <body class="iframe-body">
        <div class="layui-container">

        <form class="layui-form" action="${request.contextPath}/image/query" method="post" style="margin-top: 20px" id="submitform">
            <input type="hidden" name="page" value="" id="formpage">
            <input type="hidden" name="groupid" value="${groupid!}" id="formgroupid">
            <input type="hidden" name="classname" value="${classname!}" id="classname">

            <div class="layui-inline">
                <label class="layui-form-label">开始时间</label>
                <div class="layui-input-inline">
                    <input type="text" name="startdate" placeholder="" value="" class="layui-input start_time">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">结束时间</label>
                <div class="layui-input-inline">
                    <input type="text" name="enddate" placeholder="" value="" class="layui-input end_time">
                </div>
            </div>
            <div class="layui-inline" style="padding-bottom: 5px">
                <div class="layui-input-inline">
                    <button  class="btn btn-success" type="submit">搜索</button>
                </div>
            </div>
            <div class="layui-inline" style="padding-bottom: 5px">
                <div class="layui-input-inline">
                    <button  class="btn" type="button" onclick="delimg();">删除</button>
                </div>
            </div>
            <div class="layui-inline" style="padding-bottom: 5px">
                <div class="layui-input-inline">
                    <button type="button" class="layui-btn" id="uploadfile">
                        <i class="layui-icon">&#xe67c;</i>上传图片
                    </button>
                </div>

            </div>
        </form>
            <div class="layui-row" style="margin-top: 30px">
                <div class="layui-row layui-col-space10">
                <div class="layui-col-xs3">
                    <button type="button" class="layui-btn layui-btn-primary layui-btn-sm" style="margin-top: 10px" onclick="addgroup();">添加分组</button>
                    <ul>
                        <li style="margin-top: 10px"><button type="button" class="layui-btn ${(checkall=='true')?string('layui-btn-normal','layui-btn-primary')} layui-btn-sm" onclick="changegroup();">全部</button></li>
                        <#if (lmImageGroupList)??>
                        <#if (lmImageGroupList?size > 0)>
                        <#list lmImageGroupList as list>
                        <li style="margin-top: 10px">
                            <button type="button" class="layui-btn ${(list.selected =='true')?string('layui-btn-normal','layui-btn-primary')} layui-btn-sm" onclick="changegroup(${list.id});" >${list.name}</button>
                            <input type="text" name="name" value="${list.name}" onblur="savechange(${list.id},this);"  class="" style="display: none">
                            <button type="button" class="layui-btn layui-btn-sm" onclick="edit(this);">编辑</button>
                            <button type="button" class="layui-btn layui-btn-sm" onclick="deletethis(${list.id});">删除</button>
                        </li>
                        </#list>
                        </#if>
                        </#if>
                    </ul>
                </div>
                <div class="layui-col-xs9">
                    <div class="layui-row layui-col-space30" id="imglist">
                        <#if (lmImageCoreList)??>
                        <#if (lmImageCoreList?size > 0)>
                        <#list lmImageCoreList as list>
                            <div class="layui-col-xs2" style="width: 130px;height: 130px;margin-left: 30px; margin-top: 20px;" >
                                <#if list.imgurl??>
                                    <img width="130px" height="130px"  src="${list.imgurl!}" />
                                    <input type="checkbox" name="imgbox" value="${list.imgurl!}" style="position:absolute;z-index: 10" data-id="${list.id!}"/>
                                <#else >
                                        <img width="130px" height="130px"  src="${request.contextPath}/image/IoReadImage/${list.filename!}" />
                                    <input type="checkbox" name="imgbox" value="${list.filename!}" style="position:absolute;z-index: 10" data-id="${list.id!}"/>
                                </#if>
                            </div>
                        </#list>
                        </#if>
                        </#if>
                    </div>
                </div>
                </div>
            </div>
            <div class="layui-row"  style="margin-top: 80px">
            <div class="datatable-footer" >
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
            <div class="modal-footer" style="text-align:center;margin-top: 30px">
                <button type="button" class="btn btn-primary" onclick="checkimg();">确定</button>
                <button type="button" class="btn btn-default" onclick="goout();">取消</button>
            </div>
        </div>
    </body>
    <script>
        layui.use(['form', 'element','laydate'], function () {
            var form = layui.form;
            var laydate = layui.laydate;
            //日期时间选择器
            laydate.render({
                elem: '.start_time'
                , type: 'datetime'
            });
            laydate.render({
                elem: '.end_time'
                , type: 'datetime'
            });
        });
        layui.use('upload', function () {
            var groupid = $("#formgroupid").val();
            var upload = layui.upload;
            var uploadInst = upload.render({
                elem: '#uploadfile', //绑定元素
                url: '${request.contextPath}/image/uploadfile?id='+groupid, //上传接口
                method: 'POST',
                accept: 'images', //允许上传的文件类型
                size: 204800, //设置文件最大可允许上传的大小，单位 KB
                done: function (res) { //上传完毕回调
                    layer.msg("上传成功", {icon: 1});

                    $("#submitform").submit();
                },
                error: function () {
                    //请求异常回调
                    layer.msg("上传失败", {icon: 0});
                }
            });
        });

        function addgroup() {
            $.ajax({
                url:"${request.contextPath}/image/addgroup",
                dataType:"json",
                success:function(result){
                    if(result.code == 200){
                        window.location.reload();
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        }
        function querypage(){
            var page = $(".gopagenum").val();
            $("#formpage").val(page);
            $("#submitform").submit();
        }
        function changegroup(groupid){
            $("#formgroupid").val(groupid);
            $("#submitform").submit();
        }
        function submitform(page){
            $("#formpage").val(page);
            $("#submitform").submit();
        }
        function checkimg(){
            var imgurls = "";
            $("input[name='imgbox']:checked").each(function () {
                imgurls+=$(this).val()+",";
            });
            var classname = $("#classname").val();
            parent.ChooseAdidValues(imgurls.substring(0,imgurls.length-1),classname);
            var index=parent.layer.getFrameIndex(window.name); //获取当前窗口的name
            parent.layer.close(index);
        }
        function deletethis(id) {
            $.ajax({
                url:"${request.contextPath}/image/deletegroup",
                dataType:"json",
                data:{"id":id},
                success:function(result){
                    if(result.code == 200){
                        window.location.reload();
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        }
        function delimg() {
            var ids = "";
            $("input[name='imgbox']:checked").each(function () {
                ids+=$(this).attr("data-id");
            });
            $.ajax({
                url:"${request.contextPath}/image/deleteimg",
                dataType:"json",
                data:{"ids":ids},
                success:function(result){
                    if(result.code == 200){
                        window.location.reload();
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        }


        function goout(){
            var index=parent.layer.getFrameIndex(window.name); //获取当前窗口的name
            parent.layer.close(index);
        }
        function edit(element) {
            $(element).prev().show();
            $(element).prev().prev().hide();
        }
        function savechange(id,obj) {
            var data = $(obj);
            var name = data.val();
            $.ajax({
                url:"${request.contextPath}/image/editgroup",
                dataType:"json",
                data:{"id":id,"name":name},
                success:function(result){
                    if(result.code == 200){
                        window.location.reload();
                    }else{
                        layer.open({
                            title: '警告'
                            ,content: result.msg
                        });
                    }
                }});
        }
    </script>
</html>
