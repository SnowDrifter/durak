package ru.lam.durak.controllers.websocket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.lam.durak.objects.Game;
import ru.lam.durak.objects.Lobby;
import ru.lam.durak.objects.players.RealPlayer;
import ru.lam.durak.users.service.UserService;

import java.util.HashMap;


public class MultiplayerWebSocket extends TextWebSocketHandler {
    private static final Logger LOG = LogManager.getLogger(MultiplayerWebSocket.class);

    @Autowired
    private UserService userService;

    private static HashMap<String, Game>  games = new HashMap<>();

    private Lobby lobby = new Lobby();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getPrincipal().getName();
        lobby.addPlayer(username, session);
        lobby.updateLobbyView();
        LOG.info("Open session for multiplayer");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage text) throws Exception {
        String request = text.getPayload();

        if (request.startsWith("offer=")) {
            startMultiplayerGame(request);
        } else if (request.startsWith("game/")) {
            String username = request.split("/")[1];
            Game game = games.get(username);
            game.requestFromPlayer(username, request.split("/")[2]);
        } else if (request.startsWith("chat:")) {
            lobby.sendMessageToAll(request);
        } else if (request.startsWith("gamechat:")) {
            // TODO прикрутить проверку, чтобы не крашилось с ArrayIndexOutOfBoundsException
            String username = request.replace("gamechat:", "").split("/")[0];
            String message = request.replace("gamechat:", "").split("/")[1];

            Game game = games.get(username);
            game.sendChatMessage("chat:" + username + "/" + message);
        }
    }

    private void startMultiplayerGame(String request){
        String[] users = request.replace("offer=", "").split("-");

        String firstUsername = users[0];
        String secondUsername = users[1];

        RealPlayer firstPlayer = new RealPlayer();
        RealPlayer secondPlayer = new RealPlayer();

        firstPlayer.setUsername(firstUsername);
        secondPlayer.setUsername(secondUsername);

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

        firstPlayer.sendMessage("start");
        secondPlayer.sendMessage("start");

        games.put(firstUsername, game);
        games.put(secondUsername, game);

        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        lobby.removeBySession(session);
        lobby.updateLobbyView();

        for(Game game : games.values()){
            if(session.equals(game.getFirstPlayer().getSession())  ){
                game.getSecondPlayer().sendMessage("enemy_disconnected");

            } else if (session.equals(game.getSecondPlayer().getSession())) {
                game.getFirstPlayer().sendMessage("enemy_disconnected");
            }
        }
    }
}
