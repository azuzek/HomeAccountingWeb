<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul>
	<c:forEach var="category" items="${categories}">
		<c:set var="children" value="${category.children}"/>
		<c:set var="activeAccounts" value="${category.activeAccounts}"/>
		<li>
			${category.name}
			<a href="AccountsController/add?parentId=${category.id}" title="Agregar cuenta">+</a>
			<a href="CategoriesController/add?parentId=${category.id}" title="Agregar categoria">+</a>
			<c:if test="${empty children && empty activeAccounts && !category.fixed}">
				<a href="CategoriesController/delete?id=${category.id}" title="Eliminiar categoria">-</a>
			</c:if>
			<c:if test="${!empty activeAccounts}">
				<ul>
				<c:forEach var="account" items="${activeAccounts}">
					<li>
						${account.name}
						<a href="AccountsController/edit?accountId=${account.id}">editar</a>
						<a href="AccountsController/deactivate?accountId=${account.id}" title="Desactivar cuenta">-</a>
					</li>
				</c:forEach>
				</ul>
			</c:if>
			<c:if test="${!empty children}">
    			<c:set var="categories" value="${children}" scope="request"/>
    			<jsp:include page="CategoriesTree.jsp"/>
			</c:if>
		</li>
	</c:forEach>
</ul>