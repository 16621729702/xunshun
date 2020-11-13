$.randomString = function (len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
};

/**
 * 替换url参数
 * @param url
 * @param paramName
 * @param paramValue
 * @returns {*}
 */
function replaceUrlParam(url, paramName, paramValue) {
    if (paramValue == null) {
        paramValue = '';
    }
    var pattern = new RegExp('\\b(' + paramName + '=).*?(&|#|$)');
    if (url.search(pattern) >= 0) {
        return url.replace(pattern, '$1' + paramValue + '$2');
    }
    url = url.replace(/[?#]$/, '');
    return url + (url.indexOf('?') > 0 ? '&' : '?') + paramName + '=' + paramValue;
}

//弹框html1
$(document).on('click', '.ajax_load', function (e) {
    var $_url = $(this).data('url');
    var $_title = $(this).data('title') ? $(this).data('title') : $(this).html();
    var $_width = $(this).data('width') ? $(this).data('width') : '500px';
    var $_height = $(this).data('height') ? $(this).data('height') : '500px';

    layer.open({
        type: 2,
        title: $_title,
        shadeClose: true,
        shade: 0.8,
        shift: -1,
        maxmin: true, //允许全屏最小化
        area: [$_width, $_height],
        content: $_url
    });
});

$(document).on('click', '.ajax-link', function () {
    var $_url = $(this).data('url');
    var $_id = $(this).data('id');
    if ($_url.indexOf("?") == -1) {
        $_url = $_url + '?callback=' + $_id;
    } else {
        $_url = $_url + '&callback=' + $_id;
    }
    layer.open({
        type: 2,
        shade: false,
        area: ['800px', '500px'],
        maxmin: true,
        shift: -1,
        title: '选择链接',
        content: $_url,
        zIndex: layer.zIndex, //重点1
        success: function (layero) {

        }
    });
});

//弹框iframe
$(document).on('click', '.load_iframe', function () {
    var $_url = $(this).data('url');
    var $_title = $(this).data('title') ? $(this).data('title') : $(this).html();
    var $_width = $(this).data('width') ? $(this).data('width') : '500px';
    var $_height = $(this).data('height') ? $(this).data('height') : '500px';

    //批量操作
    if ($(this).data("batch")) {
        var batch = $(this).data("batch");
        var parmkey = batch;
        var parmvalue = [];
        $(".check_item:checked").each(function () {
            var value = $(this).data(batch);
            parmvalue.push(value);
        });
        if (parmvalue.length <= 0) {
            layer.msg('请选择要操作的数据', {time: 2000, icon: 5});
            return;
        }
        if ($_url.indexOf('?') == -1) {
            $_url += "?" + parmkey + "=" + parmvalue;
        } else {
            $_url += "&" + parmkey + "=" + parmvalue;
        }
    }

    layer.open({
        type: 2,
        shade: false,
        area: [$_width, $_height],
        maxmin: true,
        shift: -1,
        title: $_title,
        content: $_url,
        zIndex: layer.zIndex, //重点1
        success: function (layero, index) {
        },
    });
});

//toastr参数初始化
toastr.options = {
    closeButton: true,
    progressBar: true,
    showMethod: 'slideDown',
    timeOut: 4000
};

function ajaxSbumit(obj) {
    $("#ajaxsubmit").attr("disabled", "true");
    var loading_text = "正在提交数据...";
    var submit_text = obj.html();

    if (obj.data('loading_text')) {
        loading_text = obj.data('loading_text');
    }
    var index = layer.load(2);
    $("#ajaxsubmit").text(loading_text);
    $('#ajaxform').ajaxSubmit(
        {
            dataType: "json",           //html(默认), xml, script, json...接受服务端返回的类型
            clearForm: false,          //成功提交后，清除所有表单元素的值
            resetForm: false,          //成功提交后，重置所有表单元素的值
            target: '#output',          //把服务器返回的内容放入id为output的元素中
            success: function (data, status, xhr, $form) {
                layer.close(index);
                var $_success = data;
                if ($_success.errno == 0) {
                    $("#ajaxsubmit").removeAttr("disabled");
                    $("#ajaxsubmit").text(submit_text);
                    layer.msg($_success.message, {time: 2000, icon: 1});
                    var request_type = $('meta[name="request-type"]').attr('content');
                    console.log(121121212, request_type)
                    if ($_success.redirect && request_type == 'page') {
                        setTimeout(function () {
                            window.location = $_success.redirect;
                        }, 1000);
                    } else {
                        setTimeout(function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            if (index) {
                                parent.layer.close(index);
                                window.parent.location.reload();
                            }
                        }, 1000);
                    }
                } else {
                    layer.msg($_success.message, {time: 2000, icon: 5});
                    $("#ajaxsubmit").removeAttr("disabled");
                    $("#ajaxsubmit").text(submit_text);
                    return false;
                }

            },
            error: function (data, xhr, status, error, $form) {
                layer.close(index);
                console.log(data);
                var $_error = jQuery.parseJSON(data.responseText);
                for (var v in $_error.errors) {
                    layer.msg($_error.errors[v][0], {time: 2000, icon: 5});
                    $("#ajaxsubmit").removeAttr("disabled");
                    $("#ajaxsubmit").text(submit_text);
                    break;
                }
            },
            complete: function (data, xhr, status, $form) {
            }
        }
    );
    return false; //阻止表单默认提交
}

function ajaxSubmitClass(obj) {
    $(".ajaxsubmit").attr("disabled", "true");
    var loading_text = "正在提交数据...";
    var submit_text = obj.html();

    if (obj.data('loading_text')) {
        loading_text = obj.data('loading_text');
    }
    var index = layer.load(2);
    $(".ajaxsubmit").text(loading_text);
    $('#ajaxform').ajaxSubmit(
        {
            dataType: "json",           //html(默认), xml, script, json...接受服务端返回的类型
            clearForm: false,          //成功提交后，清除所有表单元素的值
            resetForm: false,          //成功提交后，重置所有表单元素的值
            target: '#output',          //把服务器返回的内容放入id为output的元素中
            success: function (data, status, xhr, $form) {
                layer.close(index);
                var $_success = data;
                if ($_success.errno == 0) {
                    $(".ajaxsubmit").removeAttr("disabled");
                    $(".ajaxsubmit").text(submit_text);
                    layer.msg($_success.message, {time: 2000, icon: 1});
                    var request_type = $('meta[name="request-type"]').attr('content');
                    console.log(121121212, request_type)
                    if ($_success.redirect && request_type == 'page') {
                        setTimeout(function () {
                            window.location = $_success.redirect;
                        }, 1000);
                    } else {
                        setTimeout(function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            if (index) {
                                parent.layer.close(index);
                                window.parent.location.reload();
                            }
                        }, 1000);
                    }
                } else {
                    layer.msg($_success.message, {time: 2000, icon: 5});
                    $(".ajaxsubmit").removeAttr("disabled");
                    $(".ajaxsubmit").text(submit_text);
                    return false;
                }

            },
            error: function (data, xhr, status, error, $form) {
                layer.close(index);
                console.log(data);
                var $_error = jQuery.parseJSON(data.responseText);
                for (var v in $_error.errors) {
                    layer.msg($_error.errors[v][0], {time: 2000, icon: 5});
                    $(".ajaxsubmit").removeAttr("disabled");
                    $(".ajaxsubmit").text(submit_text);
                    break;
                }
            },
            complete: function (data, xhr, status, $form) {
            }
        }
    );
    return false; //阻止表单默认提交
}

