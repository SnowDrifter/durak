package ru.romanov.durak.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.controller.websocket.message.*;
import ru.romanov.durak.model.Game;
import ru.romanov.durak.model.GameInvite;
import ru.romanov.durak.model.Lobby;
import ru.romanov.durak.model.player.Player;
import ru.romanov.durak.model.player.RealPlayer;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.util.JsonHelper;

import java.util.HashMap;

@Slf4j
public class MultiplayerWebSocket extends TextWebSocketHandler {

    @Autowired
    private UserService userService;
    private static HashMap<String, Game> games = new HashMap<>();
    private Lobby lobby = new Lobby();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = session.getPrincipal().getName();
        lobby.addUser(username, session);
        lobby.sendLobbyState(username, session);

        log.info("Open session for multiplayer");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);

        switch (message.getType()) {
            case INVITE: {
                InviteMessage inviteMessage = (InviteMessage) message;
                lobby.createInvite(inviteMessage.getInitiator(), inviteMessage.getInvitee());
                break;
            }
            case ACCEPT_INVITE: {
                String invitee = session.getPrincipal().getName();
                GameInvite invite = lobby.getInvite(invitee);
                startMultiplayerGame(invite);
                break;
            }
            case REJECT_INVITE: {
                String invitee = session.getPrincipal().getName();
                lobby.rejectInvite(invitee);
                break;
            }
            case LOBBY_CHAT_MESSAGE: {
                ChatMessage chatMessage = (ChatMessage) message;
                lobby.sendMessageToAll(chatMessage, chatMessage.getUsername());
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

    private void startMultiplayerGame(GameInvite invite) {
        String firstUsername = invite.getInitiator();
        String secondUsername = invite.getInvitee();

        RealPlayer firstPlayer = new RealPlayer();
        RealPlayer secondPlayer = new RealPlayer();

        firstPlayer.setUsername(firstUsername);
        secondPlayer.setUsername(secondUsername);

        firstPlayer.setSession(lobby.getSessionByUsername(firstUsername));
        secondPlayer.setSession(lobby.getSessionByUsername(secondUsername));

        lobby.removeByUsername(firstUsername);
        lobby.removeByUsername(secondUsername);

        Game game = new Game();
        game.setFirstPlayer(firstPlayer);
        game.setSecondPlayer(secondPlayer);
        game.setEndGameConsumer(this::updateStatistics);

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
        String username = session.getPrincipal().getName();
        lobby.removeByUsername(username);

        for (Game game : games.values()) {
            if (session.equals(game.getFirstPlayer().getSession())) {
                game.getSecondPlayer().sendMessage(new DefaultMessage(MessageType.ENEMY_DISCONNECTED));
            } else if (session.equals(game.getSecondPlayer().getSession())) {
                game.getFirstPlayer().sendMessage(new DefaultMessage(MessageType.ENEMY_DISCONNECTED));
            }
        }
    }

    private void updateStatistics(Game game) {
        updateStatisticsForPlayer(game.getFirstPlayer());
        updateStatisticsForPlayer(game.getSecondPlayer());
    }

    private void updateStatisticsForPlayer(Player player) {
        if (StringUtils.isEmpty(player.getUsername())) {
            return;
        }

        User user = userService.findByUsername(player.getUsername());

        int totalGames = user.getTotalGames();
        user.setTotalGames(++totalGames);

        if (player.isWin()) {
            int oldWins = user.getWins();
            user.setWins(++oldWins);
        } else {
            int oldLoses = user.getLoses();
            user.setLoses(++oldLoses);
        }

        userService.save(user);
    }
}
