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
        <script src="${request.contextPath}/web${request.contextPath}/goods.js"></script>
        <script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
        <script src="${request.contextPath}/web/js/myconfig.js"></script>
        <script src="${request.contextPath}/web${request.contextPath}/goods.js"></script>

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
<#--                                    <li><a class="nav-link " data-toggle="tab" href="#tab-2">规格参数</a></li>-->
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
                                                <input type="hidden" name="id" value="${good.id}"/>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品名称</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="title" placeholder="请输入商品名称,最多40个字"
                                                               value="${good.title!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label">商品类型</label>
                                                    <div class="layui-input-block">
                                                        <input type="radio" name="type" value="0" ${(good.type==0)?string('checked','')} title="一口价" lay-filter = "type">
                                                        <input type="radio" name="type" value="1" ${(good.type==1)?string('checked','')} title="拍卖" lay-filter = "type">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item auction " ${(good.type==0)?string('style="display:none"','')} >
                                                    <label class="layui-form-label">拍卖起始价格</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="startprice"  value="${good.startprice!}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item auction " ${(good.type==0)?string('style="display:none"','')}>
                                                    <label class="layui-form-label">每次加价价格</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="stepprice" placeholder="" value="${good.stepprice!}" class="layui-input" >
                                                    </div>
                                                </div>
                                                <div class="layui-form-item auction " ${(good.type==0)?string('style="display:none"','')}>
                                                    <label class="layui-form-label">拍卖开始时间</label>
                                                    <div class="layui-input-block">
                                                        <#if good.auction_start_time?? >
                                                        <input type="text" class="layui-input start_time"  name="auction_start_time" value="${good.auction_start_time?string('yyyy-MM-dd HH:mm:ss')!}" readonly>
                                                        <#else >
                                                            <input type="text" class="layui-input start_time"  name="auction_start_time" readonly>
                                                        </#if>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item auction " ${(good.type==0)?string('style="display:none"','')} >
                                                    <label class="layui-form-label">拍卖结束时间</label>
                                                    <div class="layui-input-block">
                                                        <#if good.auction_end_time?? >
                                                        <input type="text" class="layui-input start_time" name="auction_end_time" value="${good.auction_end_time?string('yyyy-MM-dd HH:mm:ss')!}"  readonly>
                                                        <#else >
                                                            <input type="text" class="layui-input start_time" name="auction_end_time" readonly >
                                                        </#if>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show" style='display:none'>
                                                    <label class="layui-form-label">商品副标题</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="subtitle" placeholder="请输入商品副标题,最多20个字"
                                                               value="${good.subtitle!}"
                                                               class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label"><span style="color: red;"></span>商品关键词</label>
                                                    <div class="layui-input-block">
                                                        <input type="text" name="keyword" placeholder=""
                                                               value="${good.keyword!}"
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
                                                            <option value="" disabled="disabled" selected>请选择，最多选10个
                                                            </option>
                                                            <#if (categoryList)??>
                                                                <#if (categoryList?size > 0)>
                                                                    <#list categoryList as list>
                                                                        <option value="${list.id}" ${list.isuse} ${list.isselected!} >${list.name}</option>
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
                                                            <input type="text" name="thumb" value="${good.thumb!}"  lay-verify="required" readonly
                                                                   class="form-control">
                                                            <span class="input-group-btn">
                                                            <button class="btn btn-default" type="button" onclick="showimage('thumb');">选择图片</button>
                                                        </span>
                                                        </div>
                                                        <div class="input-group multi-img-details" id="thumbimgshow">
                                                            <#if good.thumb?contains("http")>
                                                                <img src="${good.thumb!}" width="90px" height="90px">
                                                                　　<#else >
                                                                <img src="${request.contextPath}/image/IoReadImage/${good.thumb!}" width="90px" height="90px">
                                                            </#if>
                                                        </div>
                                                        <div class="form-text m-b-none">
                                                            （建议尺寸800*800或1：1，仅支持JPG、PNG2种格式大小不超过2.0MB）
                                                        </div>
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label">商品轮播图</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <div class="input-group">-->
<#--                                                            <input type="text" class="form-control"  lay-verify="required" value="${good.thumbs!}" name="thumbs" readonly>-->
<#--                                                            <span class="input-group-btn">-->
<#--                                                            <button class="btn btn-default" type="button" onclick="showimage('thumbs');">选择图片</button>-->
<#--                                                        </span>-->
<#--                                                        </div>-->
<#--                                                        <div class="input-group multi-img-details" id="thumbsimgshow">-->
<#--                                                            <#if good.thumb?contains("http")>-->
<#--                                                                <img src="${good.thumbs!}" width="90px" height="90px">-->
<#--                                                                　　<#else >-->
<#--                                                                <img src="${request.contextPath}/image/IoReadImage/${good.thumbs!}" width="90px" height="90px">-->
<#--                                                            </#if>-->
<#--                                                        </div>-->
<#--                                                        <div class="form-text m-b-none">-->
<#--                                                            （建议尺寸800*800或1：1，仅支持JPG、PNG2种格式大小不超过2.0MB，最多上传5张）-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                            <#--    <div class="layui-form-item">
                                                    <label class="layui-form-label">商品视频</label>
                                                    <div class="layui-input-block">
                                                        <div class="input-group">
                                                            <input type="text" value="${good.video}" name="video"
                                                                   class="form-control">
                                                            <span class="input-group-btn">
                                                                <button class="btn btn-default"
                                                                        type="button">选择媒体文件</button>
                                                            </span>
                                                        </div>
                                                        <div class="form-text m-b-none">
                                                            商品详情首图视频
                                                        </div>
                                                    </div>
                                                </div>-->
