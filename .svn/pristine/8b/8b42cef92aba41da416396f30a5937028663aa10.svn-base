String.prototype.replaceAll = function (search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

function switcBuyway() {
    var buyway = $('input[name="buyway"]').val();
    if (buyway == 1) {
        $(".buyway1_show").show();
        $(".buyway2_show").hide();
        $(".buyway1_switch").removeClass('btn-outline');
        $(".buyway2_switch").addClass('btn-outline');
    } else if (buyway == 2) {
        $(".buyway2_show").show();
        $(".buyway1_show").hide();
        $(".buyway1_switch").addClass('btn-outline');
        $(".buyway2_switch").removeClass('btn-outline');
    }
}

switcBuyway();

$(".buyway_switch").click(function () {
    var buyway = $(this).data('buyway');
    $('input[name="buyway"]').val(buyway);
    switcBuyway();
});
/**
 * 规格
 */
var maxSpecItems = 0;//最大的规格项数
//规格切换
layui.use('form', function () {
    var form = layui.form;
    var layer = layui.layer;
    form.on('checkbox(hasoption)', function (data) {
        var isCheck = data.elem.checked;
        if (isCheck) {
            $(".hasoption_box").show();
        } else {
            $(".hasoption_box").hide();
        }
    });
});
var hasoption = $('input[name="hasoption"]:checked').val();
if (hasoption) {
    $(".hasoption_box").show();
} else {
    $(".hasoption_box").hide();
}

//添加规格
function addSpec() {
    var spec_item_sample = $(".spec_item_sample").html();
    var spec_item_id = $(".spec_item").length + 1;//规格编号id
    //替换编号
    spec_item_sample = spec_item_sample.replaceAll(/{number}/, spec_item_id);
    spec_item_sample = spec_item_sample.replaceAll(/{demo}/, '');
    $("#specs").append(spec_item_sample);
}

//删除规格
function delSpec(number) {
    layer.confirm('确定删除该规格吗', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var id = $("input[name=spec_"+number+"_id").val();
        $(".spec_item_" + number).remove();

        if(number){
            var index = layer.load(0);
            $.ajax({
                url: '/admin/good/delSpec',
                type: 'post',
                data: {id:number},
                dataType: "json",
                success: function (result) {
                    layer.closeAll();
                    if (result.code == 200) {
                    } else {
                        layer.msg(result.message, {time: 2000, icon: 5});
                    }

                }
            });
        } else {
            layer.closeAll();
        }
    });
}

//添加规格项
function addSpecItem(spec_id) {
    var spec_item_item_sample = $(".spec_item_item_sample").html();

    var spec_item_item_id = $(".spec_item_item_" + spec_id).length + 1;//规格项目编号id
    var spec_item_item_title = $("#spec_item_"+spec_id).val();
    //替换编号
    spec_item_item_sample = spec_item_item_sample.replaceAll(/{spec_number}/, spec_id);
    spec_item_item_sample = spec_item_item_sample.replaceAll(/{number}/, spec_item_item_id);
    spec_item_item_sample = spec_item_item_sample.replaceAll(/{specname}/,spec_item_item_title );
    var arr = $(".spec_item_items_box_" + spec_id);
    arr.append(spec_item_item_sample);
}

//删除规格项
function delSpecItem(spec_id, item_id) {
    layer.confirm('确定删除该规格项吗', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $(".spec_item_item_" + spec_id + '_' + item_id).remove();

        if(item_id){
            var index = layer.load(0);
            $.ajax({
                url: "/admin/good/delSpecItem",
                type: 'post',
                data: {id:item_id},
                dataType: "json",
                success: function (result) {
                    layer.closeAll();
                    if (result.code == 200) {

                    } else {
                        layer.msg(result.message, {time: 2000, icon: 5});
                    }

                }
            });
        } else {
            layer.closeAll();
        }
    });
}

//上传规格项图片
function selectSpecItemImage(elm, spec_id) {
    require(["util"], function (util) {
        var btn = $(elm);
        var ipt = btn.parent().prev();
        var val = ipt.val();
        var img = ipt.parent().next().children();
        options = {
            'global': false,
            'class_extra': '',
            'direct': true,
            'multiple': false,
            'fileSizeLimit': 33554432
        };
        util.image(val, function (url) {
            if (url.url) {
                if (img.length > 0) {
                    img.get(0).src = url.url;
                }
                ipt.val(url.attachment);
                ipt.attr("filename", url.filename);
                ipt.attr("url", url.url);
            }
            if (url.media_id) {
                if (img.length > 0) {
                    img.get(0).src = url.url;
                }
                ipt.val(url.media_id);
            }
        }, options);
    });
}

