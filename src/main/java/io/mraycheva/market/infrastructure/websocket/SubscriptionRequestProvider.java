package io.mraycheva.market.infrastructure.websocket;

import io.mraycheva.market.infrastructure.mapping.BaseMapper;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SubscriptionRequestProvider {

  BaseMapper baseMapper;

  Supplier<WebSocketSubscriptionRequest> delegate;

  WebSocketMessage get(final WebSocketSession session) {
    val request = delegate.get();
    val requestString = baseMapper.stringify(request);
    return session.textMessage(requestString);
  }
}
