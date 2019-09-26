package ru.romanov.durak.game;

import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.durak.model.player.AIPlayer;
import ru.romanov.durak.model.player.HumanPlayer;
import ru.romanov.durak.model.player.Player;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.websocket.WebSocketService;
import ru.romanov.durak.websocket.message.DefaultMessage;
import ru.romanov.durak.websocket.message.Message;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ru.romanov.durak.websocket.message.MessageType.*;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserService userService;

    private final Map<String, Game> singleplayerGames = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();
    private final Map<String, Game> multiplayerGames = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

    @Override
    public void processSingleplayerMessage(String username, Message message) {
        if (message.getType() == START_GAME) {
            createSingleplayerGame(username);
        } else {
            Game game = singleplayerGames.get(username);
            game.processPlayerMessage(username, message);
        }
    }

    @Override
    public void processMultiplayerMessage(String username, Message message) {
        Game game = multiplayerGames.get(username);
        game.processPlayerMessage(username, message);
    }

    @Override
    public void createMultiplayerGame(String firstUsername, String secondUsername) {
        Game game = new Game();
        game.setEndGameConsumer(this::updateStatistics);
        game.setWebSocketService(webSocketService);
        game.initGame(new HumanPlayer(firstUsername), new HumanPlayer(secondUsername));

        multiplayerGames.put(firstUsername, game);
        multiplayerGames.put(secondUsername, game);

        webSocketService.sendMessage(firstUsername, new DefaultMessage(START_GAME));
        webSocketService.sendMessage(secondUsername, new DefaultMessage(START_GAME));
    }

    @Override
    public void playerDisconnected(String username) {
        Game game = multiplayerGames.get(username);

        if (username.equals(game.getAttackPlayer().getUsername())) {
            webSocketService.sendMessage(game.getDefendPlayer().getUsername(), new DefaultMessage(ENEMY_DISCONNECTED));
        } else {
            webSocketService.sendMessage(game.getAttackPlayer().getUsername(), new DefaultMessage(ENEMY_DISCONNECTED));
        }
    }

    private void createSingleplayerGame(String username) {
        stopPreviousGame(username);

        Game game = new Game();
        game.setWebSocketService(webSocketService);
        game.initGame(new HumanPlayer(username), new AIPlayer());
        singleplayerGames.put(username, game);
    }

    private void updateStatistics(Game game) {
        updateStatisticsForPlayer(game.getAttackPlayer());
        updateStatisticsForPlayer(game.getDefendPlayer());
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

    private void stopPreviousGame(String username) {
        Game game = singleplayerGames.remove(username);
        if (game != null) {
            game.forceStop();
        }
    }

}
