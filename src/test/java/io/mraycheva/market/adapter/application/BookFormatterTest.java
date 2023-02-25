package io.mraycheva.market.adapter.application;

import io.mraycheva.market.common.CurrencyCode;
import io.mraycheva.market.common.CurrencyPair;
import io.mraycheva.market.order.Book;
import io.mraycheva.market.order.BookUpdate;
import io.mraycheva.market.order.OrderCollectionUpdate;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.util.OrderCollectionUpdateProvider;

@FieldDefaults(level = AccessLevel.PRIVATE)
class BookFormatterTest {

  BookFormatter formatter;

  @BeforeEach
  void setup() {
    formatter = new BookFormatter();
  }

  @Test
  void givenInputBook_format_returnsCorrectStringRepresentation() {
    // GIVEN
    val book = getBook();
    // WHEN
    val bookString = formatter.format(book);
    // THEN
    Assertions.assertEquals(getExpectedBookString(book), bookString);
  }

  private Book getBook() {
    val currencyPair = new CurrencyPair(CurrencyCode.ETH, CurrencyCode.USD);
    val book = new Book(currencyPair);
    addAsks(book);
    return book;
  }

  private void addAsks(final Book book) {
    val asksUpdate = OrderCollectionUpdateProvider.getAsksUpdate();
    val update = getBookUpdate(book.getCurrencyPair(), asksUpdate);
    book.apply(update);
  }

  private BookUpdate getBookUpdate(
    final CurrencyPair currencyPair,
    final OrderCollectionUpdate askUpdates
  ) {
    return BookUpdate
      .builder()
      .asks(askUpdates)
      .currencyPair(currencyPair)
      .timestamp(Instant.now())
      .build();
  }

  private String getExpectedBookString(final Book book) {
    val bookLastUpdate = book.getLastUpdate().toString();
    return String.format(
      """
                            
            <------------------------------------->
            asks:
            [ [24609.1, 0.00041764],
            [24608.8, 0.10310571] ]
            best bid: []
            best ask: [24608.8, 0.10310571]
            bids:
            [  ]
            %s
            ETH/USD
            >-------------------------------------<
                          
            """,
      bookLastUpdate
    );
  }
}
