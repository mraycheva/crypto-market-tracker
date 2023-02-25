package io.mraycheva.market.infrastructure.websocket;

public interface MessageHandler<T> {
  void handle(T message);
}
