<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="header">
<span>Contabilidad Casera</span>
<span id="span-user">
<c:if test="${!empty user}">
		${user.userId}
</c:if>
</span>
</div>