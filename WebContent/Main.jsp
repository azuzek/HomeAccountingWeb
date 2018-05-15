<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">   -->
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Style.css">
<c:if test="${empty action}">
	<c:set var="action" value="login" scope="request"/>
</c:if>
<title>Contabilidad Casera</title>
</head>
<body>
<jsp:include page="Header.jsp"/>
<c:choose>
	<c:when test="${action eq 'login'}"><jsp:include page="Login.jsp"/></c:when>
	<c:when test="${action eq 'entry'}"><jsp:include page="Entry.jsp"/></c:when>
	<c:when test="${action eq 'accounts'}"><jsp:include page="Categories.jsp"/></c:when>
	<c:when test="${action eq 'account'}"><jsp:include page="Account.jsp"/></c:when>
	<c:when test="${action eq 'category'}"><jsp:include page="Category.jsp"/></c:when>
	<c:when test="${action eq 'deleteCategory'}"><jsp:include page="DeleteCategory.jsp"/></c:when>
	<c:when test="${action eq 'deactivateAccount'}"><jsp:include page="DeactivateAccount.jsp"/></c:when>
	<c:when test="${action eq 'journal'}"><jsp:include page="Journal.jsp"/></c:when>
</c:choose>
<jsp:include page="Footer.jsp"/>
</body>
</html>