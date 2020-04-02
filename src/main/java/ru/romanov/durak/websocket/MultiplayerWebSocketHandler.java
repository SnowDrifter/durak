package ru.romanov.durak.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.game.GameService;
import ru.romanov.durak.lobby.InviteService;
import ru.romanov.durak.lobby.LobbyService;
import ru.romanov.durak.model.GameInvite;
import ru.romanov.durak.util.JsonHelper;
import ru.romanov.durak.websocket.message.*;

@Component
@RequiredArgsConstructor
public class MultiplayerWebSocketHandler extends TextWebSocketHandler {

    private final LobbyService lobbyService;
    private final InviteService inviteService;
    private final GameService gameService;
    private final WebSocketService webSocketService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = session.getPrincipal().getName();
        webSocketService.addSession(username, session);
        lobbyService.addUser(username);
        lobbyService.sendLobbyState(username);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);

        switch (message.getType()) {
            case INVITE: {
                InviteMessage inviteMessage = (InviteMessage) message;
                inviteService.createInvite(inviteMessage.getInitiator(), inviteMessage.getInvitee());
                break;
            }
            case ACCEPT_INVITE: {
                String invitee = session.getPrincipal().getName();
                GameInvite invite = inviteService.getInvite(invitee);
                startMultiplayerGame(invite);
                break;
            }
            case REJECT_INVITE: {
                String invitee = session.getPrincipal().getName();
                inviteService.rejectInvite(invitee);
                break;
            }
            case LOBBY_CHAT_MESSAGE: {
                ChatMessage chatMessage = (ChatMessage) message;
                lobbyService.sendMessageToAll(chatMessage, chatMessage.getUsername());
                break;
            }
            case CHAT_MESSAGE: {
                ChatMessage chatMessage = (ChatMessage) message;
                webSocketService.sendMessage(chatMessage.getUsername(), message);
                break;
            }
            default: {
                String username = session.getPrincipal().getName();
                gameService.processMultiplayerMessage(username, message);
            }
        }
    }

    private void startMultiplayerGame(GameInvite invite) {
        String firstUsername = invite.getInitiator();
        String secondUsername = invite.getInvitee();

        lobbyService.removeByUsername(firstUsername);
        lobbyService.removeByUsername(secondUsername);

        gameService.createMultiplayerGame(firstUsername, secondUsername);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = session.getPrincipal().getName();
        webSocketService.removeSession(username);
        lobbyService.removeByUsername(username);
        gameService.playerDisconnected(username);
    }

}
