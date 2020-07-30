// let titles;
$(document).ready(function () {
    $("#controlWrite").click(function () {
        if ($("#controlWrite").text() === '开始写作') {
            $.ajax({
                url: '/checkSurplus',
                type: 'get',
                success: function (data) {
                    if (data=='success'){
                        // $(".modal").modal();
                        $("#writeArea").css('display', 'block');
                        $("#controlWrite").text('放弃写作');
                        startJishi();
                    }else {
                        bootbox.alert("没有更多的修改次数！");
                    }
                }
            })
        } else {
            stopJishi();
            $("#controlWrite").text('开始写作');
            $("#writeArea").css('display', 'none');
        }
    });
    $("#myFile1").change(function () {
        //创建blob对象，浏览器将文件放入内存中，并生成标识
        var img_src = URL.createObjectURL($(this)[0].files[0]);
        //给img标检的src赋值
        document.getElementById("preview_img").src=img_src;
    });
});
// function getTitles(){
//     $.ajax({
//         url: '/titles',
//         type: 'get',
//         success: function (data) {
//             titles = data;
//             for (let i = 0; i < data.length; i++) {
//                 let add_options = '<option value="' + data[i].titleCode + '">' + data[i].titleName + '</option>';
//                 $('datalist#title_list').append(add_options);
//             }
//         }
//     });
// }
function saveTitle() {
    let titleCode = $("#title").val();
    titleName(titleCode);
    $(".modal").modal('hide');
}

function saveTitle1() {
    let titleCode = $("#title").val();
    titleName(titleCode);
    $(".modal").modal('hide');
    $("#uploadArea").css('display', 'block');
    $("#chooseTitle").text('放弃上传');
}

function titleName(titleCode) {
    for (let i = 0; i < titles.length; i++) {
        if (titles[i].titleCode == titleCode) {
            $("#titleName").val(titles[i].titleName);
            $("#titleCode").val(titles[i].titleCode);
            break;
        }
    }
}

//封装一个处理单位数字的函数
function showNum(num) {
    if (num < 10) {
        return '0' + num;
    }
    return num;
}
let count = 0;
let timer = null;
function startJishi() {
    //点击开始建 开始计数
    timer = setInterval(function() {
        count++;
        // 需要改变页面上时分秒的值
        $("#id_S").text(showNum(count % 60));
        $("#id_M").text(showNum(parseInt(count / 60) % 60));
        $("#id_H").text(showNum(parseInt(count / 60 / 60)));
    }, 1000);
}
//关闭定时器
function stopJishi() {
    clearInterval(timer);
    count = 0;
    $("#id_S").text(0);
    $("#id_M").text(0);
    $("#id_H").text(0);
}
//统计textarea单词数
function wordStatic(input) {
    let text = $(input).val();
    text = text.replace(/\r\n/g, " ")
    text = text.replace(/\n/g, " ");
    const arr = text.split(" ");
    for (let i = 0; i < arr.length; i++) {
        if (arr[i].length < 1) {
            arr.splice(i, 1);
            i = i - 1;
        }
    }
    $("#wordCount").text(arr.length);
}
//打开关闭上传区域
function showFileUpload() {
    $("#uploadTitleArea").toggle(500);
}