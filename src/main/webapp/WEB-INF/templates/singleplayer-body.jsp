<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tiles:insertTemplate template="/WEB-INF/templates/game-messages.jsp"/>

<script type="text/javascript" src="<c:url value="/static/js/singleplayer-final.min.js"/>"></script>

<script>
    $(document).ready(initSingleplayerGame());
</script>

<div class="singleplayer_container" id="game_container">
    <div id="preloader"><span class="spinner"></span></div>

    <div id="enemy_side"></div>
    <div id="table"></div>
    <div id="trump" class="card"></div>
    <div id="deck">
        <span class="cards_number"></span>
    </div>
    <div id="player_side"></div>

    <div id="notification">
        <div id="notification_text"></div>
        <button id="close_notification_button">OK</button>
    </div>

    <button id="finish_button" class="action_button"><spring:message code="game.button.finish"/></button>
    <button id="take_button" class="action_button"><spring:message code="game.button.take"/></button>
    <button id="start_new_game_button" class="action_button"><spring:message code="game.button.newgame"/></button>
</div>






