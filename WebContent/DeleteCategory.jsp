<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Menu.jsp"/>
<c:url var="deleteCategoryControllerUrl" value="/DeleteCategoryController"/>
${message}<br>
<form action="${deleteCategoryControllerUrl}">
<input type="hidden" name="id" value="${id}">
<input type="submit" name="ok" value="OK"><br>
<input type="submit" name="cancel" value="Cancel"><br>
</form>