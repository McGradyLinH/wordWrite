$(function () {
    let payjs_order_id = $("#payjs_order_id").val();
    let i = 0;
    setInterval(function () {
        console.log(i++);
        $.ajax({
            url: '/api/pay/check',
            type: 'post',
            data: {payjs_order_id: payjs_order_id},
            success: function (data) {
                if (data == 'success') {
                    window.location.href = "/stuIndex";
                }
            }
        });
    }, 3000);
})