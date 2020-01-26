<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<header>
    <nav class="navigation">
        <a class="nav_item" href="${pageContext.request.contextPath}/"><spring:message code="home"/></a>
        <a class="nav_item" href="${pageContext.request.contextPath}/rules"><spring:message code="rules.title"/></a>
        <a class="nav_item" href="${pageContext.request.contextPath}/statistic"><spring:message code="statistics.title"/></a>

        <div class="right_section">
            <security:authorize access="isAnonymous()">
                <a class="nav_item" href="${pageContext.request.contextPath}/registration"><spring:message code="registration"/></a>
                <a class="nav_item" href="${pageContext.request.contextPath}/login"><spring:message code="login"/></a>
            </security:authorize>

            <security:authorize access="isAuthenticated()">
                <a class="nav_item" href="${pageContext.request.contextPath}/logout"><spring:message code="logout"/></a>
                <a class="nav_item" href="${pageContext.request.contextPath}/edit"><spring:message code="edit.menu"/></a>

                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <span class="nav_item"><spring:message code="welcome"/> ${pageContext.request.userPrincipal.name}</span>
                </c:if>
            </security:authorize>

            <div class="language_section">
                <a href="?lang=ru"><img src="${pageContext.request.contextPath}/resources/images/flags/ru.png" alt="ru"/></a>
                <a href="?lang=en"><img src="${pageContext.request.contextPath}/resources/images/flags/en.png" alt="en"/></a>
            </div>
        </div>
    </nav>
</header>