<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div id="rules">
    <div id="rules_header">
        <spring:message code="rules.header"/>
    </div>

    <div id="rules_content">
        <img align="left" src='${pageContext.request.contextPath}/resources/images/rules.jpg'/>
        <spring:message code="rules.content"/>

    </div>
</div>