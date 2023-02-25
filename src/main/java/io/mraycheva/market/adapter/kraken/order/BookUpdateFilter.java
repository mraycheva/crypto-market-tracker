package io.mraycheva.market.adapter.kraken.order;

import io.mraycheva.market.infrastructure.websocket.MessageFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;

@Component
class BookUpdateFilter implements MessageFilter {

  private static final String SYSTEM_EVENT_FLAG = "event";

  @Override
  public boolean shouldKeep(final WebSocketMessage message) {
    return isUpdate(message);
  }

  private boolean isUpdate(final WebSocketMessage message) {
    return !(isSystemEvent(message));
  }

  private boolean isSystemEvent(final WebSocketMessage message) {
    return message.getPayloadAsText().contains(SYSTEM_EVENT_FLAG);
  }
}
