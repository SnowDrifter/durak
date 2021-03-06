<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form name="loginForm" id="login" action="login" method="POST">
    <div id="login_header"><spring:message code="login.header"/></div>

    <div id="login_errors">
        <c:if test="${not empty error}">
            <div>${error}</div>
        </c:if>
    </div>

    <table>
        <tr>
            <td><spring:message code="login.username"/></td>
            <td><input class="field" title="" type="text" name="username"></td>
        </tr>

        <tr>
            <td><spring:message code="login.password"/></td>
            <td><input class="field" title="" type="password" name="password"></td>
        </tr>

        <tr>
            <td>
                <input type="checkbox" checked class="css-checkbox" id="remember-me" name="remember-me"/>
                <label for="remember-me" class="css-label lite-gray-check"><spring:message
                        code="login.rememberme"/></label>
            </td>
        </tr>
    </table>

    <button id="login_submit" type="submit"><spring:message code="login.submit"/></button>
</form>
