package io.mraycheva.market.order;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {

  BookRepository repository;

  public Option<Book> apply(final BookUpdate update) {
    return getBook(update)
      .onEmpty(() -> logBookNotFound(update))
      .peek(update::applyToBook);
  }

  private Option<Book> getBook(final BookUpdate update) {
    val currencyPair = update.getCurrencyPair();
    return repository.getBook(currencyPair);
  }

  private void logBookNotFound(final BookUpdate bookUpdate) {
    log.warn("No book found for update {}", bookUpdate);
  }
}