<#--                                                <div class="layui-form-item buyway1_show">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品单位</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="text" name="unit" placeholder="如件，个等单位名称"-->
<#--                                                               value="${good.unit}"-->
<#--                                                               class="layui-input">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>市场价</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="marketprice" placeholder="0.00"
                                                               value="${good.marketprice}" class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品价格</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="productprice" placeholder="0.00"
                                                               value="${good.productprice}"
                                                               class="layui-input">
                                                    </div>
                                                </div>
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品库存</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="stock" placeholder="0"
                                                               value="${good.stock}"
                                                               class="layui-input">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item buyway1_show">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>虚拟销量</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="number" name="ficti_num" placeholder="0"-->
<#--                                                               value="${good.ficti_num}"-->
<#--                                                               class="layui-input">-->
<#--                                                        <div class="form-text m-b-none"> （前台展示销量=虚拟销量加真实销量）-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item buyway1_show">
                                                    <label class="layui-form-label"><span style="color: red;">*</span>快递运费</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="expressprice" placeholder="0.00"
                                                               value="${good.expressprice}"
                                                               class="layui-input">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label"><span style="color: red;">*</span>商品描述</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="text" name="description"-->
<#--                                                               value="${good.description}"-->
<#--                                                               class="layui-input">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label"><span style="color: red;"></span>商品描述</label>
                                                    <div class="layui-input-block">
                                                        <textarea placeholder="" class="layui-textarea" name="description" id="pattern" lay-verify="pattern" style="display:none">${good.description}</textarea>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品状态</label>
                                                    <div class="layui-input-block">
                                                        <input type="radio" name="state" value="1"
                                                               title="上架" ${(good.state==1)?string('checked','')}
                                                               lay-filter="state">
                                                        <input type="radio" name="state" value="0"
                                                               title="下架" ${(good.state==0)?string('checked','')}
                                                               lay-filter="state">
                                                    </div>
                                                </div>
