<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="locale" value="${pageContext.response.locale}" />

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jQueryUI/ui/i18n/datepicker-${locale}.min.js"><jsp:text/></script>

<script type="text/javascript">
    $(function () {
        $("#birthDate").datepicker({
            dateFormat: "dd mm yy",
            changeYear: true,
            yearRange: "-100:+0"
        });
    });
</script>

<form:form id="registration" modelAttribute="user" action="registration" method="POST" enctype="multipart/form-data">
    <div id="registration_header"><spring:message code="registration.header"/></div>

    <div class="message">
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>

    <table>
        <tr>
            <td><label><spring:message code="user.username"/>
                <span class="error">*</span></label></td>
            <td><form:input class="field" path="username" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="username" cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.password"/>
                <span class="error">*</span></label></td>
            <td><form:input class="field" path="password" value="" type="password"/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="password" cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.email"/></label></td>
            <td><form:input class="field" path="email" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="email"  cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.about"/></label></td>
            <td><form:input class="field" path="about" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="about"  cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.firstName"/></label></td>
            <td><form:input class="field" path="firstName" value=""/>
                <form:errors path="firstName" element="div"/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.lastName"/></label></td>
            <td><form:input class="field" path="lastName" value=""/>
                <form:errors path="lastName" element="div"/>
            </td>
        </tr>

        <tr>
            <td><label> <spring:message code="user.birthDate"/></label></td>
            <td>
                <form:input class="field birth_date" id="birthDate" path="birthDate" value=""/>
                <form:errors path="birthDate" element="div"/>
            </td>
        </tr>

    </table>

    <button id="registration_submit" type="submit"><spring:message code="registration.submit"/>
</form:form>



