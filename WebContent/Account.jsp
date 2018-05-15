<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="dbResources" scope="page" class="com.ha.database.DBResources"/>
<jsp:setProperty name="dbResources" property="haDataSource" value="${haDataSource}"/>
<jsp:useBean id="instrumentDAO" scope="page" class="com.ha.database.InstrumentDAO"/>
<jsp:setProperty name="instrumentDAO" property="dbResources" value="${dbResources}"/>
<jsp:setProperty name="instrumentDAO" property="user" value="${user}"/>
<jsp:include page="Menu.jsp"/>
<c:url var="accountControllerUrl" value="/AccountController?operation=${operation}"/>
<form action="${accountControllerUrl}">
	Nombre:<input type="text" name="name" value="${name}"><br>
	<input type="hidden" name="type" value="${parentCategory.type.code}"/>
	<input type="hidden" name="parentId" value="${parentCategory.id}">
	<input type="hidden" name="accountId" value="${accountId}">
	Categoria:
	<select name="category" <c:if test="${operation eq 'add' }">disabled</c:if>>
		<c:forEach var="category" items="${categories}">
			<option value="${category.id}" <c:if test="${parentCategory.id eq category.id}">selected="selected"</c:if>>${category.name}</option>
		</c:forEach>
	</select><br>
	Saldo:<input type="text" name="balance" value="${balance}"  onkeypress='filterNumbersAndDecimalSeparators(event)' <c:if test="${operation eq 'edit' }">disabled</c:if>><br>
	Moneda local:<br>
		<input type="radio" name="isLocalCurrency" value="Yes" checked>Si<br>
		<input type="radio" name="isLocalCurrency" value="No">No<br>
	Instrumento:
	<select name="instrumentId">
		<option value="new">Crear nuevo</option>
		<c:forEach var="instrument" items="${instrumentDAO. }">
		</c:forEach>
		<option value="1">Dolar</option>
		<option value="2">Bono Discount</option>
		<option value="3">Letras del tesoro</option>
	</select><br>
	Nombre: <input type="text" name="instrumentName"/><br>
	Código: <input type="text" name="instrumentCode"/><br>
	Price:<br>
		<input type="radio" name="isSinglePrice" value="Yes" checked>Precio Único<br>
		<input type="radio" name="isSinglePrice" value="No">Precio de Compra y de Venta<br>
	<input type="hidden" name="operation" value="${operation}">
	<input type="submit" name="ok" value="Ok"><br>
	<input type="submit" name="cancel" value="Cancel">
</form>
<p class="error">${errorMessage}</p>
<script type="text/javascript">
function filterNumbersAndDecimalSeparators(evt) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	key = String.fromCharCode( key );
	var regex = /[0-9]|\.|\,/;
	if( !regex.test(key) ) {
		theEvent.returnValue = false;
		if(theEvent.preventDefault) theEvent.preventDefault();
	}
}
</script>