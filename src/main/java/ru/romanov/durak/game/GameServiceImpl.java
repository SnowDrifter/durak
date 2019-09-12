package ru.romanov.durak.game;

import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.durak.model.player.HumanPlayer;
import ru.romanov.durak.model.player.Player;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.websocket.WebSocketService;
import ru.romanov.durak.websocket.message.CardMessage;
import ru.romanov.durak.websocket.message.DefaultMessage;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.websocket.message.MessageType;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserService userService;

    private final Map<String, Game> singleplayerGames = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();
    private final Map<String, Game> multiplayerGames = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();
    private final ExecutorService singleplayerExecutorService = Executors.newFixedThreadPool(10);
    private final ExecutorService multiplayerExecutorService = Executors.newFixedThreadPool(20);

    @Override
    public void processSingleplayerMessage(String username, Message message) {
        switch (message.getType()) {
            case START_GAME: {
                createSingleplayerGame(username);
                break;
            }
            case SELECT_CARD: {
                CardMessage cardMessage = (CardMessage) message;
                Game game = singleplayerGames.get(username);
                game.getFirstPlayer().selectCard(cardMessage.getCard());
                break;
            }
            case TAKE_CARD: {
                Game game = singleplayerGames.get(username);
                game.getFirstPlayer().setTake(true);
                break;
            }
            case FINISH_MOVE: {
                Game game = singleplayerGames.get(username);
                game.getFirstPlayer().setFinishMove(true);
                break;
            }
        }
    }

    @Override
    public void processMultiplayerMessage(String username, Message message) {
        Game game = multiplayerGames.get(username);
        game.requestFromPlayer(username, message);
    }

    @Override
    public void createMultiplayerGame(String firstUsername, String secondUsername) {
        Player firstPlayer = new HumanPlayer(firstUsername);
        Player secondPlayer = new HumanPlayer(secondUsername);

        Game game = new Game();
        game.setFirstPlayer(firstPlayer);
        game.setSecondPlayer(secondPlayer);
        game.setEndGameConsumer(this::updateStatistics);
        game.setWebSocketService(webSocketService);
        game.initGame();

        webSocketService.sendMessage(firstUsername, new DefaultMessage(MessageType.START_GAME));
        webSocketService.sendMessage(secondUsername, new DefaultMessage(MessageType.START_GAME));

        multiplayerGames.put(firstUsername, game);
        multiplayerGames.put(secondUsername, game);

        multiplayerExecutorService.execute(game);
    }

    @Override
    public void playerDisconnected(String username) {
        Game game = multiplayerGames.get(username);

        if (username.equals(game.getFirstPlayer().getUsername())) {
            webSocketService.sendMessage(game.getSecondPlayer().getUsername(), new DefaultMessage(MessageType.ENEMY_DISCONNECTED));
        } else {
            webSocketService.sendMessage(game.getFirstPlayer().getUsername(), new DefaultMessage(MessageType.ENEMY_DISCONNECTED));
        }
    }

    private void createSingleplayerGame(String username) {
        Game game = new Game();
        HumanPlayer player = new HumanPlayer(username);
        player.setUsername(username);
        game.setFirstPlayer(player);
        game.setWebSocketService(webSocketService);
        game.initGame();

        singleplayerGames.put(username, game);
        singleplayerExecutorService.execute(game);
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
