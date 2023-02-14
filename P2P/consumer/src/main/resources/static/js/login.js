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


var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
    try {
        if (window.opener) {
            // IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性
            referrer = window.opener.location.href;
        }
    } catch (e) {
    }
}

//按键盘Enter键即可登录
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        login();
    }
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
            showSuccess("phone");
        }
    })

    $("#phone").focus(function () {
        hideError("phone");
    })
}

function checkPassword() {
    $("#loginPassword").on("focus", function () {
        hideError("loginPassword");
    });

    $("#loginPassword").blur(function () {
        var password = $.trim($(this).val());
        if (!password || password.length < 6) {
            showError("loginPassword", "请输入有效的登录密码");
            return;
        }
        showSuccess("loginPassword");
    })
}

function sendCode() {

    $("#messageCode").focus(function () {
        hideError("messageCode");
    })
    $("#messageCodeBtn").click(function () {
        $("#phone").blur();
        $("#loginPassword").blur();
        var content = $("div[id$=Err]").text();
        if (content) {
            return;
        }
        if ($("#messageCodeBtn").hasClass("on")) {
            return;
        }
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
        $.leftTime(180, function (d) {
            if (d.status) {
                $("#messageCodeBtn").addClass("on");
            }
            $("#messageCodeBtn").text((parseInt(d.m) * 60 + (parseInt(d.s) == "00" ? 0 : parseInt(d.s))) + "秒后失效");
            if (!d.status) {
                $("#messageCodeBtn").removeClass("on");
                $("#messageCodeBtn").text("获取验证码");
            }


        })
    })
}

function login() {
    $("#loginBtn").click(function () {
        $("#phone").blur();
        $("#loginPassword").blur();
        var code = $.trim($("#messageCode").val());
        if (!code) {
            showError("messageCode", "验证码不能为空");
            return;
        }
        var content = $("div[id$=Err]").text();
        if (content) {
            return;
        }
        var passwd = $("#loginPassword").val();
        passwd = $.md5(passwd);
        $("#loginPassword").val(passwd);
        console.log($("#params").serialize());
        $.ajax({
            url: "/p2p/user/login",
            type: "post",
            data: $("#params").serialize(),
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = "/p2p";
                } else {
                    showError("messageCode", data.message);
                }
            }
        })


    })

}


//入口函数
$(function () {

    checkPhone();
    checkPassword();
    sendCode();
    login();

})
