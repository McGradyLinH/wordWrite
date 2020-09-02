$(function () {
    initTable();
})

function initTable() {
//初始化表格
    $('#data').bootstrapTable({
        url: "/teacherDoneEssays",
        method: 'GET',                      //请求方式（*）
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 5,                        //每页的记录行数（*）
        queryParams: queryParams,
        columns: [{
            field: 'id',
            title: '#',
            align: 'center',
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: 'titleName',
            title: '作文名',
            align: 'center'
        }, {
            field: 'student.name',
            title: '学生姓名',
            align: 'center',
            formatter: function (value, row, index) {
                return row.student.name;
            }
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            formatter: function (value, row, index) {
                if (value === 1) {
                    return "外教修改";
                } else if (value === 2) {
                    return "中教修改";
                } else {
                    return "已完成";
                }
            }
        }, {
            field: 'create_time',
            title: '操作',
            align: 'center',
            formatter: function (value, row, index) {
                let a = '<a href="/stucheck?essayName=' + row.name + '&stuName=' + row.student.name + '&versions=' + row.versions + '" class="btn btn-primary">查看</a>';
                return a;
            }
        }]
    });
}

//请求服务数据时所传参数
function queryParams(params) {
    return {
        pageSize: params.pageSize, //每一页的数据行数，默认是上面设置的10(pageSize)
        pageIndex: params.pageNumber, //当前页面,默认是上面设置的1(pageNumber)
    }
}

