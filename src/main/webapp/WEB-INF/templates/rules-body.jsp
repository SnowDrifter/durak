<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="rules">
    <div id="rules_header">
        <spring:message code="rules.header"/>
    </div>

    <div id="rules_content">
        <img src="<c:url value="/static/images/rules.jpg"/>" alt="rules"/>
        <spring:message code="rules.content"/>
    </div>
</div>