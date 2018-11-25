<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Home APP1</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<form:form action="logout" method="post" modelAttribute="user">
    You are logged in
    <br>
    	<form:hidden path="username" value="${user.username }"/>
    	<form:button name="logout" value="logout">Logout</form:button>
    </form:form>
</body>
</html>
