package ru.romanov.durak.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.lobby.InviteService;
import ru.romanov.durak.lobby.LobbyService;
import ru.romanov.durak.game.Game;
import ru.romanov.durak.model.GameInvite;
import ru.romanov.durak.model.player.HumanPlayer;
import ru.romanov.durak.model.player.Player;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.util.JsonHelper;
import ru.romanov.durak.websocket.message.*;

import java.util.HashMap;

@Slf4j
public class MultiplayerWebSocket extends TextWebSocketHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private WebSocketService webSocketService;
    private static HashMap<String, Game> games = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = session.getPrincipal().getName();
        webSocketService.addSession(username, session);
        lobbyService.addUser(username);
        lobbyService.sendLobbyState(username);
        log.info("Open session for multiplayer");
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

        HumanPlayer firstPlayer = new HumanPlayer();
        HumanPlayer secondPlayer = new HumanPlayer();

        firstPlayer.setUsername(firstUsername);
        secondPlayer.setUsername(secondUsername);

        lobbyService.removeByUsername(firstUsername);
        lobbyService.removeByUsername(secondUsername);

        Game game = new Game();
        game.setFirstPlayer(firstPlayer);
        game.setSecondPlayer(secondPlayer);
        game.setEndGameConsumer(this::updateStatistics);
        game.setWebSocketService(webSocketService);
        game.initGame();

        firstPlayer.setGame(game);
        secondPlayer.setGame(game);

        webSocketService.sendMessage(firstUsername, new DefaultMessage(MessageType.START_GAME));
        webSocketService.sendMessage(secondUsername, new DefaultMessage(MessageType.START_GAME));

        games.put(firstUsername, game);
        games.put(secondUsername, game);

        new Thread(game).start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = session.getPrincipal().getName();
        webSocketService.removeSession(username);
        lobbyService.removeByUsername(username);

        for (Game game : games.values()) {
            if (username.equals(game.getFirstPlayer().getUsername())) {
                webSocketService.sendMessage(game.getSecondPlayer().getUsername(), new DefaultMessage(MessageType.ENEMY_DISCONNECTED));
            } else if (username.equals(game.getSecondPlayer().getUsername())) {
                webSocketService.sendMessage(game.getFirstPlayer().getUsername(), new DefaultMessage(MessageType.ENEMY_DISCONNECTED));
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
