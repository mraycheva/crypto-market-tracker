package io.mraycheva.market.adapter.application;

import io.mraycheva.market.infrastructure.websocket.MessageHandler;
import io.mraycheva.market.order.BookService;
import io.mraycheva.market.order.BookUpdate;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class BookUpdateHandler implements MessageHandler<BookUpdate> {

  BookService service;
  BookFormatter formatter;

  @Override
  public void handle(final BookUpdate update) {
    Try
      .of(() -> service.apply(update))
      .map(Option::get)
      .map(formatter::format)
      .andThen(log::info)
      .onFailure(failure -> log(update, failure))
      .recover(failure -> null);
  }

  private void log(final BookUpdate update, final Throwable failure) {
    log.error("Could not apply book update {}", update, failure);
  }
}
