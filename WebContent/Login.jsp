<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1>Ingrese sus datos</h1>
<form action="LoginController" method="post">
	Usuario:<br>
	<input type="text" name="userId" value="${param.userId}"><br>
	Contraseña:<br>
	<input type="password" name="password"><br>
	<input type="checkbox" name="rememberMe" value="true"> Recordarme<br>
	<input type="submit" name="ok" value="OK"><br>
	<input type="submit" name="cancel" value="Cancel"><br>
</form>
<p class="error">${errorMessage}</p>