<#--                                                <div class="layui-form-item">-->
<#--                                                    <label class="layui-form-label">包邮</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="radio" name="is_free_freight" value="1"-->
<#--                                                               title="是" ${(good.is_free_freight==1)?string('checked','')}-->
<#--                                                               lay-filter="is_free_freight">-->
<#--                                                        <input type="radio" name="is_free_freight" value="0"-->
<#--                                                               title="否" ${(good.is_free_freight==0)?string('checked','')}-->
<#--                                                               checked-->
<#--                                                               lay-filter="is_free_freight">-->
<#--                                                    </div>-->
<#--                                                </div>-->
                                                <div class="layui-form-item">
                                                    <label class="layui-form-label">商品排序</label>
                                                    <div class="layui-input-block">
                                                        <input type="number" name="sort" placeholder="0"
                                                               value="${good.sort}"
                                                               class="layui-input">
                                                        <div class="form-text m-b-none"> （排序数字越大，商品在购买页展示顺序越前）
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="layui-form-item"
                                                     style="position: relative;border: 0;left: 0">
                                                    <div class="layui-input-block">
                                                        <button class="btn btn-primary" type="button"
                                                                lay-filter="*" lay-submit>提交保存
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
<#--                                        <div role="tabpanel" id="tab-2" class="tab-pane">-->
<#--                                            <div class="panel-body">-->
<#--                                                <div class="layui-form-item buyway1_show">-->
<#--                                                    <label class="layui-form-label" ${(good.type==1) ? string('disabled','')}>启用商品规格</label>-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <input type="checkbox" name="hasoption" value="1" title="启用商品规格" ${(good.hasoption==1) ? string('checked','')} lay-filter="hasoption">-->
<#--                                                        <div class="form-text">-->
<#--                                                            启用商品规格后，商品的价格及库存以商品规格为准,库存设置为0则会到”已售罄“中，手机也不会显示, -1为不限制-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->
<#--                                                <div class="alert alert-info hasoption_box" style="${(good.hasoption==1) ? string('','display: none')}">-->
<#--                                                    1. 更改规格及规格项后请点击下方的【刷新规格项目表】来更新数据。<br>-->
<#--                                                    2.-->
<#--                                                    每一种规格代表不同型号，例如颜色为一种规格，尺寸为一种规格，如果设置多规格，手机用户必须每一种规格都选择一个规格项，才能添加购物车或购买。-->
<#--                                                </div>-->

<#--                                                <div id="specs" class="ui-sortable hasoption_box">-->
<#--                                                    <#if (itemlist)??>-->
<#--                                                        <#if (itemlist?size > 0)>-->
<#--                                                            <#list itemlist as list>-->
<#--                                                                <div class="spec_item spec_item_${list.specid}" style="" data-number="${list.specid}">-->
<#--                                                                    <div style="border:1px solid #e7eaec;padding:10px;margin-bottom: 10px;">-->
<#--                                                                        <input name="spec_id" type="hidden" class="form-control spec_id" value="${list.specid}">-->
<#--                                                                        <div class="form-group">-->
<#--                                                                            <div class="col-sm-12">-->
<#--                                                                                <div class="input-group">-->
<#--                                                                                    <input name="spec_title"  type="text" class="form-control  spec_title" id="spec_item_${list.specid}" value="${list.spectitle}"  placeholder="规格名称 (比如: 颜色)">-->
<#--                                                                                    <div class="input-group-btn">-->
<#--                                                                                        <a href="javascript:;"  specid="" class="btn btn-info add-specitem" onclick="addSpecItem(${list.specid})"><i class="fa fa-plus"></i>添加规格项</a>-->
<#--                                                                                        <a href="javascript:void(0);" class="btn btn-danger" onclick="delSpec(${list.specid})"><i class="fa fa-remove"></i></a>-->
<#--                                                                                    </div>-->
<#--                                                                                </div>-->
<#--                                                                            </div>-->
<#--                                                                        </div>-->
<#--                                                                        <div class="form-group">-->
<#--                                                                            <div class="col-md-12">-->
<#--                                                                                <#if (list.itemlist)??>-->
<#--                                                                                <#if (list.itemlist?size > 0)>-->
<#--                                                                                <div id="spec_item_${list.specid}" class="spec_item_items_box_${list.specid}">-->
<#--                                                                                    <#list list.itemlist as itemlist>-->
<#--                                                                                        <div class="spec_item_item spec_item_item_${list.specid} spec_item_item_${list.specid}_${itemlist.itemid}" data-number="${itemlist.itemid}" style="float:left;margin:5px;position: relative">-->
<#--                                                                                            <input name="spec_item_id_${list.specid}" type="hidden" class="form-control spec_item_id" value="${itemlist.itemid}">-->
<#--                                                                                            <div class="input-group">-->
<#--                                                                                                <input type="text" class="form-control spec_item_title valid" name="spec_item_title_${list.spectitle}" value="${itemlist.itemtitle}" aria-invalid="false">-->
<#--                                                                                                <span class="input-group-addon">-->
<#--                                                                                            <a href="javascript:;" title="删除"><i class="fa fa-times" onclick="delSpecItem(${list.specid},${itemlist.itemid})"></i></a>-->
<#--                                                                                            </span>-->
<#--                                                                                            </div>-->
<#--                                                                                        </div>-->
<#--                                                                                </#list>-->
<#--                                                                                </div>-->
<#--                                                                                </#if>-->
<#--                                                                                </#if>-->
<#--                                                                            </div>-->
<#--                                                                        </div>-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </#list>-->
<#--                                                        </#if>-->
<#--                                                    </#if>-->

