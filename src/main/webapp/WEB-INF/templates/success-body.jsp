<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="success">

    <c:if test="${not empty message}">
        <span>${message}</span>
    </c:if>

    <br/>
    <img src="<c:url value="/static/images/success.png"/>" alt="success"/>
</div>

