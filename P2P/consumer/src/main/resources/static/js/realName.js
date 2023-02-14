$(function () {
    agree();
    checkPhone();
    checkName();
    checkID();
    sendCode();
    codeSyncCheck();
    realName();


});

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


//同意实名认证协议
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

var nameRegex = /[\u4e00-\u9fa5]{2,}/;

function checkName() {
    $("#realName").focus(function () {
        hideError("realName");
    })
    $("#realName").on("blur", function () {
        var name = $.trim($(this).val());
        if (!name) {
            showError("realName", "姓名项不可为空");
            return;
        } else if (!nameRegex.test(name)) {
            showError("realName", "请输入真实姓名");
            return;
        }
        showSuccess("realName");

    })


}

function checkPhoneExists(phone) {
    $.ajax({
        url: "/p2p/user/phone/" + phone,
        type: "get",
        success: function (data) {
            if (data.code != 200) {
                showSuccess("phone");

            } else {
                showError("phone", "请使用注册时使用的手机号进行实名认证");
            }
        }
    })
}

var idRegex = /[0-9Xx]{18}/;

function checkID() {
    $("#idCard").focus(function () {
        hideError("idCard");
    })

    $("#idCard").blur(function () {
        var id = $.trim($(this).val());
        if (!id) {
            showError("idCard", "身份证号不可为空");
            return;
        } else if (!idRegex.test(id)) {
            showError("idCard", "请输入有效的身份证号码");
            return;
        }
        showSuccess("idCard");
    })

}

//请求发送验证码
function sendCode() {

    $("#messageCodeBtn").click(function () {
        $("#phone").blur();
        $("#realName").blur();
        $("#idCard").blur();
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
        $.leftTime(5, function (d) {
            if (d.status) {
                $("#messageCodeBtn").addClass("on");
            }
            $("#messageCodeBtn").text((parseInt(d.m) * 60 + (d.s == "00" ? 0 : d.s)) + "后失效")
            if (!d.status) {
                $("#messageCodeBtn").removeClass("on");
                $("#messageCodeBtn").text("获取验证码");
            }
        })


    })
}

function codeSyncCheck() {

    $("#messageCode").focus(function () {
        hideError("messageCode");
    })
    $("#messageCode").blur(function () {
        if (!$("#messageCodeBtn").hasClass("on")) {
            return;
        }
        var code = $.trim($("#messageCode").val());
        var phone = $.trim($("#phone").val());
        if (!code) {
            return;
        }
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

    })


}

//发起实名认证请求
function realName() {
    $("#btnRegist").click(function () {
        $("#phone").blur();
        $("#realName").blur();
        $("#idCard").blur();
        var code = $.trim($("#messageCode").val());
        if (!code) {
            showError("messageCode", "验证码不可为空");
            return;
        }
        var content = $("div[id$=Err]").text();
        if (content) {
            return;
        }
        var data = $("#param").serialize();
        console.log(data)
        $.ajax({
            url: "/p2p/user/info/check",
            type: "put",
            data: data,
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = "/p2p"
                } else {
                    showError("messageCode", data.message);
                }
            }
        })


    })
}