<#--                                                </div>-->

<#--                                                <table class="table hasoption_box" style="${(good.hasoption==1) ? string('','display: none')}" >-->
<#--                                                    <tbody>-->
<#--                                                    <tr>-->
<#--                                                        <td style="border: none">-->
<#--                                                            <h4>-->
<#--                                                                <a href="javascript:;" class="btn btn-primary" id="add-spec" onclick="addSpec()" style="margin-top:10px;margin-bottom:10px;" title="添加规格"><i class="fa fa-plus"></i> 添加规格</a>-->
<#--                                                                <a href="javascript:;" onclick="refreshOptions();" title="刷新规格项目表" class="btn btn-primary"><i class="fa fa-refresh"></i> 刷新规格项目表</a>-->
<#--                                                            </h4>-->
<#--                                                        </td>-->
<#--                                                    </tr>-->
<#--                                                    <tr style="display: none;" id="optiontip">-->
<#--                                                        <td>-->
<#--                                                            <div class="alert alert-danger">警告：规格数据有变动，请重新点击上方 [刷新规格项目表]-->
<#--                                                                按钮！-->
<#--                                                            </div>-->
<#--                                                        </td>-->
<#--                                                    </tr>-->
<#--                                                    </tbody>-->
<#--                                                </table>-->
<#--                                                <div id="options" style="padding:0;" class="hasoption_box">-->
<#--                                                    <table class="table table-bordered table-condensed">-->
<#--                                                        <thead>-->
<#--                                                        <tr class="active">-->
<#--                                                            <#if (itemlist)??>-->
<#--                                                            <#if (itemlist?size > 0)>-->
<#--                                                            <#list itemlist as list>-->
<#--                                                            <th>${list.spectitle}</th>-->
<#--                                                            </#list>-->
<#--                                                            </#if>-->
<#--                                                            </#if>-->
<#--                                                            <th>-->
<#--                                                                <div class="">-->
<#--                                                                    <div style="padding-bottom:10px;text-align:center;">库存</div>-->
<#--                                                                    <div class="input-group">-->
<#--                                                                        <input type="text" class="form-control  input-sm option_stock_all" value=""><span-->
<#--                                                                                class="input-group-addon">-->
<#--                                                                            <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置"-->
<#--                                                                               onclick="setCol('option_stock');">-->
<#--                                                                            </a>-->
<#--                                                                        </span>-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </th>-->
<#--                                                            <th>-->
<#--                                                                <div class="">-->
<#--                                                                    <div style="padding-bottom:10px;text-align:center;">销售价</div>-->
<#--                                                                    <div class="input-group">-->
<#--                                                                        <input type="text" class="form-control  input-sm option_marketprice_all" value=""><span-->
<#--                                                                                class="input-group-addon">-->
<#--                                                                                <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置"-->
<#--                                                                                   onclick="setCol('option_marketprice');">-->
<#--                                                                                </a>-->
<#--                                                                            </span>-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </th>-->
<#--                                                            <th>-->
<#--                                                                <div class="">-->
<#--                                                                    <div style="padding-bottom:10px;text-align:center;">市场价</div>-->
<#--                                                                    <div class="input-group">-->
<#--                                                                        <input type="text" class="form-control  input-sm option_productprice_all" value=""><span-->
<#--                                                                                class="input-group-addon">-->
<#--                                                                            <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置"-->
<#--                                                                               onclick="setCol('option_productprice');">-->
<#--                                                                            </a>-->
<#--                                                                        </span>-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </th>-->
<#--                                                            <th>-->
<#--                                                                <div class="">-->
<#--                                                                    <div style="padding-bottom:10px;text-align:center;">原价</div>-->
<#--                                                                    <div class="input-group">-->
<#--                                                                        <input type="text" class="form-control  input-sm option_costprice_all" value=""><span-->
<#--                                                                                class="input-group-addon">-->
<#--                                                                            <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置"-->
<#--                                                                               onclick="setCol('option_costprice');">-->
<#--                                                                            </a>-->
<#--                                                                        </span>-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </th>-->
<#--                                                        </tr>-->
<#--                                                        </thead>-->
<#--                                                        <tbody>-->
<#--                                                        <#if (goodSpecOptionList)??>-->
<#--                                                        <#if (goodSpecOptionList?size > 0)>-->
<#--                                                        <#list goodSpecOptionList as list>-->
<#--                                                        <tr><input type="hidden" value="${list.id}" name="option_id">-->
<#--                                                            <#if (list.namelist)??>-->
<#--                                                            <#if (list.namelist?size > 0)>-->
<#--                                                            <#list list.namelist as tlist>-->
<#--                                                                <td class="full" rowspan="1" >${tlist}</td>-->
<#--                                                            </#list>-->
<#--                                                            </#if>-->
<#--                                                            </#if>-->
<#--                                                            <td><input type="text" class="form-control option_stock" value="${list.stock}" name="option_stock"></td>-->
<#--                                                            <td><input type="text" class="form-control option_marketprice" value="${list.marketprice}" name="option_marketprice"></td>-->
<#--                                                            <td><input type="text" class="form-control option_productprice" value="${list.productprice}" name="option_productprice"></td>-->
<#--                                                            <td><input type="text" class="form-control option_costprice" value="${list.costprice}" name="option_costprice"></td>-->
<#--                                                        </tr>-->
<#--                                                        </#list>-->
<#--                                                        </#if>-->
<#--                                                        </#if>-->
<#--                                                        </tbody>-->
<#--                                                    </table>-->

