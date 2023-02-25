package io.mraycheva.market.order;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookServiceTest {

  @Mock
  BookRepository bookRepository;

  @InjectMocks
  BookService service;

  @Test
  void givenMatchingBook_apply_appliesUpdateAndReturnsUpdatedBook() {
    // GIVEN
    val update = Mockito.mock(BookUpdate.class);
    val book = Mockito.mock(Book.class);
    mockMatchingBookUpdate(update, book);

    // WHEN
    val updatedBook = service.apply(update);

    // THEN
    Assertions.assertTrue(updatedBook.isDefined());
    Assertions.assertEquals(book, updatedBook.get());
    Mockito.verify(update).applyToBook(book);
  }

  private void mockMatchingBookUpdate(
    final BookUpdate update,
    final Book book
  ) {
    val currencyPair = new CurrencyPair(CurrencyCode.ETH, CurrencyCode.USD);
    Mockito.when(update.getCurrencyPair()).thenReturn(currencyPair);
    Mockito
      .when(bookRepository.getBook(currencyPair))
      .thenReturn(Option.of(book));
  }

  @Test
  void givenNoMatchingBook_apply_doesNotApplyUpdateAndReturnsNoBook() {
    // GIVEN
    val update = Mockito.mock(BookUpdate.class);
    mockNoMatchingBookUpdate(update);

    // WHEN
    val updatedBook = service.apply(update);

    // THEN
    Assertions.assertTrue(updatedBook.isEmpty());
  }

  private void mockNoMatchingBookUpdate(final BookUpdate update) {
    val currencyPair = new CurrencyPair(CurrencyCode.ETH, CurrencyCode.USD);
    Mockito.when(update.getCurrencyPair()).thenReturn(currencyPair);
    Mockito
      .when(bookRepository.getBook(currencyPair))
      .thenReturn(Option.none());
  }
}
