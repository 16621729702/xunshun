
$(function(){
    load_datetimepicker();
    require(['jquery.ui'],function(){
        $('#type-items').sortable();
    })
})
function addField() {
    var fieldType = $("#data_type").val();
    $.ajax({
        url: fieldTplUrl,
        type: 'GET',
        dataType: 'html',
        data: {fieldkey: fieldkey, fieldType: fieldType, flag: 1},
        success: function (html) {
            $("#type-items").append(html);

            if (fieldType == 7 || fieldType == 8) {
                load_datetimepicker();
            }
            fieldkey++;
        }
    });
}

function tp_change_default(knum) {
    if ($("#tp_is_default" + knum).val() == 1) {
        $("#tp_default" + knum).css("display", "inline");
    } else {
        $("#tp_default" + knum).hide();
    }
}

function tp_change_default_time(obj, ids) {
    if (obj.value == 2) {
        $("#" + ids).css("display", "inline");

    } else {
        $("#" + ids).hide();
    }
}

function load_datetimepicker() {
    require(["datetimepicker"], function () {
        $(function () {
            $(".datetimepicker1").each(function () {
                var option = {
                    lang: "zh",
                    step: "10",
                    timepicker: false,
                    closeOnDateSelect: true,
                    format: "Y-m-d"
                };
                $(this).datetimepicker(option);
            });
        });
    });
}

function removeField(obj) {
    $(obj).parent().parent().parent().parent().remove();
}