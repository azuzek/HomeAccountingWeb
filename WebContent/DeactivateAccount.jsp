<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="Menu.jsp"/>
<c:url var="deactivateAccountControllerUrl" value="/DeactivateAccountController"/>
${message}<br>
Account Id: ${accountId}<br>
<form action="${deactivateAccountControllerUrl}">
<input type="hidden" name="accountId" value="${accountId}">
<input type="submit" name="ok" value="OK"><br>
<input type="submit" name="cancel" value="Cancel"><br>
</form>