package io.mraycheva.market.infrastructure.websocket;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.WebSocketClient;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionConfiguration {

  @Value("${websocket.uri}")
  String uriString;

  WebSocketClient client;
  WebSocketHandler sessionHandler;

  @PostConstruct
  void create() {
    val uri = URI.create(uriString);
    log.info("Connecting to {}...", uriString);
    client.execute(uri, sessionHandler).subscribe();
  }
}
