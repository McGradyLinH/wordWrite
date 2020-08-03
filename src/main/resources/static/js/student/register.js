function refresh() {
    document.getElementById('captcha_img').src = "/kaptcha?" + Math.random();
}

function checkpwd(_this) {
    let repwd = $(_this).val();
    let pwd = $("#userPwd").val();
    if (pwd !== repwd) {
        $("#zhuce").attr("disabled", "true");
        $("#pwdMsg").text("两次密码不一致！");
    } else {
        $("#pwdMsg").text("");
        $("#zhuce").removeAttr("disabled")
    }
}