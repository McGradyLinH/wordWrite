$(function () {
    initTable();
})

function initTable() {
//初始化表格
    $('#data').bootstrapTable({
        url: "/stuessays",
        method: 'GET',                      //请求方式（*）
        //toolbar: '#toolbar',              //工具按钮用哪个容器
        striped: false,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 5,                        //每页的记录行数（*）
        // pageList: [5, 10, 25, 50, 100],        //可供选择的每页的行数（*）
        // search: true,                      //是否显示表格搜索
        strictSearch: true,
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: false,                //是否启用点击选中行
        //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        columns: [{
            field: 'id',
            title: '序号',
            align: 'center'
        }, {
            field: 'titleName',
            title: '作文名',
            align: 'center'
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            formatter: function (value, row, index) {
                //通过formatter可以自定义列显示的内容
                //value：当前field的值，即id
                //row：当前行的数据
                // var a = '<a href="/teacher/data/data_download?name=' + name + '&type=' + type + '" style="background-color: #00a0e8">下载</a>&nbsp;<a class="edit" data-toggle="modal">编辑</a>&nbsp;<a class="del" name="del">删除</a>';
                // return a;
                if (value===1){
                    return "外教修改";
                }else if (value===2){
                    return "中教修改";
                }else {
                    return "已完成";
                }
            }
        }, {
            field: 'versions',
            title: '版本',
            align: 'center'
        }, {
            field: 'create_time',
            title: '操作',
            align: 'center'
        }]
    });
}

function updateEssay(essayName, stuName, versions) {
    $.ajax({
        url: '/stucheck',
        type: 'get',
        data: {essayName: essayName, stuName: stuName, versions: versions},
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