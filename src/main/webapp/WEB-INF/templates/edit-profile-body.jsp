<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="locale" value="${pageContext.response.locale}" />

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jQueryUI/ui/i18n/datepicker-${locale}.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/editProfile.js"></script>

<form:form id="edit" modelAttribute="userDto" method="POST" enctype="multipart/form-data">
    <div id="edit_header"><spring:message code="edit.title"/></div>

    <div class="message">
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>

    <div id="photo">
        <c:choose>
            <c:when test="${userDto.hasPhoto}">
                <script>loadPhoto(${userDto.id})</script>
            </c:when>
            <c:otherwise>
                <img class="default_photo" src="<c:url value="/static/images/default_photo.png"/>" alt="default_photo"/>
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
                <form:errors path="lastName" cssClass="error" element="div"/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.email"/></label></td>
            <td><form:input class="field" path="email" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="email" cssClass="error" element="div"/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.about"/></label></td>
            <td><form:input class="field" path="about" value=""/></td>
        </tr>
        <tr class="errors">
            <td colspan="2">
                <form:errors path="about" cssClass="error" element="div"/>
            </td>
        </tr>

        <tr>
            <td><label><spring:message code="user.photo"/></label></td>
            <td><input name="photo" type="file" onchange="uploadPhoto(${userDto.id})"/></td>
        </tr>
        <tr>
            <td></td>
            <td><span id="upload_photo_error" class="error" style="display: none"><spring:message code="photo.upload.fail"/></span></td>
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

    <button id="edit_submit" type="submit"><spring:message code="submit"/></button>
</form:form>