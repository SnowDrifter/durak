<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script>
    const messages = new Map();
    messages.set("YOUR_MOVE", "<spring:message code="game.move.player"/>");
    messages.set("ENEMY_MOVE", "<spring:message code="game.move.enemy"/>");
    messages.set("WRONG_CARD", "<spring:message code="game.wrong.card"/>");
    messages.set("WIN", "<spring:message code="game.result.win"/>");
    messages.set("LOSE", "<spring:message code="game.result.lose"/>");
    messages.set("DRAW", "<spring:message code="game.result.draw"/>");
    messages.set("ENEMY_DISCONNECTED", "<spring:message code="game.disconnected"/>");
    messages.set("SESSION_CLOSE", "<spring:message code="game.disconnected"/>");

    function getMessageText(type) {
        return messages.get(type);
    }
</script>