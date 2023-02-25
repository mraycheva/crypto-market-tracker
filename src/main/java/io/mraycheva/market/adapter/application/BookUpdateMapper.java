package io.mraycheva.market.adapter.application;

import io.mraycheva.market.infrastructure.websocket.MessageMapper;
import io.mraycheva.market.order.BookUpdate;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class BookUpdateMapper implements MessageMapper<BookUpdate> {

  BookUpdateParser parser;

  @Override
  public Flux<BookUpdate> map(final Flux<WebSocketMessage> webSocketMessages) {
    return webSocketMessages
      .map(WebSocketMessage::getPayloadAsText)
      .map(parser::parse)
      .filter(Option::isDefined)
      .map(Option::get);
  }
}
