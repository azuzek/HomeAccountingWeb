<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Menu.jsp"/>
<c:url var="categoryControllerUrl" value="/CategoryController"/>
<form action="${categoryControllerUrl}">
	<input type="hidden" name="id" value="${id}">
	<input type="hidden" name="parentId" value="${parentId}">
	Nombre de categoria: <input type="text" name="name"><br>
	<input type="submit" name="ok" value="Ok"><br>
	<input type="submit" name="cancel" value="Cancel">
</form>