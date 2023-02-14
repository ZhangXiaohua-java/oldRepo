<%--
  Created by IntelliJ IDEA.
  User: 33233
  Date: 2022/4/9
  Time: 22:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>员工信息</title>
    <script src="static/js/jquery-3.4.1.min.js" type="text/javascript"></script>
    <script src="static/bootstrap-3.4.1/dist/js/bootstrap.js" type="text/javascript"></script>
    <link href="static/bootstrap-3.4.1/dist/css/bootstrap.css" rel="stylesheet"/>
    <link href="static/css/empList.css" rel="stylesheet">
    <style type="text/css">
        *{
            padding: 0px;
            margin:  0px;
        }
        #infoBody{
            margin-left: 50px;
        }
        #infoHead{
            margin-left: 50px;
        }
    </style>
</head>
<body>
<div class="row"><h1 id="t1">员工信息列表</h1></div>

<div class="row">
    <div class="col-md-2-offset-10"><button class="btn btn-default small">添加</button></div>
    <div class="col-md-2-offset-12"><button class="btn btn-danger small">删除</button></div>
</div>
<div class="row">
    <div class="col-md-2">员工编号</div>
    <div class="col-md-2">员工姓名</div>
    <div class="col-md-2">员工年龄</div>
    <div class="col-md-2">员工性别</div>
    <div class="col-md-2">员工部门</div>
    <div class="col-md-2"></div>
</div>
<div class="row" id="infoBody">
    <table class="table table-hover" id="infoHead">
        <c:forEach items="${info.list}" var="emp" >
            <tr>
                <div class="col-md-4"><td>${emp.id}</td></div>
                <div class="col-md-2"><td>${emp.empName}</td></div>
                <div class="col-md-2"><td>${emp.empAge}</td></div>
                <div class="col-md-2"><td>${emp.empGender}</td></div>
                <div class="col-md-2"><td>${emp.dept.deptName}</td></div>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            </tr>
        </c:forEach>
    </table>
</div>

<div class="row">
    <nav aria-label="Page navigation">
        <ul class="pagination">
            <c:if test="${!info.hasPreviousPage}">
                <li class="disabled"><a href="javascript:void(0)" >首页</a></li>
            </c:if>
            <c:if test="${!info.isFirstPage}">
                <li><a href="${pageContext.request.contextPath}/emp">首页</a></li>
            </c:if>

           <c:if test="${info.hasPreviousPage}">
               <li>
                   <a href="${pageContext.request.contextPath}/emp?emp=${info.pageNum-1}" aria-label="Previous">
                       <span aria-hidden="true">&laquo;</span>
                   </a>
               </li>
           </c:if>
            <c:if test="${!info.hasPreviousPage}">
                <li class="disabled">
                    <a href="javascript:void(0)" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:forEach items="${info.navigatepageNums}" var="num">
                <c:if test="${info.pageNum==num}">
                    <li class="active"><a  href="${pageContext.request.contextPath}/emp?pageNum=${num}">${num}</a></li>
                </c:if>
               <c:if test="${info.pageNum !=num }">
                   <li><a href="${pageContext.request.contextPath}/emp?pageNum=${num}">${num}</a></li>
               </c:if>
            </c:forEach>
            <li>
               <c:if test="${info.hasNextPage}">
                   <a href="${pageContext.request.contextPath}/emp?pageNum=${info.pageNum+1}" aria-label="Next">
                       <span aria-hidden="true">&raquo;</span>
                   </a>
               </c:if>
                <c:if test="${!info.hasNextPage}">
                    <li class="disabled">
                    <a href="javascript:void(0)" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                    </li>
                </c:if>
            </li>
            <c:if test="${info.isLastPage}">
                <li class="disabled"><a href="javascript:void(0)">尾页</a></li>
            </c:if>
            <c:if test="${!info.isLastPage}">
                <li ><a href="${pageContext.request.contextPath}/emp?pageNum=${info.pages}">尾页</a></li>
            </c:if>
        </ul>
</div>



</body>
</html>
