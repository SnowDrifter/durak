<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="rules">
    <div id="rules_header">
        <spring:message code="rules.header"/>
    </div>

    <div id="rules_content">
        <img src="${pageContext.request.contextPath}/resources/images/rules.jpg" alt="rules"/>
        <spring:message code="rules.content"/>
    </div>
</div>