<#--                                                </div>-->


<#--                                                <div class="spec_item_sample" style="display: none">-->
<#--                                                    <div class="spec_item{demo} spec_item_{number}" style="" data-number="{number}">-->
<#--                                                        <div style="border:1px solid #e7eaec;padding:10px;margin-bottom: 10px;">-->
<#--                                                            <input name="spec_id" type="hidden" class="form-control spec_id" value="">-->
<#--                                                            <div class="form-group">-->
<#--                                                                <div class="col-sm-12">-->
<#--                                                                    <div class="input-group">-->
<#--                                                                        <input name="spec_title" id="spec_item_{number}" type="text" class="form-control  spec_title" value="" placeholder="规格名称 (比如: 颜色)">-->
<#--                                                                        <div class="input-group-btn">-->
<#--                                                                            <a href="javascript:;" class="btn btn-info add-specitem" onclick="addSpecItem({number})"><i class="fa fa-plus"></i>添加规格项</a>-->
<#--                                                                            <a href="javascript:void(0);" class="btn btn-danger" onclick="delSpec({number})"><i class="fa fa-remove"></i></a>-->
<#--                                                                        </div>-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                            <div class="form-group">-->
<#--                                                                <div class="col-md-12">-->
<#--                                                                    <div id="spec_item_{number}" class="spec_item_items_box_{number}">-->
<#--                                                                    </div>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->


<#--&lt;#&ndash;                                                <div class="spec_item_item_sample" style="display: none">&ndash;&gt;-->
<#--&lt;#&ndash;                                                    <div class="spec_item_item spec_item_item_{spec_number} spec_item_item_{spec_number}_{number}" data-number="{number}" style="float:left;margin:5px;position: relative">&ndash;&gt;-->
<#--&lt;#&ndash;                                                        <input name="spec_item_id_{spec_number}" type="hidden" class="form-control spec_item_id" value="">&ndash;&gt;-->
<#--&lt;#&ndash;                                                        <div class="input-group">&ndash;&gt;-->
<#--&lt;#&ndash;                                                            <input type="text" class="form-control spec_item_title valid" name="spec_item_title_{specname}" value="" aria-invalid="false">&ndash;&gt;-->
<#--&lt;#&ndash;                                                        </div>&ndash;&gt;-->
<#--&lt;#&ndash;                                                    </div>&ndash;&gt;-->
<#--&lt;#&ndash;                                                </div>&ndash;&gt;-->
<#--                                                <div class="spec_item_item_sample" style="display: none">-->
<#--                                                    <div class="spec_item_item spec_item_item_{spec_number} spec_item_item_{spec_number}_{number}" data-number="{number}" style="float:left;margin:5px;position: relative">-->
<#--                                                        <input name="spec_item_id_{spec_number}" type="hidden" class="form-control spec_item_id" value="">-->
<#--                                                        <div class="input-group">-->
<#--                                                            <input type="text" class="form-control spec_item_title valid" name="spec_item_title_{specname}" value="" aria-invalid="false">-->
<#--                                                            <span class="input-group-addon">-->
<#--                                                                    <a href="javascript:;" title="删除"><i class="fa fa-times" onclick="delSpecItem({spec_number},{number})"></i></a>-->
<#--                                                                </span>-->
<#--                                                        </div>-->
<#--                                                    </div>-->
<#--                                                </div>-->

