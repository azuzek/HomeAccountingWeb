<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.0/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
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
<script>
$( function() {
	$( "#datepicker" ).datepicker({dateFormat: "dd/mm/yy"});
} );
</script>
<script>
var calculatedAmountAccounts = ${calculatedAmountAccounts};

function accountSelected(account,quote) {
	var accountSelect = document.getElementById(account);
	var accountSelectValue = accountSelect.options[accountSelect.selectedIndex].value;
	if(calculatedAmountAccounts.indexOf(accountSelectValue) >= 0) {
		document.getElementById(quote).style.display = "inline";
	} else {
		document.getElementById(quote).style.display = "none";
	}
}

function debitAmountVisibility(account,creditAccounts,creditAmount) {
	var accountSelect = document.getElementById(account);
	var accountSelectValue = accountSelect.options[accountSelect.selectedIndex].value;
	if(creditAccounts > 1) {
		visibility = "inline";
	} else {
		if (calculatedAmountAccounts.indexOf(accountSelectValue) >= 0) {
			visibility = "inline";
		} else {
			visibility = "none";
		}
	}
	document.getElementById(creditAmount).style.display = visibility;
}
</script>
<jsp:useBean id="dbResources" scope="page" class="com.ha.database.DBResources"/>
<jsp:setProperty name="dbResources" property="haDataSource" value="${haDataSource}"/>
<jsp:useBean id="accountDAO" scope="page" class="com.ha.database.AccountDAO"/>
<jsp:setProperty name="accountDAO" property="dbResources" value="${dbResources}"/>
<jsp:setProperty name="accountDAO" property="user" value="${user}"/>
<jsp:useBean id="now" class="java.util.Date"/>
<c:if test="${empty accountsByDebit}">
	<c:set var="accountsByDebit" value="${user.accountsByDebitFrequency}" scope="session"/>
</c:if>
<c:if test="${empty accountsByCredit}">
	<c:set var="accountsByCredit" value="${user.accountsByCreditFrequency}" scope="session"/>
</c:if>
<c:choose>
	<c:when test="${empty param.debitAccounts}">
		<c:set var="debitAccounts" value="1"/>
	</c:when>
	<c:otherwise>
		<c:set var="debitAccounts" value="${param.debitAccounts}"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty param.debitAccounts}">
		<c:set var="creditAccounts" value="1"/>
	</c:when>
	<c:otherwise>
		<c:set var="creditAccounts" value="${param.creditAccounts}"/>
	</c:otherwise>
</c:choose>
<c:url var="entryControllerUrl" value="/EntryController"/>
<jsp:include page="Menu.jsp"/>
<form action="${entryControllerUrl}">
	<p>Descripcion</p>
	<input type="text" name="description" value="${description}">
	<p>Felcha</p>
	<c:choose>
	<c:when test="${empty date}">
		<input type="text" name="date" id="datepicker" value=<fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/>>
	</c:when>
	<c:otherwise>
		<input type="text" name="date" id="datepicker" value="${date}">
	</c:otherwise>
	</c:choose>
	<table>
		<tr>
			<td>Debe</td>
			<td>
				<c:url var="addDebitUrl" value="/EntryController/addDebit?debitAccounts=${debitAccounts}&creditAccounts=${creditAccounts}"/>
				<a href="${addDebitUrl}">+</a>
				<c:if test="${debitAccounts>1}">
					<c:url var="deleteDebitUrl" value="/EntryController/deleteDebit?debitAccounts=${debitAccounts}&creditAccounts=${creditAccounts}"/>
					<a href="${deleteDebitUrl}">-</a>
				</c:if>
			</td>
		</tr>
		<c:forEach var="order" begin="1" end="${debitAccounts}">
			<tr>
				<td>
					<select id="debitAccount${order}" name="debitAccount${order}" onchange="accountSelected('debitAccount${order}','debitQuote${order}')">
						<c:forEach var="account" items="${accountsByDebit}">
							<c:if test="${empty debitQuoteDisplay}">
								<c:set var="debitQuoteDisplay" value="${account.localCurrency? 'none' : 'inline'}"/>
							</c:if>
							<option value="${account.id}">${account.name}</option>
						</c:forEach>
					</select>
					<div id="debitQuote${order}" style="display: ${debitQuoteDisplay}"><br>
						Cantidad: <input type="text" name="debitQuoteQuantity${order}" onkeypress='filterNumbersAndDecimalSeparators(event)'><br>
						Precio Unitario: <input type="text" name="debitQuoteUnitPrice${order}" onkeypress='filterNumbersAndDecimalSeparators(event)'>
					</div>
				</td>
				<td valign="top">
					<input type="text" name="debitAmount${order}" onkeypress='filterNumbersAndDecimalSeparators(event)' value="${debitAmountValues[order - 1]}">
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td>Haber</td>
			<td>
				<c:url var="addCreditUrl" value="/EntryController/addCredit?debitAccounts=${debitAccounts}&creditAccounts=${creditAccounts}"/>
				<a href="${addCreditUrl}">+</a>
				<c:if test="${creditAccounts>1}">
					<c:url var="deleteCreditUrl" value="/EntryController/deleteCredit?debitAccounts=${debitAccounts}&creditAccounts=${creditAccounts}"/>
					<a href="${deleteCreditUrl}">-</a>
				</c:if>
			</td>
		</tr>
		<c:forEach var="order" begin="1" end="${creditAccounts}">
			<tr>
				<td>
					<select id="creditAccount${order}" name="creditAccount${order}" onchange="accountSelected('creditAccount${order}','creditQuote${order}'); debitAmountVisibility('creditAccount${order}','${creditAccounts}','creditAmount${order}')">
						<c:forEach var="account" items="${accountsByCredit}">
							<c:if test="${empty creditQuoteDisplay}">
								<c:set var="creditQuoteDisplay" value="${account.localCurrency? 'none' : 'inline'}"/>
							</c:if>
							<option value="${account.id}">${account.name}</option>
						</c:forEach>
					</select>
						<div id="creditQuote${order}" style="display: ${creditQuoteDisplay}"><br>
							Cantidad: <input type="text" name="creditQuoteQuantity${order}" onkeypress='filterNumbersAndDecimalSeparators(event)'><br>
							Precio Unitario: <input type="text" name="creditQuoteUnitPrice${order}" onkeypress='filterNumbersAndDecimalSeparators(event)'>
						</div>
				</td>
				<td valign="top">
					<c:if test="${empty creditAmountDisplay}">
						<c:set var="creditAmountDisplay" value="${creditAccounts>1 or !account.localCurrency? 'inline' : 'none'}"/>
					</c:if>
					<input type="text" id="creditAmount${order}" name="creditAmount${order}" style="display: ${creditAmountDisplay}" onkeypress='filterNumbersAndDecimalSeparators(event)' value="${creditAmountValues[order - 1]}">
				</td>
			</tr>
		</c:forEach>
	</table>
	<p class="error">${errorMessage}</p>
	<input type=hidden name="debitAccounts" value="${debitAccounts}">
	<input type=hidden name="creditAccounts" value="${creditAccounts}">
	<input type="submit" value="OK">
	<input type="submit" value="Cancel">
</form>