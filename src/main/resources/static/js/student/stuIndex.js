function updateEssay(essayName, stuName) {
    $.ajax({
        url: '/stucheck',
        type: 'get',
        data: {essayName: essayName, stuName: stuName},
        dataType: 'html',
        success: function (data) {
            $("#changeBody").html(data);
        }
    })
}