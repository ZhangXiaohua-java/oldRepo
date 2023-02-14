var username = "";
var password = "";
var flag = false;
var comment = "";
$(function () {
    bindingClickEvent();
    $("#loginBtn").on("click",function () {
        checkOutAllInfo();
    })


})

//登录前的校验
checkOutAllInfo = ()=>{
    flag =false;
    comment = "";
    $("#username").get(0).focus();
    $("#username").get(0).blur();
    $("#pwd").get(0).focus();
    $("#pwd").get(0).blur();
    comment += $("#usernameTip").text();
    comment += $("#passwordTip").text();
    if (!comment){
        flag = true;
    }
    forwardLogin();
}


//发起登录请求
forwardLogin = ()=>{
    if (!flag){
        alert("请检查输入的参数");
        return;
    }
    $.ajax({
        url:"login",
        data:$("#loginForm").serialize(),
        type:"POST",
        success:function () {

        }
    })

}

checkUserName = ()=>{
    username = "";
    username = $("#username").val();
    username = username.trim();
    if (username == ""){
        $("#usernameTip").text("用户名不能为空");
        return;
    }
    $("#usernameTip").text("");

}

checkPassword = ()=>{
    password = "";
    password = $("#pwd").val();
    password = password.trim();
    if (password == ""){
        $("#passwordTip").text("密码不允许为空");
        return;
    }
    $("#passwordTip").text("");

}



bindingClickEvent = ()=>{
    $(".loginInfo").each(function(){
        $(this).on("blur",function () {
            if ($(this).attr("type") == "text"){
                checkUserName();
            }else{
                checkPassword();
            }
        })
    })
}