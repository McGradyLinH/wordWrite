let titles;
$(document).ready(function () {
    //点击开始建 开始计数
    let count = 0;
    setInterval(function() {
        count++;
        // 需要改变页面上时分秒的值
        $("id_S").innerHTML = showNum(count % 60)
        $("id_M").innerHTML = showNum(parseInt(count / 60) % 60)
        $("id_H").innerHTML = showNum(parseInt(count / 60 / 60))
    }, 1000)


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
            $("#titleCode").val('');
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
            $("#selectTitle").html(titles[i].titleName);
            break;
        }
    }
}

//可以将查找标签节点的操作进行简化  var btn = getElementById('btn')
function $(id) {
    return document.getElementById(id)
}


//封装一个处理单位数字的函数
function showNum(num) {
    if (num < 10) {
        return '0' + num
    }
    return num
}
