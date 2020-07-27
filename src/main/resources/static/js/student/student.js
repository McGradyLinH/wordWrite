let titles;
$(document).ready(function () {
    $("#controlWrite").click(function () {
        if ($("#controlWrite").text() === '开始写作') {
            $.ajax({
                url: '/checkSurplus',
                type: 'get',
                success: function (data) {
                    if (data=='success'){
                        $(".modal").modal();
                    }else {
                        bootbox.alert("没有更多的修改次数！");
                    }
                }
            })
        } else {
            $("#selectTitle").html('');
            $("#titleName").val('');
            $("#controlWrite").text('开始写作');
            $("#writeArea").css('display', 'none');
        }
    });

    $.ajax({
        url: '/titles',
        type: 'get',
        success: function (data) {
            titles = data;
            for (let i = 0; i < data.length; i++) {
                let add_options = '<option value="' + data[i].titleCode + '">' + data[i].titleName + '</option>';
                $('datalist#title_list').append(add_options);
            }
        }
    });
});

function saveTitle() {
    let titleCode = $("#title").val();
    titleName(titleCode);
    $(".modal").modal('hide');
    $("#writeArea").css('display', 'block');
    $("#controlWrite").text('放弃写作');
}

function titleName(titleCode) {
    for (let i = 0; i < titles.length; i++) {
        if (titles[i].titleCode == titleCode) {
            $("#titleName").val(titles[i].titleName);
            $("#titleCode").val(titles[i].titleCode);
            $("#selectTitle").html(titles[i].titleName);
            break;
        }
    }
}