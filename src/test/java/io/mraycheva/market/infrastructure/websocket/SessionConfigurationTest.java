package io.mraycheva.market.infrastructure.websocket;

import java.net.URI;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SessionConfigurationTest {

  private static final String WEBSOCKET_URI = "URI";

  @Mock
  WebSocketHandler sessionHandler;

  @Mock
  WebSocketClient delegate;

  @InjectMocks
  SessionConfiguration sessionConfiguration;

  @BeforeEach
  void setup() {
    sessionConfiguration =
      new SessionConfiguration(WEBSOCKET_URI, delegate, sessionHandler);
  }

  @Test
  void create_createsSession() {
    // GIVEN
    final Mono<Void> stream = Mockito.spy(Mono.empty());
    Mockito
      .when(delegate.execute(URI.create(WEBSOCKET_URI), sessionHandler))
      .thenReturn(stream);

    // WHEN
    sessionConfiguration.create();

    // THEN
    Mockito.verify(stream).subscribe();
  }
}
