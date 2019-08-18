<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="game_menu">
    <a href="${pageContext.servletContext.contextPath}/singleplayer">
        <div id="singleplayer_ref">
            <spring:message code="home.singleplayer"/>
        </div>
    </a>
    <br/>
    <a href="${pageContext.servletContext.contextPath}/multiplayer">
        <div id="multiplayer_ref">
            <spring:message code="home.multiplayer"/>
        </div>
    </a>
</div>
