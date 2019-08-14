<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery/jquery.min.js"></script>
<title>通知非共享会话的客户端应用身份已切换</title>
<script type="text/javascript">
$(document).ready(function(){
	$("form").submit();
});
</script>
</head>
<body>
	<c:forEach items="${clientUrls}" var="p">
		<iframe name="${p.frontLogoutUrl}" style="display: none;"></iframe>
		<form action="${p.frontLogoutUrl}" target="${p.frontLogoutUrl}" method="get">
			<input name="${p.paramName}" value="${p.paramValue}"/>
		</form>
	</c:forEach>
</body>
</html>