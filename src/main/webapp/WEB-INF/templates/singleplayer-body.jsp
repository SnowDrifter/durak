<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/resources/js/singleplayer-final.min.js"/>"><jsp:text/></script>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
    var playerMoveText = '<spring:message code="game.move.player"/>';
    var enemyMoveText = '<spring:message code="game.move.enemy"/>';
    var wrongCardText = '<spring:message code="game.wrong.card"/>';
    var winText = '<spring:message code="game.result.win"/>';
    var loseText = '<spring:message code="game.result.lose"/>';
    var drawText = '<spring:message code="game.result.draw"/>';
    var sessionCloseText = '<spring:message code="game.sessionClose"/>';

    $(document).ready(initSingleplayerGame());
</script>

<div class="singleplayer_container" id="game_container">
    <div id="preloader"><span class="spinner"></span></div>

    <div id="enemy_side"></div>
    <div id="table"></div>
    <div id="trump" class="card"></div>
    <div id="deck">
        <span class='cards_number'></span>
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






