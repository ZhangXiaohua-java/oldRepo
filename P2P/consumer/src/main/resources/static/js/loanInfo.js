$(function () {
    invest();
    checkMoney();

})

function closeit() {
    $("#failurePayment").hide();
    $("#dialog-overlay1").hide();
    window.location.href = "${pageContext.request.contextPath}/loan/myCenter";
}

function invest() {
    $("#investNow").click(function () {


    })
}

function checkMoney() {


    $("#bidMoney").focus(function () {
        $(".max-invest-money").text("");
    })

    $("#bidMoney").blur(function () {
        let rate = $(this).attr("rate");
        let leftProductMoney = $(this).attr("leftMoney");
        let bidMinLimit = parseInt($(this).attr("bidMinLimit"));
        let bidMaxLimit = parseInt($(this).attr("bidMaxLimit"));
        let productId = $(this).attr("pid");
        let bidMoney = parseInt($.trim($(this).val()));
        if (!bidMoney) {
            $(".max-invest-money").text("投资金额不能为空");
        } else if (isNaN(bidMoney) || bidMoney <= 0) {
            $(".max-invest-money").text("请输入有效的投资金额");
        } else if (bidMoney % 100 != 0) {
            $(".max-invest-money").text("投资金额必须是100的整数位");
        } else if (bidMoney > parseInt(bidMaxLimit)) {
            $(".max-invest-money").text("单笔投资最大允许可投金额为" + bidMaxLimit + "元");
        } else if (bidMoney < parseInt(bidMinLimit)) {
            $(".max-invest-money").text("单笔投资最小可投金额为" + bidMinLimit + "元");
        } else {
            let cycle = $("#cycle").attr("cycle");
            let pid = $("#cycle").attr("type");
            console.log(cycle)
            console.log(pid)
            if (pid == 1) {
                cycle *= 30;
                console.log("周期==" + cycle)
            }
            let exceptMoney = (rate / 365 / 100) * bidMoney * cycle;
            exceptMoney = Math.round(exceptMoney * 100) / 100;
            $("#exceptMoney").text(exceptMoney);
        }

    })

}