package io.mraycheva.market.infrastructure.websocket;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SessionHandler<T> implements WebSocketHandler {

  MessageFilter messageFilter;
  MessageMapper<T> messageMapper;
  MessageHandler<T> messageHandler;
  SubscriptionRequestProvider subscriptionRequestProvider;

  @Override
  public Mono<Void> handle(final WebSocketSession session) {
    return Try
      .of(() -> handleSession(session))
      .onFailure(failure -> log.error("Session failure", failure))
      .get();
  }

  private Mono<Void> handleSession(final WebSocketSession session) {
    log.info("Handling session...");
    return subscribeToMessages(session)
      .thenMany(handleMessages(session))
      .then();
  }

  private Mono<Void> subscribeToMessages(final WebSocketSession session) {
    log.info("Subscribing to messages...");
    val subscriptionRequest = subscriptionRequestProvider.get(session);
    return Flux
      .just(subscriptionRequest)
      .as(session::send)
      .doOnSubscribe(subscription -> log.info("Subscribed to messages!"));
  }

  private Flux<T> handleMessages(final WebSocketSession session) {
    log.info("Handling messages...");
    return session
      .receive()
      .filter(messageFilter::shouldKeep)
      .transform(messageMapper::map)
      .doOnNext(messageHandler::handle);
  }
}