<#--                                                <table class="table table-bordered table-condensed spec_option_sample"-->
<#--                                                       style="display:none">-->
<#--                                                    <thead>-->
<#--                                                    <tr class="active">-->
<#--                                                        <th>spec_titles</th>-->
<#--                                                        <th>-->
<#--                                                            <div class="">-->
<#--                                                                <div style="padding-bottom:10px;text-align:center;">库存-->
<#--                                                                </div>-->
<#--                                                                <div class="input-group">-->
<#--                                                                    <input type="text" class="form-control  input-sm option_stock_all" value="">-->
<#--                                                                    <span class="input-group-addon">-->
<#--                                                                        <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置" onclick="setCol('option_stock');"></a>-->
<#--                                                                    </span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                        </th>-->
<#--                                                        <th>-->
<#--                                                            <div class="">-->
<#--                                                                <div style="padding-bottom:10px;text-align:center;">-->
<#--                                                                    销售价-->
<#--                                                                </div>-->
<#--                                                                <div class="input-group">-->
<#--                                                                    <input type="text" class="form-control  input-sm option_marketprice_all" value="">-->
<#--                                                                    <span class="input-group-addon">-->
<#--                                                                        <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置" onclick="setCol('option_marketprice');"></a>-->
<#--                                                                    </span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                        </th>-->
<#--                                                        <th>-->
<#--                                                            <div class="">-->
<#--                                                                <div style="padding-bottom:10px;text-align:center;">-->
<#--                                                                    市场价-->
<#--                                                                </div>-->
<#--                                                                <div class="input-group">-->
<#--                                                                    <input type="text" class="form-control  input-sm option_productprice_all" value="">-->
<#--                                                                    <span class="input-group-addon"><a href="javascript:;" class="fa fa-angle-double-down" title="批量设置" onclick="setCol('option_productprice');"></a>-->
<#--                                                                    </span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                        </th>-->
<#--                                                        <th>-->
<#--                                                            <div class="">-->
<#--                                                                <div style="padding-bottom:10px;text-align:center;">原价-->
<#--                                                                </div>-->
<#--                                                                <div class="input-group">-->
<#--                                                                    <input type="text" class="form-control  input-sm option_costprice_all" value=""><span class="input-group-addon">-->
<#--                                                                        <a href="javascript:;" class="fa fa-angle-double-down" title="批量设置" onclick="setCol('option_costprice');"></a>-->
<#--                                                                    </span>-->
<#--                                                                </div>-->
<#--                                                            </div>-->
<#--                                                        </th>-->
<#--                                                    </tr>-->
<#--                                                    </thead>-->
<#--                                                    <tbody>-->
<#--                                                    <tr><th>spec_options</th></tr>-->
<#--                                                    </tbody>-->
<#--                                                </table>-->
<#--                                                <div class="layui-form-item"-->
<#--                                                     style="position: relative;border: 0;left: 0">-->
<#--                                                    <div class="layui-input-block">-->
<#--                                                        <button class="btn btn-primary " type="button"-->
<#--                                                                onclick="submitform()">提交保存-->
<#--                                                        </button>-->
<#--                                                    </div>-->
<#--                                                </div>-->
<#--                                            </div>-->
<#--                                        </div>-->
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
                        url: "${request.contextPath}/good/edit",
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
                });

            });

            function hiddenitem(){
                $(".auction").addClass("hidden");
            }
            function showitem() {
                $(".auction").removeClass("hidden");
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
