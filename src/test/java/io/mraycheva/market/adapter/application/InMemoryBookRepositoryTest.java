package io.mraycheva.market.adapter.application;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.common.CurrencyPairRepository;
import io.mraycheva.market.order.BookRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@FieldDefaults(level = AccessLevel.PRIVATE)
class InMemoryBookRepositoryTest {

  private static final CurrencyPair ONLY_CURRENCY_PAIR = new CurrencyPair(
    CurrencyCode.ETH,
    CurrencyCode.USD
  );

  BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    val currencyPairProvider = Mockito.mock(CurrencyPairRepository.class);
    Mockito
      .when(currencyPairProvider.getAll())
      .thenReturn(List.of(ONLY_CURRENCY_PAIR));
    bookRepository = new InMemoryBookRepository(currencyPairProvider);
  }

  @Test
  void givenKnownCurrencyValuePair_getBook_returnsMatchingBook() {
    // WHEN
    val book = bookRepository.getBook(ONLY_CURRENCY_PAIR);
    // THEN
    Assertions.assertTrue(book.isDefined());
  }

  @Test
  void givenUnknownCurrencyValuePair_getBook_returnsNoBook() {
    // WHEN
    val book = bookRepository.getBook(
      new CurrencyPair(CurrencyCode.USD, CurrencyCode.ETH)
    );
    // THEN
    Assertions.assertTrue(book.isEmpty());
  }
}
