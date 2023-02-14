//当前页码
var pageNum = 1;
//首页页码
var firstPage = 1;
//最后一页页码
var lastPage = 1;
//导航分页数组
var navArray = new Array();
var postFlag = false;

$(function () {
    forwardAjax();
    blurCheckBinding();
    processCheckAll();
    //发起查询部门信息的ajax请求
    $("#addBtn").click(function () {
        $("#addModel").modal(true);
        $("#formData")[0].reset();
        forwardAjaxQueryDept("query");
    })
    //发起添加员工信息的ajax请求
    $("#addEmp").click(function () {
        // judgeCanForwardAjaxPostEmp()
        if (postFlag){
            forwardAjaxAddEmp();
        }else{
            alert("用户名重复,不允许添加");
        }
    })

//    发起修改用户信息的ajax请求
    $("#editEmp").click(function () {
        forwardAjaxUpdateEmpInfo();
        $("#editModel").modal("hide");
        forwardAjax();
    })



})

/**
 * 发起Ajax请求
 */
function forwardAjax() {
    $.ajax({
        url:"/emps",
        data:{"pageNum":pageNum},
        type:"GET",
        success:function (responseJson) {
            $("#checkAll").prop('checked',false);
            pageNum = responseJson.map.page.pageNum;
            lastPage = responseJson.map.page.pages;
            updateEmpBody(responseJson.map.page.list);
            resolveNavigatePages(responseJson.map.page.navigatepageNums,responseJson.map.page.hasPreviousPage,responseJson.map.page.hasNextPage);
            processPageInfo(responseJson.map.page.total);

        }
    })
}

/**
 * 更新员工信息
 * @param data
 */
function updateEmpBody(data) {
    var empBody = $("#empBody");
    empBody.empty();
    var  html = "";
    $.each(data,function (index,element) {
        var ele = "<tr><td>"+"<input type='checkbox' class='delEmpBox' name='delEmpBox' value='"+element.id+"' />"+"</td>";
        ele += ""+"<td>"+element.id+"</td>"+
        "<td>"+element.empName+"</td>"+"<td>"+element.empGender+"</td>"+
        "<td>"+element.empAge+"</td>"+"<td>"+element.dept.deptName+"</td>"
        +"";
        var editBtn = "<td><button type=\"button\" empid='"+element.id+"' class=\"btn btn-default  btn-sm edit-btn\">\n" +
            "    <span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">修改</span> \n" +
            "</button> </td>";
        var deleteBtn = "<td><button type=\"button\" emp_id='"+element.id+"' class=\"btn btn-default btn-sm del-btn \">\n" +
            "    <span class=\"glyphicon glyphicon-minus\" aria-hidden=\"true\">删除</span>\n" +
            "</button></td> </tr>";
        ele += editBtn;
        ele += deleteBtn;
        html += ele;
    })

    //修改按钮的时间
    empBody.append(html);
    //为所有的复选框绑定单击事件
    $(".delEmpBox").each(function (index,element) {
        $(this).on("click",function () {
            if ($(".delEmpBox:checked").length == $(".delEmpBox").length){
                $("#checkAll").prop("checked",true);
            }
            // else{
            //     $("#checkAll").prop("checked",false);
            // }
            // let id = $(this).val();
        })

    })

    $(".edit-btn").each(function (index,element) {
        $(this).click(function () {
            var attr = $(this).attr("empid");
            $("#editEmp").attr("emp_id",attr);
            processEditEvent(attr);
        })
    })
    //删除按钮的事件
    $(".del-btn").each(function (index, element) {
        $(this).on("click",function () {
            let empId = $(this).attr("emp_id");
            let empName = $(this).parents("tr").find("td:eq(2)").text();
            batchDeleteEmp({empId},empName);
        })
    })

}

/**
 * 处理导航分页信息
 * @param data
 */
