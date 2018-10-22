<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Login APP1</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <form:form action="login" method="post" modelAttribute="user">
    	<form:errors path="username"></form:errors>
    	<br>
    	Username <form:input path="username" value="${user.username }"/>
    	<br>
    	<form:errors path="password"></form:errors>
    	<br>
    	Password <form:password path="password" value="${user.password }"/>
    	<br>
    	<form:button name="login" value="login">Login</form:button>
    </form:form>
</body>
</html>
