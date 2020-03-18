package ru.romanov.durak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.romanov.durak.websocket.MultiplayerWebSocket;
import ru.romanov.durak.websocket.SingleplayerWebSocket;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(singleplayerWebSocket(), "/ws/singleplayer");
        registry.addHandler(multiplayerWebSocket(), "/ws/multiplayer");
    }

    @Bean
    public SingleplayerWebSocket singleplayerWebSocket() {
        return new SingleplayerWebSocket();
    }

    @Bean
    public MultiplayerWebSocket multiplayerWebSocket() {
        return new MultiplayerWebSocket();
    }

}