function resolveNavigatePages(data,flag,status) {
    var navArea = $("#navArea");
    navArea.empty();
    var comment = "";
    // <button id="firstPage">首页</button>
    navArea.append("<li id='pre'><a href='javascript:void(0);' id='firstPage'>首页</a></li>");
    if (pageNum ==1 ){
        $("#pre").addClass("disabled");
    }
    $("#firstPage").click(function () {
        pageNum = 1 ;
        forwardAjax();
    })
    navArea.append(" <li >\n" +
        "      <a href=\"#\" id='prePage' aria-label=\"Previous\">\n" +
        "        <span aria-hidden=\"true\">&laquo;</span>\n" +
        "      </a>\n" +
        "    </li>");

    $("#prePage").click(function () {
       if (pageNum!=1){
           pageNum = pageNum -1 ;
           forwardAjax();
       }
    })
    if (!flag){
            $("#prePage").addClass("disabled");
    }
    if (pageNum == 1){
        $("#firstPage").addClass("disabled");
    }
    $.each(data,function (index,element) {
        if(pageNum == element){
            var ele = "<li class='active'><a href='javascript:void(0);'>"+element+"</a></li>";
        }else{
            var ele = "<li><a href='javascript:void(0);'>"+element+"</a></li>";
        }
        navArea.append(ele);
    });
    navArea.append("<li >\n" +
        "      <a href=\"#\" id='nextPage' aria-label=\"Next\">\n" +
        "        <span aria-hidden=\"true\">&raquo;</span>\n" +
        "      </a>\n" +
        "    </li>");
    $("#nextPage").click(function () {
      if (pageNum!=lastPage){
          pageNum = pageNum+1;
          forwardAjax();
      }
    })
   if (!status){
       $("#nextPage").addClass("disabled");
   }

    // <button id="lastPage">尾页</button>
   var btn2 = "<li id='last'><a href='javascript:void(0);' id='lastPage'>尾页</a> </li>";
   navArea.append(btn2);
   $("#lastPage").click(function () {
       pageNum = lastPage;
       forwardAjax();
   })
    if (pageNum == lastPage){
        $("#last").addClass("disabled");
    }
   $("a").each(function () {
       $(this).click(function () {
           pageNum = $(this).text();
           forwardAjax();
       })
   })


}

function processPageInfo(total) {
    var info = $("#pageInfo");
    info.empty();
    info.html("<font>"+"当前页码: "+pageNum+",共有"+lastPage+"页,"+"共有"+total+"条数据"+"</font>")
    info.css("color","blue");
}



/**
 * 数据模板
 * {"code":200,"message":"请求处理成功","map":{"page":{"total":2373,"list":[{"id":987,"empName":"ddd45","empAge":44,"empGender":"女","did":null,"dept":{"deptId":3,"deptName":"快递","empList":null}},{"id":988,"empName":"db016","empAge":2,"empGender":"男","did":null,"dept":{"deptId":1,"deptName":"零担","empList":null}},{"id":989,"empName":"5bc6d","empAge":43,"empGender":"女","did":null,"dept":{"deptId":1,"deptName":"零担","empList":null}},{"id":990,"empName":"4b180","empAge":15,"empGender":"男","did":null,"dept":{"deptId":3,"deptName":"快递","empList":null}},{"id":991,"empName":"c14dd","empAge":37,"empGender":"女","did":null,"dept":{"deptId":1,"deptName":"零担","empList":null}},{"id":992,"empName":"a31e6","empAge":10,"empGender":"男","did":null,"dept":{"deptId":1,"deptName":"零担","empList":null}},{"id":993,"empName":"ecb9f","empAge":17,"empGender":"女","did":null,"dept":{"deptId":3,"deptName":"快递","empList":null}},{"id":994,"empName":"a38c2","empAge":41,"empGender":"男","did":null,"dept":{"deptId":1,"deptName":"零担","empList":null}},{"id":995,"empName":"ba5f3","empAge":11,"empGender":"女","did":null,"dept":{"deptId":1,"deptName":"零担","empList":null}},{"id":996,"empName":"8f0c5","empAge":24,"empGender":"男","did":null,"dept":{"deptId":3,"deptName":"快递","empList":null}}],"pageNum":99,"pageSize":10,"size":10,"startRow":981,"endRow":990,"pages":238,"prePage":98,"nextPage":100,"isFirstPage":false,"isLastPage":false,"hasPreviousPage":true,"hasNextPage":true,"navigatePages":5,"navigatepageNums":[97,98,99,100,101],"navigateFirstPage":97,"navigateLastPage":101}}}
 */

//查询所有的部门信息

