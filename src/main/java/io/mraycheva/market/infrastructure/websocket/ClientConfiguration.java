package io.mraycheva.market.infrastructure.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.WebsocketClientSpec;
import reactor.netty.http.client.WebsocketClientSpec.Builder;

@Configuration
class ClientConfiguration {

  @Bean
  WebSocketClient webSocketClient() {
    return new ReactorNettyWebSocketClient(getHttpClient(), getSpec());
  }

  private HttpClient getHttpClient() {
    return HttpClient.create();
  }

  private Builder getSpec() {
    return WebsocketClientSpec
      .builder()
      .maxFramePayloadLength(Integer.MAX_VALUE);
  }
}
