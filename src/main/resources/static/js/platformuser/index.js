$(function () {
    initTable();
})

function initTable() {
//初始化表格
    $('#data').bootstrapTable({
        url: "/getUsers",
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
            field: 'name',
            title: 'name',
            align: 'center'
        }, {
            field: 'phone',
            title: 'phone',
            align: 'center'
        }, {
            field: 'email',
            title: 'email',
            align: 'center'
        }, {
            field: 'role',
            title: 'role',
            align: 'center',
            formatter: function (value, row, index) {
                if (value === 0) {
                    return "管理员";
                } else if (value === 1) {
                    return "学生";
                }else if (value === 2) {
                    return "外教";
                } else {
                    return "中教";
                }
            }
        }, {
            field: '',
            title: '操作',
            align: 'center',
            formatter: function (value, row, index) {
                return '<a href="/user/' + row.id + '" class="btn btn-primary">修改</a>';
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