//获取规格，规格项
function getSpecs() {
    var specs = [];
    $(".spec_item").each(function () {
        var spec_number = $(this).data('number');
        var spec = {};
        spec.number = spec_number;
        spec.spec_id = $(this).find(".spec_id").val();
        spec.title = $(this).find(".spec_title").val();

        //获取规格项
        var spec_items = [];
        $(".spec_item_item_" + spec_number).each(function () {
            var spec_item = {};
            var spec_item_number = $(this).data('number');
            var objClass = "spec_item_item_" + spec_number + '_' + spec_item_number;
            spec_item.spec_number = spec_number;
            spec_item.number = spec_item_number;
            spec_item.id = $("." + objClass).find(".spec_item_id").val();
            spec_item.title = $("." + objClass).find(".spec_item_title").val();
            spec_item.thumb = $("." + objClass).find(".spec_item_thumb").val();

            if (spec_item.title) {
                spec_items.push(spec_item);
            }
        });

        spec.spec_items = spec_items;

        if (spec_items.length > 0 && spec.title) {
            if (spec_items.length > maxSpecItems) {
                maxSpecItems = spec_items.length;
            }
            specs.push(spec);
        }
    });

    return specs
}

//刷新规格项
function refreshOptions() {
    var specs = getSpecs();
    if (specs.length > 0) {
        var spec_titles = '';
        var spec_options = '';
        var options = $(".spec_option_sample")[0].outerHTML;

        var tr_num = 1;//行数

        if (specs.length == 1) {
            for (i = 0; i < specs.length; i++) {
                var spec = specs[i];
                var spec_items = spec.spec_items;
                spec_titles += "<th>" + spec.title + "</th>";
                var option_tr = '';

                for (j = 0; j < spec_items.length; j++) {
                        var spec_item = spec_items[j];
                        option_tr += '<tr><input type="hidden" value="" name="option_id">';
                        option_tr += '<td class="full" rowspan="1"><input type="hidden"  value="'+spec_item.id+'" name="option_title_' + spec.title +  '">' + spec_item.title + '</td>';
                        option_tr += '<td><input type="text" class="form-control option_stock" value="" name="option_stock"></td>';
                        option_tr += '<td><input type="text" class="form-control option_marketprice" value="" name="option_marketprice"></td>';
                        option_tr += '<td><input type="text" class="form-control option_productprice" value="" name="option_productprice"></td>';
                        option_tr += '<td><input type="text" class="form-control option_costprice" value="" name="option_costprice"></td>';
                        option_tr += '</tr>';
                }
            }
        } else {
            var spec_titles = '';
            for (i = 0; i < specs.length; i++) {
                var spec = specs[i];
                spec_titles += "<th>" + spec.title + "</th>";
            }

            //计算商品规格笛卡尔积
            var option_tr = '';
            var result = [[]];
            for (var i = 0; i < specs.length; i++) {
                var currentSubArray = specs[i].spec_items;
                var temp = [];
                for (var j = 0; j < result.length; j++) {
                    for (var k = 0; k < currentSubArray.length; k++) {
                        temp.push(result[j].concat(currentSubArray[k]));
                    }
                }
                result = temp;
            }

            if (result.length > 0) {
                for (i = 0; i < result.length; i++) {
                    option_tr += '<tr>';
                    var spec_item_number = '';
                    for (j = 0; j < result[i].length; j++) {
                        var spec = specs[j];
                        var spec_item = result[i][j];
                        option_tr += '<input type="hidden" value="" name="option_id"><td class="full" rowspan="1"><input type="hidden"  value="'+spec_item.title+'" name="option_title_' + spec.title +  '">' + spec_item.title + '</td>';
                        if(j>=result[i].length-1) {
                            spec_item_number += spec_item.number;
                        } else {
                            spec_item_number += spec_item.number+'_';
                        }
                    }
                    option_tr += '<td><input type="text" class="form-control option_stock" value="" name="option_stock"></td>';
                    option_tr += '<td><input type="text" class="form-control option_marketprice" value="" name="option_marketprice"></td>';
                    option_tr += '<td><input type="text" class="form-control option_productprice" value="" name="option_productprice"></td>';
                    option_tr += '<td><input type="text" class="form-control option_costprice" value="" name="option_costprice"></td>';
                    option_tr += '</tr>';
                }
            }
        }

        spec_options = option_tr;
        options = options.replaceAll(/spec_option_sample/, '');
        options = options.replaceAll(/\<th\>spec_titles\<\/th\>/, spec_titles);
        options = options.replaceAll(/\<tr\>\<th\>spec_options\<\/th\>\<\/tr\>/, spec_options);
        options = options.replaceAll(/display\:none/, '');
        $("#options").html(options);
    }
}
//批量设置库存，价格
function setCol(type) {
    $("." + type).val($("." + type + "_all").val());
}
