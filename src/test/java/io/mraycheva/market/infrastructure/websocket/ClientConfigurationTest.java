package io.mraycheva.market.infrastructure.websocket;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.netty.http.client.HttpClient;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ClientConfigurationTest {

  ClientConfiguration configuration;

  @BeforeEach
  void setup() {
    configuration = new ClientConfiguration();
  }

  @Test
  void webSocketClient() {
    Try
      .withResources(() -> Mockito.mockStatic(HttpClient.class))
      .of(this::getHttpClientMockedStatic)
      .get();
  }

  private MockedStatic<HttpClient> getHttpClientMockedStatic(
    final MockedStatic<HttpClient> httpClientStaticMock
  ) {
    // GIVEN
    val httpClient = getHttpClient(httpClientStaticMock);

    // WHEN
    val webSocketClient = configuration.webSocketClient();

    // THEN
    verifyConfiguration(httpClient, webSocketClient);
    return httpClientStaticMock;
  }

  private HttpClient getHttpClient(final MockedStatic<HttpClient> httpClient) {
    val mock = Mockito.mock(HttpClient.class);
    httpClient.when(HttpClient::create).thenReturn(mock);
    return mock;
  }

  private void verifyConfiguration(
    final HttpClient httpClient,
    final WebSocketClient webSocketClient
  ) {
    val nettyClient = (ReactorNettyWebSocketClient) webSocketClient;
    Assertions.assertEquals(httpClient, nettyClient.getHttpClient());
    Assertions.assertEquals(
      Integer.MAX_VALUE,
      getMaxPayloadLength(nettyClient)
    );
  }

  private int getMaxPayloadLength(
    final ReactorNettyWebSocketClient nettyClient
  ) {
    return nettyClient.getWebsocketClientSpec().maxFramePayloadLength();
  }
}
