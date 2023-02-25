package io.mraycheva.market.infrastructure.websocket;

import org.springframework.web.reactive.socket.WebSocketMessage;

public interface MessageFilter {
  boolean shouldKeep(WebSocketMessage webSocketMessage);
}
