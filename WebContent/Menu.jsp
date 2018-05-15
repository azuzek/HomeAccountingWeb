<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul class="horizontal-list">
	<li class="horizontal-list-item">
		<c:url var="menuUrl" value="/Menu?action=entry"/>
		<a class="menu-anchor<c:if test="${action eq 'entry'}"> active</c:if>" href="${menuUrl}">Asiento</a>
	</li>
	<li class="horizontal-list-item">
		<c:url var="menuUrl" value="/Menu?action=journal"/>
		<a class="menu-anchor<c:if test="${action eq 'journal'}"> active</c:if>" href="${menuUrl}">Diario</a>
	</li>
	<li class="horizontal-list-item">
		<c:url var="menuUrl" value="/Menu?action=ledger"/>
		<a class="menu-anchor<c:if test="${action eq 'ledger'}"> active</c:if>" href="${menuUrl}">Mayor</a>
	</li>
	<li class="horizontal-list-item">
		<c:url var="menuUrl" value="/Menu?action=recon"/>
		<a class="menu-anchor<c:if test="${action eq 'recon'}"> active</c:if>" href="${menuUrl}">Reconciliación</a>
	</li>
	<li class="horizontal-list-item">
		<c:url var="menuUrl" value="/Menu?action=accounts"/>
		<a class="menu-anchor<c:if 
			test="${(action eq 'accounts') || (action eq 'account') || (action eq 'category') || (action eq 'deleteCategory') || (action eq 'deactivateAccount')}"
		> active
		</c:if>" href="${menuUrl}">Cuentas</a>
	</li>
</ul>