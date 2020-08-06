function updateEssay(essayName, stuName, versions) {
    $.ajax({
        url: '/stucheck',
        type: 'get',
        data: {essayName: essayName, stuName: stuName, versions : versions},
        dataType: 'html',
        success: function (data) {
            $("#changeBody").html(data);
        }
    })
}

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
                data: {_method: 'put', versions: id},
                success: function (data) {
                    window.location.reload();
                }
            });
        }
    });
}