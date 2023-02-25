package io.mraycheva.market.adapter.application;

import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.common.CurrencyPairRepository;
import io.mraycheva.market.order.Book;
import io.mraycheva.market.order.BookRepository;
import io.vavr.control.Option;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Repository;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InMemoryBookRepository implements BookRepository {

  Map<CurrencyPair, Book> books;

  InMemoryBookRepository(final CurrencyPairRepository currencyPairRepository) {
    val newBooks = getNewBookPerCurrency(currencyPairRepository);
    this.books = Collections.unmodifiableMap(newBooks);
  }

  private Map<CurrencyPair, Book> getNewBookPerCurrency(
    final CurrencyPairRepository currencyPairRepository
  ) {
    return currencyPairRepository
      .getAll()
      .stream()
      .collect(Collectors.toMap(Function.identity(), Book::new));
  }

  @Override
  public Option<Book> getBook(final CurrencyPair currencyPair) {
    return Option.of(books.get(currencyPair));
  }
}
