package io.mraycheva.market.order;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import java.time.Instant;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BookUpdateTest {

  @Test
  void givenInputBook_applyToBook_updatesBook() {
    // GIVEN
    val book = Mockito.mock(Book.class);
    val update = getBookUpdate();

    // WHEN
    update.applyToBook(book);

    // THEN
    Mockito.verify(book).apply(update);
  }

  private BookUpdate getBookUpdate() {
    return BookUpdate
      .builder()
      .currencyPair(getCurrencyPair())
      .timestamp(Instant.now())
      .build();
  }

  private CurrencyPair getCurrencyPair() {
    return new CurrencyPair(CurrencyCode.XBT, CurrencyCode.USD);
  }
}
