let i = 0;
let teacherName = $("#teacherName").val();
let maxId = 0;
let addComments = [];
let delComments = [];
$(function () {
    initComments();
    $("#content").mouseup(function (e) {
        for (let y = 0; y <= i; y++) {
            let a = $("#text"+y).val();
            if (a===null || a===""){
                removetooltip(y);
            }
            $("#tooltip" + y).remove();
        }
        $('#content').find("span").each(function (e) {
            let spanId = $(this).attr('id');
            let tempMax = spanId.substring(5);
            if (tempMax > maxId) {
                maxId = tempMax;
            }
        });
        for (let y = 0; y <= maxId; y++) {
            $("#tooltipx" + y).remove();
        }
        const x = 10;
        const y = 10;
        let text = "";
        if (document.selection) {
            text = document.selection.createRange().text;
        } else if (window.getSelection()) {
            text = window.getSelection();
        }
        text = $.trim(text);
        if (text !== "" && text != null) {
            i = i + 1;
            let tooltip = '<div id="tooltip' + i + '"><p>' + teacherName + '</p >' +
                '<button class="color" style="background-color: #ff0000;" onclick="changeColor1(this,' + i + ')"></button>' +
                '<button class="color" style="background-color: #0000ff;" onclick="changeColor1(this,' + i + ')"></button>' +
                '<button class="color" style="background-color: #00ffff;" onclick="changeColor1(this,' + i + ')"></button>' +
                '<button class="color" style="background-color: #7fff00" onclick="changeColor1(this,' + i + ')"></button>' +
                '<br /><input type="text" style="border: none;" id="text' + i + '" placeholder="write something"/>' +
                '<br /><button onclick="savepigai(' + i + ')">Save</button><button onclick="removetooltip(' + i +
                ')">Delete</button></div>';
            $("#correctComments").append(tooltip);
            $("#tooltip" + i).css({
                "top": (e.pageY + y) + "px",
                "left": (e.pageX + x) + "px",
                // "position": "absolute",
                "float": "left",
                "border-top": "1px solid black",
                "border-left": "1px solid black"
            }).show("fast");
            changeColor($("<button style='background-color: red;'></button>"));
        }
    })
});
//初始化评论
function initComments() {
    $.ajax({
        url: '/comments',
        type: 'get',
        data: {index:essayNumber},
        success: function (data) {
            if (data.length !== 0){
                i = data[data.length-1].spanId;
                for (let j = 0;j<data.length;j++){
                    let spanId = data[j].spanId;
                    $("#pigai"+spanId).on("click", function () {
                        createTooltip(spanId, data[j].comment,data[j].teacher.name);
                    });
                }
            }
        }
    });
}

//删除右边弹出的
function removetooltip(index) {
    $("#tooltipx" + index).remove();
    $("#tooltip" + index).remove();
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
            maxId = maxId + 1;
            $(this).on("click", function () {
                createTooltip(index, inputText ,teacherName);
            });
            $("#tooltip" + index).remove();
            saveToServer(index, inputText);
        }
    });
}
//增加评论
let essayNumber = $("#essayNumber").val();
function saveToServer(index, inputText) {
    addComments.push({
       spanId : index,
       comment: inputText,
       essayNumber : essayNumber
    });
}
//删除评论
function deleteComment(index) {
    delComments.push({
        spanId: index,
        essayNumber: essayNumber
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
    let teacherName = $("#teacherId" + index).text();
    $("#pigai" + index).off("click");
    $("#pigai" + index).on("click", function () {
        createTooltip(index, inputtext);
    });
    $("#tooltipx" + index).remove();
}

//绑定点击事件时生产的html
function createTooltip(m, inputtext,tName) {
    for (let y = 0; y <= maxId; y++) {
        $("#tooltipx" + y).remove();
    }
    let tooltip = '<div id="tooltipx' + m + '"><p id="teacherId' + m + '">' + tName + '</p >' +
        '<button class="color" style="background-color: red;" onclick="changeColor1(this,' + m + ')"></button>' +
        '<button class="color" style="background-color: blue;" onclick="changeColor1(this,' + m + ')"></button>' +
        '<button class="color" style="background-color: aqua;" onclick="changeColor1(this,' + m + ')"></button>' +
        '<button class="color" style="background-color: chartreuse" onclick="changeColor1(this,' + m + ')"></button>' +
        '<br /><input type="text" style="border: none;" id="text' + m + '" value="' + inputtext + '"' +
        'placeholder="write something"/>' +
        '<br /><button onclick="updatepigai(' + m + ')">Update</button><button onclick="removetooltip(' + m +
        ')">Delete</button></div>';
    $("#correctComments").append(tooltip);
    $("#tooltipx" + i).css({
        "float": "left",
    }).show("fast");
}

//完成批改一篇文章
function correctDone(index) {
    let text = $("#content").prop("innerHTML");
    $.ajax({
        url: '/correctessay',
        type: 'post',
        data: {index: index, content: text,
            addComments: JSON.stringify(addComments),delComments: JSON.stringify(delComments)},
        success: function (data) {
            window.location.reload();
        }
    });
}