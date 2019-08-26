<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="game_menu">
    <a id="singleplayer_ref" href="${pageContext.servletContext.contextPath}/singleplayer"><spring:message code="home.singleplayer"/></a>
    <a id="multiplayer_ref" href="${pageContext.servletContext.contextPath}/multiplayer"><spring:message code="home.multiplayer"/></a>
</div>
