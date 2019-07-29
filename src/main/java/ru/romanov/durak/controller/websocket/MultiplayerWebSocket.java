package ru.romanov.durak.controller.websocket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.controller.websocket.message.*;
import ru.romanov.durak.object.Game;
import ru.romanov.durak.object.Lobby;
import ru.romanov.durak.object.player.RealPlayer;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.util.JsonHelper;

import java.util.HashMap;


public class MultiplayerWebSocket extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger(MultiplayerWebSocket.class);

    @Autowired
    private UserService userService;
    private static HashMap<String, Game> games = new HashMap<>();
    private Lobby lobby = new Lobby();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = session.getPrincipal().getName();
        lobby.addPlayer(username, session);
        lobby.updateLobbyView();
        logger.info("Open session for multiplayer");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);

        switch (message.getType()) {
            case OFFER: {
                startMultiplayerGame((OfferMessage) message);
                break;
            }
            case LOBBY_CHAT_MESSAGE: {
                ChatMessage chatMessage = (ChatMessage) message;
                lobby.sendMessageToAll(chatMessage);
                break;
            }
            case CHAT_MESSAGE: {
                ChatMessage chatMessage = (ChatMessage) message;
                Game game = games.get(chatMessage.getUsername());
                game.sendChatMessage(chatMessage);
                break;
            }
            default: {
                String username = session.getPrincipal().getName();
                Game game = games.get(username);
                game.requestFromPlayer(username, message);
            }
        }
    }

    private void startMultiplayerGame(OfferMessage message) {
        String firstUsername = message.getFirstUsername();
        String secondUsername = message.getSecondUsername();

        RealPlayer firstPlayer = new RealPlayer();
        RealPlayer secondPlayer = new RealPlayer();

        firstPlayer.setUsername(message.getFirstUsername());
        secondPlayer.setUsername(message.getSecondUsername());

        firstPlayer.setSession(lobby.getSessionByUsername(firstUsername));
        secondPlayer.setSession(lobby.getSessionByUsername(secondUsername));

        lobby.removeByUsername(firstUsername);
        lobby.removeByUsername(secondUsername);
        lobby.updateLobbyView();

        Game game = new Game();
        game.setUserService(userService);
        game.setFirstPlayer(firstPlayer);
        game.setSecondPlayer(secondPlayer);

        game.initGame();

        firstPlayer.setGame(game);
        secondPlayer.setGame(game);

        firstPlayer.sendMessage(new DefaultMessage(MessageType.START_GAME));
        secondPlayer.sendMessage(new DefaultMessage(MessageType.START_GAME));

        games.put(firstUsername, game);
        games.put(secondUsername, game);

        new Thread(game).start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        lobby.removeBySession(session);
        lobby.updateLobbyView();

        for (Game game : games.values()) {
            if (session.equals(game.getFirstPlayer().getSession())) {
                game.getSecondPlayer().sendMessage(new DefaultMessage(MessageType.ENEMY_DISCONNECTED));

            } else if (session.equals(game.getSecondPlayer().getSession())) {
                game.getFirstPlayer().sendMessage(new DefaultMessage(MessageType.START_GAME));
            }
        }
    }
}
