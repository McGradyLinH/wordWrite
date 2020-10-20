function checkAgain(id) {
    let str;
    if (id === 2) {
        str = "确定提交该篇文章重新批改吗？"
    } else {
        str = "确认该文章批改结束？"
    }
    bootbox.confirm(str, function (result) {
        if (result) {
            $.ajax({
                url: '/stuessay',
                type: 'post',
                data: {_method: 'put', versions: id, essayContent: $("#content").val()},
                success: function (data) {
                    window.location.href = "/stuIndex"
                }
            });
        }
    });
}

function openText() {
    $("#contentLabel").show();
    $("#check1").show();
    $("#check2").hide();
}

const openCheck = function (id){
    $.ajax({
        url: '/check/'+id,
        type: 'get',
        dataType: 'html',
        success: function (data) {
            $("#checkContent").html(data)
        }
    });
}

$(function (){
    openCheck(1);
})