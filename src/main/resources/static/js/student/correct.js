let maxId = 0;
$(function () {
    initComments();
    let lastSpanid = $('#content span').last().attr("id");
    if (lastSpanid != null && lastSpanid != ""){
        maxId = lastSpanid.substring(5);
    }
});
//初始化评论
function initComments() {
    let essayNumber = $("#essayNumber").val();
    $.ajax({
        url: '/comments',
        type: 'get',
        data: {index:essayNumber},
        success: function (data) {
            let length = data.length;
            for (let i = 0;i<length;i++){
                let spanId = data[i].spanId;
                $("#pigai"+spanId).on("click", function () {
                    createTooltip(spanId, data[i].comment,data[i].teacher.name,data[i].teacher.role);
                });
            }
        }
    });
}

//绑定点击事件时生产的html
function createTooltip(m, inputtext,tName,role) {
    for (let y = 0; y <= maxId; y++) {
        $("#tooltipx" + y).remove();
    }
    let whoteacher = role === 2 ? 'enTeacher':'CNTeacher';
    let tooltip = '<div id="tooltipx' + m + '"><p>'+whoteacher+':' + tName + '</p >' +
        '<input type="text" style="border: none;" id="text' + m + '" value="' + inputtext + '"' +
        'readonly /></div>';
    $("body").append(tooltip);
    $("#tooltipx" + m).css({
        "float": "left",
        "border" : "1px solid",
    }).show("fast");
}