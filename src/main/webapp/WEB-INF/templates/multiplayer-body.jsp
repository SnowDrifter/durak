<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/multiplayer.js"><jsp:text/></script>
<script>
    $(document).ready(initMultiplayerGame());
</script>

<div id="lobby">
    <div id="lobby_header"><spring:message code="lobby.header"/></div>
    <div id="users"></div>
    <div id="empty_lobby"><spring:message code="lobby.empty"/></div>
</div>

<div id="game_container">
    <div id="preloader"><span class="spinner"></span></div>

    <div class="enemy_side"></div>
    <div class="table"></div>
    <div class="trump"></div>
    <div class="deck"></div>
    <div class="player_side"></div>

    <div class="message_window" id="move_player">
        <spring:message code="game.move.player"/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window" id="move_enemy">
        <spring:message code="game.move.enemy"/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window alert_window" id="wrong_card">
        <spring:message code="game.wrong.card"/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window" id="result_win">
        <br/><spring:message code="game.result.win"/><br/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window" id="result_lose">
        <br/><spring:message code="game.result.lose"/><br/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window" id="result_draw">
        <br/><spring:message code="game.result.draw"/><br/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window" id="disconnected">
        <spring:message code="game.disconnected"/><br/><br/>
        <button class="close_message">OK</button>
    </div>
    <div class="message_window" id="session_close">
        <spring:message code="game.sessionClose"/><br/>
        <button class="close_message">OK</button>
    </div>

    <button class="action_button" id="finish_button"><spring:message code="game.button.finish"/></button>
    <button class="action_button" id="take_button"><spring:message code="game.button.take"/></button>
</div>

<div id="chat">
    <div id="chat_header"><spring:message code="lobby.chat.header"/>  <img src="${pageContext.request.contextPath}/resources/images/arrow_up.png" id="chat_switch"/></div>
    <div id="chat_content">
        <div id="chat_history"></div>
        <input id="chat_text_field" title="" class="field" type="text"/>
        <button id="chat_send"><spring:message code="chat.send"/></button>
    </div>
</div>

<div id="sump" style="display:none">
    <div class='card actionCard' id='c1'><img src='${pageContext.request.contextPath}/resources/images/cards/c1.png'/></div>
    <div class='card actionCard' id='c2'><img src='${pageContext.request.contextPath}/resources/images/cards/c2.png'/></div>
    <div class='card actionCard' id='c3'><img src='${pageContext.request.contextPath}/resources/images/cards/c3.png'/></div>
    <div class='card actionCard' id='c4'><img src='${pageContext.request.contextPath}/resources/images/cards/c4.png'/></div>
    <div class='card actionCard' id='c5'><img src='${pageContext.request.contextPath}/resources/images/cards/c5.png'/></div>
    <div class='card actionCard' id='c6'><img src='${pageContext.request.contextPath}/resources/images/cards/c6.png'/></div>
    <div class='card actionCard' id='c7'><img src='${pageContext.request.contextPath}/resources/images/cards/c7.png'/></div>
    <div class='card actionCard' id='c8'><img src='${pageContext.request.contextPath}/resources/images/cards/c8.png'/></div>
    <div class='card actionCard' id='c9'><img src='${pageContext.request.contextPath}/resources/images/cards/c9.png'/></div>

    <div class='card actionCard' id='d1'><img src='${pageContext.request.contextPath}/resources/images/cards/d1.png'/></div>
    <div class='card actionCard' id='d2'><img src='${pageContext.request.contextPath}/resources/images/cards/d2.png'/></div>
    <div class='card actionCard' id='d3'><img src='${pageContext.request.contextPath}/resources/images/cards/d3.png'/></div>
    <div class='card actionCard' id='d4'><img src='${pageContext.request.contextPath}/resources/images/cards/d4.png'/></div>
    <div class='card actionCard' id='d5'><img src='${pageContext.request.contextPath}/resources/images/cards/d5.png'/></div>
    <div class='card actionCard' id='d6'><img src='${pageContext.request.contextPath}/resources/images/cards/d6.png'/></div>
    <div class='card actionCard' id='d7'><img src='${pageContext.request.contextPath}/resources/images/cards/d7.png'/></div>
    <div class='card actionCard' id='d8'><img src='${pageContext.request.contextPath}/resources/images/cards/d8.png'/></div>
    <div class='card actionCard' id='d9'><img src='${pageContext.request.contextPath}/resources/images/cards/d9.png'/></div>

    <div class='card actionCard' id='h1'><img src='${pageContext.request.contextPath}/resources/images/cards/h1.png'/></div>
    <div class='card actionCard' id='h2'><img src='${pageContext.request.contextPath}/resources/images/cards/h2.png'/></div>
    <div class='card actionCard' id='h3'><img src='${pageContext.request.contextPath}/resources/images/cards/h3.png'/></div>
    <div class='card actionCard' id='h4'><img src='${pageContext.request.contextPath}/resources/images/cards/h4.png'/></div>
    <div class='card actionCard' id='h5'><img src='${pageContext.request.contextPath}/resources/images/cards/h5.png'/></div>
    <div class='card actionCard' id='h6'><img src='${pageContext.request.contextPath}/resources/images/cards/h6.png'/></div>
    <div class='card actionCard' id='h7'><img src='${pageContext.request.contextPath}/resources/images/cards/h7.png'/></div>
    <div class='card actionCard' id='h8'><img src='${pageContext.request.contextPath}/resources/images/cards/h8.png'/></div>
    <div class='card actionCard' id='h9'><img src='${pageContext.request.contextPath}/resources/images/cards/h9.png'/></div>

    <div class='card actionCard' id='s1'><img src='${pageContext.request.contextPath}/resources/images/cards/s1.png'/></div>
    <div class='card actionCard' id='s2'><img src='${pageContext.request.contextPath}/resources/images/cards/s2.png'/></div>
    <div class='card actionCard' id='s3'><img src='${pageContext.request.contextPath}/resources/images/cards/s3.png'/></div>
    <div class='card actionCard' id='s4'><img src='${pageContext.request.contextPath}/resources/images/cards/s4.png'/></div>
    <div class='card actionCard' id='s5'><img src='${pageContext.request.contextPath}/resources/images/cards/s5.png'/></div>
    <div class='card actionCard' id='s6'><img src='${pageContext.request.contextPath}/resources/images/cards/s6.png'/></div>
    <div class='card actionCard' id='s7'><img src='${pageContext.request.contextPath}/resources/images/cards/s7.png'/></div>
    <div class='card actionCard' id='s8'><img src='${pageContext.request.contextPath}/resources/images/cards/s8.png'/></div>
    <div class='card actionCard' id='s9'><img src='${pageContext.request.contextPath}/resources/images/cards/s9.png'/></div>

    <div class='card' id="back"><img src='${pageContext.request.contextPath}/resources/images/cards/back.png'/></div>
</div>