//询问框
$(document).on('click', '.ajax-confirm', function () {
    var $_url = $(this).data('url');
    var $_title = $(this).data('title') ? $(this).data('title') : $(this).html();
    var param = {};
    var method = "get";
    if ($(this).hasClass("delete")) {
        param['_method'] = 'delete';
        method = "post";
    }
    if ($(this).data("method")) {
        method = $(this).data("method");
    }
    //post参数
    if ($(this).data("param")) {
        param = $(this).data("param");
    }
    //批量操作
    if ($(this).data("batch")) {
        var batch = $(this).data("batch");
        param[batch] = [];
        $(".check_item:checked").each(function () {
            var value = $(this).data(batch);
            param[batch].push(value);
        });
        if (param[batch].length <= 0) {
            layer.msg('请选择要操作的数据', {time: 2000, icon: 5});
            return;
        }
    }

    layer.confirm($_title, {
        btn: ['确定', '取消'] //按钮
    }, function () {
        //ajax删除内容一般是delete方式laravel 伪造方法
        var index = layer.load(0);

        $.ajax({
            url: $_url,
            type: method,
            data: param,
            dataType: "json",
            success: function (result) {
                layer.close(index);
                if (result.errno == 0) {
                    layer.msg(result.message, {time: 2000, icon: 6});
                    if (result.data.redirect) {
                        window.location = result.data.redirect;
                    } else {
                        location.reload();
                    }
                } else {
                    layer.msg(result.message, {time: 2000, icon: 5});
                }

            }
        });
    });
});

