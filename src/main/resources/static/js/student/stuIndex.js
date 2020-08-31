$(function () {
    initTable();
})

function initTable() {
//初始化表格
    $('#data').bootstrapTable({
        url: "/stuessays",
        method: 'GET',                      //请求方式（*）
        toolbar: '#toolbar',              //工具按钮用哪个容器
        // striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 5,                        //每页的记录行数（*）
        // search: true,                      //是否显示表格搜索
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        queryParams: function (params) {//自定义参数，这里的参数是传给后台的，我这是是分页用的
            return {//这里的params是table提供的
                pageNumber: (params.offset / params.limit) + 1,//从数据库第几条记录开始
                pageSize: params.limit//找多少条
            };
        },
        columns: [{
            field: '#',
            title: '序号',
            align: 'center',
            formatter: function (value, row, index) {
                return index;
            }
        }, {
            field: 'titleName',
            title: '作文名',
            align: 'center'
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            formatter: function (value, row, index) {
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
            align: 'center',
            formatter: function (value, row, index) {
                let a = '<a href="/stucheck?essayName=' + row.name + '&stuName=' + row.student.name + '&versions=' + row.versions + '"  style="background-color: #00a0e8">查看</a>';
                return a;
            }
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