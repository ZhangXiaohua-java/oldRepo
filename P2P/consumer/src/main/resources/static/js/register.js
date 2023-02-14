//错误提示
function showError(id, msg) {
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").html("<i></i><p>" + msg + "</p>");
    $("#" + id + "Err").show();
    $("#" + id).addClass("input-red");
}

//错误隐藏
function hideError(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id).removeClass("input-red");
}

//显示成功
function showSuccess(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id + "Ok").show();
    $("#" + id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid, bosid) {
    $("#" + maskid).show();
    $("#" + bosid).show();
}

//关闭注册协议弹层
function closeBox(maskid, bosid) {
    $("#" + maskid).hide();
    $("#" + bosid).hide();
}

function agree() {
    $("#agree").click(function () {
        var ischeck = document.getElementById("agree").checked;
        if (ischeck) {
            $("#btnRegist").attr("disabled", false);
            $("#btnRegist").removeClass("fail");
        } else {
            $("#btnRegist").attr("disabled", "disabled");
            $("#btnRegist").addClass("fail");
        }
    });
}


//入口函数
//注册协议确认
$(function () {
    agree();
    checkPhone();
    checkPassword();
    register();
    checkCode();
    CodeBtnEvent();
    checkPhoneCode();
});

var phoneRegex = /^(13|15|18|19|17)+([0-9]{9,})$/g;

function checkPhone() {
    $("#phone").blur(function () {
        phoneRegex.lastIndex = 0;
        var val = $(this).val();
        if (!val) {
            showError("phone", "手机号不能为空");
        } else if (!phoneRegex.test(val)) {
            showError("phone", "手机号格式不正确");
        } else {
            checkPhoneExists($.trim(val));
        }
    })

    $("#phone").focus(function () {
        hideError("phone");
    })
}

var passwordRegex = /[0-9a-zA-Z]{6,20}/g;

var checkPassword = () => {

    $("#loginPassword").focus(function () {
        hideError("loginPassword");
    })

    $("#loginPassword").blur(function () {
        passwordRegex.lastIndex = 0;
        var passowd = $.trim($(this).val());
        if (!passowd) {
            showError("loginPassword", "密码不能为空");
        } else if (!passwordRegex.test(passowd)) {
            showError("loginPassword", "密码只能是由6到20位长度的数字和字母")
        } else {
            showSuccess("loginPassword");
        }
    })

}

function register() {
    $("#btnRegist").click(function () {
        $("#phone").focus();
        $("#phone").blur();
        $("#loginPassword").focus();
        $("#loginPassword").blur();
        $("#messageCode").blur();
        var tipContent = $("div[id$=Err]").text();
        if (tipContent) {
            // alert(tipContent);
            showError("phone", "手机号或者密码信息错误,请检查后再尝试注册");
        } else {
            var phone = $.trim($("#phone").val());
            var password = $.trim($("#loginPassword").val());
            password = $.md5(password);
            var code = $.trim($("#messageCode").val());
            $("#loginPassword").val(password);
            $.ajax({
                url: "/p2p/user/register",
                type: "post",
                data: {phone: phone, loginPassword: password, code: code},
                success: function (data) {
                    //注册成功跳转页面
                    if (data.code == 200) {
                        window.location.href = '/p2p/user/realName';
                    } else {
                        showError("phone", "注册失败,请稍候再试");
                    }
                }
            })

        }
    })
}

function checkCode() {
    $("#messageCode").focus(function () {
        hideError("messageCode");
    })

}

function checkPhoneExists(phone) {
    $.ajax({
        url: "/p2p/user/phone/" + phone,
        type: "get",
        success: function (data) {
            if (data.code == 200) {
                showSuccess("phone");
            } else {
                showError("phone", data.message);
            }
        }
    })
}

//验证码相关处理

function CodeBtnEvent() {
    $("#messageCodeBtn").on("click", function () {
        $("#phone").blur();
        $("#loginPassword").blur();
        if ($("div[id$=Err]").text()) {
            return;
        }
        //请求发送验证码
        var phone = $.trim($("#phone").val());
        $.ajax({
            url: "/p2p/user/code/" + phone,
            type: "get",
            success: function (data) {
                if (data.code == 200) {
                    countTime();
                } else {
                    showError("messageCode", "系统忙")
                }
            },
            error: function (data) {
                showError("messageCode", "系统忙,请稍候重试");
            }
        })


    })
}

function countTime() {
    if ($("#messageCodeBtn").hasClass("on")) {
        return;
    }
    $.leftTime(180, function (d) {
        $("#messageCodeBtn").addClass("on");
        $("#messageCodeBtn").text(parseInt(d.m) * 60 + parseInt(d.s) + "秒后失效");
        if (!d.status) {
            $("#messageCodeBtn").removeClass("on");
            $("#messageCodeBtn").text("获取验证码");
        }
    })
}

function checkPhoneCode() {

    $("#messageCode").focus(function () {
        hideError("messageCode");
    })

    $("#messageCode").blur(function () {
        if (!$("#messageCodeBtn").hasClass("on")) {
            return;
        }
        $("#phone").blur();
        $("#loginPassword").blur();
        if ($("div[id$=Err]").text()) {
            showError("messageCode", "请检查注册信息");
            return;
        }
        var code = $.trim($(this).val());
        if (!code) {
            showError("messageCode", "验证码不能为空");
            return;
            return;
        } else if (code.length != 6) {
            showError("messageCode", "请输入六位长度的验证码");
            return;
        } else {
            var phone = $.trim($("#phone").val());
            $.ajax({
                url: "/p2p/user/code/" + phone + "/" + code,
                type: "get",
                success: function (data) {
                    if (data.code == 200) {
                        showSuccess("messageCode");
                    } else {
                        showError("messageCode", data.message);
                    }
                }
            })
        }

    })

}