//ajax 表单令牌验证
$(function () {
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
        }
    });
})


$(document).on('click', '.ajax-switch', function (e) {
    var $_url = $(this).data('url');
    var datas = {value: ($(this).data('value')), id: $(this).data('tional')};
    $.ajax(
        {
            url: $_url,
            type: 'get',
            data: datas,
            dataType: "json",
            success: function (result) {
                if (result.errno || result.errno == 0) {
                    layer.msg(result.message, {time: 2000, icon: 6});
                    if (result.redirect) {
                        window.location = result.redirect;
                    } else {
                        location.reload();
                    }
                } else {
                    layer.msg(result.message, {time: 2000, icon: 5});
                }
            }
        })
});

//
$(document).on('click', '.load_prompt', function () {
    var $_url = $(this).data('url');
    var $_title = $(this).data('title');
    var method = "POST";
    layer.prompt({title: '更改排序-' + $_title, formType: 3}, function (sort, index) {
        layer.close(index);
        $.ajax({
            url: $_url,
            type: method,
            data: {data: sort},
            dataType: "json",
            success: function (result) {
                if (result.errno || result.errno == 0) {
                    layer.msg(result.message, {time: 2000, icon: 6});
                    if (result.redirect) {
                        window.location = result.redirect;
                    } else {
                        location.reload();
                    }
                } else {
                    layer.msg(result.message, {time: 2000, icon: 5});
                }

            }
        });
    });
});

/**
 * 图片路径不存在，默认图片
 */
$(document).ready(function () {
    $("#ajaxsubmit").on("click", function () {
        ajaxSbumit($(this));
    });
    $(".ajaxsubmit").on("click", function () {
        ajaxSubmitClass($(this));
    });
    $("img").each(function (i, ele) {
        var obj = $(this);
        $("<img/>").attr("src", $(ele).attr("src")).on('error', function () {
            if (!$(this).attr("onerror")) {
                var e = "/web/resource/images/nopic-107.png";
                obj.width() == obj.height() ? e = "/web/resource/images/nopic-107.png" : obj.width() < obj.height() && (e = "/admin/web/resource/images/nopic-203.png"), obj.hasClass("user-avatar") && (e = "/web/resource/images/nopic-user.png"), obj.hasClass("module-img") && (e = "/web/resource/images/nopic-module.png"), obj.hasClass("template-img") && (e = "/web/resource/images/nopic-template.png"), obj.hasClass("account-img") && (e = "/web/resource/images/nopic-account.png"),
                    obj.attr("src", e)
            }
        })
    });
});

/**
 * 切换分页条数
 */
function pagesizeChange(obj) {

}

/**
 * 列表全选，反选
 */
$(document).on('change', '.check_all', function () {
    if ($(".check_all").is(":checked")) {
        $(".check_item").prop("checked", true);
    } else {
        $(".check_item").prop("checked", false);
    }
});

/**
 * 图片点击放大效果
 */
$(document).on('click', '.img_pop_show', function () {
    var src = $(this).prop('src');
    var img = new Image();
    img.src = src;
    var imgHtml = "<img src='" + src + "' width='500px' height='500px'/>";
    //弹出层
    layer.open({
        type: 1,
        shade: 0.8,
        offset: 'auto',
        area: [500 + 'px', 550 + 'px'],
        shadeClose: true,
        scrollbar: false,
        title: "图片预览",
        content: imgHtml,
        cancel: function () {
        }
    });
});
/**
 * tr 行点击checkbox正选反选
 */
$(document).on('click', '.tr_checkbox_switch', function () {
    if ($(this).find(".check_item").is(":checked")) {
        $(this).find(".check_item").attr("checked", false);
    } else {
        $(this).find(".check_item").attr("checked", true);
    }
});
$(document).ready(function () {
    $('.gopagenum').on('keypress', function (e) {
        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) {
            $(".btn-gotopage").trigger('click');
        }
    });
});