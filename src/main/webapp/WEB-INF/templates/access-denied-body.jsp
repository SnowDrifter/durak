<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="access_denied">
    <c:if test="${not empty error}">
        <span>${error}</span>
    </c:if>
    <img src="${pageContext.request.contextPath}/resources/images/cancel.png"/>
</div>