function forwardAjaxQueryDept (status) {
    $.ajax({
        url: "dept",
        type: "GET",
        success:function (result) {
            if (status === "query"){
                processSelectOption(result.map.dept);
            }else if (status === "edit"){
                showDept(result.map.dept);
            }

        }
    })
}
//为模态窗口中的下拉列表添加数据

function processSelectOption(dept) {
    var deptOption = $("#deptOption");
    deptOption.empty();
    $.each(dept,function (index,element) {
        deptOption.append("<option value='"+element.deptId+"'>"+element.deptName+"</option>");
    })
}

//添加用户信息
function forwardAjaxAddEmp() {

   var txt = $("#formData").serialize();
   $.ajax({
       url:"emp",
       type:"POST",
       data: txt,
       success:function (result) {
           if (result.code ==200){
               $("#addModel").modal("hide");
               pageNum=lastPage+1;
               forwardAjax();
           }else{
               if (result.map.responseMessage.empAge != undefined){
                   $("#ageErrorTip").text(result.responseMessage.empAge);
               }else if (result.map.responseMessage.empName != undefined){
                   $("#nameErrorTip").text(result.map.responseMessage.empName);
               }
           }

       }
   })

   // txt = decodeURIComponent(txt,true)
   //  alert(txt);

}
//姓名和年龄输入框的blur事件,当输入框失去焦点就立即进行校验

function blurCheckBinding() {
    $("#inputName").blur(function () {
        checkName();
    });
    $("#inputAge").on("blur",function () {
        checkAge();
    })
}


// 姓名 和  年龄的校验,注意校验的顺序,先前端,前端通过后再进行后端的校验

var regexName = /[\u4e00-\u9fa5]{2,5}/;
function checkName() {
    $("#nameTip").removeClass("has-success has-error");
    var nameVal = $("#inputName").val();
    nameVal = nameVal.trim();
    if (nameVal == ""){
        $("#nameTip").addClass("has-error");
        $("#nameErrorTip").text("姓名不允许为空");
        return;
    }
    var flag = regexName.test(nameVal);
    console.log("姓名判断的结果 "+flag)
    if (!flag){
        $("#nameTip").addClass("has-error");
        $("#nameErrorTip").text("姓名格式不正确");
    }else{
        $("#nameTip").removeClass("has-success has-error");
        $("#nameErrorTip").text("");
        checkNameIsDistinct();
    }

}


function checkAge() {
    $("#ageTip").removeClass("has-success has-error");
    var ageVal = $("#inputAge").val();
    ageVal = ageVal.trim();
    if (ageVal == "" || ageVal == "0"){
        $("#ageTip").addClass("has-error")
        $("#ageErrorTip").text("年龄输入有误,请检查输入的数据是否合法");
        return;
    }

    if (this.isNaN(ageVal)){
        $("#ageTip").addClass("has-error")
        $("#ageErrorTip").text("年龄不能是不支持的非数字类型")
    } else{
        ageVal = Number.parseInt(ageVal);
        if (ageVal >100 ||ageVal <3){
            $("#ageTip").addClass("has-error")
            $("#ageErrorTip").text("输入的年龄范围有误");
            return;
        }
        $("#ageTip").removeClass("has-error has-success");
        $("#ageErrorTip").text("");
    }
    ageVal = Number.parseInt(ageVal);
}

//ajax发起的姓名是否重复的校验
function checkNameIsDistinct() {
    // allInputBlur();
    // var comment  = "";
    // comment = $("#nameErrorTip").text()+$("#ageErrorTip").text();
    // if (comment != ""){
    //     return ;
    // }
    //发起这个ajax请求的前提是,用户的数据已经通过前端的校验,否则不允许发起请求
    var empName = $("#inputName").val();
    $.ajax({
        url:"http://localhost:8080/empNameQuery",
        data:{"empName":empName},
        type:"GET",
        success:function (data) {
            if (data.code == 200){
                $("#nameErrorTip").text("用户名可以注册");
                postFlag = true;
            }else{
                $("#nameErrorTip").text("该用户名已经被注册");
                postFlag = false;
            }
        }
    })
}

function allInputBlur() {
    $("#inputName")[0].focus();
    $("#inputName")[0].blur();
    $("#inputAge")[0].focus();
    $("#inputAge")[0].blur();
}


