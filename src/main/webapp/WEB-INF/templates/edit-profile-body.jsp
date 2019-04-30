<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/jQueryUI/ui/i18n/datepicker-${locale}.min.js">
    <jsp:text/>
</script>

<script type="text/javascript">
    $(function () {
        var currentYear = new Date().getFullYear();

        $('#birthDate').datepicker({
            dateFormat: 'dd mm yy',
            changeYear: true,
            yearRange: '1900:' + currentYear
        });

    });
</script>

<div id="locale" style="display:none">
    ${locale}
</div>

<form:form id="edit" modelAttribute="user" method="POST" enctype="multipart/form-data">
      <div id="edit_header"><spring:message code="edit.title"/></div>

    <div class="message">
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>


    <div id="photo">
        <c:choose>
            <c:when test="${not empty user.photo}">
                <img class="photo_borders" src='${pageContext.request.contextPath}/profile/${user.id}/photo'>
            </c:when>
            <c:otherwise>
                <img class="standard_photo" src='${pageContext.request.contextPath}/resources/images/standard_photo.png'/>
            </c:otherwise>
        </c:choose>
    </div>

    <table>
        <tr>
            <td><label><spring:message code="user.firstName"/></label></td>
            <td><form:input class="field" path="firstName" value=""/>
                <form:errors path="firstName" cssClass="error" element="div"/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.lastName"/></label></td>
            <td><form:input class="field" path="lastName" value=""/>
                <form:errors path="lastName" cssClass="error"  element="div"/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.email"/></label></td>
            <td><form:input class="field" path="email" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="email" cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.about"/></label></td>
            <td><form:input class="field" path="about" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="about" cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.photo"/></label></td>
            <td><input name="file" type="file"/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="photo" cssClass="error" element="div"/>
                <jsp:text/>
            </td>
        </tr>

        <tr>
            <td><label> <spring:message code="user.birthDate"/></label></td>
            <td>
                <form:input class="field birth_date" id="birthDate" path="birthDate" value=""/>
                <form:errors path="birthDate" cssClass="error" element="div"/>
            </td>
        </tr>
    </table>

    <form:hidden path="username"/>
    <form:hidden path="password"/>

    <button id="edit_submit" type="submit"><spring:message code="registration.submit"/></button>
</form:form>