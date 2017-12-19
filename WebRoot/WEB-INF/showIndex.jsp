<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach items="${ars }" var="a">
		<div style="width: 100%;height: 100px;">
			<div style="height: 20px;">
				<a href=${a.url }>${a.title }</a>
			</div>
			<div style="height: 80px;overflow: scroll;">
				${a.content }
			</div>
		</div>
	</c:forEach>
</body>
</html>