package io.mraycheva.market.infrastructure.websocket;

import io.mraycheva.market.infrastructure.mapping.BaseMapper;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SubscriptionRequestProviderTest {

  @Mock
  BaseMapper baseMapper;

  @Mock
  Supplier<WebSocketSubscriptionRequest> delegate;

  @InjectMocks
  SubscriptionRequestProvider adapter;

  @Test
  void givenWebSocketSession_get_returnsSubscriptionRequestWebSocketMessage() {
    // GIVEN
    val webSocketSession = Mockito.mock(WebSocketSession.class);
    val mockedWebSocketMessage = Mockito.mock(WebSocketMessage.class);
    mockSubscriptionRequest(webSocketSession, mockedWebSocketMessage);

    // WHEN
    val actualWebSocketMessage = adapter.get(webSocketSession);

    // THEN
    Assertions.assertEquals(actualWebSocketMessage, mockedWebSocketMessage);
  }

  private void mockSubscriptionRequest(
    final WebSocketSession webSocketSession,
    final WebSocketMessage webSocketMessage
  ) {
    val request = Mockito.mock(WebSocketSubscriptionRequest.class);
    val mappedRequest = request + " mapped";
    Mockito.when(baseMapper.stringify(request)).thenReturn(mappedRequest);
    Mockito
      .when(webSocketSession.textMessage(mappedRequest))
      .thenReturn(webSocketMessage);
    Mockito.when(delegate.get()).thenReturn(request);
  }
}
