<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>员工信息</title>
    <script src="static/js/jquery-3.4.1.min.js" type="text/javascript"></script>
    <script src="static/bootstrap-3.4.1/dist/js/bootstrap.js" type="text/javascript"></script>
    <link rel="stylesheet" href="static/bootstrap-3.4.1/dist/css/bootstrap.css">
    <script src="static/js/emp.js" type="text/javascript" ></script>
    <style>
        #t1{
            text-align: center;
        }
        #empTable{
            text-align: center;
        }

        #navBody{
            text-align: center;
        }

    </style>
</head>
<body>
<div class="row"><h1 id="t1">员工信息列表</h1></div>
<div class="row">

    <div  id="btnStyle">
        <tr>
       <td>
           <div class="col-md-4">
               <button type="button" class="btn btn-default btn-lg btn-primary" id="addBtn">
                   <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 添加
               </button>
           </div>
           <div class="col-md-4">
               <button type="button" class="btn btn-default btn-lg btn-danger" id="delBtn">
                   <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 删除
               </button>
           </div>
           <div class="col-md-4 ">
               <button type="button" class="btn btn-default btn-lg btn-danger" id="logout">
                   <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                   <a href="${pageContext.request.contextPath}/logout">退出登录</a>
               </button>
           </div>
       </td>
        </tr>

    </div>
</div>

<div class="row">
    <table id="empTable" class="table table-hover">

        <thead>
        <td><input type="checkbox" id="checkAll" >批量选取</td>
        <td>员工编号</td>
        <td>员工姓名</td>
        <td>员工年龄</td>
        <td>员工性别</td>
        <td>员工部门</td>
        <td></td>
        <td></td>
        </thead>
        <tbody id="empBody">

        </tbody>
    </table>
</div>
<div id="pageInfo">

</div>
<nav aria-label="Page navigation"  id="navBody">
    <ul class="pagination" id="navArea">
    </ul>
</nav>

<%--  模态窗口 --%>
<div id="addModel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="gridSystemModalLabel">员工添加</h4>
            </div>
            <div class="modal-body">
                <!--            模态body      -->

                <form class="form-horizontal" id="formData">
                    <div class="form-group has-error">
                        <label for="inputName" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10 " id="nameTip">
                            <input type="text" class="form-control" name="empName" id="inputName" placeholder="姓名">
                            <span class="help-block" id="nameErrorTip"></span>
                        </div>
                    </div>

                    <div class="form-group" >
                        <label for="inputAge" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10" id="ageTip">
                            <input type="text" class="form-control"  name="empAge" id="inputAge" placeholder="年龄">
                            <span class="help-block" id="ageErrorTip"></span>
                        </div>
                    </div>
                    <!--                    性别单选框        -->
                    <div class="radio">
                        <label>
                            <input type="radio" name="empGender" id="genderOption1" value="男" checked> 男
                        </label>
                    </div>

                    <div class="radio">
                        <label>
                            <input type="radio" name="empGender" id="genderOption2" value="女"> 女
                        </label>

                    </div>
                    <!--                    部门下拉列表              -->
                    <div id="deptInfo">请选择对应的部门编号</div>
                    <select class="form-control" id="deptOption" name="did">

                    </select>

            </form>
                    <!--                模态Body结束-->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="addEmp">提交</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<%--修改员工信息的模态框--%>

<%--<button type="button" class="btn btn-default btn-lg" id="editBtn">--%>
<%--    <span class="glyphicon glyphicon-minus" aria-hidden="true">删除</span>--%>
<%--</button>--%>
<div id="editModel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" >员工信息修改</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="editEmpFormData">
<%--                    姓名调整为只读状态,不允许修改     --%>

<%--                    <div class="form-group has-error">--%>
<%--                        <label for="showEmpName" class="col-sm-2 control-label">姓名</label>--%>
<%--                        <div class="col-sm-10 ">--%>
<%--                            <input type="text" class="form-control" name="empName" id="showEmpName" placeholder="姓名">--%>
<%--                        </div>--%>
<%--                    </div>--%>

                    <label class="col-sm-2 control-label">员工ID</label>
                    <div class="col-sm-10">
                        <p class="form-control-static" id="showEmpId"></p>
                    </div>

                    <label class="col-sm-2 control-label">姓名</label>
                    <div class="col-sm-10" >
                        <p class="form-control-static" id="showName"></p>
                    </div>
<%--     分隔符                --%>
                    <div class="form-group" >
                        <label for="inputAge" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10" id="editAgeTip">
                            <input type="text" class="form-control"  name="empAge" id="editEmpAge" placeholder="年龄">
                            <span class="help-block" id="editAgeErrorTip"></span>
                        </div>
                    </div>
                    <!--                    性别单选框        -->
                    <div class="radio genderGroup">
                        <label>
                            <input type="radio" name="empGender" id="manSymbol" value="男"> 男
                        </label>
                    </div>

                    <div class="radio genderGroup" >
                        <label>
                            <input type="radio" name="empGender" id="womanSymbol" value="女"> 女
                        </label>

                    </div>
                    <!--                    部门下拉列表              -->
                    <div id="editDeptInfo">请选择员工所属的部门</div>
                    <select class="form-control" id="showDeptOption" name="did">

                    </select>

                </form>
                <!--                模态Body结束-->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="editEmp">修改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->



</body>
</html>