package ru.romanov.durak.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.romanov.durak.websocket.MultiplayerWebSocketHandler;
import ru.romanov.durak.websocket.SingleplayerWebSocketHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {

    private final SingleplayerWebSocketHandler singleplayerWebSocketHandler;
    private final MultiplayerWebSocketHandler multiplayerWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(singleplayerWebSocketHandler, "/ws/singleplayer");
        registry.addHandler(multiplayerWebSocketHandler, "/ws/multiplayer");
    }

}
