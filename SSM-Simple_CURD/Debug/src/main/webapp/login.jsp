<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <script src="static/js/jquery-3.4.1.min.js" type="text/javascript"></script>
    <script src="static/bootstrap-3.4.1/dist/js/bootstrap.js" type="text/javascript"></script>
    <link rel="stylesheet" href="static/bootstrap-3.4.1/dist/css/bootstrap.css">
    <script type="text/javascript" src="static/js/login.js"></script>
    <style type="text/css" rel="stylesheet">
        #formStyle{
            position: relative;
            margin:  390px;
            padding: 90px;
        }
    </style>
</head>
<body>


<div id="formStyle">
    <form action="${pageContext.request.contextPath}/login"
          method="post" id="loginForm">
        <div>${tip}</div>
        用户名: <input type="text" name="username" class="loginInfo" id="username"> <span id="usernameTip"></span> <br>
        密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="password" class="loginInfo" name="password" id="pwd"><span id="passwordTip"></span> <br>
        <button id="loginBtn">登录</button>
    </form>

</div>

<%--<form id="loginForm" >--%>
<%--    <div class="form-group">--%>
<%--        <label for="username">用户名</label>--%>
<%--        <input type="text" class="form-control loginInfo" name="username" id="username" placeholder="用户名">--%>
<%--        <span id="usernameTip"></span>--%>
<%--    </div>--%>
<%--    <div class="form-group">--%>
<%--        <label for="pwd">密码</label>--%>
<%--        <input type="password" class="form-control loginInfo" name="password" id="pwd" placeholder="密码">--%>
<%--        <span id="passwordTip"></span>--%>
<%--    </div>--%>
<%--    <button id="loginBtn" class="btn-default">登录</button>--%>
<%--</form>--%>



</body>
</html>
