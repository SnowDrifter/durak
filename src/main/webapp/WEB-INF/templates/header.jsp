<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/x-icon">
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<header>
    <div class="lang right">
        <a href="?lang=ru"><img src="${pageContext.request.contextPath}/resources/images/RU.png"/></a>
        <a href="?lang=en"><img src="${pageContext.request.contextPath}/resources/images/GB.png"/></a>
    </div>
    <br/>

    <div class="navigation">
        <span class="contact"><a href="${pageContext.request.contextPath}/home">
            <spring:message code="home"/></a></span>
        <span class="contact"><a href="${pageContext.request.contextPath}/rules">
            <spring:message code="rules.title"/></a></span>
        <span class="contact"><a href="${pageContext.request.contextPath}/stat">
            <spring:message code="statistics.title"/></a></span>


        <security:authorize access="isAnonymous()">
            <span class="contact right"><a href="${pageContext.request.contextPath}/registration">
                <spring:message code="registration"/></a></span>
            <span class="contact right"><a href="${pageContext.request.contextPath}/login">
                <spring:message code="login"/></a></span>
        </security:authorize>

        <security:authorize access="isAuthenticated()">
            <span class="contact right"><a href="${pageContext.request.contextPath}/logout">
                <spring:message code="logout"/></a></span>

            <span class="contact right"><a href="${pageContext.request.contextPath}/edit">
                <spring:message code="edit.menu"/></a></span>

            <c:if test="${pageContext.request.userPrincipal.name != null}">
             <span class="contact right">
		        <spring:message code="welcome"/>
                 <span id="username">${pageContext.request.userPrincipal.name}</span>
            </span>
            </c:if>

        </security:authorize>

    </div>
</header>