let i = 0;
let teacherName = $("#teacherName").val();
$(function () {
    initComments();
    $("#content").mouseup(function (e) {
        for (let y = 0; y <= i; y++) {
            $("#tooltip" + y).remove();
        }
        var x = 10;
        var y = 10;
        var text = "";
        if (document.selection) {
            text = document.selection.createRange().text;
        } else if (window.getSelection()) {
            text = window.getSelection();
        }
        text = $.trim(text);
        if (text != "" && text != null) {
            i = i + 1;
            let tooltip = '<div id="tooltip' + i + '"><p>' + teacherName + '</p >' +
                '<button class="color" style="background-color: #ff0000;" onclick="changeColor1(this,' + i + ')"></button>' +
                '<button class="color" style="background-color: #0000ff;" onclick="changeColor1(this,' + i + ')"></button>' +
                '<button class="color" style="background-color: #00ffff;" onclick="changeColor1(this,' + i + ')"></button>' +
                '<button class="color" style="background-color: #7fff00" onclick="changeColor1(this,' + i + ')"></button>' +
                '<br /><input type="text" style="border: none;" id="text' + i + '" placeholder="write something"/>' +
                '<br /><button onclick="savepigai(' + i + ')">Save</button><button onclick="removetooltip(' + i +
                ')">Delete</button></div>';
            $("body").append(tooltip);
            $("#tooltip" + i).css({
                "top": (e.pageY + y) + "px",
                "left": (e.pageX + x) + "px",
                "position": "absolute",
                "border-top": "1px solid black",
                "border-left": "1px solid black"
            }).show("fast");
            changeColor($("<button style='background-color: red;'></button>"));
        }
    })
});
//初始化评论
function initComments() {
    let essayNumber = $("#essayNumber").val();
    $.ajax({
        url: '/comments',
        type: 'get',
        data: {index:essayNumber},
        success: function (data) {
            console.log(data);
            for (let i = 0;i<data.length;i++){
                let spanId = data[i].spanId;
                $("#pigai"+spanId).on("click", function () {
                    createTooltip(spanId, data[i].comment);
                });
            }
        }
    });
}

//删除右边弹出的
function removetooltip(index) {
    $("#tooltipx" + index).remove();
    $("#pigai" + index).off("click");
    $("#content").find("span").each(function (m) {
        let str = $(this).attr("id");
        let a = str.substring(5);
        if (a == index) {
            let text = "";
            $(this).attr("id", "yishanchu" + index);
            $(this).css("background-color", "");
            $(this).css("cursor", "");
            $(this).find("font").each(function (n) {
                text = $(this).text();
            });
            $(this).after(text);
            $(this).remove();
            deleteComment(index);
        }
    });
}

//保存添加时的批改
function savepigai(index) {
    $("#content").find("span").each(function (m) {
        let str = $(this).attr("id");
        let a = str.substring(5);
        if (a == index) {
            let inputText = $("#text" + index).val();
            if (inputText == null || inputText == "") {
                alert("input suggest!");
                return;
            }
            $(this).css({
                "cursor": "pointer"
            });
            $(this).on("click", function () {
                createTooltip(index, inputText);
            });
            $("#tooltip" + index).remove();
            saveToServer(index, inputText);
        }
    });
}

//增加评论
function saveToServer(index, inputText) {
    let essayNumber = $("#essayNumber").val();
    $.ajax({
        url: '/comment',
        type: 'post',
        data: {spanId: index, comment: inputText,essayNumber:essayNumber},
        success: function (data) {
            console.log(data)
        }
    });
}

//删除评论
function deleteComment(index) {
    let essayNumber = $("#essayNumber").val();
    $.ajax({
        url: '/comment',
        type: 'post',
        data: {spanId: index, _method: "delete",essayNumber:essayNumber},
        success: function (data) {
            console.log(data)
        }
    });
}

//添加时改变背景色
function changeColor(dom) {
    let colors = $(dom).css("background-color");
    document.execCommand("BackColor", false, colors);
    document.execCommand("ForeColor", false, "white");
    $("#content").find("span").each(function (m) {
        let spanid = $(this).attr("id");
        if (spanid == null || spanid == "") {
            $(this).attr("id", "pigai" + i);
        }
    });
}

//修改时改变背景色
function changeColor1(dom, index) {
    let colors = $(dom).css("background-color");
    $("#pigai" + index).css("background-color", colors);
}

//修改内容触发
function updatepigai(index) {
    let inputtext = $("#text" + index).val();
    $("#pigai" + index).off("click");
    $("#pigai" + index).on("click", function () {
        createTooltip(index, inputtext);
    });
    $("#tooltipx" + index).remove();
}

//绑定点击事件时生产的html
function createTooltip(m, inputtext) {
    for (let y = 0; y <= i; y++) {
        $("#tooltipx" + y).remove();
    }
    let tooltip = '<div id="tooltipx' + m + '"><p>' + teacherName + '</p >' +
        '<button class="color" style="background-color: red;" onclick="changeColor1(this,' + m + ')"></button>' +
        '<button class="color" style="background-color: blue;" onclick="changeColor1(this,' + m + ')"></button>' +
        '<button class="color" style="background-color: aqua;" onclick="changeColor1(this,' + m + ')"></button>' +
        '<button class="color" style="background-color: chartreuse" onclick="changeColor1(this,' + m + ')"></button>' +
        '<br /><input type="text" style="border: none;" id="text' + m + '" value="' + inputtext + '"' +
        'placeholder="write something"/>' +
        '<br /><button onclick="updatepigai(' + m + ')">Update</button><button onclick="removetooltip(' + m +
        ')">Delete</button></div>';
    $("body").append(tooltip);
    $("#tooltip" + i).css({
        "float": "left",
    }).show("fast");
}

//完成批改一篇文章
function correctDone(index) {
    let text = $("#content").prop("innerHTML");
    $.ajax({
        url: '/correctessay',
        type: 'post',
        data: {index: index, content: text, _method: "put"},
        success: function (data) {
            if(data=="success"){
                window.location.history(-1);
            }
        }
    });
}