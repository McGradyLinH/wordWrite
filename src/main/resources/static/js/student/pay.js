/********input表单只允许输入大于0的整数，没有小数点************/
function carNum($this) {
    //输入框的值
    let value = $this.val();
    caculateTotal(value);
    if (isNaN(value)) { //判断值是不是数字
        $this.val(1);
        caculateTotal(1);
    } else if (value == 0) { //判断值是不是1
        $this.val(1);
        caculateTotal(1);
    } else if (value.indexOf(".") != -1) { //判断有没有输入小数点
        value = value.substring(0, value.indexOf("."));
        $this.val(value);
        caculateTotal(value);
    } else if (value < 0) { //判断有没有输入小数点
        $this.val(1);
        caculateTotal(1);
    } else if (value == '-'){
        $this.val(1);
        caculateTotal(1);
    }
}
/**
 * 计算总价
 * @param {Object} num
 */
function caculateTotal(num){
    $("#totalPrice").val(num * 69);
}
/**
 * 查询优惠码的合理性
 */
function applyCoupon(){
    let coupon = $("#couponCode").val();
    if (coupon == 'save14' || coupon == 'SAVE14'){
        $("#totalPrice").val(num * 69 * 0.86);
    }
}