<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/resources/js/multiplayer-final.min.js"/>"><jsp:text/></script>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
    var username = "${pageContext.request.userPrincipal.name}";

    var playerMoveText = "<spring:message code="game.move.player"/>";
    var enemyMoveText = "<spring:message code="game.move.enemy"/>";
    var wrongCardText = "<spring:message code="game.wrong.card"/>";
    var winText = "<spring:message code="game.result.win"/>";
    var loseText = "<spring:message code="game.result.lose"/>";
    var drawText = "<spring:message code="game.result.draw"/>";
    var disconnectedText = "<spring:message code="game.disconnected"/>";
    var sessionCloseText = "<spring:message code="game.sessionClose"/>";

    $(document).ready(initMultiplayerGame());
</script>

<div id="lobby">
    <div id="lobby_header"><spring:message code="lobby.header"/></div>
    <div id="lobby_users"></div>
    <div id="empty_lobby"><spring:message code="lobby.empty"/></div>
</div>

<div id="invite_dialog">
    <div id="invite_dialog_content">
        <spring:message code="invite.message"/> '<span id="invitation_initiator_username"></span>'

        <div id="invite_button_group">
            <button id="invite_accept_button" type="button" class="invite_button">
               <spring:message code="invite.accept"/>
            </button>
            <button id="invite_reject_button" type="button" class="invite_button">
               <spring:message code="invite.reject"/>
            </button>
        </div>
    </div>
</div>

<div id="game_container">
    <div id="preloader"><span class="spinner"></span></div>

    <div id="enemy_side"></div>
    <div id="table"></div>
    <div id="trump"></div>
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
</div>

<div id="chat">
    <div id="chat_header"><spring:message code="lobby.chat.header"/>  <img src="${pageContext.request.contextPath}/resources/images/arrow.png" id="chat_switch"/></div>
    <div id="chat_content">
        <div id="chat_history"></div>
        <div id="chat_form">
            <textarea id="chat_text_field" title="" class="field" rows="3"></textarea>
            <button id="chat_send_button"><spring:message code="send"/></button>
        </div>
    </div>
</div>