// function judgeCanForwardAjaxPostEmp() {
//     var comment  = "";
//     allInputBlur();
//     comment = $("#nameErrorTip").text();
//     comment = comment + $("#ageErrorTip").text();
//     console.log(comment);
//     comment = comment.trim();
//     if (comment == "" || comment == "用户名可以注册"){
//         postFlag = true;
//         return ;
//     }
//     postFlag = false;
// }


//处理每一个员工的修改和删除事件

//处理员工的修改事件 empid 为按钮添加的属性,用来记录当前操作的员工的id信息
function processEditEvent(empid) {

//    打开模态框
    $("#editModel").modal(true);
//    发起ajax请求,渲染部门信息
    forwardAjaxQueryDept("edit");
//    回显员工信息
    forwardAjaxQueryEmpInfo(empid);
//    渲染员工信息


}

//渲染修改页面的部门信息

function showDept(dept) {
    var deptOption = $("#showDeptOption");
    deptOption.empty();
    $.each(dept,function (index,element) {
        deptOption.append("<option value='"+element.deptId+"'>"+element.deptName+"</option>");
    })

}
//查询员工信息

function forwardAjaxQueryEmpInfo(empId) {
    if (empId != null && empId != undefined){
        $.ajax({
            url:"emp/"+empId,
            type:"GET",
            success:function (result) {
                console.log(result);
                if (result.code == 200){
                   renderEmp(result.map.emp);
                }else{
                    alert("服务器出故障了");
                }
            }
        })
    }
}

function renderEmp(data) {
    $("#showEmpId").text(data.id);
    $("#showName").text(data.empName);
    $("#editEmpAge").val(data.empAge);
    // $("input:radio[name='editEmpGender']").each(function () {
    //     if ($(this).val() == data.empGender){
    //         $(this).attr("checked","true");
    //     }
    // })
    // $(".genderGroup").each(function () {
    //     if ($(this).val() == data.empGender){
    //         $(this).prop("checked",true);
    //     }
    // })

    if (data.empGender == "男"){
        $("#manSymbol").attr('checked','checked');
    }else{
        $("#womanSymbol").attr('checked','checked');
    }
    var rest = "[value = '"+data.dept.deptId+"']";
    $("#showDeptOption").find(rest).attr('selected',true);
    
}

function forwardAjaxUpdateEmpInfo() {
    $.ajax({
            url:"emp",
            data:$("#editEmpFormData").serialize()+"&_method=put&id="+$("#editEmp").attr("emp_id"),
            type:"POST",
            success:function (responseComment) {
                alert(responseComment.message);
            }
    })

}


//复选框全选和取消全选的函数
function processCheckAll() {
    $("#checkAll").on("click",function () {
        if ($(".delEmpBox:checked").length == $(".delEmpBox").length){
            $(".delEmpBox").each(function () {
                $(this).prop("checked",false);
            })
        }else {
            $(".delEmpBox").each(function () {
                $(this).prop("checked",true);
            })
        }

    })
    $("#delBtn").click(function () {
        var index = 0;
        var idArray = [];
        var empName = "";
        $(".delEmpBox:checked").each(function () {
            empName += $(this).parents("tr").find("td:eq(2)").text()+",";
            idArray[index++] = $(this).val();
        })
        empName = empName.substring(0,empName.length-1);
        batchDeleteEmp(idArray,empName);
        return;
        // forwardAjaxDeleteSingleEmp();
    })
}

function batchDeleteEmp(idsArray,empName) {
    if (!confirm("是否确认删除"+empName+"的信息")){
        return ;
    }
    var  ids = "";
    $.each(idsArray,function (index, element) {
        ids += element;
        ids += "-";
    })
    ids = ids.substring(0,ids.length-1);
    console.log(ids);
    $.ajax({
        url:"emp",
        data:{"ids":ids},
        type:"DELETE",
        success:function(result){
            alert(result.message);
            forwardAjax();
        }
    })

}

var resolveBug = ()=>{
    $("#checkAll").prop("checked",($(".delEmpBox:checked").length == $(".delEmpBox").length));
}

// logoutBtn = ()=>{
//     $("#logout").on("click",function () {
//         console.log("绑定了事件");
//         logout();
//     })
// }

// function logout() {
//     $.ajax({
//         url:"logout",
//         type:"GET",
//         success:()=>{}
//     })
// }
