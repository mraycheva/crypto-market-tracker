package io.mraycheva.market.infrastructure.websocket;

import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;

public interface MessageMapper<T> {
  Flux<T> map(Flux<WebSocketMessage> messages);
}
