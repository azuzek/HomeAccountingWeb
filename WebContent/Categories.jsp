<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Menu.jsp"/>
<c:set var="categories" value="${user.categories}" scope="request"/>
<jsp:include page="CategoriesTree.jsp"/>