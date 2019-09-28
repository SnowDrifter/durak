<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/resources/js/multiplayer-final.min.js"/>"><jsp:text/></script>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
    var username = '${pageContext.request.userPrincipal.name}';

    var playerMoveText = '<spring:message code="game.move.player"/>';
    var enemyMoveText = '<spring:message code="game.move.enemy"/>';
    var wrongCardText = '<spring:message code="game.wrong.card"/>';
    var winText = '<spring:message code="game.result.win"/>';
    var loseText = '<spring:message code="game.result.lose"/>';
    var drawText = '<spring:message code="game.result.draw"/>';
    var disconnectedText = '<spring:message code="game.disconnected"/>';
    var sessionCloseText = '<spring:message code="game.sessionClose"/>';

    $(document).ready(initMultiplayerGame());
</script>

<div id="lobby">
    <div id="lobby_header"><spring:message code="lobby.header"/></div>
    <div id="lobby_users"></div>
    <div id="empty_lobby"><spring:message code="lobby.empty"/></div>
</div>

<div id="game_container">
    <div id="preloader"><span class="spinner"></span></div>

    <div id="enemy_side"></div>
    <div id="table"></div>
    <div id="trump"></div>
    <div id="deck"></div>
    <div id="player_side"></div>

    <div id="notification">
        <div id="notification_text"></div>
        <button id="close_notification_button">OK</button>
    </div>

    <button id="finish_button" class="action_button"><spring:message code="game.button.finish"/></button>
    <button id="take_button" class="action_button"><spring:message code="game.button.take"/></button>
</div>

<div id="chat">
    <div id="chat_header"><spring:message code="lobby.chat.header"/>  <img src="${pageContext.request.contextPath}/resources/images/arrow_up.png" id="chat_switch"/></div>
    <div id="chat_content">
        <div id="chat_history"></div>
        <div id="chat_form">
            <textarea id="chat_text_field" title="" class="field" rows="3"></textarea>
            <button id="chat_send_button"><spring:message code="chat.send"/></button>
        </div>
    </div>
</div>

<div id="sump" style="display:none">
    <div class='card actionCard' id='c0'><img src='${pageContext.request.contextPath}/resources/images/cards/c0.png'/></div>
    <div class='card actionCard' id='c1'><img src='${pageContext.request.contextPath}/resources/images/cards/c1.png'/></div>
    <div class='card actionCard' id='c2'><img src='${pageContext.request.contextPath}/resources/images/cards/c2.png'/></div>
    <div class='card actionCard' id='c3'><img src='${pageContext.request.contextPath}/resources/images/cards/c3.png'/></div>
    <div class='card actionCard' id='c4'><img src='${pageContext.request.contextPath}/resources/images/cards/c4.png'/></div>
    <div class='card actionCard' id='c5'><img src='${pageContext.request.contextPath}/resources/images/cards/c5.png'/></div>
    <div class='card actionCard' id='c6'><img src='${pageContext.request.contextPath}/resources/images/cards/c6.png'/></div>
    <div class='card actionCard' id='c7'><img src='${pageContext.request.contextPath}/resources/images/cards/c7.png'/></div>
    <div class='card actionCard' id='c8'><img src='${pageContext.request.contextPath}/resources/images/cards/c8.png'/></div>

    <div class='card actionCard' id='d0'><img src='${pageContext.request.contextPath}/resources/images/cards/d0.png'/></div>
    <div class='card actionCard' id='d1'><img src='${pageContext.request.contextPath}/resources/images/cards/d1.png'/></div>
    <div class='card actionCard' id='d2'><img src='${pageContext.request.contextPath}/resources/images/cards/d2.png'/></div>
    <div class='card actionCard' id='d3'><img src='${pageContext.request.contextPath}/resources/images/cards/d3.png'/></div>
    <div class='card actionCard' id='d4'><img src='${pageContext.request.contextPath}/resources/images/cards/d4.png'/></div>
    <div class='card actionCard' id='d5'><img src='${pageContext.request.contextPath}/resources/images/cards/d5.png'/></div>
    <div class='card actionCard' id='d6'><img src='${pageContext.request.contextPath}/resources/images/cards/d6.png'/></div>
    <div class='card actionCard' id='d7'><img src='${pageContext.request.contextPath}/resources/images/cards/d7.png'/></div>
    <div class='card actionCard' id='d8'><img src='${pageContext.request.contextPath}/resources/images/cards/d8.png'/></div>

    <div class='card actionCard' id='h0'><img src='${pageContext.request.contextPath}/resources/images/cards/h0.png'/></div>
    <div class='card actionCard' id='h1'><img src='${pageContext.request.contextPath}/resources/images/cards/h1.png'/></div>
    <div class='card actionCard' id='h2'><img src='${pageContext.request.contextPath}/resources/images/cards/h2.png'/></div>
    <div class='card actionCard' id='h3'><img src='${pageContext.request.contextPath}/resources/images/cards/h3.png'/></div>
    <div class='card actionCard' id='h4'><img src='${pageContext.request.contextPath}/resources/images/cards/h4.png'/></div>
    <div class='card actionCard' id='h5'><img src='${pageContext.request.contextPath}/resources/images/cards/h5.png'/></div>
    <div class='card actionCard' id='h6'><img src='${pageContext.request.contextPath}/resources/images/cards/h6.png'/></div>
    <div class='card actionCard' id='h7'><img src='${pageContext.request.contextPath}/resources/images/cards/h7.png'/></div>
    <div class='card actionCard' id='h8'><img src='${pageContext.request.contextPath}/resources/images/cards/h8.png'/></div>

    <div class='card actionCard' id='s0'><img src='${pageContext.request.contextPath}/resources/images/cards/s0.png'/></div>
    <div class='card actionCard' id='s1'><img src='${pageContext.request.contextPath}/resources/images/cards/s1.png'/></div>
    <div class='card actionCard' id='s2'><img src='${pageContext.request.contextPath}/resources/images/cards/s2.png'/></div>
    <div class='card actionCard' id='s3'><img src='${pageContext.request.contextPath}/resources/images/cards/s3.png'/></div>
    <div class='card actionCard' id='s4'><img src='${pageContext.request.contextPath}/resources/images/cards/s4.png'/></div>
    <div class='card actionCard' id='s5'><img src='${pageContext.request.contextPath}/resources/images/cards/s5.png'/></div>
    <div class='card actionCard' id='s6'><img src='${pageContext.request.contextPath}/resources/images/cards/s6.png'/></div>
    <div class='card actionCard' id='s7'><img src='${pageContext.request.contextPath}/resources/images/cards/s7.png'/></div>
    <div class='card actionCard' id='s8'><img src='${pageContext.request.contextPath}/resources/images/cards/s8.png'/></div>

    <div class='card' id="back"><img src='${pageContext.request.contextPath}/resources/images/cards/back.png'/></div>
</div>












